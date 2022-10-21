package com.example.nasagalleryapp.ui.image_detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.databinding.FragmentImageDetailBinding
import com.example.nasagalleryapp.ui.ImageViewModel
import com.example.nasagalleryapp.ui.getImageByIndex
import com.example.nasagalleryapp.util.setImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageDetailFragment : Fragment() {
    private val viewModel: ImageViewModel by activityViewModels()
    private lateinit var binding: FragmentImageDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailBinding.inflate(inflater)

        arguments?.takeIf { it.containsKey(ARG_POSITION) }?.apply {
            viewModel.imagesUiState.value.getImageByIndex(getInt(ARG_POSITION))?.let {
                binding.image = it
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                if (binding.root.width > 0 && binding.imageView.width > 0) {
                    binding.imageView.maxWidth = binding.root.width / 2
                    binding.imageView.requestLayout()
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
                }
            }
            binding.root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                binding.image?.let {
                    val result = getThumbnailFromUrlAndLoad(it.url)
                    binding.imageView.setImageDrawable(result.drawable)
                    viewModel.isOffline.collect { isOffline ->
                        setImageUrl(binding.imageView, it.hdurl, result.drawable, isOffline)
                    }
                }
            } catch (e: Exception) {
                //Coroutine Job Exception Catch
            }
        }
    }

    private suspend fun getThumbnailFromUrlAndLoad(url: String): ImageResult =
        withContext(Dispatchers.IO) {
            val imageLoader = ImageLoader.Builder(requireContext())
                .build()
            val request = ImageRequest.Builder(requireContext())
                .target(binding.imageView)
                .placeholder(R.drawable.loading_animation)
                .data(url)
                .build()
            imageLoader.execute(request)
        }
}