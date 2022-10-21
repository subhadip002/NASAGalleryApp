package com.example.nasagalleryapp.ui.image_grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nasagalleryapp.databinding.ImageGridItemBinding
import com.example.nasagalleryapp.ui.ImageDiffCallback
import com.example.nasagalleryapp.ui.ImageItemUiState

class ImageGridAdapter(val listener: ImageGridAdapterListener) : ListAdapter<ImageItemUiState,
        ImageGridAdapter.ImageViewHolder>(ImageDiffCallback) {

    interface ImageGridAdapterListener {
        fun onItemClicked(cardView: CardView, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        return ImageViewHolder(
            ImageGridItemBinding.inflate(LayoutInflater.from(parent.context)),
            this
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder(
        private var binding: ImageGridItemBinding,
        private val adapter: ImageGridAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageItemUiState) {
            binding.image = image
            binding.cardView.setOnClickListener {
                adapter.listener.onItemClicked(binding.cardView, adapterPosition)
            }
            binding.executePendingBindings()
        }
    }

}
