package com.oelnooc.petbook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oelnooc.petbook.data.remote.PetRequestDto
import com.oelnooc.petbook.data.repository.PetRepository
import com.oelnooc.petbook.data.repository.Result
import com.oelnooc.petbook.domain.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PetViewModel : ViewModel() {
    private val repository = PetRepository()

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getPets()) {
                is Result.Success -> {
                    _pets.value = result.data.map { dto ->
                        Pet(
                            id = dto.id,
                            name = dto.name,
                            type = dto.type,
                            age = dto.age
                        )
                    }
                }
                is Result.Failure -> {
                    _errorMessage.value = result.exception.message
                }
            }

            _isLoading.value = false
        }
    }

    fun addPet(name: String, type: String, age: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.createPet(PetRequestDto(name, type, age))) {
                is Result.Success -> {
                    loadPets()
                }
                is Result.Failure -> {
                    _errorMessage.value = result.exception.message
                }
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}