package com.example.nasagalleryapp.ui

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasagalleryapp.data.ImageItemUiState
import com.example.nasagalleryapp.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageGridViewModel @Inject constructor(imageRepository: ImageRepository) :
    ViewModel() {

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isNetworkAvailable.postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isNetworkAvailable.postValue(false)
        }
    }

    val imageListUiItems = imageRepository.getImageList()?.map {
        ImageItemUiState(
            explanation = it.explanation ?: "",
            hdurl = it.hdurl ?: "",
            title = it.title ?: "",
            url = it.url ?: ""
        )
    } ?: listOf()

    fun getImage(index: Int): ImageItemUiState? {
        return if (imageListUiItems.size >= index)
            imageListUiItems[index]
        else null
    }

    private var _isNetworkAvailable = MutableLiveData<Boolean?>(null)
    val isNetworkAvailable: LiveData<Boolean?> = _isNetworkAvailable
}