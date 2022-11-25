package com.example.nasagalleryapp.di

import com.example.nasagalleryapp.util.ConnectivityManagerNetworkMonitor
import com.example.nasagalleryapp.util.NetworkMonitor
import dagger.Binds
import dagger.Module

@Module
interface NetworkModule {
    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}