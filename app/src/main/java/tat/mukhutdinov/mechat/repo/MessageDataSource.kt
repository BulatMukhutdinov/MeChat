package tat.mukhutdinov.mechat.repo

import androidx.paging.ItemKeyedDataSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import tat.mukhutdinov.mechat.model.COLLECTION_PATH
import tat.mukhutdinov.mechat.model.FIELD_IMAGE
import tat.mukhutdinov.mechat.model.FIELD_TEXT
import tat.mukhutdinov.mechat.model.FIELD_TIMESTAMP
import tat.mukhutdinov.mechat.model.Message

class MessageDataSource : ItemKeyedDataSource<Message, Message>() {

    private lateinit var lastPage: Query

    override fun loadInitial(params: LoadInitialParams<Message>, callback: LoadInitialCallback<Message>) {
        lastPage = FirebaseFirestore.getInstance()
            .collection(COLLECTION_PATH)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .limit(params.requestedLoadSize.toLong())

        val querySnapshot = Tasks.await(lastPage.get())

        callback.onResult(toMessages(querySnapshot))
    }

    override fun loadBefore(params: LoadParams<Message>, callback: LoadCallback<Message>) {
        val lastQuerySnapshot = Tasks.await(lastPage.get())

        val lastVisible = lastQuerySnapshot.documents[lastQuerySnapshot.size() - 1]

        lastPage = FirebaseFirestore.getInstance()
            .collection(COLLECTION_PATH)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .startAfter(lastVisible)
            .limit(params.requestedLoadSize.toLong())

        val querySnapshot = Tasks.await(lastPage.get())

        callback.onResult(toMessages(querySnapshot))
    }

    override fun loadAfter(params: LoadParams<Message>, callback: LoadCallback<Message>) {
        // not used
    }

    private fun toMessages(querySnapshot: QuerySnapshot?): List<Message> {
        if (querySnapshot == null) {
            return emptyList()
        }

        val messages = mutableListOf<Message>()

        for (document in querySnapshot) {
            messages.add(
                Message(
                    id = document.id,
                    timestamp = document.data[FIELD_TIMESTAMP] as Long,
                    text = document.data[FIELD_TEXT] as String?,
                    image = document.data[FIELD_IMAGE] as String?)
            )
        }

        return messages.reversed()
    }

    override fun getKey(item: Message): Message {
        return item
    }
}