package com.example.nasagalleryapp

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.nasagalleryapp.databinding.ActivityMainBinding
import com.example.nasagalleryapp.ui.ImageGridViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    private val viewModel: ImageGridViewModel by viewModels()

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setNetworkObservers()
    }

    private fun setNetworkObservers() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, viewModel.networkCallback)

        if (connectivityManager.activeNetwork == null) {
            showInternetUnavailableMessage()
        }
        viewModel.isNetworkAvailable.observe(this) {
            if (it == false) {
                showInternetUnavailableMessage()
            }
        }
    }

    private fun showInternetUnavailableMessage() {
        Snackbar.make(
            binding.root,
            getString(R.string.internet_unavailable_message),
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(
                ResourcesCompat.getColor(
                    resources,
                    android.R.color.holo_red_dark,
                    null
                )
            )
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}