package com.example.f1_app.features.drivers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale


@Composable
fun DriversScreen(viewModel: DriverViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsState()
    val year = Calendar.getInstance().get(Calendar.YEAR)

    LaunchedEffect(Unit) { viewModel.load(year) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF16151B))
            .padding(16.dp)
    ) {
        when {
            state.value.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.value.error != null -> {
                Text(
                    text = state.value.error ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Drivers — $year",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = {}, label = { Text(text = "Season: $year", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))})
                        AssistChip(onClick = {}, label = { Text("Filter: All", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))})
                        AssistChip(onClick = {}, label = { Text("Sort: Default", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))})
                    }

                    Spacer(Modifier.height(12.dp))

                    val groups = state.value.drivers
                        .groupBy { it.nationality ?: "Unknown" }
                        .toSortedMap(String.CASE_INSENSITIVE_ORDER)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        content = {
                            groups.forEach { (nation, drivers) ->
                                item(span = { GridItemSpan(2) }) {
                                    SectionHeader(title = nation, count = drivers.size)
                                }
                                items(drivers) { driver ->
                                    DriverCard(driverName = driver.name ?: "Sin nombre",
                                               abbr = driver.abbr ?: "",
                                               number = driver.number ?: 0,
                                                imageUrl = driver.image)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF2A2831))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color(0xFFE6E0F8), fontWeight = FontWeight.SemiBold)
        Text(text = "$count drivers", color = Color(0xFFBEB8D1))
    }
}

@Composable
private fun DriverCard(driverName: String, abbr: String, number: Int, imageUrl: String?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1D26)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Driver image",
                        modifier = Modifier
                            .matchParentSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0x66000000)),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0xFF6C49FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = abbr,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = driverName,
                    color = Color(0xFFE6E0F8),
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$abbr • #$number",
                    color = Color(0xFFBEB8D1),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
