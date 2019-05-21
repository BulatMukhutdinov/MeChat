package tat.mukhutdinov.mechat.main.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.main.model.Message
import tat.mukhutdinov.mechat.main.ui.adapter.viewholder.ImageMessageViewHolder
import tat.mukhutdinov.mechat.main.ui.adapter.viewholder.TextMessageViewHolder

class MessagesAdapter : PagedListAdapter<Message, RecyclerView.ViewHolder>(DiffUtilItemCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.image_item -> ImageMessageViewHolder.create(parent)
            R.layout.text_item -> TextMessageViewHolder.create(parent)
            else -> throw IllegalStateException("No such view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.image_item -> (holder as ImageMessageViewHolder).bindTo(getItem(position))
            R.layout.text_item -> (holder as TextMessageViewHolder).bindTo(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return 0

        return if (item.image != null || item.location != null) {
            R.layout.image_item
        } else {
            R.layout.text_item
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ImageMessageViewHolder) {
            holder.onViewRecycled()
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.hashCode()?.toLong() ?: -1
    }
}