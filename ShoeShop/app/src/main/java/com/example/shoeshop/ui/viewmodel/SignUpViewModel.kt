// SignUpViewModel.kt
package com.example.myfirstproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstproject.data.RetrofitInstance
import com.example.myfirstproject.data.model.SignUpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun signUp(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                Log.d("SignUpViewModel", "Attempting to sign up: ${signUpRequest.email}")

                val response = RetrofitInstance.userManagementService.signUp(signUpRequest)

                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("SignUpViewModel", "Sign up successful, user id: ${it.id}")
                        _signUpState.value = SignUpState.Success(signUpRequest.email)
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid email or password"
                        422 -> "Unable to validate email address: invalid format"
                        429 -> "Too many requests"
                        else -> "Registration failed: ${response.message()}"
                    }
                    Log.e("SignUpViewModel", "Sign up failed: $errorMessage")
                    _signUpState.value = SignUpState.Error(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.ConnectException -> "No internet connection"
                    is java.net.SocketTimeoutException -> "Connection timeout"
                    else -> "Network error: ${e.message}"
                }
                _signUpState.value = SignUpState.Error(errorMessage)
                Log.e("SignUpViewModel", "Sign up error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _signUpState.value = SignUpState.Idle
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val email: String) : SignUpState()  // Важно: data class с email
    data class Error(val message: String) : SignUpState()
}