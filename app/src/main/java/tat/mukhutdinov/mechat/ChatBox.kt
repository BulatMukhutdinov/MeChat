package tat.mukhutdinov.mechat

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.chat_box.view.image
import kotlinx.android.synthetic.main.chat_box.view.location
import kotlinx.android.synthetic.main.chat_box.view.send
import kotlinx.android.synthetic.main.chat_box.view.text

class ChatBox(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    var onSendClicked: ((text: String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.chat_box, this, true)

        text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    image.visibility = View.VISIBLE
                    location.visibility = View.VISIBLE
                    send.visibility = View.GONE
                } else {
                    image.visibility = View.GONE
                    location.visibility = View.GONE
                    send.visibility = View.VISIBLE
                }
            }
        })

        send.setOnClickListener {
            onSendClicked?.let {
                it(text.text.toString())
                text.text.clear()
            }
        }
    }
}