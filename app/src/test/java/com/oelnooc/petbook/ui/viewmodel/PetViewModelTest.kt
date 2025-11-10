package com.oelnooc.petbook.ui.viewmodel

import app.cash.turbine.test
import com.oelnooc.petbook.data.repository.PetRepository
import com.oelnooc.petbook.data.repository.Result
import com.oelnooc.petbook.data.remote.PetDto
import com.oelnooc.petbook.data.remote.PetRequestDto
import com.oelnooc.petbook.domain.model.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock

class PetViewModelTest {

    private lateinit var viewModel: PetViewModel
    private lateinit var mockRepository: PetRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mock()
        viewModel = PetViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPets should update pets state when successful`() = runTest {
        // Given
        val mockPetsDto = listOf(
            PetDto(1, "Iron", "Perro", 14),
            PetDto(2, "Michi", "Gato", 2)
        )
        val expectedPets = listOf(
            Pet(1, "Iron", "Perro", 14),
            Pet(2, "Michi", "Gato", 2)
        )
        `when`(mockRepository.getPets()).thenReturn(Result.Success(mockPetsDto))

        // When & Then
        viewModel.pets.test {
            skipItems(1)

            viewModel.loadPets()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(expectedPets, awaitItem())
        }

        verify(mockRepository, times(2)).getPets()
    }

    @Test
    fun `loadPets should update error state when failure`() = runTest {
        // Given
        val exception = Exception("Network error")
        `when`(mockRepository.getPets()).thenReturn(Result.Failure(exception))

        // When & Then
        viewModel.errorMessage.test {
            skipItems(1)

            viewModel.loadPets()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals("Network error", awaitItem())
        }

        verify(mockRepository, times(2)).getPets()
    }

    @Test
    fun `loadPets should update loading state correctly`() = runTest {
        // Given
        val mockPetsDto = listOf(PetDto(1, "Iron", "Perro", 14))
        `when`(mockRepository.getPets()).thenReturn(Result.Success(mockPetsDto))

        // When & Then
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())

            viewModel.loadPets()
            assertEquals(true, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `getPetById should update selectedPet when successful`() = runTest {
        // Given
        val mockPetDto = PetDto(1, "Iron", "Perro", 14)
        val expectedPet = Pet(1, "Iron", "Perro", 14)
        `when`(mockRepository.getPetById(1)).thenReturn(Result.Success(mockPetDto))

        // When & Then
        viewModel.selectedPet.test {
            assertNull(awaitItem())

            viewModel.getPetById(1)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(expectedPet, awaitItem())
        }

        verify(mockRepository).getPetById(1)
    }

    @Test
    fun `getPetById should update error state when failure`() = runTest {
        // Given
        val exception = Exception("Not found")
        `when`(mockRepository.getPetById(1)).thenReturn(Result.Failure(exception))

        // When & Then
        viewModel.errorMessage.test {
            skipItems(1)

            viewModel.getPetById(1)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals("Not found", awaitItem())
        }

        verify(mockRepository).getPetById(1)
    }

    @Test
    fun `addPet should reload pets when successful`() = runTest  {
        // Given
        val mockPetDto = PetDto(4, "Nueva", "Perro", 1)

        val expectedRequest = PetRequestDto("Nueva", "Perro", 1)
        `when`(mockRepository.createPet(expectedRequest)).thenReturn(Result.Success(mockPetDto))
        `when`(mockRepository.getPets()).thenReturn(Result.Success(emptyList()))

        // When
        viewModel.addPet("Nueva", "Perro", 1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Verificar que se llamó con los parámetros correctos
        verify(mockRepository).createPet(expectedRequest)

        // init (1 vez) + addPet llama a loadPets (1 vez más)
        verify(mockRepository, atLeast(2)).getPets()
    }

    @Test
    fun `addPet should update error state when failure`() = runTest {
        // Given
        val exception = Exception("Creation failed")

        val expectedRequest = PetRequestDto("Nueva", "Perro", 1)
        `when`(mockRepository.createPet(expectedRequest)).thenReturn(Result.Failure(exception))

        // When & Then
        viewModel.errorMessage.test {
            skipItems(1)

            viewModel.addPet("Nueva", "Perro", 1)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals("Creation failed", awaitItem())
        }

        verify(mockRepository).createPet(expectedRequest)
    }

    @Test
    fun `clearError should reset error message`() = runTest {
        // Given
        val exception = Exception("Test error")

        `when`(mockRepository.getPets())
            .thenReturn(Result.Failure(exception))
            .thenReturn(Result.Success(emptyList()))

        // When & Then
        viewModel.errorMessage.test {
            val firstItem = awaitItem()

            if (firstItem != "Test error") {
                viewModel.loadPets()
                testDispatcher.scheduler.advanceUntilIdle()
                assertEquals("Test error", awaitItem())
            }

            viewModel.clearError()
            assertNull(awaitItem())
        }
    }

    @Test
    fun `clearSelectedPet should reset selected pet`() = runTest {
        // Given
        val mockPetDto = PetDto(1, "Iron", "Perro", 14)
        `when`(mockRepository.getPetById(1)).thenReturn(Result.Success(mockPetDto))

        // When & Then
        viewModel.selectedPet.test {
            assertNull(awaitItem())

            viewModel.getPetById(1)
            testDispatcher.scheduler.advanceUntilIdle()

            val pet = awaitItem()
            assertNotNull(pet)
            assertEquals("Iron", pet!!.name)

            viewModel.clearSelectedPet()
            assertNull(awaitItem())
        }
    }

    @Test
    fun `initial state should be empty`() = runTest {
        assertEquals(emptyList<Pet>(), viewModel.pets.value)
        assertNull(viewModel.selectedPet.value)
        assertEquals(false, viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }
}