package com.oelnooc.petbook.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("pets")
    suspend fun getPets(): Response<List<PetDto>>

    @GET("pets/{id}")
    suspend fun getPet(@Path("id") id: Int): Response<PetDto>

    @POST("pets")
    suspend fun createPet(@Body pet: PetRequestDto): Response<PetDto>
}