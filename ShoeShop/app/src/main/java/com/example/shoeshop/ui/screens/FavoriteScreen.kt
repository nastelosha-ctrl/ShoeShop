package com.example.shoeshop.ui.screens

import Product
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shoeshop.R
import com.example.shoeshop.ui.theme.AppTypography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favoriteProducts: List<Product>,
    onBackClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onRemoveFromFavorites: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Избранное",
                        style = AppTypography.headingSemiBold16,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Вернуться к покупкам"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            if (favoriteProducts.isEmpty()) {
                // Пустое состояние
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Список избранного пуст",
                        style = AppTypography.bodyRegular16,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Добавляйте товары, нажимая на сердечко",
                        style = AppTypography.bodyRegular14,
                        color = Color.Gray.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Список избранных товаров
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = favoriteProducts,
                        key = { it.id }
                    ) { product ->
                        FavoriteItem(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onRemoveClick = { onRemoveFromFavorites(product) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(
    product: Product,
    onProductClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Изображение товара
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👟",
                            fontSize = 32.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о товаре
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Метка BEST SELLER
                if (product.is_best_seller == true) {
                    Text(
                        text = "Популярное".uppercase(),
                        style = AppTypography.bodyMedium16,
                        color = Color(0xFF2F80ED),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Название товара
                Text(
                    text = product.title,
                    style = AppTypography.bodyMedium16,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Цена
                Text(
                    text = "₽${String.format("%.2f", product.cost)}",
                    style = AppTypography.bodyMedium16,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Кнопка удаления из избранного
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Избранное",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Для превью
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoriteScreenPreview() {
    val sampleProducts = listOf(
        Product(
            id = "1",
            title = "Nike Air Max",
            cost = 752.00,
            description = "Стильные и комфортные кроссовки",
            category_id = "76ab9d74-7d5b-4dee-9c67-6ed4019fa202",
            is_best_seller = true,
            imageUrl = null,
            imageResId = null
        ),
        Product(
            id = "2",
            title = "Nike Air Max",
            cost = 752.00,
            description = "Стильные и комфортные кроссовки",
            category_id = "76ab9d74-7d5b-4dee-9c67-6ed4019fa202",
            is_best_seller = true,
            imageUrl = null,
            imageResId = null
        ),
        Product(
            id = "3",
            title = "Nike Air Max",
            cost = 752.00,
            description = "Стильные и комфортные кроссовки",
            category_id = "76ab9d74-7d5b-4dee-9c67-6ed4019fa202",
            is_best_seller = true,
            imageUrl = null,
            imageResId = null
        ),
        Product(
            id = "4",
            title = "Nike Air Max",
            cost = 752.00,
            description = "Стильные и комфортные кроссовки",
            category_id = "76ab9d74-7d5b-4dee-9c67-6ed4019fa202",
            is_best_seller = true,
            imageUrl = null,
            imageResId = null
        )
    )

    FavoriteScreen(
        favoriteProducts = sampleProducts,
        onBackClick = {},
        onProductClick = {},
        onRemoveFromFavorites = {}
    )
}