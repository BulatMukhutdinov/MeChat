package tat.mukhutdinov.mechat.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import tat.mukhutdinov.mechat.model.Message

class MessageDataSourceFactory : DataSource.Factory<Message, Message>() {

    val liveDataSource: MutableLiveData<MessageDataSource> = MutableLiveData()

    override fun create(): DataSource<Message, Message> {
        val dataSource = MessageDataSource()
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}