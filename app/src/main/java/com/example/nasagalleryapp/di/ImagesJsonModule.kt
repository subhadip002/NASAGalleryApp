package com.example.nasagalleryapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

private const val FILE_NAME = "data.json"

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ImagesJson

@Module
@InstallIn(SingletonComponent::class)
object ImagesJsonModule {

    @ImagesJson
    @Provides
    fun provideImagesJson(@ApplicationContext context: Context): String {
        val inputStream = context.assets.open(FILE_NAME)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}