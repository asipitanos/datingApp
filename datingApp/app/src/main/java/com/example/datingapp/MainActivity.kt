package com.example.datingapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datingapp.pages.ChatPage
import com.example.datingapp.pages.LandingPage
import com.example.datingapp.ui.theme.DatingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                DisposableEffect(Unit) {
                    val window = (view.context as Activity).window
                    val insetsController = WindowCompat.getInsetsController(window, view)
                    insetsController.isAppearanceLightStatusBars = true
                    onDispose {
                        insetsController.isAppearanceLightStatusBars = false
                    }
                }
            }
            DatingAppTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "landing") {
                        composable("landing") {
                            LandingPage(navController = navController)
                        }
                        composable(
                            route = "chat/{name}",
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("name") ?: "User"
                            ChatPage(
                                userName = userName,
                                onBack = { navController.popBackStack() },
                            )
                        }
                    }
                }
            }
        }
    }
}
