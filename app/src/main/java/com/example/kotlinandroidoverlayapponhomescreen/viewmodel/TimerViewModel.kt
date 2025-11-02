package com.example.kotlinandroidoverlayapponhomescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinandroidoverlayapponhomescreen.data.*
import com.example.kotlinandroidoverlayapponhomescreen.repository.SpellRepository
import com.example.kotlinandroidoverlayapponhomescreen.utils.TimerUtils
import com.example.kotlinandroidoverlayapponhomescreen.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TimerViewModel: ViewModel() {
    private val spellRepository = SpellRepository()
    private val defaultSpells = spellRepository.getAllSpells()
    private val _timers = MutableStateFlow<List<BattleTimer>>(TimerUtils.createDefaultTimers())
    val timers: StateFlow<List<BattleTimer>> = _timers.asStateFlow()

    private val _isSpellPickerOpen = MutableStateFlow(false)
    val isSpellPickerOpen = _isSpellPickerOpen.asStateFlow()

    private val _overlayShowing = MutableStateFlow(false)
    val overlayShowing = _overlayShowing.asStateFlow()

    private val _selectedTimerIndex = MutableStateFlow(0)
    val selectedTimerIndex = _selectedTimerIndex.asStateFlow()

    private var tickerJob: Job? = null

    init {
        startTicker()
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (isActive) {
                delay(Constants.TIMER_UPDATE_INTERVAL_MS)
                val currentTimers = _timers.value.toMutableList()
                var hasChanges = false
                
                currentTimers.forEachIndexed { index, timer ->
                    if (timer.isRunning && timer.remainingSeconds > 0.0) {
                        val newRemaining = maxOf(0.0, timer.remainingSeconds - 0.1)
                        val newIsRunning = newRemaining > 0.0
                        currentTimers[index] = timer.copy(
                            remainingSeconds = newRemaining,
                            isRunning = newIsRunning
                        )
                        hasChanges = true
                    }
                }
                
                if (hasChanges) {
                    _timers.value = currentTimers
                }
            }
        }
    }

    fun onTimerClicked(index: Int) {
        val current = _timers.value.toMutableList()
        val t = current[index]
        // toggle start/stop: if the timer is zero, reset to maxSeconds first
        if (t.remainingSeconds <= 0.0) {
            t.remainingSeconds = t.maxSeconds
        }
        t.isRunning = !t.isRunning
        current[index] = t
        _timers.value = current
    }

    fun openSpellPicker(index: Int) { 
        _selectedTimerIndex.value = index
        _isSpellPickerOpen.value = true 
    }
    
    fun closeSpellPicker() { _isSpellPickerOpen.value = false }

    fun selectSpellForActiveTimer(spell: BattleSpell) {
        val idx = _selectedTimerIndex.value
        val current = _timers.value.toMutableList()
        val t = current[idx]
        t.selectedSpell = spell
        t.maxSeconds = TimerUtils.calculateCooldownWithPYT(spell, t.hasPYT)
        t.remainingSeconds = t.maxSeconds
        current[idx] = t
        _timers.value = current
    }

    fun setHasPYT(timerId: Int, has: Boolean) {
        val current = _timers.value.toMutableList()
        val t = current[timerId]
        t.hasPYT = has
        // apply reduction if a spell is already selected
        t.maxSeconds = TimerUtils.calculateCooldownWithPYT(t.selectedSpell, has)
        if (t.remainingSeconds > t.maxSeconds) t.remainingSeconds = t.maxSeconds
        current[timerId] = t
        _timers.value = current
    }

    fun toggleOverlay() { _overlayShowing.value = !_overlayShowing.value }

    fun resetDefaults() {
        _timers.value = TimerUtils.createDefaultTimers()
    }

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
    }
}
