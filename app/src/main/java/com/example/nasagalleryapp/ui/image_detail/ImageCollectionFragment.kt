package com.example.nasagalleryapp.ui.image_detail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.nasagalleryapp.MainActivity
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.databinding.FragmentImageCollectionBinding
import com.example.nasagalleryapp.ui.ImageViewModel
import com.example.nasagalleryapp.util.ViewModelFactory
import com.example.nasagalleryapp.util.themeColor
import com.google.android.material.R.attr
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

const val ARG_POSITION = "position"

class ImageCollectionFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ImageViewModel by activityViewModels { viewModelFactory }
    private lateinit var binding: FragmentImageCollectionBinding
    private val args: ImageCollectionFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageCollectionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter =
            ImageCollectionAdapter(this, viewModel.imagesUiState.value.imageItems.count())
        binding.pager.setCurrentItem(args.index, false)
    }

}

