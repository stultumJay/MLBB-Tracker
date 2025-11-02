package com.example.kotlinandroidoverlayapponhomescreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinandroidoverlayapponhomescreen.ui.screens.MainScreen
import com.example.kotlinandroidoverlayapponhomescreen.ui.theme.MLBBTrackerTheme
import com.example.kotlinandroidoverlayapponhomescreen.overlay.OverlayService
import com.example.kotlinandroidoverlayapponhomescreen.utils.Constants
import com.example.kotlinandroidoverlayapponhomescreen.viewmodel.TimerViewModel

class MainActivity : ComponentActivity() {
    private val activityResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (Settings.canDrawOverlays(this)) {
                startService(Intent(this, OverlayService::class.java))
            }
        }
    }
    
    private var overlayTapReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLBBTrackerTheme {
                val vm: TimerViewModel = viewModel()
                
                // Register receiver for overlay taps
                LaunchedEffect(Unit) {
                    overlayTapReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            if (intent?.action == Constants.ACTION_OVERLAY_TIMER_TAP) {
                                val index = intent.getIntExtra(Constants.EXTRA_TIMER_INDEX, -1)
                                if (index in 0..4) {
                                    vm.onOverlayTimerTapped(index)
                                }
                            }
                        }
                    }
                    registerReceiver(overlayTapReceiver, IntentFilter(Constants.ACTION_OVERLAY_TIMER_TAP))
                }
                
                MainScreen(
                    requestOverlayPermission = { 
                        activityResultLauncher.launch(
                            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                        )
                    }
                )
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        overlayTapReceiver?.let { unregisterReceiver(it) }
    }
}