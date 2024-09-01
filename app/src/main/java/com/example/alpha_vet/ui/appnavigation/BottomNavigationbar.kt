package com.example.alpha_vet.ui.appnavigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.alpha_vet.state.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Profile
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = screen.icon), contentDescription = screen.title, modifier = Modifier.size(48.dp)) }, // 아이콘 크기를 48dp로 맞춤
                label = { Text(text = screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray
            )
        }
    }
}