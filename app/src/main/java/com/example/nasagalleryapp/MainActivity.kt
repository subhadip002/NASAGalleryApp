package com.example.nasagalleryapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.nasagalleryapp.databinding.ActivityMainBinding
import com.example.nasagalleryapp.ui.ImageViewModel
import com.example.nasagalleryapp.util.MessageType
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }
    private val viewModel: ImageViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var isOffline = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                observeNetworkStatus()
                observeUserMessage()
            }
        })
    }

    private fun observeUserMessage() {
        lifecycleScope.launch {
            viewModel.imagesUiState.map { it.errorMessage }.filterNotNull().collect {
                showSnackbar(it, MessageType.NEGATIVE)
                viewModel.userMessageShown()
            }
        }
    }

    private fun observeNetworkStatus() {
        lifecycleScope.launch {
            viewModel.isOffline.collect {
                viewModel.setImages()
                if (it) {
                    showInternetUnavailableMessage()
                } else if (isOffline) {
                    showInternetAvailableMessage()
                }
                isOffline = it
            }
        }
    }

    private fun showInternetUnavailableMessage() {
        showSnackbar(getString(R.string.internet_unavailable_message), MessageType.NEGATIVE)
    }

    private fun showInternetAvailableMessage() {
        showSnackbar(getString(R.string.internet_available_message), MessageType.POSITIVE)
    }

    private fun showSnackbar(message: String, type: MessageType = MessageType.NEUTRAL) {
        Snackbar.make(
            binding.root, message, Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(
                ResourcesCompat.getColor(
                    resources, when (type) {
                        MessageType.POSITIVE -> android.R.color.holo_green_dark
                        MessageType.NEGATIVE -> android.R.color.holo_red_dark
                        else -> android.R.color.white
                    }, null
                )
            )
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}