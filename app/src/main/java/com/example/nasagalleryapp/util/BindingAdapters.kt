package com.example.nasagalleryapp.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.ui.ImageItemUiState
import com.example.nasagalleryapp.ui.image_grid.ImageGridAdapter
import com.facebook.shimmer.ShimmerFrameLayout

@BindingAdapter("imageList")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ImageItemUiState>) {
    val adapter = recyclerView.adapter as ImageGridAdapter
    adapter.submitList(data)
}

@BindingAdapter(value = ["imageUrl", "placeholder", "isOffline"], requireAll = false)
fun setImageUrl(
    imageView: ImageView,
    url: String?,
    placeHolder: Drawable?,
    isOffline: Boolean
) {
    if (!isOffline || imageView.drawable == null) {
        imageView.load(url) {
            placeholder(
                placeHolder ?: AppCompatResources.getDrawable(
                    imageView.context,
                    R.drawable.loading_animation
                )
            )
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("isVisible")
fun bindVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("isLoading")
fun bindShimmer(shimmerFrameLayout: ShimmerFrameLayout, isLoading: Boolean) {
    if (!isLoading) {
        shimmerFrameLayout.stopShimmer()
        bindVisibility(shimmerFrameLayout, false)
    } else {
        shimmerFrameLayout.startShimmer()
        bindVisibility(shimmerFrameLayout, true)
    }
}

