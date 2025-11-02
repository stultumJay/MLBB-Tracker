package com.example.kotlinandroidoverlayapponhomescreen.data

data class BattleSpell(
    val id: String,           // e.g. "flicker"
    val displayName: String,  // e.g. "Flicker"
    val cooldown: Double,     // seconds e.g. 120.0
    val drawableName: String  // e.g. "flicker" (without ext for resource lookup)
)

data class BattleTimer(
    val id: Int,              // 0..4
    val label: String,        // "GOLD", "EXP", "JUNGLE", "MID", "ROAM"
    var selectedSpell: BattleSpell,
    var maxSeconds: Double,
    var remainingSeconds: Double,
    var isRunning: Boolean = false,
    var hasPYT: Boolean = false
)

data class HeroUltimate(
    val name: String,
    val cdLevel1: Int,
    val cdLevel2: Int,
    val cdLevel3: Int
)

data class Hero(
    val id: Int,
    val name: String,
    val image: String, // drawable name (without extension)
    val ultimate: HeroUltimate
)
