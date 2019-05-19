package tat.mukhutdinov.mechat

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import tat.mukhutdinov.mechat.model.TextMessage

class MessageDataSourceFactory : DataSource.Factory<TextMessage, TextMessage>() {

    val liveDataSource: MutableLiveData<MessageDataSource> = MutableLiveData()

    override fun create(): DataSource<TextMessage, TextMessage> {
        val dataSource = MessageDataSource()
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}