package tat.mukhutdinov.mechat.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item.view.image
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.model.Message

class ImageMessageViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    private val image = itemView.image

    fun bindTo(item: Message?) {
        image.clipToOutline = true

        if (item?.image?.isNotEmpty() == true) {
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.ic_loading)
                .into(image)
        }
    }

    fun onViewRecycled() {
        Picasso.get().cancelRequest(image)
    }

    companion object {
        fun create(parent: ViewGroup): ImageMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.image_item, parent, false)
            return ImageMessageViewHolder(view)
        }
    }
}