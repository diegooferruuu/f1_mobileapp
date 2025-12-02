package com.example.f1_app.features.results.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.time.Year

@Composable
fun ResultsScreen(viewModel: ResultsViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadInitialLatestRace(Year.now().value) }

    // Mostrar resultados en pantalla completa cuando hay selectedSession
    val showFullResults = remember(state.selectedSessionKey, state.resultsEnriched) {
        state.selectedSessionKey != null && state.resultsEnriched.isNotEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF16151B))
    ) {
        if (showFullResults) {
            ResultsFullScreen(state = state, onBack = {
                // limpiar selección para volver a lista sin recargar
                viewModel.clearSelection()
            })
        } else {
            // Lista de sesiones (ocupa gran parte de pantalla)
            SessionSelectionScreen(state = state, onTab = { tab ->
                viewModel.setTab(tab)
                viewModel.updateFilters(location = null, year = Year.now().value, sessionType = tab.apiValue)
                viewModel.search()
            }, onOpen = { key -> viewModel.loadResults(key) })
        }
    }
}

@Composable
private fun PodiumLayout(rows: List<ResultRow>) {
    val top3 = rows.sortedBy { it.position }.take(3)
    // Alturas fijas para P2/P3, P1 ocupa la suma exacta (evita huecos)
    val cardHeight = 110.dp
    val columnSpacing = 12.dp
    val totalHeight = cardHeight * 2 + columnSpacing
    Row(Modifier.fillMaxWidth().height(totalHeight), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        // P1 grande a la izquierda
        if (top3.isNotEmpty()) {
            PodiumCard(
                position = "P1",
                name = top3[0].driverName ?: "",
                team = top3[0].team,
                points = top3[0].points,
                gap = formatGap(top3[0].gapToLeader),
                accent = Color(0xFF0E1233),
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
        // P2 y P3 apilados a la derecha
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(columnSpacing)) {
            if (top3.size > 1) PodiumCard(
                position = "P2",
                name = top3[1].driverName ?: "",
                team = top3[1].team,
                points = top3[1].points,
                gap = formatGap(top3[1].gapToLeader),
                accent = Color(0xFFD40000),
                modifier = Modifier.fillMaxWidth().height(cardHeight)
            )
            if (top3.size > 2) PodiumCard(
                position = "P3",
                name = top3[2].driverName ?: "",
                team = top3[2].team,
                points = top3[2].points,
                gap = formatGap(top3[2].gapToLeader),
                accent = Color(0xFF2F2F2F),
                modifier = Modifier.fillMaxWidth().height(cardHeight)
            )
        }
    }
}

@Composable
private fun PodiumCard(position: String, name: String, team: String?, points: Int, gap: String, accent: Color, modifier: Modifier = Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = accent), modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(position, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
            if (!team.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(team, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            Text(gap, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            Text("$points pts", color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ResultsTable(rows: List<ResultRow>) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                HeaderCell("POS", 0.8f)
                HeaderCell("PILOTO", 2.1f)
                HeaderCell("EQUIPO", 2f)
                HeaderCell("DIF.", 1.2f)
                HeaderCell("PTS", 0.8f)
            }
            rows.forEachIndexed { idx, r ->
                val bg = when (r.position) {
                    4 -> Color(0xFF161616)
                    5 -> Color(0xFF161616)
                    else -> Color.Transparent
                }
                Row(Modifier.fillMaxWidth().background(bg).padding(vertical = 4.dp)) {
                    BodyCell("P${r.position}", 0.8f, highlight = r.position != null && r.position <= 3)
                    BodyCell(r.driverName ?: "#${r.driverNumber}", 2.1f)
                    BodyCell(r.team ?: "—", 2f, dim = true)
                    BodyCell(formatGap(r.gapToLeader), 1.2f, dim = true)
                    BodyCell("${r.points}", 0.8f)
                }
            }
        }
    }
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        color = Color(0xFFBEB8D1),
        modifier = Modifier.weight(weight),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun RowScope.BodyCell(
    text: String,
    weight: Float,
    highlight: Boolean = false,
    dim: Boolean = false
) {
    val color = when {
        highlight -> Color(0xFFD40000)
        dim -> Color(0xFFBEB8D1)
        else -> Color(0xFFE6E0F8)
    }
    Text(
        text = text,
        color = color,
        modifier = Modifier.weight(weight)
    )
}

private fun formatGap(gap: Double?): String = when {
    gap == null -> "—"
    gap == 0.0 -> "+0.000s"
    else -> String.format("+%.3fs", gap)
}

@Composable
private fun SessionSelectionScreen(
    state: ResultsUiState,
    onTab: (SessionTab) -> Unit,
    onOpen: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Gran Premio de ${state.headerTitle}",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
            Text(text = state.headerSub, color = Color(0xFFBEB8D1))
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SessionTab.values().forEach { tab ->
                    FilterChip(
                        selected = state.activeTab == tab,
                        onClick = { onTab(tab) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
        item {
            Text(text = if (state.activeTab == SessionTab.Qualifying) "Clasificaciones del año" else "Carreras del año", color = Color(0xFFE6E0F8), fontWeight = FontWeight.SemiBold)
        }
        if (state.isLoading) {
            item { CircularProgressIndicator() }
        } else if (state.error != null) {
            item { Text(state.error ?: "Error", color = Color.Red) }
        } else {
            itemsIndexed(state.sessions) { _, s ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1D26)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            // Mostrar solo nombre del GP (location) y año, sin "Race"/"Qualifying"
                            Text(text = s.location, color = Color(0xFFE6E0F8), fontWeight = FontWeight.Bold)
                            Text(text = "${s.year}", color = Color(0xFFBEB8D1), style = MaterialTheme.typography.bodySmall)
                        }
                        Button(
                            onClick = { onOpen(s.sessionKey) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD40000)),
                            modifier = Modifier
                                .height(44.dp)
                        ) {
                            Text("Ver resultados", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultsFullScreen(state: ResultsUiState, onBack: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
                Column {
                    Text(
                        text = "Gran Premio de ${state.headerTitle}",
                        style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    Text(text = state.headerSub, color = Color(0xFFBEB8D1))
                }
            }
        }
        if (state.resultsEnriched.isNotEmpty()) {
            item { PodiumLayout(state.resultsEnriched) }
            item { ResultsTable(state.resultsEnriched.drop(3)) }
            item {
                Column {
                    state.fastestLap?.let { fl ->
                        Text(
                            text = "VUELTA RÁPIDA: ${fl.driver} (${fl.team}) - ${fl.time}",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (state.nonClassified.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "NO CLASIFICADO: " + state.nonClassified.joinToString(", "),
                            color = Color(0xFFBEB8D1),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        } else if (state.isLoading) {
            item { CircularProgressIndicator() }
        } else if (state.error != null) {
            item { Text(state.error ?: "Error", color = Color.Red) }
        }
    }
}
