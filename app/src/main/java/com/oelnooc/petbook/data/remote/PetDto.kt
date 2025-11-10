package com.oelnooc.petbook.data.remote

data class PetDto(
    val id: Int,
    val name: String,
    val type: String,
    val age: Int
)

data class PetRequestDto(
    val name: String,
    val type: String,
    val age: Int
)
