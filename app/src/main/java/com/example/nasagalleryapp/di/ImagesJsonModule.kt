package com.example.nasagalleryapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

private const val FILE_NAME = "data.json"

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ImagesJson

@Module
object ImagesJsonModule {

    @ImagesJson
    @Provides
    fun provideImagesJson(context: Context): String {
        val inputStream = context.assets.open(FILE_NAME)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}