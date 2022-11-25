package com.example.nasagalleryapp.di

import android.content.Context
import com.example.nasagalleryapp.MainActivity
import com.example.nasagalleryapp.ui.image_detail.ImageCollectionFragment
import com.example.nasagalleryapp.ui.image_detail.ImageDetailFragment
import com.example.nasagalleryapp.ui.image_grid.ImagesGridFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CoroutinesDispatchersModule::class, CoroutinesScopesModule::class,
        ImagesJsonModule::class, NetworkModule::class, ViewModelModule::class]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(activity: ImageCollectionFragment)
    fun inject(activity: ImageDetailFragment)
    fun inject(activity: ImagesGridFragment)
}