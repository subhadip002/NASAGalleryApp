package com.example.nasagalleryapp.ui.image_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImageCollectionAdapter(fragment: Fragment, private val count: Int) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = count

    override fun createFragment(position: Int): Fragment {
        val fragment = ImageDetailFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION, position)
        }
        return fragment
    }
}