package com.example.kotlinandroidoverlayapponhomescreen

import com.example.kotlinandroidoverlayapponhomescreen.data.BattleSpell
import com.example.kotlinandroidoverlayapponhomescreen.data.BattleTimer
import com.example.kotlinandroidoverlayapponhomescreen.viewmodel.TimerViewModel
import org.junit.Test
import org.junit.Assert.*

class TimerViewModelTest {
    
    @Test
    fun testTimerCreation() {
        val viewModel = TimerViewModel()
        val timers = viewModel.timers.value
        
        assertEquals(5, timers.size)
        assertEquals("GOLD", timers[0].label)
        assertEquals("EXP", timers[1].label)
        assertEquals("JUNGLE", timers[2].label)
        assertEquals("MID", timers[3].label)
        assertEquals("ROAM", timers[4].label)
    }
    
    @Test
    fun testTimerToggle() {
        val viewModel = TimerViewModel()
        val initialTimers = viewModel.timers.value
        
        // Toggle first timer
        viewModel.onTimerClicked(0)
        val updatedTimers = viewModel.timers.value
        
        assertTrue(updatedTimers[0].isRunning)
        assertFalse(initialTimers[0].isRunning)
    }
    
    @Test
    fun testSpellSelection() {
        val viewModel = TimerViewModel()
        val newSpell = BattleSpell("test", "Test Spell", 60.0, "test")
        
        viewModel.selectSpellForActiveTimer(newSpell)
        val timers = viewModel.timers.value
        
        assertEquals(newSpell, timers[0].selectedSpell)
        assertEquals(60.0, timers[0].maxSeconds, 0.1)
    }
    
    @Test
    fun testPYTReduction() {
        val viewModel = TimerViewModel()
        val timers = viewModel.timers.value
        val midTimer = timers[3] // MID timer
        
        // Enable PYT for MID timer
        viewModel.setHasPYT(3, true)
        val updatedTimers = viewModel.timers.value
        
        assertTrue(updatedTimers[3].hasPYT)
        // Should be 20% reduction (120 * 0.8 = 96)
        assertEquals(96.0, updatedTimers[3].maxSeconds, 0.1)
    }
}
