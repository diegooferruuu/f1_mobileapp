package com.example.f1_app.features.news.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
// Note: Avoid importing LazyColumn here to prevent nested scrollables
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsSection(viewModel: NewsViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadNews() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Latest F1 News",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(12.dp))

        when {
            state.value.isLoading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.value.error != null -> {
                Text(
                    text = state.value.error ?: "Error",
                    color = Color(0xFFFF6B6B)
                )
            }
            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.value.news.take(6).forEach { item ->
                        NewsCard(
                            title = item.title,
                            source = item.source,
                            url = item.url
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewsCard(title: String, source: String, url: String) {
    val ctx = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1D26)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { openUrl(ctx, url) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1F1D26), Color(0xFF16151B))
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val sourceLabel = if (source.isBlank()) "UNKNOWN" else source.uppercase()
            AssistChip(
                onClick = { /* no-op */ },
                label = { Text(sourceLabel, color = Color(0xFFBEB8D1), maxLines = 1) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFF2A2733),
                    labelColor = Color(0xFFBEB8D1)
                )
            )

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { openUrl(ctx, url) }) {
                    Text("Open")
                }
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
