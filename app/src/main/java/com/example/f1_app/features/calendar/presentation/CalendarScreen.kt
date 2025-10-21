package com.example.f1_app.features.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
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
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.load(2025) }

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
                        text = "Season Calendar",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = {}, label = { Text("Season: 2025") })
                        AssistChip(onClick = {}, label = { Text("Region: All") })
                        AssistChip(onClick = {}, label = { Text("Session: Race") })
                    }

                    Spacer(Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val months = state.value.months.toSortedMap(compareBy { it.value })
                        months.forEach { (month, meetings) ->
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                MonthHeader(month = month.getDisplayName(TextStyle.FULL, Locale.getDefault()), count = meetings.size)
                            }
                            items(meetings) { meeting ->
                                MeetingCard(title = meeting.meetingName)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(month: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF2A2831))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = month, color = Color(0xFFE6E0F8), fontWeight = FontWeight.SemiBold)
        Text(text = "$count GPs", color = Color(0xFFBEB8D1))
    }
}

@Composable
private fun MeetingCard(title: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1D26)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(0xFF6C49FF))
            )
            Spacer(Modifier.width(12.dp))
            Text(text = title, color = Color(0xFFE6E0F8), fontWeight = FontWeight.Medium)
        }
    }
}
