package com.example.alpha_vet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alpha_vet.mainScreen.EntryScreen
import com.example.alpha_vet.mainScreen.MainScreen
import com.example.alpha_vet.mainScreen.MenuScreen
import com.example.alpha_vet.mainScreen.ProfileScreen
import com.example.alpha_vet.mainScreen.SetScreen
import com.example.alpha_vet.model.DarkModeViewModel
import com.example.alpha_vet.model.PetProfileViewModel
import com.example.alpha_vet.navigationScreen.ProfileScreen2
import com.example.alpha_vet.navigationScreen.SearchScreen
import com.example.alpha_vet.petlistScreen.AichatScreen
import com.example.alpha_vet.petlistScreen.CatlistScreen
import com.example.alpha_vet.petlistScreen.DoglistScreen
import com.example.alpha_vet.state.Screen
import com.example.alpha_vet.ui.appnavigation.BottomNavigationBar
import com.example.alpha_vet.ui.theme.AlphaVetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkModeViewModel: DarkModeViewModel = viewModel()
            val petProfileViewModel: PetProfileViewModel = viewModel()

            MyApp(darkModeViewModel, petProfileViewModel)
        }
    }
}

@Composable
fun MyApp(
    darkModeViewModel: DarkModeViewModel,
    petProfileViewModel: PetProfileViewModel
) {
    val isDarkModeEnabled by darkModeViewModel.isDarkMode.collectAsState()

    AlphaVetTheme(darkTheme = isDarkModeEnabled) {
        AppContent(darkModeViewModel, petProfileViewModel)
    }
}

@Composable
fun AppContent(
    darkModeViewModel: DarkModeViewModel,
    petProfileViewModel: PetProfileViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "entryScreen"
            ) {
                composable(Screen.Home.route) { MainScreen(navController, darkModeViewModel, petProfileViewModel) }
                composable(Screen.Search.route) { SearchScreen() }
                composable(Screen.Profile.route) { ProfileScreen2() }
                composable("entryScreen") { EntryScreen(navController) }
                composable("doglistScreen") { DoglistScreen(navController, darkModeViewModel, petProfileViewModel) }
                composable("catlistScreen") { CatlistScreen(navController, darkModeViewModel, petProfileViewModel) }
                composable("menuScreen") {
                    MenuScreen(navController, petProfileViewModel)
                }
                composable("setScreen") { SetScreen(navController, darkModeViewModel) }
                composable("aichatScreen") { AichatScreen(navController, darkModeViewModel) }
                composable("profileScreen") {
                    ProfileScreen(navController, petProfileViewModel)
                }
            }
        }
    }
}
