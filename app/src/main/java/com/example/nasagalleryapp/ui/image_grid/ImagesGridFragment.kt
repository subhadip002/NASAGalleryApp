package com.example.nasagalleryapp.ui.image_grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.databinding.FragmentImagesGridBinding
import com.example.nasagalleryapp.ui.ImageViewModel
import com.google.android.material.transition.MaterialElevationScale

class ImagesGridFragment : Fragment(), ImageGridAdapter.ImageGridAdapterListener {

    private val viewModel: ImageViewModel by activityViewModels()
    lateinit var binding: FragmentImagesGridBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesGridBinding.inflate(inflater).apply {
            viewModel = this@ImagesGridFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            imageGrid.adapter = ImageGridAdapter(this@ImagesGridFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onItemClicked(cardView: CardView, position: Int) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.image_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.image_motion_duration_large).toLong()
        }
        val imageCardDetailTransitionName = getString(R.string.image_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to imageCardDetailTransitionName)
        val action =
            ImagesGridFragmentDirections.actionImagesGridFragmentToImageDetailFragment(position)
        findNavController().navigate(action, extras)
    }
}