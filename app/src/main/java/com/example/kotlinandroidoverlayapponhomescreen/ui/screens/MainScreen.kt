package com.example.kotlinandroidoverlayapponhomescreen.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(requestOverlayPermission: () -> Unit) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Timer", "Heroes")

    Scaffold { padding ->
        Column(Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index, onClick = { tabIndex = index }) {
                        Text(title, modifier = Modifier.padding(16.dp))
                    }
                }
// Define constants at the top level for better readability and reuse
                private const val TIMER_TAB_INDEX = 0
                private const val HEROES_TAB_INDEX = 1
                private val TABS = listOf("Timer", "Heroes")

                @Composable
                fun MainScreen(
                    requestOverlayPermission: () -> Unit,
                    modifier: Modifier = Modifier // Add a modifier parameter
                ) {
                    var tabIndex by remember { mutableIntStateOf(TIMER_TAB_INDEX) }

                    Scaffold(
                        modifier = modifier // Apply the modifier to the Scaffold
                    ) { innerPadding ->
                        Column(
                            // Apply padding from the Scaffold and fill the available size
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            TabRow(selectedTabIndex = tabIndex) {
                                TABS.forEachIndexed { index, title ->
                                    Tab(
                                        selected = (tabIndex == index),
                                        onClick = { tabIndex = index },
                                        // Use the dedicated 'text' parameter for the Tab
                                        text = { Text(text = title) }
                                    )
                                }
                            }

                            // Use the named constants for better readability
                            when (tabIndex) {
                                TIMER_TAB_INDEX -> TimerScreen(requestOverlayPermission = requestOverlayPermission)
                                HEROES_TAB_INDEX -> HeroesScreen()
                            }
                        }
                    }
                }
            }

            when (tabIndex) {
                0 -> TimerScreen(requestOverlayPermission)
                1 -> HeroesScreen()
            }
        }
    }
}
