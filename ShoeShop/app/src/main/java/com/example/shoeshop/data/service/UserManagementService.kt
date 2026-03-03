

import com.example.shoeshop.data.model.ForgotPasswordRequest
import com.example.shoeshop.data.model.OTPVerificationRequest
import com.example.shoeshop.data.model.OTPVerificationResponse
import com.example.shoeshop.data.model.ResendOTPResponse
import com.example.shoeshop.data.model.SignInRequest
import com.example.shoeshop.data.model.SignInResponse
import com.example.shoeshop.data.model.SignUpRequest
import com.example.shoeshop.data.model.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

const val API_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp5ZHJ2eGhjcWdwZG1wZWVzdmxzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI0Nzc4NTgsImV4cCI6MjA4ODA1Mzg1OH0.Er7dbKS-8GnU5iZ2lh83oxkn3qqDh6E9gamM74s4Olc"
interface UserManagementService {

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @POST("auth/v1/logout")
    suspend fun logout(): Response<Unit>

    @Headers(
        "apikey: $API_KEY",
        "Authorization: Bearer $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("auth/v1/verify")
    suspend fun verifyOTP(@Body request: OTPVerificationRequest): Response<OTPVerificationResponse>

    @Headers(
        "apikey: $API_KEY",
        "Authorization: Bearer $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("rest/v1/rpc/resend_otp")
    suspend fun resendOTP(@Body request: Map<String, String>): Response<ResendOTPResponse>

    @Headers(
        "apikey: ${API_KEY}",
        "Content-Type: application/json"
    )
    @POST("auth/v1/recover")
    suspend fun recoverPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    @Headers(
        "apikey: ${API_KEY}",
        "Content-Type: application/json"
    )
    @PUT("auth/v1/user")
    suspend fun changePassword(
        @Header("Authorization") token: String, // Bearer токен пользователя
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

}