package com.oelnooc.petbook.ui.components

import com.oelnooc.petbook.domain.model.Pet
import org.junit.Assert.assertEquals
import org.junit.Test

class PetItemTest {

    @Test
    fun `Pet should have correct properties`() {
        // Given
        val pet = Pet(1, "Iron", "Perro", 14)

        // Then
        assertEquals(1, pet.id)
        assertEquals("Iron", pet.name)
        assertEquals("Perro", pet.type)
        assertEquals(14, pet.age)
    }

    @Test
    fun `Pet should handle different data correctly`() {
        // Given
        val pets = listOf(
            Pet(1, "Iron", "Perro", 14),
            Pet(2, "Michi", "Gato", 2),
            Pet(3, "Tweety", "Pájaro", 1)
        )

        // Then
        assertEquals(3, pets.size)
        assertEquals("Perro", pets[0].type)
        assertEquals("Gato", pets[1].type)
        assertEquals("Pájaro", pets[2].type)
    }

    @Test
    fun `Pet data class works correctly`() {
        val pet1 = Pet(1, "Iron", "Perro", 14)
        val pet2 = Pet(1, "Iron", "Perro", 14)
        val pet3 = pet1.copy(name = "Max")

        // Then
        assertEquals(pet1, pet2) // Deben ser iguales
        assertEquals("Max", pet3.name) // Copy funciona
        assertEquals(pet1.type, pet3.type) // Otros campos se mantienen
    }
}