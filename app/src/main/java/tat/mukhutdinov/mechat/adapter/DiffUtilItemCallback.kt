package tat.mukhutdinov.mechat.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import tat.mukhutdinov.mechat.db.MessageEntity
import tat.mukhutdinov.mechat.model.TextMessage

class DiffUtilItemCallback<T : TextMessage> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}