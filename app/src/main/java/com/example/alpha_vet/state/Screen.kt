package com.example.alpha_vet.state

import androidx.annotation.DrawableRes
import com.example.alpha_vet.R

sealed class Screen(val route: String, val title: String, @DrawableRes val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Search : Screen("search", "Search", R.drawable.ic_search)
    object Profile : Screen("profile", "Profile", R.drawable.ic_profile)
}