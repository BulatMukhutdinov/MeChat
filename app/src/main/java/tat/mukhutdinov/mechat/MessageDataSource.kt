package tat.mukhutdinov.mechat

import androidx.paging.ItemKeyedDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import tat.mukhutdinov.mechat.model.TextMessage
import timber.log.Timber

class MessageDataSource : ItemKeyedDataSource<TextMessage, TextMessage>() {

    override fun loadInitial(params: LoadInitialParams<TextMessage>, callback: LoadInitialCallback<TextMessage>) {
        FirebaseFirestore.getInstance().collection("message") // todo move collection name to const
            .orderBy("timestamp")
            .limit(params.requestedLoadSize.toLong())
            .get()
            .addOnSuccessListener {
                callback.onResult(toMessages(it))
            }
            .addOnFailureListener { Timber.e(it) }
    }

    override fun loadAfter(params: LoadParams<TextMessage>, callback: LoadCallback<TextMessage>) {
        FirebaseFirestore.getInstance().collection("message") // todo move collection name to const
            .orderBy("timestamp")
            .startAfter(params.key.timestamp)
            .limit(params.requestedLoadSize.toLong())
            .get()
            .addOnSuccessListener {
                callback.onResult(toMessages(it))
            }
            .addOnFailureListener { Timber.e(it) }
    }

    private fun toMessages(querySnapshot: QuerySnapshot): MutableList<TextMessage> {
        val messages = mutableListOf<TextMessage>()

        for (document in querySnapshot) {
            messages.add(TextMessage(document.id, document.data["timestamp"] as Long, document.data["text"].toString()))
        }

        return messages
    }

    override fun loadBefore(params: LoadParams<TextMessage>, callback: LoadCallback<TextMessage>) {
        // doesn't used
    }

    override fun getKey(item: TextMessage): TextMessage =
        item
}