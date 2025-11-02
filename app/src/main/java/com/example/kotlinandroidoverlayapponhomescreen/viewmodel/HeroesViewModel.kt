package com.example.kotlinandroidoverlayapponhomescreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.kotlinandroidoverlayapponhomescreen.data.Hero
import com.example.kotlinandroidoverlayapponhomescreen.repository.HeroRepository

class HeroesViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = HeroRepository(app)
    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes = _heroes.asStateFlow()

    init {
        loadHeroes()
    }

    private fun loadHeroes() {
        viewModelScope.launch {
            _heroes.value = repository.loadHeroes()
        }
    }
}
