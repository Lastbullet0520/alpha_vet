package com.example.alpha_vet.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PetProfileViewModel : ViewModel() {
    var species by mutableStateOf("")
    var name by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("")
    var photoUri by mutableStateOf<String?>(null)
}