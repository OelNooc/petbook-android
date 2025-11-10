package com.oelnooc.petbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oelnooc.petbook.ui.screens.AddPetScreen
import com.oelnooc.petbook.ui.screens.PetDetailScreen
import com.oelnooc.petbook.ui.screens.PetListScreen
import com.oelnooc.petbook.ui.viewmodel.PetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val petViewModel: PetViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "petList"
                    ) {
                        composable("petList") {
                            PetListScreen(
                                viewModel = petViewModel,
                                onPetClick = { petId ->
                                    navController.navigate("petDetail/$petId")
                                },
                                onAddPetClick = {
                                    navController.navigate("addPet")
                                }
                            )
                        }
                        composable("addPet") {
                            AddPetScreen(
                                viewModel = petViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("petDetail/{petId}") { backStackEntry ->
                            val petId = backStackEntry.arguments?.getString("petId")?.toIntOrNull() ?: 0
                            PetDetailScreen(
                                petId = petId,
                                viewModel = petViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}