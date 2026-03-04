package com.example.shoeshop.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shoeshop.ui.screens.*


@Composable
fun NavigationApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "sign_up"
    ) {
        composable("sign_up") {
            RegisterAccount(
                onSignInClick = { navController.navigate("sign_in") },
                onSignUpClick = { navController.navigate("email_verification") }
            )
        }
        composable("sign_in") {
            SignInScreen(
                onForgotPasswordClick = { navController.navigate("forgot_password") },
                onSignInClick = { navController.navigate("home") },
                onSignUpClick = { navController.navigate("sign_up") }

            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                onNavigateToOtpVerification = { navController.navigate("reset_password") },
                onBackClick = {navController.navigate("sign_in")}
            )
        }

        composable("email_verification") {
            EmailVerificationScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onVerificationSuccess = { navController.navigate("home") }

            )
        }
        composable("reset_password") {
            RecoveryVerificationScreen(
                onSignInClick = {navController.navigate("sign_in")},
                onResetPasswordClick = { resetToken ->  // Токен приходит сюда
                    // Передаем токен в маршрут
                    navController.navigate("create_password/$resetToken")
                }

            )
        }
        composable("create_password/{resetToken}") { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""

            CreateNewPasswordScreen(
                userToken = resetToken,  // Передаем токен в экран
                onPasswordChanged = { navController.navigate("sign_in") }
            )
        }
        composable("home") {
            HomeScreen({},{},{})
        }





    }
}

