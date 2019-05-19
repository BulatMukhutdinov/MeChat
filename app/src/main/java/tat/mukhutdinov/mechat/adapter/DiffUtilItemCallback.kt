package tat.mukhutdinov.mechat.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import tat.mukhutdinov.mechat.model.Message

class DiffUtilItemCallback<T : Message> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}