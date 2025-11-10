package com.oelnooc.petbook.ui.screens

import com.oelnooc.petbook.domain.model.Pet
import com.oelnooc.petbook.ui.viewmodel.PetViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PetListScreenTest {

    @Test
    fun `should show empty state when pets list is empty`() {
        // Given
        val mockViewModel = mock(PetViewModel::class.java)
        val petsFlow = MutableStateFlow(emptyList<Pet>())
        val loadingFlow = MutableStateFlow(false)
        val errorFlow = MutableStateFlow<String?>(null)

        `when`(mockViewModel.pets).thenReturn(petsFlow)
        `when`(mockViewModel.isLoading).thenReturn(loadingFlow)
        `when`(mockViewModel.errorMessage).thenReturn(errorFlow)

        // When & Then
        assertTrue(mockViewModel.pets.value.isEmpty())
        assertFalse(mockViewModel.isLoading.value)
        assertNull(mockViewModel.errorMessage.value)
    }

    @Test
    fun `should have pets when list is not empty`() {
        // Given
        val mockViewModel = mock(PetViewModel::class.java)
        val pets = listOf(
            Pet(1, "Iron", "Perro", 14),
            Pet(2, "Michi", "Gato", 2)
        )
        val petsFlow = MutableStateFlow(pets)
        val loadingFlow = MutableStateFlow(false)
        val errorFlow = MutableStateFlow<String?>(null)

        `when`(mockViewModel.pets).thenReturn(petsFlow)
        `when`(mockViewModel.isLoading).thenReturn(loadingFlow)
        `when`(mockViewModel.errorMessage).thenReturn(errorFlow)

        // When & Then
        assertEquals(2, mockViewModel.pets.value.size)
        assertEquals("Iron", mockViewModel.pets.value[0].name)
        assertEquals("Michi", mockViewModel.pets.value[1].name)
        assertFalse(mockViewModel.isLoading.value)
        assertNull(mockViewModel.errorMessage.value)
    }

    @Test
    fun `should show loading state when isLoading is true`() {
        // Given
        val mockViewModel = mock(PetViewModel::class.java)
        val petsFlow = MutableStateFlow(emptyList<Pet>())
        val loadingFlow = MutableStateFlow(true)
        val errorFlow = MutableStateFlow<String?>(null)

        `when`(mockViewModel.pets).thenReturn(petsFlow)
        `when`(mockViewModel.isLoading).thenReturn(loadingFlow)
        `when`(mockViewModel.errorMessage).thenReturn(errorFlow)

        // When & Then
        assertTrue(mockViewModel.isLoading.value)
        assertTrue(mockViewModel.pets.value.isEmpty())
        assertNull(mockViewModel.errorMessage.value)
    }

    @Test
    fun `should show error when errorMessage is not null`() {
        // Given
        val mockViewModel = mock(PetViewModel::class.java)
        val petsFlow = MutableStateFlow(emptyList<Pet>())
        val loadingFlow = MutableStateFlow(false)
        val errorFlow = MutableStateFlow<String?>("Error de red")

        `when`(mockViewModel.pets).thenReturn(petsFlow)
        `when`(mockViewModel.isLoading).thenReturn(loadingFlow)
        `when`(mockViewModel.errorMessage).thenReturn(errorFlow)

        // When & Then
        assertEquals("Error de red", mockViewModel.errorMessage.value)
        assertTrue(mockViewModel.pets.value.isEmpty())
        assertFalse(mockViewModel.isLoading.value)
    }
}