/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDomainModel
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.*
import java.io.IOException

/**
 * DevByteViewModel dirancang untuk menyimpan dan mengelola data terkait UI dengan cara sadar siklus hidup. Ini
 * memungkinkan data bertahan dari perubahan konfigurasi seperti rotasi layar. Selain itu, latar
 * pekerjaan seperti mengambil hasil jaringan dapat dilanjutkan melalui perubahan konfigurasi dan pengiriman
 * hasil setelah Fragmen atau Aktivitas baru tersedia.
 *
 * Aplikasi @param Aplikasi yang dilampirkan model tampilan ini, aman untuk dipegang a
 * referensi ke aplikasi lintas rotasi karena Aplikasi tidak pernah dibuat ulang selama aktivitas
 * atau peristiwa siklus hidup fragmen.
 */
class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Sumber data ViewModel ini akan mengambil hasil.
     */
    private val videosRepository = VideosRepository(getDatabase(application))

    /**
     * Daftar putar video yang ditampilkan di layar.
     */
    val playlist = videosRepository.videos

    /**
     * Acara dipicu untuk kesalahan jaringan. Ini pribadi untuk menghindari mengekspos a
     * cara untuk menyetel nilai ini ke pengamat.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Acara dipicu untuk kesalahan jaringan. Tampilan harus menggunakan ini untuk mendapatkan akses
     * ke datanya.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Tandai untuk menampilkan pesan kesalahan. Ini pribadi untuk menghindari mengekspos a
     * cara untuk menyetel nilai ini ke pengamat.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Tandai untuk menampilkan pesan kesalahan. Tampilan harus menggunakan ini untuk mendapatkan akses
     * ke datanya.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * init{} langsung dipanggil saat ViewModel ini dibuat.
     */
    init {
        refreshDataFromRepository()
    }

    /**
     * Segarkan data dari repositori. Gunakan peluncuran coroutine untuk dijalankan di a
     * utas latar belakang.
     */
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                videosRepository.refreshVideos()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Tampilkan pesan kesalahan Toast dan sembunyikan bilah progres.
                if(playlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    /**
     * Mereset bendera kesalahan jaringan.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    /**
     * Pabrik untuk membuat DevByteViewModel dengan parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
