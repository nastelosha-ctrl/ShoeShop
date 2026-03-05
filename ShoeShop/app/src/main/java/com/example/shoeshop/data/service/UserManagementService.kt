

import com.example.shoeshop.data.model.Category
import com.example.shoeshop.data.model.ForgotPasswordRequest
import com.example.shoeshop.data.model.Profile

import com.example.shoeshop.data.model.ResendOTPResponse
import com.example.shoeshop.data.model.SignInRequest
import com.example.shoeshop.data.model.SignInResponse
import com.example.shoeshop.data.model.SignUpRequest
import com.example.shoeshop.data.model.SignUpResponse
import com.example.shoeshop.data.model.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

const val API_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inhud2Vpb2p0enFqbnNkd3JycmZpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI2MzUxNDksImV4cCI6MjA4ODIxMTE0OX0.MFu1Vr80Yl_7UFp7wBNfbi56ZONW4ZSGo6SsKePmONY"
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
        "apikey: ${API_KEY}",
        "Content-Type: application/json"
    )
    @POST("auth/v1/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtpResponse>

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
    @POST("auth/v1/token?grant_type=refresh_token")
    suspend fun refreshToken(
        @Body request: Map<String, String>
    ): Response<Map<String, Any>>

    @Headers(
        "apikey: ${API_KEY}",
        "Content-Type: application/json"
    )
    @PUT("auth/v1/user")
    suspend fun changePassword(
        @Header("Authorization") authorization: String, // Bearer токен
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>


    // ===== ПРОФИЛЬ =====

    @GET("rest/v1/profiles")
    suspend fun getProfile(
        @Query("user_id") filter: String,
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY
    ): Response<List<Profile>>

    @POST("rest/v1/profiles")
    suspend fun createProfile(
        @Body profile: Profile,
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY,
        @Header("Prefer") prefer: String = "return=representation"
    ): Response<List<Profile>>


    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(
        @Query("user_id") filter: String,
        @Body updates: UpdateProfileRequest,  // ← Используем специальный data class
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY,
        @Header("Prefer") prefer: String = "return=representation"
    ): Response<List<Profile>>

    @PATCH("rest/v1/profiles")
    suspend fun updateProfileById(
        @Query("id") filter: String,
        @Body updates: UpdateProfileRequest,  // ← Используем специальный data class
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY,
        @Header("Prefer") prefer: String = "return=representation"
    ): Response<List<Profile>>

    // ===== CATALOG METHODS =====

    @GET("rest/v1/categories")
    suspend fun getCategories(
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY
    ): Response<List<Category>>

    @GET("rest/v1/products")
    suspend fun getProducts(
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY
    ): Response<List<Product>>

    @GET("rest/v1/products")
    suspend fun getProductsByCategory(
        @Query("category_id") filter: String,
        @Header("Authorization") authorization: String,
        @Header("apikey") apiKey: String = API_KEY
    ): Response<List<Product>>





}