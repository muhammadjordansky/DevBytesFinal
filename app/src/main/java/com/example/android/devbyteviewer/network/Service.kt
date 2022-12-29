

package com.example.android.devbyteviewer.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Karena kita hanya memiliki satu layanan, ini semua bisa masuk dalam satu file.
// Jika Anda menambahkan lebih banyak layanan, pisahkan ini menjadi beberapa file dan pastikan untuk berbagi retrofit
// objek antar layanan.

//Layanan retrofit untuk mengambil daftar putar devbyte.
interface DevbyteService {
    @GET("devbytes")
    suspend fun getPlaylist(): NetworkVideoContainer
}

//Titik masuk utama untuk akses jaringan. Panggil seperti `DevByteNetwork.devbytes.getPlaylist()`
object DevByteNetwork {

    // Konfigurasi retrofit untuk mengurai JSON dan menggunakan coroutine
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val devbytes = retrofit.create(DevbyteService::class.java)

}


