package com.example.nasagalleryapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasagalleryapp.domain.GetSortedImagesWithDateUseCase
import com.example.nasagalleryapp.util.NetworkMonitor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageViewModel @Inject constructor(
    private val getSortedImagesWithDateUseCase: GetSortedImagesWithDateUseCase,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    private var _imagesUiState = MutableStateFlow(ImagesUiState())
    val imagesUiState: StateFlow<ImagesUiState> = _imagesUiState.asStateFlow()

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setImages() {
        viewModelScope.launch {
            try {
                val imageItems = getSortedImagesWithDateUseCase().map {
                    ImageItemUiState(
                        explanation = it.explanation ?: "",
                        hdurl = it.hdurl ?: "",
                        title = it.title ?: "",
                        url = it.url ?: "",
                        isOffline = isOffline.value
                    )
                }
                _imagesUiState.update {
                    it.copy(imageItems = imageItems)
                }
            } catch (ioe: Exception) {
                _imagesUiState.update {
                    val messages = ioe.message
                    it.copy(userMessage = messages)
                }
            } finally {
                _imagesUiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun userMessageShown() {
        _imagesUiState.update {
            it.copy(userMessage = null)
        }
    }

}