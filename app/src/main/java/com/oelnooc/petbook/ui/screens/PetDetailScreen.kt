package com.oelnooc.petbook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oelnooc.petbook.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: Int,
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val selectedPet by viewModel.selectedPet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(petId) {
        viewModel.getPetById(petId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedPet()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Mascota") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            errorMessage?.let { message ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Error") },
                    text = { Text(message) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.clearError()
                            onBack()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (selectedPet != null) {
                PetDetailContent(pet = selectedPet!!)
            } else if (!isLoading && selectedPet == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se pudo cargar la información de la mascota")
                }
            }
        }
    }
}

@Composable
fun PetDetailContent(pet: com.oelnooc.petbook.domain.model.Pet) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val icon = when (pet.type.lowercase()) {
            "perro" -> "🐕"
            "gato" -> "🐈"
            "pájaro" -> "🐦"
            "pez" -> "🐠"
            else -> "🐾"
        }

        Text(
            text = icon,
            fontSize = 80.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                InfoRow(title = "ID", value = pet.id.toString())
                InfoRow(title = "Nombre", value = pet.name)
                InfoRow(title = "Tipo", value = pet.type)
                InfoRow(title = "Edad", value = "${pet.age} años")
            }
        }

        Text(
            text = when (pet.type.lowercase()) {
                "perro" -> "¡Los perros son los mejores amigos del humano!"
                "gato" -> "Los gatos son animales independientes y curiosos"
                "pájaro" -> "Los pájaros alegran con su canto"
                "pez" -> "Los peces son tranquilos y relajantes"
                else -> "¡Qué linda mascota!"
            },
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}