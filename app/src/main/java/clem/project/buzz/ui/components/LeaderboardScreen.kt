package clem.project.buzz.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clem.project.buzz.data.local.Score
import clem.project.buzz.viewmodels.LeaderboardViewModel

@Composable
fun LeaderboardScreen(vm: LeaderboardViewModel) {
    // 1) On collecte la StateFlow DANS le composable, hors du LazyColumn
    val scores by vm.topScores.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "🏆 Leaderboard",
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn {
            // 2) On passe la liste déjà collectée à items()
            items(scores) { score: Score ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = score.playerName)
                        Text(
                            text = "${score.value}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
