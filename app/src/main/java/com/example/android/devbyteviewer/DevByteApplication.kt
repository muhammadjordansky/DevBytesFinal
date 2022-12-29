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

package com.example.android.devbyteviewer

import android.app.Application
import timber.log.Timber

/**
 * Ganti aplikasi untuk menyiapkan pekerjaan latar belakang melalui WorkManager
 */
class DevByteApplication : Application() {

    /**
     * onCreate dipanggil sebelum layar pertama ditampilkan kepada pengguna.
     *
     * Gunakan untuk mengatur tugas latar belakang apa pun, menjalankan operasi penyiapan yang mahal di latar belakang
     * utas untuk menghindari keterlambatan memulai aplikasi.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
