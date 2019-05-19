package tat.mukhutdinov.mechat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.chat_box.view.*
import timber.log.Timber

class ChatBox(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.chat_box, this, true)

        action.setOnClickListener {
            val message = HashMap<String, Any>()
            message["timestamp"] = System.currentTimeMillis()
            message["text"] = text.text.toString()

            FirebaseFirestore.getInstance().collection("message")
                .add(message)
                .addOnSuccessListener { documentReference ->
                    Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
                    text.text.clear()
                }
                .addOnFailureListener { e ->
                    Timber.e(e)
                }
        }
    }
}