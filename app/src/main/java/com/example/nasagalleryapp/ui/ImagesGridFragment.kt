package com.example.nasagalleryapp.ui

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
import com.google.android.material.transition.MaterialElevationScale

class ImagesGridFragment : Fragment(), ImageGridAdapter.ImageGridAdapterListener {

    private val viewModel: ImageGridViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImagesGridBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = this@ImagesGridFragment
            viewModel = this@ImagesGridFragment.viewModel
        }
        binding.imageGrid.adapter = ImageGridAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onItemClicked(cardView: CardView, position: Int) {
        viewModel.imageDetailCurrentImageIndex.postValue(position)
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }
        val imageCardDetailTransitionName = getString(R.string.image_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to imageCardDetailTransitionName)
        val action =
            ImagesGridFragmentDirections.actionImagesGridFragmentToImageDetailFragment()
        findNavController().navigate(action, extras)
    }
}