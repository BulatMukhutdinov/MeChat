package tat.mukhutdinov.mechat.adapter.viewholder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.text_item.view.text
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.model.TextMessage

class TextMessageViewHolder(parent: ViewGroup, clickListener: ((Int) -> Unit)) :
    BaseViewHolder(parent, R.layout.text_item, clickListener) {

    private val text = itemView.text

    fun bindTo(item: TextMessage?) {
        text.text = item?.text
    }
}