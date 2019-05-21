package tat.mukhutdinov.mechat.main.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import tat.mukhutdinov.mechat.main.model.Message

class DiffUtilItemCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
        oldItem == newItem
}