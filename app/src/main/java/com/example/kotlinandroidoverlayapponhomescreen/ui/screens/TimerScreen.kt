package com.example.kotlinandroidoverlayapponhomescreen.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinandroidoverlayapponhomescreen.data.DEFAULT_BATTLE_SPELLS
import com.example.kotlinandroidoverlayapponhomescreen.viewmodel.TimerViewModel
import com.example.kotlinandroidoverlayapponhomescreen.utils.TimerUtils
import com.example.kotlinandroidoverlayapponhomescreen.utils.Constants
import com.example.kotlinandroidoverlayapponhomescreen.overlay.OverlayService
import android.content.Intent
import android.provider.Settings

@Composable
fun TimerScreen(requestOverlayPermission: () -> Unit, vm: TimerViewModel = viewModel()) {
    val timers by vm.timers.collectAsState()
    val context = LocalContext.current

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        // Top: 3 + 2 grid, labels above each
        Column {
            // Row 1: three circles (GOLD, EXP, JUNGLE)
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                TimerCircle(timers[0], onClick = { vm.onTimerClicked(0) }, vm = vm)
                TimerCircle(timers[1], onClick = { vm.onTimerClicked(1) }, vm = vm)
                TimerCircle(timers[2], onClick = { vm.onTimerClicked(2) }, vm = vm)
            }
            // Row 2: two circles (MID, ROAM)
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                TimerCircle(timers[3], onClick = { vm.onTimerClicked(3) }, vm = vm)
                TimerCircle(timers[4], onClick = { vm.onTimerClicked(4) }, vm = vm)
            }
        }

        // Middle: SET SPELL buttons for each timer
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            timers.forEachIndexed { index, timer ->
                Button(
                    onClick = { vm.openSpellPicker(index) },
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text("${timer.label}\n${timer.selectedSpell.displayName}", fontSize = 10.sp)
                }
            }
        }

        // Bottom: SHOW / RESET (centered)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val showing by vm.overlayShowing.collectAsState()
            Button(onClick = {
                if (!showing) {
                    if (Settings.canDrawOverlays(context)) {
                        context.startService(Intent(context, OverlayService::class.java))
                        vm.toggleOverlay()
                    } else {
                        requestOverlayPermission()
                    }
                } else {
                    context.stopService(Intent(context, OverlayService::class.java))
                    vm.toggleOverlay()
                }
            }) {
                Text(if (showing) "HIDE" else "SHOW")
            }
            Button(onClick = { vm.resetDefaults() }) {
                Text("RESET")
            }
        }
    }

    // Spell picker dialog
    if (vm.isSpellPickerOpen.collectAsState().value) {
        val spells = DEFAULT_BATTLE_SPELLS
        AlertDialog(
            onDismissRequest = { vm.closeSpellPicker() },
            confirmButton = {
                Button(onClick = { vm.closeSpellPicker() }) { 
                    Text("CLOSE") 
                }
            },
            title = {
                Text(
                    "Select Spell",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    spells.forEach { s ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    vm.selectSpellForActiveTimer(s)
                                    vm.closeSpellPicker()
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = s.displayName,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${s.cooldown}s",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TimerCircle(timer: com.example.kotlinandroidoverlayapponhomescreen.data.BattleTimer, onClick: () -> Unit, vm: TimerViewModel) {
    val isReady = TimerUtils.isTimerReady(timer.remainingSeconds)
    val iconName = if (isReady) "hourglass_empty" else "hourglass_full"
    val resId = LocalContext.current.resources.getIdentifier(iconName, "drawable", LocalContext.current.packageName)
    
    // Animation for button press
    val scale by animateFloatAsState(
        targetValue = if (timer.isRunning) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "timer_scale"
    )
    
    // Animate color changes
    val timerColor by animateColorAsState(
        targetValue = when {
            isReady -> Color(Constants.TIMER_READY_COLOR)
            timer.isRunning -> Color(Constants.TIMER_RUNNING_COLOR)
            else -> Color(Constants.TIMER_PAUSED_COLOR)
        },
        animationSpec = tween(durationMillis = 300),
        label = "timer_color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Text(
            text = timer.label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Box(
            modifier = Modifier
                .size(96.dp)
                .scale(scale)
                .clip(CircleShape)
                .clickable { onClick() }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glowing effect for ready state
            if (isReady) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(Constants.TIMER_READY_COLOR).copy(alpha = 0.3f),
                                    Color(Constants.TIMER_READY_COLOR).copy(alpha = 0.0f)
                                )
                            )
                        )
                )
            }
            
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(timerColor)
            )
        }

        // For MID and ROAM show Has PYT checkbox
        if (timer.label == "MID" || timer.label == "ROAM") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Checkbox(
                    checked = timer.hasPYT,
                    onCheckedChange = { checked ->
                        vm.setHasPYT(timer.id, checked)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    "Has PYT",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Text(
            text = "${TimerUtils.formatTime(timer.remainingSeconds)}s",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
