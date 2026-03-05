// data/model/Profile.kt
package com.example.shoeshop.data.model

data class Profile(
    val id: String? = null,
    val user_id: String,
    val firstname: String? = "",
    val lastname: String? = "",
    val address: String? = "",
    val phone: String? = ""
)