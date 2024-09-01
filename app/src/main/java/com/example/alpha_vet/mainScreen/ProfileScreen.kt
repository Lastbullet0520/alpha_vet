package com.example.alpha_vet.mainScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button // material3 사용
import androidx.compose.material3.MaterialTheme // material3 사용
import androidx.compose.material3.OutlinedTextField // material3 사용
import androidx.compose.material3.Text // material3 사용
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.alpha_vet.model.DarkModeViewModel
import com.example.alpha_vet.model.PetProfileViewModel


@Composable
fun ProfileScreen(
    navController: NavController,
    petProfileViewModel: PetProfileViewModel,
    darkModeViewModel: DarkModeViewModel,
) {
    var species by remember { mutableStateOf(TextFieldValue(petProfileViewModel.species)) }
    var name by remember { mutableStateOf(TextFieldValue(petProfileViewModel.name)) }
    var age by remember { mutableStateOf(TextFieldValue(petProfileViewModel.age)) }
    var gender by remember { mutableStateOf(TextFieldValue(petProfileViewModel.gender)) }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(petProfileViewModel.photoUri?.let {
            Uri.parse(
                it
            )
        })
    }



    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "프로필 추가하기", style = MaterialTheme.typography.headlineSmall)

        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("프로필 사진", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = species,
            onValueChange = { species = it },
            label = { Text("종") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("나이") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("성별") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                petProfileViewModel.species = species.text
                petProfileViewModel.name = name.text
                petProfileViewModel.age = age.text
                petProfileViewModel.gender = gender.text
                petProfileViewModel.photoUri = selectedImageUri?.toString()

                // Navigate back to MenuScreen
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("프로필 저장")
        }
    }
}