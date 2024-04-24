package com.example.innerloop.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val icon: ImageVector,
    val route: String,
    val title: String
)
