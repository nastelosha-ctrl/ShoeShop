package com.example.shoeshop.data.model

data class OTPVerificationResponse(
    val success: Boolean,
    val message: String? = null,
    val user_id: String? = null
)