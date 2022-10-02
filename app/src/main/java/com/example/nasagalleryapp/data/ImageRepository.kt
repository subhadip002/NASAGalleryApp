package com.example.nasagalleryapp.data

import com.example.nasagalleryapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val imageLocalDataSource: ImageLocalDataSource,
    @ApplicationScope private val externalScope: CoroutineScope
) {
    private val imagesMutex = Mutex()
    private var images: List<ImageApiModel> = emptyList()

    suspend fun getImages(): List<ImageApiModel> {
        return if (images.isEmpty()) {
            withContext(externalScope.coroutineContext) {
                imageLocalDataSource.getImages().also { data ->
                    imagesMutex.withLock {
                        images = data
                    }
                }
            }
        } else imagesMutex.withLock { this.images }
    }
}