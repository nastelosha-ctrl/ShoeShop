package com.example.shoeshop.data.model
data class OTPVerificationRequest(
    val type: String,
    val email: String,
    val token: String
)