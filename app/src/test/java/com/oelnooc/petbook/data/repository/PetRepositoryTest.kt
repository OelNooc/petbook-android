package com.oelnooc.petbook.data.repository

import com.oelnooc.petbook.data.remote.ApiService
import com.oelnooc.petbook.data.remote.PetDto
import com.oelnooc.petbook.data.remote.PetRequestDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import retrofit2.Response

class PetRepositoryTest {

    private lateinit var repository: PetRepository
    private lateinit var mockApiService: ApiService

    @Before
    fun setUp() {
        mockApiService = mock()
        repository = PetRepository(mockApiService)
    }

    @Test
    fun `getPets should return success when API call is successful`() = runTest {
        // Given
        val mockPets = listOf(
            PetDto(1, "Iron", "Perro", 14),
            PetDto(2, "Michi", "Gato", 2),
            PetDto(3, "Onix", "Perro", 3)
        )
        val mockResponse = Response.success(mockPets)
        `when`(mockApiService.getPets()).thenReturn(mockResponse)

        // When
        val result = repository.getPets()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockPets, (result as Result.Success).data)
        verify(mockApiService).getPets()
    }

    @Test
    fun `getPets should return failure when API call fails`() = runTest {
        // Given
        val mockResponse = Response.error<List<PetDto>>(500, okhttp3.ResponseBody.create(null, "Error"))
        `when`(mockApiService.getPets()).thenReturn(mockResponse)

        // When
        val result = repository.getPets()

        // Then
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).exception.message!!.contains("500"))
        verify(mockApiService).getPets()
    }

    @Test
    fun `getPets should return failure when API throws exception`() = runTest {
        // Given
        val exception = Exception("Network error")
        `when`(mockApiService.getPets()).thenAnswer { throw exception }

        // When
        val result = repository.getPets()

        // Then
        assertTrue(result is Result.Failure)
        assertEquals(exception, (result as Result.Failure).exception)
        verify(mockApiService).getPets()
    }

    @Test
    fun `getPetById should return success when API call is successful`() = runTest {
        // Given
        val mockPet = PetDto(1, "Iron", "Perro", 14)
        val mockResponse = Response.success(mockPet)
        `when`(mockApiService.getPet(1)).thenReturn(mockResponse)

        // When
        val result = repository.getPetById(1)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockPet, (result as Result.Success).data)
        verify(mockApiService).getPet(1)
    }

    @Test
    fun `getPetById should return failure when pet not found`() = runTest {
        // Given
        val mockResponse = Response.error<PetDto>(404, okhttp3.ResponseBody.create(null, "Not found"))
        `when`(mockApiService.getPet(1)).thenReturn(mockResponse)

        // When
        val result = repository.getPetById(1)

        // Then
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).exception.message!!.contains("404"))
        verify(mockApiService).getPet(1)
    }

    @Test
    fun `createPet should return success when API call is successful`() = runTest {
        // Given
        val petRequest = PetRequestDto("Nueva Mascota", "Perro", 2)
        val mockPet = PetDto(4, "Nueva Mascota", "Perro", 2)
        val mockResponse = Response.success(mockPet)
        `when`(mockApiService.createPet(petRequest)).thenReturn(mockResponse)

        // When
        val result = repository.createPet(petRequest)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockPet, (result as Result.Success).data)
        verify(mockApiService).createPet(petRequest)
    }

    @Test
    fun `createPet should return failure when API call fails`() = runTest {
        // Given
        val petRequest = PetRequestDto("Nueva Mascota", "Perro", 2)
        val mockResponse = Response.error<PetDto>(400, okhttp3.ResponseBody.create(null, "Bad request"))
        `when`(mockApiService.createPet(petRequest)).thenReturn(mockResponse)

        // When
        val result = repository.createPet(petRequest)

        // Then
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).exception.message!!.contains("400"))
        verify(mockApiService).createPet(petRequest)
    }
}