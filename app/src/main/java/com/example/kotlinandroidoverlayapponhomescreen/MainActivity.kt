package com.example.kotlinandroidoverlayapponhomescreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.kotlinandroidoverlayapponhomescreen.ui.screens.MainScreen
import com.example.kotlinandroidoverlayapponhomescreen.ui.theme.MLBBTrackerTheme
import com.example.kotlinandroidoverlayapponhomescreen.overlay.OverlayService

class MainActivity : ComponentActivity() {
    private val activityResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (Settings.canDrawOverlays(this)) {
                startService(Intent(this, OverlayService::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLBBTrackerTheme {
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
}