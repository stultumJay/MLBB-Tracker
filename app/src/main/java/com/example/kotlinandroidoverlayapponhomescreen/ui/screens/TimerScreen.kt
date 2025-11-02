package com.example.kotlinandroidoverlayapponhomescreen.ui.screens

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        AlertDialog(onDismissRequest = { vm.closeSpellPicker() }, confirmButton = {
            Button(onClick = { vm.closeSpellPicker() }) { Text("CLOSE") }
        }, text = {
            Column {
                spells.forEach { s ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.selectSpellForActiveTimer(s) }
                        .padding(8.dp)) {
                        Text("${s.displayName} ${s.cooldown}s")
                    }
                }
            }
        })
    }
}

@Composable
fun TimerCircle(timer: com.example.kotlinandroidoverlayapponhomescreen.data.BattleTimer, onClick: () -> Unit, vm: TimerViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Text(timer.label)
        Box(modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(8.dp),
            contentAlignment = Alignment.Center) {
            // draw either empty or full hourglass depending on remaining
            val iconName = if (TimerUtils.isTimerReady(timer.remainingSeconds)) "hourglass_empty" else "hourglass_full"
            val resId = LocalContext.current.resources.getIdentifier(iconName, "drawable", LocalContext.current.packageName)
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                colorFilter = if (TimerUtils.isTimerReady(timer.remainingSeconds)) ColorFilter.tint(Color(Constants.TIMER_READY_COLOR)) else ColorFilter.tint(Color(Constants.TIMER_RUNNING_COLOR))
            )
        }

        // For MID and ROAM show Has PYT checkbox
        if (timer.label == "MID" || timer.label == "ROAM") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = timer.hasPYT, onCheckedChange = { checked ->
                    vm.setHasPYT(timer.id, checked)
                })
                Text("Has PYT")
            }
        }

        Text("${TimerUtils.formatTime(timer.remainingSeconds)}s")
    }
}
