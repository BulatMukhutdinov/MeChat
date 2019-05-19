package tat.mukhutdinov.mechat.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.adapter.viewholder.BaseViewHolder
import tat.mukhutdinov.mechat.adapter.viewholder.NetworkErrorViewHolder
import tat.mukhutdinov.mechat.adapter.viewholder.TextMessageViewHolder
import tat.mukhutdinov.mechat.model.Message
import tat.mukhutdinov.mechat.model.NetworkState
import tat.mukhutdinov.mechat.model.Status

class MessagesAdapter(private val clickListener: ((Int) -> Unit))
    : PagedListAdapter<Message, BaseViewHolder>(DiffUtilItemCallback<Message>()) {

    private var networkState: NetworkState? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            R.layout.text_item -> TextMessageViewHolder(parent, clickListener)
            R.layout.error_item -> NetworkErrorViewHolder(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.text_item -> (holder as TextMessageViewHolder).bindTo(getItem(position))
            R.layout.error_item -> (holder as NetworkErrorViewHolder).bindTo(null)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState?.status == Status.FAILED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.error_item
        } else {
            R.layout.text_item
        }
    }

    fun updateNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.hashCode()?.toLong() ?: -1
    }
}