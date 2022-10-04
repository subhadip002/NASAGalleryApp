package com.example.nasagalleryapp.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.databinding.ImageGridItemBinding

class ImageGridAdapter(val listener: ImageGridAdapterListener) : ListAdapter<ImageItemUiState,
        ImageGridAdapter.ImageViewHolder>(ImageDiffCallback) {

    interface ImageGridAdapterListener {
        fun onItemClicked(cardView: CardView, position: Int)
        fun saveImageDrawable(drawable: Drawable, position: Int)
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
            image.thumbnail?.let {
                binding.imageView.setImageDrawable(it)
            } ?: run {
                val imageLoader = binding.imageView.context.imageLoader
                val request = ImageRequest.Builder(binding.imageView.context)
                    .data(image.url)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .target(
                        onStart = { placeholder ->
                            binding.imageView.setImageDrawable(placeholder)
                        },
                        onSuccess = { result ->
                            binding.imageView.setImageDrawable(result)
                            adapter.listener.saveImageDrawable(result, adapterPosition)
                        },
                        onError = { error ->
                            binding.imageView.setImageDrawable(error)
                        }
                    )
                    .build()
                imageLoader.enqueue(request)
            }
            binding.cardView.setOnClickListener {
                adapter.listener.onItemClicked(binding.cardView, adapterPosition)
            }
            binding.executePendingBindings()
        }
    }

}
