package com.example.kotlinandroidoverlayapponhomescreen.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlinandroidoverlayapponhomescreen.data.BattleSpell
import com.example.kotlinandroidoverlayapponhomescreen.data.BattleTimer
import com.example.kotlinandroidoverlayapponhomescreen.data.DEFAULT_BATTLE_SPELLS
import org.json.JSONArray
import org.json.JSONObject

class TimerStorage(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_TIMERS = "timers"
        private const val KEY_OVERLAY_SHOWING = "overlay_showing"
    }
    
    fun saveTimers(timers: List<BattleTimer>) {
        val jsonArray = JSONArray()
        timers.forEach { timer ->
            val json = JSONObject().apply {
                put("id", timer.id)
                put("label", timer.label)
                put("spellId", timer.selectedSpell.id)
                put("maxSeconds", timer.maxSeconds)
                put("remainingSeconds", timer.remainingSeconds)
                put("isRunning", timer.isRunning)
                put("hasPYT", timer.hasPYT)
            }
            jsonArray.put(json)
        }
        prefs.edit().putString(KEY_TIMERS, jsonArray.toString()).apply()
    }
    
    fun loadTimers(): List<BattleTimer>? {
        val jsonString = prefs.getString(KEY_TIMERS, null) ?: return null
        return try {
            val jsonArray = JSONArray(jsonString)
            val timers = mutableListOf<BattleTimer>()
            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                val spellId = json.getString("spellId")
                val spell = DEFAULT_BATTLE_SPELLS.find { it.id == spellId }
                    ?: DEFAULT_BATTLE_SPELLS.first()
                
                timers.add(
                    BattleTimer(
                        id = json.getInt("id"),
                        label = json.getString("label"),
                        selectedSpell = spell,
                        maxSeconds = json.getDouble("maxSeconds"),
                        remainingSeconds = json.getDouble("remainingSeconds"),
                        isRunning = json.getBoolean("isRunning"),
                        hasPYT = json.getBoolean("hasPYT")
                    )
                )
            }
            timers
        } catch (e: Exception) {
            null
        }
    }
    
    fun saveOverlayShowing(showing: Boolean) {
        prefs.edit().putBoolean(KEY_OVERLAY_SHOWING, showing).apply()
    }
    
    fun loadOverlayShowing(): Boolean = prefs.getBoolean(KEY_OVERLAY_SHOWING, false)
}

