package tat.mukhutdinov.mechat.repo

import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class MessagesBoundaryRepo : MessagesRepo {

    override fun sendText(text: String) {
        val message = HashMap<String, Any>()
        message["timestamp"] = System.currentTimeMillis()
        message["text"] = text

        FirebaseFirestore.getInstance().collection("message")
            .add(message)
            .addOnSuccessListener {
                Timber.d("New text message: $text")
            }
            .addOnFailureListener { Timber.e(it) }
    }
}