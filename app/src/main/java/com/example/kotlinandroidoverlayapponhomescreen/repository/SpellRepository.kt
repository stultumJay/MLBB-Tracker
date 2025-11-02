package com.example.kotlinandroidoverlayapponhomescreen.repository

import com.example.kotlinandroidoverlayapponhomescreen.data.BattleSpell
import com.example.kotlinandroidoverlayapponhomescreen.data.DEFAULT_BATTLE_SPELLS

class SpellRepository {
    
    fun getAllSpells(): List<BattleSpell> = DEFAULT_BATTLE_SPELLS
    
    fun getSpellById(id: String): BattleSpell? = 
        DEFAULT_BATTLE_SPELLS.find { it.id == id }
    
    fun getSpellByName(name: String): BattleSpell? = 
        DEFAULT_BATTLE_SPELLS.find { it.displayName.equals(name, ignoreCase = true) }
}
