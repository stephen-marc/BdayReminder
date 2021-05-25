package dev.prochnow.bdayreminder.navigation

import androidx.navigation.NavHostController

sealed class Screen(val identifier: String) {
    open fun route(): String = identifier

    object MainScreen : Screen("main_screen")
}

class Actions(navController: NavHostController) {
    val openMainScreen: () -> Unit = {
//        navController.navigate(Screen.MainScreen.route())
    }
}

