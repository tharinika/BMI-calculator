package com.example.bmi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bmi.viewmodel.AuthViewModel

@Composable
fun AppNavigator(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {  // ✅ Corrected `navController` usage
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("signup") { SignUpScreen(navController, authViewModel) }
        composable("bmi") { BmiScreen() }  // ✅ Pass `NavController` properly
    }
}
