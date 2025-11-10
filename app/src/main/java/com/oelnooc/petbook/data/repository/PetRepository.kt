package com.oelnooc.petbook.data.repository

import com.oelnooc.petbook.data.remote.ApiService
import com.oelnooc.petbook.data.remote.PetDto
import com.oelnooc.petbook.data.remote.PetRequestDto
import com.oelnooc.petbook.data.remote.RetrofitInstance

class PetRepository {
    private val api: ApiService = RetrofitInstance.api

    suspend fun getPets(): Result<List<PetDto>> {
        return try {
            val response = api.getPets()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPet(pet: PetRequestDto): Result<PetDto> {
        return try {
            val response = api.createPet(pet)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}