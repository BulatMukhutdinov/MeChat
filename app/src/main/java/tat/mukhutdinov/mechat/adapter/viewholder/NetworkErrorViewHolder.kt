package tat.mukhutdinov.mechat.adapter.viewholder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.error_item.view.description
import kotlinx.android.synthetic.main.error_item.view.retry
import tat.mukhutdinov.mechat.R

class NetworkErrorViewHolder(parent: ViewGroup)
    : BaseViewHolder(parent, R.layout.error_item) {

    private val description = itemView.description
    private val retry = itemView.retry

//    fun bind(item: PostsListViewModel) {
//        super.bindTo(null)
//
//        retry.setOnClickListener { item.retry() }
//
//        item.networkState.observe(this, Observer {
//            retry.isEnabled = it != NetworkState.Loading
//        })
//
//        item.errorText.observe(this, Observer {
//            description.text = it
//        })
//    }
}