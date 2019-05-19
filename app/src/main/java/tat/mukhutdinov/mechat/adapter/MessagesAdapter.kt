package tat.mukhutdinov.mechat.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.adapter.viewholder.BaseViewHolder
import tat.mukhutdinov.mechat.adapter.viewholder.TextMessageViewHolder
import tat.mukhutdinov.mechat.model.TextMessage

class MessagesAdapter(private val clickListener: ((Int) -> Unit))
    : PagedListAdapter<TextMessage, BaseViewHolder>(DiffUtilItemCallback<TextMessage>()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        TextMessageViewHolder(parent, clickListener)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder as TextMessageViewHolder).bindTo(getItem(position))
    }

    override fun getItemViewType(position: Int): Int =
        R.layout.text_item

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.hashCode()?.toLong() ?: -1
    }
}