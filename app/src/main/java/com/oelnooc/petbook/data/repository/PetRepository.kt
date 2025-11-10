package com.oelnooc.petbook.data.repository

import com.oelnooc.petbook.data.remote.ApiService
import com.oelnooc.petbook.data.remote.PetDto
import com.oelnooc.petbook.data.remote.PetRequestDto
import com.oelnooc.petbook.data.remote.RetrofitInstance

class PetRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    suspend fun getPets(): Result<List<PetDto>> {
        return try {
            val response = api.getPets()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getPetById(id: Int): Result<PetDto> {
        return try {
            val response = api.getPet(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Exception("Error: ${response.code()} - Mascota no encontrada"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun createPet(pet: PetRequestDto): Result<PetDto> {
        return try {
            val response = api.createPet(pet)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}