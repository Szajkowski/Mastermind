package pl.pollub.android.mastermind

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/* ============================================================
   10 dostępnych kolorów
   ============================================================ */

val AVAILABLE_COLORS = listOf(
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Yellow,
    Color.Magenta,
    Color.Cyan,
    Color.Gray,
    Color(0xFFFFA500),
    Color(0xFF8A2BE2),
    Color(0xFF7E5102)
)

/* ============================================================
   Stan jednego wiersza
   ============================================================ */

data class GameRowState(
    val selectedColors: SnapshotStateList<Color>,
    var feedback: List<Color> = emptyList(),
    var confirmed: Boolean = false
)

/* ============================================================
   Losowanie wzorca (pattern)
   ============================================================ */

fun selectRandomPattern(colors: List<Color>): List<Color> {
    return colors.shuffled().take(4)
}

/* ============================================================
   Logika oceny próby
   ============================================================ */

fun evaluateAttempt(attempt: List<Color>, pattern: List<Color>): List<Color> {

    val correct = Color.Green
    val present = Color.Yellow
    val wrong = Color.Red

    val result = MutableList(4) { wrong }

    val usedPattern = BooleanArray(4)
    val usedAttempt = BooleanArray(4)

    // Zielone trafienia
    for (i in 0 until 4) {
        if (attempt[i] == pattern[i]) {
            result[i] = correct
            usedPattern[i] = true
            usedAttempt[i] = true
        }
    }

    // Żółte trafienia
    for (i in 0 until 4) {
        if (!usedAttempt[i]) {
            for (j in 0 until 4) {
                if (!usedPattern[j] && attempt[i] == pattern[j]) {
                    result[i] = present
                    usedPattern[j] = true
                    usedAttempt[i] = true
                    break
                }
            }
        }
    }

    return result
}

/* ============================================================
   Następny kolor po kliknięciu
   ============================================================ */

fun nextColor(available: List<Color>, selected: List<Color>, index: Int): Color {

    val current = selected[index]
    val usedElsewhere = selected.toMutableList().apply { removeAt(index) }

    val candidates = available.filterNot { it in usedElsewhere }

    val pos = candidates.indexOf(current).takeIf { it >= 0 } ?: 0
    val next = (pos + 1) % candidates.size

    return candidates[next]
}

/* ============================================================
   Główny ekran gry
   ============================================================ */

@Composable
fun GameScreen(
    colorsCount: Int,
    playerId: Long,
    onGameFinished: (Int) -> Unit,
    onQuitGame: () -> Unit
) {

    val viewModel: GameViewModel =
        viewModel(factory = AppViewModelProvider.Factory)
    val gameColors = remember(colorsCount) {
        AVAILABLE_COLORS.take(colorsCount)
    }
    var resultSaved by remember { mutableStateOf(false) }

    val pattern = remember(gameColors) {
        selectRandomPattern(gameColors)
    }

    val rows = remember {
        mutableStateListOf(
            GameRowState(
                selectedColors = mutableStateListOf(
                    gameColors[0],
                    gameColors[1],
                    gameColors[2],
                    gameColors[3]
                )
            )
        )
    }

    var won by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        Text(
            text = "MasterAnd",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        )

        Spacer(Modifier.height(16.dp))

        /* ===== SCROLLOWALNA CZĘŚĆ ===== */
        LazyColumn(
            modifier = Modifier
                .weight(1f)          // ← KLUCZOWA LINIA
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(rows) { index, row ->

                val isActiveRow = index == 0
                val animateFeedback = index == 1

                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = expandVertically(expandFrom = Alignment.Bottom)
                ) {
                    GameRow(
                        rowState = row,
                        showConfirmButton = isActiveRow && !row.confirmed && !won,
                        animateFeedback = animateFeedback,
                        onColorClicked = { pos ->
                            if (isActiveRow && !row.confirmed && !won) {
                                val next = nextColor(gameColors, row.selectedColors, pos)
                                row.selectedColors[pos] = next
                            }
                        },
                        onConfirm = {
                            if (!row.confirmed && !won) {
                                row.feedback = evaluateAttempt(row.selectedColors, pattern)
                                row.confirmed = true

                                if (row.feedback.all { it == Color.Green }) {
                                    won = true
                                } else {
                                    rows.add(
                                        0,
                                        GameRowState(
                                            selectedColors = row.selectedColors.toMutableStateList()
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }

        }

        /* ===== PRZYCISK NA STAŁE NA DOLE ===== */
        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onQuitGame,
                modifier = Modifier.width(200.dp)
            ) {
                Text("Quit")
            }
        }

        LaunchedEffect(won) {
            if (won && !resultSaved) {
                resultSaved = true
                viewModel.saveScore(playerId, rows.size, colorsCount)
                onGameFinished(rows.size)
            }
        }


    }
}



/* ============================================================
   Jeden wiersz gry
   ============================================================ */

@Composable
fun GameRow(
    rowState: GameRowState,
    showConfirmButton: Boolean,
    onColorClicked: (Int) -> Unit,
    onConfirm: () -> Unit,
    animateFeedback: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SelectableColorsRow(
            colors = rowState.selectedColors,
            onClick = onColorClicked
        )
        if (showConfirmButton) {
        IconButton(
            onClick = onConfirm,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp),
            colors = IconButtonDefaults.filledIconButtonColors()
        ) { Icon(Icons.Default.Check, contentDescription = "Confirm") }
    } else {
        FeedbackCircles(rowState.feedback, animate = animateFeedback)
    }
    }
}

/* ============================================================
   Wiersz 4 przycisków
   ============================================================ */

@Composable
fun SelectableColorsRow(colors: List<Color>, onClick: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        colors.forEachIndexed { index, c ->
            CircularButton(color = c) { onClick(index) }
        }
    }
}

/* ============================================================
   Okrągły przycisk
   ============================================================ */

@Composable
fun CircularButton(color: Color, onClick: () -> Unit) {
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(300),
            repeatMode = RepeatMode.Reverse
        )
    )

    Button(
        onClick = onClick,
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(containerColor = animatedColor)
    ) {}
}



/* ============================================================
   Małe kulki feedbacku
   ============================================================ */

@Composable
fun FeedbackCircles(
    colors: List<Color>,
    animate: Boolean
) {
    val anims = remember(colors) {
        colors.map { Animatable(Color.Transparent) }
    }

    LaunchedEffect(colors, animate) {
        if (animate) {
            colors.forEachIndexed { index, c ->
                anims[index].animateTo(
                    c,
                    animationSpec = tween(300)
                )
            }
        } else {
            anims.forEachIndexed { index, anim ->
                anim.snapTo(colors[index])
            }
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        anims.forEach { anim ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(anim.value)
                    .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            )
        }
    }
}
