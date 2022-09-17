package com.example.nasagalleryapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.databinding.FragmentImageBinding
import com.example.nasagalleryapp.databinding.FragmentImageDetailBinding
import com.example.nasagalleryapp.util.themeColor
import com.google.android.material.R.attr
import com.google.android.material.transition.MaterialContainerTransform

class ImageDetailFragment : Fragment() {

    private val viewModel: ImageGridViewModel by activityViewModels()
    private lateinit var binding: FragmentImageDetailBinding
    private val args: ImageDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.image_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = ImageCollectionAdapter(this, viewModel.imageListUiItems.count())
        binding.pager.setCurrentItem(args.index, false)
    }
    
}

class ImageCollectionAdapter(fragment: Fragment, private val count: Int) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = count

    override fun createFragment(position: Int): Fragment {
        val fragment = ImageFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION, position)
        }
        return fragment
    }
}

private const val ARG_POSITION = "position"

class ImageFragment : Fragment() {
    private val viewModel: ImageGridViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageBinding.inflate(inflater)
        arguments?.takeIf { it.containsKey(ARG_POSITION) }?.apply {
            binding.image = viewModel.getImage(getInt(ARG_POSITION))
        }
        return binding.root
    }
}