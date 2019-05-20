package tat.mukhutdinov.mechat.repo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import tat.mukhutdinov.mechat.model.Message

interface MessagesRepo {

    fun getMessages(): LiveData<PagedList<Message>>

    fun sendText(text: String)

    fun sendImage(uri: Uri)
}