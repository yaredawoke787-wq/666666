package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.GiftProduct
import com.example.ui.theme.*
import com.example.ui.viewmodel.GiftViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: GiftViewModel,
    onProductClick: (Int) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val context = LocalContext.current
    val products by viewModel.filteredProducts.collectAsState()
    val heroProducts by viewModel.heroProducts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val categories = listOf("All", "Luxury", "Wedding", "Birthday", "Graduation", "Business")

    // Determine Greeting based on current local time
    val greeting = remember {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    val background = MaterialTheme.colorScheme.background
    val isDarkTheme = remember(background) {
        with(background) { (red * 0.299f + green * 0.587f + blue * 0.114f) <= 0.5f }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative glowing background gradients for elite ambiance
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .blur(80.dp)
                .alpha(if (isDarkTheme) 0.12f else 0.05f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // --- TOP BAR SECTION ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Frame (Luxury Gold border holding the brand logo)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color.White else SleekLightGray)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("https://res.cloudinary.com/dnmgvjg3h/image/upload/v1783009508/IMG_20260702_192242_541_fyt0dm.png")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Teke Man Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = greeting,
                            color = WarmGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Teke Man Promotion",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                // Header Action Buttons
                Row {
                    IconButton(
                        onClick = onNavigateToFavorites,
                        modifier = Modifier
                            .background(if (isDarkTheme) Color.White.copy(alpha = 0.05f) else SleekLightGray, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorites",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        IconButton(
                            onClick = onNavigateToCart,
                            modifier = Modifier
                                .background(if (isDarkTheme) Color.White.copy(alpha = 0.05f) else SleekLightGray, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Cart",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        if (cartProducts.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .align(Alignment.TopEnd)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .border(1.5.dp, MaterialTheme.colorScheme.background, CircleShape)
                            )
                        }
                    }
                }
            }

            // --- MAIN LIST SCROLLABLE ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // --- SEARCH BAR ITEM ---
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDarkTheme) PremiumGray.copy(alpha = 0.6f) else SleekLightGray),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            unfocusedIndicatorColor = if (isDarkTheme) Color.White.copy(alpha = 0.1f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        placeholder = {
                            Text(
                                "Search luxury gifts...",
                                color = WarmGray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = WarmGray
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Search Indicator",
                                    tint = WarmGray
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                // --- 3D HERO CAROUSEL ---
                if (heroProducts.isNotEmpty() && searchQuery.isEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(vertical = 12.dp)) {
                            Text(
                                text = "EXCLUSIVE COLLECTIONS",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            val pagerState = rememberPagerState(pageCount = { heroProducts.size })
                            
                            // Auto-slide effect for premium feeling
                            LaunchedEffect(key1 = pagerState) {
                                while (true) {
                                    delay(4000)
                                    val nextPage = (pagerState.currentPage + 1) % heroProducts.size
                                    pagerState.animateScrollToPage(nextPage)
                                }
                            }

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                contentPadding = PaddingValues(horizontal = 52.dp),
                                pageSpacing = 16.dp
                            ) { page ->
                                val product = heroProducts[page]
                                
                                // Calculation of offset for 3D Projection
                                val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                                val absoluteOffset = pageOffset.absoluteValue
                                
                                // 3D transformations
                                val scale = lerp(0.85f, 1f, 1f - absoluteOffset.coerceIn(0f, 1f))
                                val rotationY = pageOffset * -28f
                                val alpha = lerp(0.6f, 1f, 1f - absoluteOffset.coerceIn(0f, 1f))
                                val translationX = pageOffset * 30.dp.value

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            this.scaleX = scale
                                            this.scaleY = scale
                                            this.rotationY = rotationY
                                            this.alpha = alpha
                                            this.cameraDistance = 12f * density
                                        }
                                        .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp))
                                        .clip(RoundedCornerShape(24.dp))
                                        .clickable { onProductClick(product.id) }
                                ) {
                                    // Hero Image background with zoom
                                    Image(
                                        painter = painterResource(id = product.imageResId),
                                        contentDescription = product.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    // Glass Gradient Overlay for luxury readable text
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        SoftBlack.copy(alpha = 0.85f)
                                                    )
                                                )
                                            )
                                    )
                                    
                                    // Info Card
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(20.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(GoldAccent, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "TRENDING",
                                                color = SoftBlack,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = product.title,
                                            color = LuxuryWhite,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = product.subtitle,
                                            color = WarmGray,
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "ETB ${product.price}",
                                            color = GoldAccent,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            
                            // Glowing Dot Indicators
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                heroProducts.forEachIndexed { index, _ ->
                                    val active = pagerState.currentPage == index
                                    val size by animateDpAsState(if (active) 24.dp else 8.dp, label = "IndicatorDot")
                                    val color by animateColorAsState(if (active) GoldAccent else Color.White.copy(alpha = 0.2f), label = "IndicatorColor")
                                    
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .height(6.dp)
                                            .width(size)
                                            .clip(CircleShape)
                                            .background(color)
                                    )
                                }
                            }
                        }
                    }
                }

                // --- CATEGORY CHIPS ---
                item {
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        Text(
                            text = "CATEGORIES",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(categories) { category ->
                                val active = selectedCategory == category
                                val bgGrad = if (active) {
                                    Brush.horizontalGradient(
                                        if (isDarkTheme) AccentGoldGradient else listOf(SleekSatinGold, SleekBrightGold)
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        if (isDarkTheme) listOf(PremiumGray, PremiumGray) else listOf(SleekLightGray, SleekLightGray)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = if (active || isDarkTheme) 0.dp else 2.dp,
                                            shape = RoundedCornerShape(14.dp),
                                            clip = false
                                        )
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(bgGrad)
                                        .clickable { viewModel.selectCategory(category) }
                                        .border(
                                            width = 1.dp,
                                            color = if (active) Color.Transparent else (if (isDarkTheme) Color.White.copy(alpha = 0.05f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .padding(horizontal = 20.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = category,
                                        color = if (active) (if (isDarkTheme) SoftBlack else Color.White) else MaterialTheme.colorScheme.onBackground,
                                        fontSize = 13.sp,
                                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // --- PRODUCT LIST CARD TITLE ---
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LUXURY GIFTS SELECTION",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${products.size} Products",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // --- PRODUCT LIST ---
                if (products.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Inbox,
                                    contentDescription = "No products found",
                                    tint = WarmGray,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No bespoke products match your criteria",
                                    color = WarmGray,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                } else {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(product) },
                            onAddToCartClick = {
                                viewModel.addToCart(product)
                                coroutineScope.launch {
                                    ScaffoldDefaults
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: GiftProduct,
    onProductClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val isDarkTheme = remember(background) {
        with(background) { (red * 0.299f + green * 0.587f + blue * 0.114f) <= 0.5f }
    }

    // Dynamic interactive hover scale
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "CardHover"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .scale(scale)
            .shadow(
                elevation = if (isDarkTheme) 0.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .border(
                width = 1.dp,
                color = if (isDarkTheme) CardBorderColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onProductClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) PremiumGray.copy(alpha = 0.4f) else Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = if (isDarkTheme) 0.05f else 0.8f))
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isDarkTheme) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else SleekBadgeGold,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.category.uppercase(),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating star",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = product.rating.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = product.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.subtitle,
                    color = WarmGray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ETB ${product.price}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Card CTA buttons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Favorite Icon
                        IconButton(
                            onClick = onFavoriteClick,
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    color = if (isDarkTheme) Color.White.copy(alpha = 0.05f) else SleekLightGray,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (product.isFavorite) Color.Red else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Add to Cart
                        IconButton(
                            onClick = onAddToCartClick,
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        if (isDarkTheme) AccentGoldGradient else listOf(SleekSatinGold, SleekBrightGold)
                                    ),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                contentDescription = "Add to Cart",
                                tint = if (isDarkTheme) SoftBlack else Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Utility linear interpolation for carousel
private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}
