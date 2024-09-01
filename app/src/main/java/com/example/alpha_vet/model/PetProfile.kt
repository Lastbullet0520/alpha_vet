package com.example.alpha_vet.model

data class PetProfile(
    val species: String,
    val name: String,
    val age: String,
    val gender: String,
    val photoUri: String?
)