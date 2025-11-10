package com.oelnooc.petbook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.oelnooc.petbook.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Mascota") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            errorMessage?.let { message ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Error") },
                    text = { Text(message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (showConfirmation) {
                AlertDialog(
                    onDismissRequest = { showConfirmation = false },
                    title = { Text("Éxito") },
                    text = { Text("Mascota registrada correctamente") },
                    confirmButton = {
                        TextButton(onClick = {
                            showConfirmation = false
                            onBack()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = name.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Tipo (Perro, Gato, etc.)") },
                modifier = Modifier.fillMaxWidth(),
                isError = type.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = age.toIntOrNull() == null || age.toInt() < 0
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val ageInt = age.toIntOrNull()
                    if (name.isNotEmpty() && type.isNotEmpty() && ageInt != null && ageInt >= 0) {
                        viewModel.addPet(name, type, ageInt)
                        showConfirmation = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && name.isNotEmpty() && type.isNotEmpty() &&
                        age.toIntOrNull() != null && age.toInt() >= 0
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Mascota")
                }
            }
        }
    }
}