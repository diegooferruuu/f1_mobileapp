package com.example.f1_app.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1_app.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()

    state.value?.let { overview ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagenpistaf1),
                contentDescription = "Circuit Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Season Overview", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Races Completed: ${overview.racesCompleted}")
                    Text("Drivers on Podium: ${overview.driversOnPodium}")
                    Text("Safety Cars: ${overview.safetyCars}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                    Text("Next: ${overview.nextRace}", modifier = Modifier.padding(8.dp))
                }
                Card(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    Text("Clima: ${overview.weather}", modifier = Modifier.padding(8.dp))
                }
                Card(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                    Text("FP1: ${overview.firstPractice}", modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(overview.newsTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(overview.newsDescription)
        }
    }
}