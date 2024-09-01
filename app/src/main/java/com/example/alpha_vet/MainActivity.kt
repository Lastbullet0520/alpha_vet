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
import com.example.alpha_vet.navigationScreen.HomeScreen
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
            MyApp(darkModeViewModel)
        }
    }
}

@Composable
fun MyApp(darkModeViewModel: DarkModeViewModel) {

    val isDarkModeEnabled by darkModeViewModel.isDarkMode.collectAsState()

    AlphaVetTheme(darkTheme = isDarkModeEnabled) {

        AppContent(darkModeViewModel)
    }
}

@Composable
fun AppContent(darkModeViewModel: DarkModeViewModel) {
    val navController = rememberNavController()
    val petProfileViewModel: PetProfileViewModel = viewModel()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // buttomNavigation 추가

        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "entryScreen"
            ) {
                //buttomNavigation 화면전환 추가
                composable(Screen.Home.route) { MainScreen(navController, darkModeViewModel) }
                composable(Screen.Search.route) { SearchScreen() }
                composable(Screen.Profile.route) { ProfileScreen2() }
                composable("entryScreen") { EntryScreen(navController) }
                composable("doglistScreen") { DoglistScreen(navController, darkModeViewModel) }
                composable("catlistScreen") { CatlistScreen(navController, darkModeViewModel) }
                composable("menuScreen") {
                    MenuScreen(navController, petProfileViewModel, darkModeViewModel)
                }
                composable("setScreen") { SetScreen(navController, darkModeViewModel) }
                composable("aichatScreen") { AichatScreen(navController, darkModeViewModel) }
                composable("profileScreen") {
                    ProfileScreen(navController, petProfileViewModel, darkModeViewModel)
                }

            }
        }
    }
}


