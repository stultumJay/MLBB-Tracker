package com.example.kotlinandroidoverlayapponhomescreen.utils

object Constants {
    const val TIMER_UPDATE_INTERVAL_MS = 100L
    const val PYT_COOLDOWN_REDUCTION = 0.8
    const val TIMER_READY_THRESHOLD = 0.01
    
    // Timer labels
    const val GOLD_LABEL = "GOLD"
    const val EXP_LABEL = "EXP"
    const val JUNGLE_LABEL = "JUNGLE"
    const val MID_LABEL = "MID"
    const val ROAM_LABEL = "ROAM"
    
    // Default spell IDs
    const val FLICKER_SPELL_ID = "flicker"
    const val RETRIBUTION_SPELL_ID = "retribution"
    
    // Colors
    const val TIMER_READY_COLOR = 0xFF4CAF50
    const val TIMER_RUNNING_COLOR = 0xFFF44336
    
    // Overlay
    const val OVERLAY_NOTIFICATION_ID = 1
    const val OVERLAY_CHANNEL_ID = "mlbb_overlay_channel"
    const val OVERLAY_CHANNEL_NAME = "Overlay"
}
