package tat.mukhutdinov.mechat.main.repo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import tat.mukhutdinov.mechat.main.model.Message

interface MessagesRepo {

    fun getMessages(): LiveData<PagedList<Message>>

    fun sendText(text: String)

    fun sendImage(uri: Uri)

    fun sendLocation(latitude: Double, longitude: Double, scale: Int)
}