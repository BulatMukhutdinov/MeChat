package tat.mukhutdinov.mechat.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.google.firebase.auth.FirebaseUser
import tat.mukhutdinov.mechat.model.Message

class MessageDataSourceFactory(private val user: FirebaseUser) : DataSource.Factory<Message, Message>() {

    val liveDataSource: MutableLiveData<MessageDataSource> = MutableLiveData()

    override fun create(): DataSource<Message, Message> {
        val dataSource = MessageDataSource(user)
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}