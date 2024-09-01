package com.example.alpha_vet.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PetProfileViewModel : ViewModel() {
    private val _petProfiles = MutableStateFlow<List<PetProfile>>(emptyList())
    val petProfiles: StateFlow<List<PetProfile>> = _petProfiles

    fun addProfile(species: String, name: String, age: String, gender: String, photoUri: String?) {
        val newProfile = PetProfile(species, name, age, gender, photoUri)
        _petProfiles.value += newProfile
    }
}
