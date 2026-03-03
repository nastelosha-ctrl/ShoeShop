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
        // Экран регистрации
        composable("sign_up") {
            RegisterAccount(
                onBackClick = {  },
                onRegisterClick = {  },
                onLoginClick = {
                    // ПЕРЕХОД НА ЭКРАН ВХОДА
                    navController.navigate("sign_in")
                }
            )
        }

        // Экран входа
        composable("sign_in") {
            SignInScreen(
                onForgotPasswordClick = { },
                onSignInClick = { },
                onSignUpClick = {
                    // ВОЗВРАТ НА ЭКРАН РЕГИСТРАЦИИ
                    navController.navigate("sign_up")
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NavigationAppPreview() {

}