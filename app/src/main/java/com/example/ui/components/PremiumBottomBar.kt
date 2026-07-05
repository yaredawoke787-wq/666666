package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGoldGradient
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SoftBlack

sealed class Screen(val route: String, val label: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    object Home : Screen("home", "Gallery", Icons.Filled.Home, Icons.Outlined.Home)
    object Favorites : Screen("favorites", "Curations", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Cart : Screen("cart", "Bag", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
    object Contact : Screen("contact", "Concierge", Icons.Filled.Call, Icons.Outlined.Call)
    object Settings : Screen("settings", "Boutique", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun PremiumBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
        Screen.Cart,
        Screen.Contact,
        Screen.Settings
    )

    // Main Floating Pill Box
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(76.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .border(1.dp, CardBorderColor, RoundedCornerShape(32.dp))
            .background(Color(0xCD16161A)) // Glassmorphic high-quality alpha
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { screen ->
                val selected = currentRoute == screen.route
                
                // Elastic physical springs for scale and rotation reactions
                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.15f else 0.95f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = "IconScaleSpring"
                )

                val tintColor by animateColorAsState(
                    targetValue = if (selected) GoldAccent else Color.White.copy(alpha = 0.5f),
                    animationSpec = tween(250),
                    label = "IconTint"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!selected) {
                                onNavigate(screen.route)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Soft liquid feedback indicator behind icon
                    Box(
                        modifier = Modifier
                            .size(height = 36.dp, width = 48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) GoldAccent.copy(alpha = 0.12f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (selected) screen.activeIcon else screen.inactiveIcon,
                            contentDescription = screen.label,
                            tint = tintColor,
                            modifier = Modifier
                                .scale(scale)
                                .size(22.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(3.dp))
                    
                    Text(
                        text = screen.label,
                        color = tintColor,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
