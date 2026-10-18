package pl.pollub.android.mastermind

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

/* =========================
   PROFILE SCREEN
   ========================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onStartGame: (colors: Int, playerId: Long) -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val colors = rememberSaveable { mutableStateOf("") }
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val scope = rememberCoroutineScope()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { imageUri.value = it }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
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
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        )

        ProfileImageWithPicker(
            imageUri = imageUri.value,
            onPickImage = { imagePicker.launch("image/*") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextFieldWithError(
            value = name.value,
            onValueChange = { name.value = it },
            label = "Name",
            keyboardType = KeyboardType.Text,
            validator = { it.isNotBlank() },
            errorText = "Name cannot be empty"
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldWithError(
            value = email.value,
            onValueChange = { email.value = it },
            label = "Email",
            keyboardType = KeyboardType.Email,
            validator = {
                android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            errorText = "Invalid email address"
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldWithError(
            value = colors.value,
            onValueChange = { colors.value = it },
            label = "Colors (5–10)",
            keyboardType = KeyboardType.Number,
            validator = {
                it.toIntOrNull()?.let { n -> n in 5..10 } ?: false
            },
            errorText = "Value must be between 5 and 10"
        )

        Spacer(modifier = Modifier.height(24.dp))

        val isNameValid = name.value.isNotBlank()
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS
            .matcher(email.value)
            .matches()
        val isColorsValid = colors.value.toIntOrNull() in 5..10

        Button(
            onClick = {
                scope.launch {
                    viewModel.login(
                        name.value,
                        email.value,
                        imageUri.value?.toString()
                    )
                    onStartGame(colors.value.toInt(), viewModel.playerId.longValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isNameValid && isEmailValid && isColorsValid
        ) {
            Text("Start game")
        }
    }
}

/* =========================
   TEXT FIELD WITH ERROR
   ========================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    validator: (String) -> Boolean,
    errorText: String
) {
    val isError = value.isNotEmpty() && !validator(value)

    Column(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (isError) {
                    Text(
                        text = "!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Box(modifier = Modifier.height(20.dp)) {
            if (isError) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/* =========================
   PROFILE IMAGE
   ========================= */

@Composable
fun ProfileImageWithPicker(
    imageUri: Uri?,
    onPickImage: () -> Unit
) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {

        if (imageUri == null) {
            Image(
                painter = painterResource(id = R.drawable.baseline_question_mark),
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        } else {
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

        IconButton(
            onClick = onPickImage,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text("✎")
        }
    }
}
