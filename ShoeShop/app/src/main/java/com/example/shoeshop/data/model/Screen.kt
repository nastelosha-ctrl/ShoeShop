package com.example.myfirstproject.data.model

sealed class Screen {
    object SignUp : Screen()
    object OTPVerification : Screen()
    object Home : Screen()
}