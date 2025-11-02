package com.example.kotlinandroidoverlayapponhomescreen.repository

import android.content.Context
import com.example.kotlinandroidoverlayapponhomescreen.data.Hero
import com.example.kotlinandroidoverlayapponhomescreen.data.HeroUltimate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class HeroRepository(private val context: Context) {
    
    suspend fun loadHeroes(): List<Hero> = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("heroes.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            val heroes = mutableListOf<Hero>()
            
            for (i in 0 until arr.length()) {
                val heroObj = arr.getJSONObject(i)
                val ultimateObj = heroObj.getJSONObject("ultimate")
                
                val hero = Hero(
                    id = heroObj.getInt("id"),
                    name = heroObj.getString("name"),
                    image = heroObj.getString("image"),
                    ultimate = HeroUltimate(
                        name = ultimateObj.getString("name"),
                        cdLevel1 = ultimateObj.getInt("cdLevel1"),
                        cdLevel2 = ultimateObj.getInt("cdLevel2"),
                        cdLevel3 = ultimateObj.getInt("cdLevel3")
                    )
                )
                heroes.add(hero)
            }
            
            heroes.sortedBy { it.name }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
