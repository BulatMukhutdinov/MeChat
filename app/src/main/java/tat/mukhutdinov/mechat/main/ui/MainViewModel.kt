package tat.mukhutdinov.mechat.main.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import tat.mukhutdinov.mechat.main.model.Message
import tat.mukhutdinov.mechat.main.repo.MessagesRepo

class MainViewModel(private val messagesRepo: MessagesRepo) : ViewModel() {

    val messages: LiveData<PagedList<Message>> = messagesRepo.getMessages()

    fun sendText(text: String) {
        messagesRepo.sendText(text)
    }

    fun sendImage(uri: Uri) {
        messagesRepo.sendImage(uri)
    }

    fun sendLocation(latitude: Double, longitude: Double, scale: Int) {
        messagesRepo.sendLocation(latitude, longitude, scale)
    }
}