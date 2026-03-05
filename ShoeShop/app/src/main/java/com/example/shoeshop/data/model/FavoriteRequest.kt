package com.example.shoeshop.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
    val user_id: String,
    val product_id: String
)