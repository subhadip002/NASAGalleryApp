package com.example.nasagalleryapp.ui

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasagalleryapp.domain.GetSortedImagesWithDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val getSortedImagesWithDateUseCase: GetSortedImagesWithDateUseCase) :
    ViewModel() {

    init {
        setImages()
    }

    private var _imagesUiState = MutableStateFlow(ImagesUiState())
    val imagesUiState: StateFlow<ImagesUiState> = _imagesUiState.asStateFlow()

    private var _isNetworkAvailable = MutableLiveData<Boolean?>(null)
    val isNetworkAvailable: LiveData<Boolean?> = _isNetworkAvailable

    private var fetchJob: Job? = null

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

    private fun setImages() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val imageItems = getSortedImagesWithDateUseCase().map {
                    ImageItemUiState(
                        explanation = it.explanation ?: "",
                        hdurl = it.hdurl ?: "",
                        title = it.title ?: "",
                        url = it.url ?: ""
                    )
                }
                _imagesUiState.update {
                    it.copy(imageItems = imageItems)
                }
            } catch (ioe: Exception) {
                _imagesUiState.update {
                    val messages = ioe.message
                    it.copy(userMessages = messages)
                }
            }
        }
    }

    fun userMessageShown() {
        _imagesUiState.update {
            it.copy(userMessages = null)
        }
    }

    fun updateUserMessage(message: String){
        _imagesUiState.update {
            it.copy(userMessages = message)
        }
    }
}