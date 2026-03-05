package com.example.shoeshop.ui.screens

import Product
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shoeshop.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    product: Product,
    onBackClick: () -> Unit,
    onAddToCartClick: (Product) -> Unit,
    onFavoriteClick: (Product, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details", // Или используйте stringResource
                        style = AppTypography.headingSemiBold16,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Изображение товара
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenWidth * 1.1f)
                    .background(Color(0xFFF5F5F5))
            ) {
                if (product.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (product.imageResId != null) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Плейсхолдер
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👟",
                            fontSize = 64.sp,
                            style = AppTypography.bodyRegular24
                        )
                    }
                }

                // Кнопка избранного
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        onFavoriteClick(product, isFavorite)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Информация о товаре
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Название магазина
                Text(
                    text = "Sneaker Shop",
                    style = AppTypography.bodyRegular14,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Название товара
                Text(
                    text = product.title,
                    style = AppTypography.headingRegular20,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Категория
                Text(
                    text = when (product.category_id) {
                        "ea4ed603-8cbe-4d57-a359-b6b843a645bc" -> "Outdoor"
                        "4f3a690b-41bf-4fca-8ffc-67cc385c6637" -> "Tennis"
                        "76ab9d74-7d5b-4dee-9c67-6ed4019fa202" -> "Men's Shoes"
                        "8143b506-d70a-41ec-a5eb-3cf09627da9e" -> "Women's Shoes"
                        else -> "Shoes"
                    },
                    style = AppTypography.bodyRegular14,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Цена
                Text(
                    text = "₽${String.format("%.2f", product.cost)}",
                    style = AppTypography.headingSemiBold16,
                    color = Color(0xFF2F80ED)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Описание
                Text(
                    text = "Описание",
                    style = AppTypography.bodyMedium14,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Text(
                        text = product.description,
                        style = AppTypography.bodyRegular14,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = if (isExpanded) "Скрыть" else "Подробнее",
                            color = Color(0xFF2F80ED),
                            style = AppTypography.bodyMedium14
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка добавления в корзину
                Button(
                    onClick = { onAddToCartClick(product) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2F80ED)
                    )
                ) {
                    Text(
                        text = "В Корзину",
                        style = AppTypography.bodyMedium16.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailsScreenPreview() {
    val sampleProduct = Product(
        id = "1",
        title = "Nike Air Max 270",
        cost = 179.39,
        description = "Вставка Max Air 270 Обеспечивает Непревзойденный Комфорт В Течение Всего Дня. Изящный Дизайн с верхом из сетки и накладками обеспечивает воздухопроницаемость и поддержку. Идеально подходит для повседневной носки и активного отдыха.",
        category_id = "76ab9d74-7d5b-4dee-9c67-6ed4019fa202",
        is_best_seller = true,
        imageUrl = null,
        imageResId = null
    )

    DetailsScreen(
        product = sampleProduct,
        onBackClick = {},
        onAddToCartClick = {},
        onFavoriteClick = { _, _ -> }
    )
}