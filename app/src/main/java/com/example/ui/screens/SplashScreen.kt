package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SoftBlack
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val context = LocalContext.current
    val background = MaterialTheme.colorScheme.background
    val isDarkTheme = remember(background) {
        with(background) { (red * 0.299f + green * 0.587f + blue * 0.114f) <= 0.5f }
    }
    
    // Animation states
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }
    var glowExpanded by remember { mutableStateOf(false) }

    // Floating and gold light sweep animations
    val transition = rememberInfiniteTransition(label = "SplashFloat")
    val logoOffset by transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LogoFloat"
    )

    // Logo scale transition on start
    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1.0f else 0.75f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LogoScale"
    )

    // Logo alpha transition
    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1.0f else 0.0f,
        animationSpec = tween(1200, easing = EaseInOutCubic),
        label = "LogoAlpha"
    )

    // Text alpha and slide transition
    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1.0f else 0.0f,
        animationSpec = tween(1000, easing = EaseOutQuart),
        label = "TextAlpha"
    )

    // Ambient radial glow spread
    val glowSize by animateDpAsState(
        targetValue = if (glowExpanded) 200.dp else 40.dp,
        animationSpec = tween(1800, easing = EaseInOutQuart),
        label = "GlowSize"
    )

    LaunchedEffect(Unit) {
        // Timeline sequence
        delay(300)
        logoVisible = true
        delay(600)
        glowExpanded = true
        delay(800)
        textVisible = true
        // Premium duration 3.2 seconds
        delay(1800)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Soft Light Radial Diffusion (Golden glow aura)
        Box(
            modifier = Modifier
                .size(glowSize)
                .blur(50.dp)
                .alpha(if (isDarkTheme) 0.25f else 0.12f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.offset(y = logoOffset.dp)
        ) {
            // Splash Logo with Shadow & Glass Border
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
                    .shadow(
                        elevation = if (isDarkTheme) 20.dp else 8.dp,
                        shape = androidx.compose.foundation.shape.CircleShape,
                        clip = false,
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        spotColor = MaterialTheme.colorScheme.primary
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://res.cloudinary.com/dnmgvjg3h/image/upload/v1783010583/IMG_20260702_192242_814_absnox.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Teke Man Splash Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Brand Typography
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = if (textVisible) 0.dp else 12.dp)
            ) {
                Text(
                    text = "TEKE MAN",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "PROMOTION",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 8.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Elegant luxury subtitle
                Text(
                    text = "B e s p o k e  L u x u r y  G i f t i n g",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
