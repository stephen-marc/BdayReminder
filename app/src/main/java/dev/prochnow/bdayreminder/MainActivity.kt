package dev.prochnow.bdayreminder

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.prochnow.bdayreminder.navigation.Actions
import dev.prochnow.bdayreminder.navigation.Screen
import dev.prochnow.bdayreminder.ui.theme.BdayTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BdayTheme() {
                val navController = rememberNavController()
                val actions = remember(navController) { Actions(navController) }
                NavHost(
                    navController = navController,
                    startDestination = Screen.MainScreen.route()
                ) {
                    composable(Screen.MainScreen.route()) {
                        val addBirthDayViewModel: AddBirthDayViewModel = hiltViewModel(it)
                        val birthdateListViewModel: BirthdayListViewModel = hiltViewModel(it)
                        BirthdayListScreen(addBirthDayViewModel, birthdateListViewModel)
                    }
                }
            }
        }
    }
}

