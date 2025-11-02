package com.example.kotlinandroidoverlayapponhomescreen.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// Constants for better readability
private const val TIMER_TAB_INDEX = 0
private const val HEROES_TAB_INDEX = 1
private val TABS = listOf("Timer", "Heroes")

@Composable
fun MainScreen(
    requestOverlayPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tabIndex by remember { mutableIntStateOf(TIMER_TAB_INDEX) }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                TABS.forEachIndexed { index, title ->
                    Tab(
                        selected = (tabIndex == index),
                        onClick = { tabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            // Show the appropriate screen based on selected tab
            when (tabIndex) {
                TIMER_TAB_INDEX -> TimerScreen(requestOverlayPermission = requestOverlayPermission)
                HEROES_TAB_INDEX -> HeroesScreen()
            }
        }
    }
}
