package pl.pollub.android.mastermind

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.pollub.android.mastermind.data.db.entity.PlayerScore

@Composable
fun ResultsScreen(
    points: Int,
    onPlayAgain: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ResultsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scores by viewModel.scores.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Results",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(8.dp))
        Text("Your score: $points")

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Ranking",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )


        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(scores) { index, score ->
                RankingRow(
                    place = index + 1,
                    score = score
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )

            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth()) {
            Text("Play again")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}


@Composable
fun RankingRow(place: Int, score: PlayerScore) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$place.")
        Text(score.name, modifier = Modifier.weight(1f))
        Text("Attempts: ${score.attempts}")
        Text("Colors: ${score.colors}")
    }
}


