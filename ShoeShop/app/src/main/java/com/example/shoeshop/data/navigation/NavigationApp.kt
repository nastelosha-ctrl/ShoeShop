import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstproject.ui.view.ForgotPassScreen
import com.example.myfirstproject.ui.view.HomeScreen
import com.example.myfirstproject.ui.view.OTPVerificationScreen
import com.example.myfirstproject.ui.view.SignInScreen
import com.example.myfirstproject.ui.view.SignUpScreen

@Composable
fun NavigationApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "sign_up"
    ) {
        composable("sign_up") {
            SignUpScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onSignUpSuccess = { email ->
                    // Переходим на OTP экран с email
                    navController.navigate("otp/${email}")
                }
            )
        }

        composable(
            route = "otp/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""

            OTPVerificationScreen(
                email = email,
                onVerificationSuccess = {
                    navController.navigate("home") {
                        popUpTo("sign_up") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
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
            ForgotPassScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onSendOTPClick = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomeScreen()
        }

        }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NavigationAppPreview() {
    val navController = rememberNavController()
    NavigationApp(navController = navController)
}