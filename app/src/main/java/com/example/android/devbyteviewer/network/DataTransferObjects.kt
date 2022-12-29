

package com.example.android.devbyteviewer.network

import com.example.android.devbyteviewer.database.DatabaseVideo
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.squareup.moshi.JsonClass

// DataTransferObjects masuk ke file ini. Ini bertanggung jawab untuk mem-parsing respons dari server
// atau memformat objek untuk dikirim ke server. Anda harus mengonversinya menjadi objek domain sebelumnya
// menggunakannya.

// @lihat paket domain untuk VideoHolder memegang daftar Video.

//Ini untuk mem-parsing level pertama dari hasil jaringan kita yang terlihat

//{
// "video": []
//}
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

//Video mewakili devbyte yang dapat diputar.
@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?)

//Mengkonversi hasil Jaringan ke objek basis data
fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
    return videos.map {
        DevByteVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}


//Mengkonversi hasil Jaringan ke objek basis data
fun NetworkVideoContainer.asDatabaseModel(): List<DatabaseVideo> {
    return videos.map {
        DatabaseVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}

