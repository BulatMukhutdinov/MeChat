package tat.mukhutdinov.mechat.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.text_item.view.text
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.model.Message

class TextMessageViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    private val text = itemView.text

    fun bindTo(item: Message?) {
        text.text = item?.text
    }

    companion object {
        fun create(parent: ViewGroup): TextMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.text_item, parent, false)
            return TextMessageViewHolder(view)
        }
    }
}