package tat.mukhutdinov.mechat

import androidx.paging.ItemKeyedDataSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import tat.mukhutdinov.mechat.model.TextMessage
import tat.mukhutdinov.mechat.repo.MessagesRepo.Companion.COLLECTION_PATH
import tat.mukhutdinov.mechat.repo.MessagesRepo.Companion.ORDER_BY_FIELD

class MessageDataSource : ItemKeyedDataSource<TextMessage, TextMessage>() {

    private lateinit var lastPage: Query

    override fun loadInitial(params: LoadInitialParams<TextMessage>, callback: LoadInitialCallback<TextMessage>) {
        lastPage = FirebaseFirestore.getInstance()
            .collection(COLLECTION_PATH)
            .orderBy(ORDER_BY_FIELD, Query.Direction.DESCENDING)
            .limit(params.requestedLoadSize.toLong())

        val querySnapshot = Tasks.await(lastPage.get())

        callback.onResult(toMessages(querySnapshot))
    }

    override fun loadBefore(params: LoadParams<TextMessage>, callback: LoadCallback<TextMessage>) {
        val lastQuerySnapshot = Tasks.await(lastPage.get())

        val lastVisible = lastQuerySnapshot.documents[lastQuerySnapshot.size() - 1]

        lastPage = FirebaseFirestore.getInstance()
            .collection(COLLECTION_PATH)
            .orderBy(ORDER_BY_FIELD, Query.Direction.DESCENDING)
            .startAfter(lastVisible)
            .limit(params.requestedLoadSize.toLong())

        val querySnapshot = Tasks.await(lastPage.get())

        callback.onResult(toMessages(querySnapshot))
    }

    override fun loadAfter(params: LoadParams<TextMessage>, callback: LoadCallback<TextMessage>) {
        // not used
//        val lastQuerySnapshot = Tasks.await(lastPage.get())
//
//        val lastVisible = lastQuerySnapshot.documents[lastQuerySnapshot.size() - 1]
//
//        lastPage = FirebaseFirestore.getInstance()
//            .collection(COLLECTION_PATH)
//            .orderBy(ORDER_BY_FIELD)
//            .startAfter(lastVisible)
//            .limit(params.requestedLoadSize.toLong())
//
//        val querySnapshot = Tasks.await(lastPage.get())
//
//        callback.onResult(toMessages(querySnapshot))

//        FirebaseFirestore.getInstance()
//            .collection(COLLECTION_PATH)
//            .orderBy(ORDER_BY_FIELD)
//            .startAfter(params.key.timestamp)
//            .limit(params.requestedLoadSize.toLong())
//            .get()
//            .addOnSuccessListener {
//                callback.onResult(toMessages(it))
//            }
    }

    private fun toMessages(querySnapshot: QuerySnapshot?): List<TextMessage> {
        if (querySnapshot == null) {
            return emptyList()
        }

        val messages = mutableListOf<TextMessage>()

        for (document in querySnapshot) {
            messages.add(TextMessage(document.id, document.data["timestamp"] as Long, document.data["text"].toString()))
        }

        return messages.reversed()
    }

    override fun getKey(item: TextMessage): TextMessage {
        return item
    }
}