package com.example.kotlinandroidoverlayapponhomescreen.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinandroidoverlayapponhomescreen.viewmodel.HeroesViewModel

@Composable
fun HeroesScreen(vm: HeroesViewModel = viewModel()) {
    val heroes by vm.heroes.collectAsState()
    var expandedId by remember { mutableStateOf<Int?>(null) }

    LazyVerticalGrid(columns = GridCells.Fixed(5), userScrollEnabled = true, modifier = Modifier.fillMaxSize()) {
        itemsIndexed(heroes) { index, hero ->
            val col = index % 5
            val isExpanded = (expandedId == hero.id)
            Box(modifier = Modifier
                .padding(4.dp)
                .animateContentSize()
                .clickable { expandedId = if (isExpanded) null else hero.id }
            ) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(4.dp)) {
                        // hero image
                        val resId = LocalContext.current.resources.getIdentifier(hero.image, "drawable", LocalContext.current.packageName)
                        Image(painter = painterResource(resId), contentDescription = hero.name, modifier = Modifier.size(48.dp))
                        Text(hero.name)
                        if (isExpanded) {
                            // Expanded area: show ultimate cooldown details
                            Column(
                                modifier = Modifier.padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = hero.ultimate.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "L1: ${hero.ultimate.cdLevel1}s",
                                    fontSize = 8.sp
                                )
                                Text(
                                    text = "L2: ${hero.ultimate.cdLevel2}s",
                                    fontSize = 8.sp
                                )
                                Text(
                                    text = "L3: ${hero.ultimate.cdLevel3}s",
                                    fontSize = 8.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
