package com.example.f1_app.features.teams.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@Composable
fun TeamsScreen(viewModel: TeamsViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTeams()
    }

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
                        text = "Teams 2025",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.value.teams) { team ->
                            TeamCard(
                                name = team.name ?: "Nombre desconocido",
                                base = team.base ?: "Ubicación no disponible",
                                director = team.director ?: "Director no especificado",
                                logoUrl = team.logo ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamCard(name: String, base: String, director: String, logoUrl: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1D26)),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFEAEAEA), shape = MaterialTheme.shapes.medium)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(logoUrl),
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(text = "Base: $base", color = Color(0xFFBEB8D1))
            Text(text = "Director: $director", color = Color(0xFFBEB8D1))
        }
    }
}