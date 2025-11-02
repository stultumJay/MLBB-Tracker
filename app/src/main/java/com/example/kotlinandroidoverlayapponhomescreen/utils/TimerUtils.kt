package com.example.kotlinandroidoverlayapponhomescreen.utils

import com.example.kotlinandroidoverlayapponhomescreen.data.BattleSpell
import com.example.kotlinandroidoverlayapponhomescreen.data.BattleTimer
import com.example.kotlinandroidoverlayapponhomescreen.data.DEFAULT_BATTLE_SPELLS

object TimerUtils {
    
    fun createDefaultTimers(): List<BattleTimer> {
        val flicker = DEFAULT_BATTLE_SPELLS.first { it.id == "flicker" }
        val retribution = DEFAULT_BATTLE_SPELLS.first { it.id == "retribution" }
        
        return listOf(
            BattleTimer(0, "GOLD", flicker, flicker.cooldown, flicker.cooldown),
            BattleTimer(1, "EXP", flicker, flicker.cooldown, flicker.cooldown),
            BattleTimer(2, "JUNGLE", retribution, retribution.cooldown, retribution.cooldown),
            BattleTimer(3, "MID", flicker, flicker.cooldown, flicker.cooldown),
            BattleTimer(4, "ROAM", flicker, flicker.cooldown, flicker.cooldown)
        )
    }
    
    fun calculateCooldownWithPYT(spell: BattleSpell, hasPYT: Boolean): Double {
        return if (hasPYT) spell.cooldown * Constants.PYT_COOLDOWN_REDUCTION else spell.cooldown
    }
    
    fun formatTime(seconds: Double): String {
        return "%.1f".format(seconds)
    }
    
    fun isTimerReady(remainingSeconds: Double): Boolean {
        return remainingSeconds <= Constants.TIMER_READY_THRESHOLD
    }
}
