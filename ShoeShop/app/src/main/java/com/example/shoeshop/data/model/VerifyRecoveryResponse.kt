

data class VerifyRecoveryResponse(
    val success: Boolean,
    val reset_token: String,  // Токен для сброса пароля
    val message: String? = null // токен для сброса пароля
)