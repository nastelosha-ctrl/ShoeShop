package com.example.shoeshop.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoeshop.data.RetrofitInstance
import com.example.shoeshop.data.model.OTPVerificationRequest

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class OTPVerificationState {
    object Initial : OTPVerificationState()
    object Loading : OTPVerificationState()
    data class Success(val message: String) : OTPVerificationState()
    data class Error(val message: String) : OTPVerificationState()
}

class OTPViewModel : ViewModel() {
    private val _verificationState = MutableStateFlow<OTPVerificationState>(OTPVerificationState.Initial)
    val verificationState: StateFlow<OTPVerificationState> = _verificationState

    private val service = RetrofitInstance.userManagementService

    fun verifyOTP(email: String, otpCode: String) {
        viewModelScope.launch {
            _verificationState.value = OTPVerificationState.Loading
            try {
                Log.d("OTPViewModel", "=== VERIFY OTP START ===")
                Log.d("OTPViewModel", "Email: $email")
                Log.d("OTPViewModel", "OTP Code: $otpCode")

                // Создаем правильный request с типом "signup"
                val request = OTPVerificationRequest(
                    type = "signup",  // или "email" в зависимости от вашего случая
                    email = email,
                    token = otpCode
                )
                Log.d("OTPViewModel", "Request: type=${request.type}, email=${request.email}, token=${request.token}")

                val response = service.verifyOTP(request)

                Log.d("OTPViewModel", "Response code: ${response.code()}")
                Log.d("OTPViewModel", "Response message: ${response.message()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("OTPViewModel", "Response body: $body")

                    // Проверяем успешность (обычно приходит access_token)
                    if (body != null) {
                        _verificationState.value = OTPVerificationState.Success(
                            "Email verified successfully!"
                        )
                    } else {
                        _verificationState.value = OTPVerificationState.Success(
                            "Email verified!"
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("OTPViewModel", "Error response: $errorBody")

                    // Парсим сообщение об ошибке
                    val errorMessage = when {
                        errorBody?.contains("expired") == true -> "OTP code has expired"
                        errorBody?.contains("invalid") == true -> "Invalid OTP code"
                        errorBody?.contains("Token has expired") == true -> "OTP code has expired"
                        else -> "Verification failed: ${response.code()}"
                    }

                    _verificationState.value = OTPVerificationState.Error(errorMessage)
                }

                Log.d("OTPViewModel", "=== VERIFY OTP END ===")

            } catch (e: Exception) {
                Log.e("OTPViewModel", "Exception in verifyOTP", e)
                _verificationState.value = OTPVerificationState.Error(
                    e.message ?: "OTP verification failed"
                )
            }
        }
    }

    fun resendOTP(email: String) {
        viewModelScope.launch {
            try {
                Log.d("OTPViewModel", "Resending OTP to $email")

                // Для повторной отправки OTP обычно используется отдельный эндпоинт
                // или можно использовать signup с тем же email
                val request = mapOf("email" to email)
                val response = service.resendOTP(request)

                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    if (otpResponse?.success == true) {
                        Log.d("OTPViewModel", "OTP resent successfully to $email")
                    } else {
                        Log.e("OTPViewModel", "Failed to resend OTP: ${otpResponse?.message}")
                    }
                } else {
                    Log.e("OTPViewModel", "Failed to resend OTP: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e("OTPViewModel", "Error resending OTP", e)
            }
        }
    }

    fun resetState() {
        _verificationState.value = OTPVerificationState.Initial
    }
}