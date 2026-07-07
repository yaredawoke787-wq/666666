package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GiftProduct
import com.example.ui.components.ProductImage
import com.example.ui.viewmodel.GiftViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: GiftViewModel,
    onBackClick: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val context = LocalContext.current

    // Admin UI States
    var editingProduct by remember { mutableStateOf<GiftProduct?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }

    // Form inputs state
    var formTitle by remember { mutableStateOf("") }
    var formSubtitle by remember { mutableStateOf("") }
    var formPrice by remember { mutableStateOf("") }
    var formCategory by remember { mutableStateOf("Luxury") }
    var formDescription by remember { mutableStateOf("") }
    var formImageUrl by remember { mutableStateOf("") }
    var formVideoUrl by remember { mutableStateOf("") }
    var formImageResSelection by remember { mutableStateOf(R.drawable.img_luxury_box_1783249841023) }

    val categories = listOf("Luxury", "Wedding", "Birthday", "Graduation", "Business")
    
    // Fallback drawables mapping
    val localDrawables = listOf(
        R.drawable.img_luxury_box_1783249841023 to "Luxury Box",
        R.drawable.img_wedding_gift_1783249854737 to "Wedding Hamper",
        R.drawable.img_birthday_gift_1783249866279 to "Surprise Box",
        R.drawable.img_graduation_gift_1783249879106 to "Graduation Portfolio",
        R.drawable.img_business_gift_1783249891507 to "Business Set"
    )

    fun resetForm() {
        formTitle = ""
        formSubtitle = ""
        formPrice = ""
        formCategory = "Luxury"
        formDescription = ""
        formImageUrl = ""
        formVideoUrl = ""
        formImageResSelection = R.drawable.img_luxury_box_1783249841023
    }

    fun startEditing(product: GiftProduct) {
        editingProduct = product
        formTitle = product.title
        formSubtitle = product.subtitle
        formPrice = product.price.toString()
        formCategory = product.category
        formDescription = product.description
        formImageUrl = product.imageUrl ?: ""
        formVideoUrl = product.videoUrl ?: ""
        formImageResSelection = if (product.imageResId != 0) product.imageResId else R.drawable.img_luxury_box_1783249841023
        isAddingNew = false
    }

    // Colors matching Teke Man Promotion design
    val LuxuryGold = Color(0xFFF2B705)
    val DarkSlateBg = Color(0xFF111115)
    val CardBg = Color(0xFF1B1B22)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isAddingNew || editingProduct != null) {
                            isAddingNew = false
                            editingProduct = null
                        } else {
                            onBackClick()
                        }
                    },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = LuxuryGold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "TEKE MAN PROMOTION",
                        color = LuxuryGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = if (currentLanguage == "AM") "አስተዳዳሪ ፖርታል" else "Admin App Portal",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            // Bulletproof Separator Line
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.08f))
            )

            if (!isAddingNew && editingProduct == null) {
                // DASHBOARD VIEW
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (currentLanguage == "AM") "አጠቃላይ ምርቶች" else "TOTAL PRODUCTS",
                                        color = LuxuryGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${products.size} Items",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Button(
                                    onClick = {
                                        resetForm()
                                        isAddingNew = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (currentLanguage == "AM") "አዲስ ምርት" else "Add Gift",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (currentLanguage == "AM") "የምርቶች ዝርዝር (ለመቀየር ይጫኑ)" else "MANAGE PRODUCTS (Tap card to edit)",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(products) { product ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { startEditing(product) }
                                        .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                                    colors = CardDefaults.cardColors(containerColor = CardBg),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ProductImage(
                                            product = product,
                                            modifier = Modifier
                                                .size(68.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )

                                        Spacer(modifier = Modifier.width(14.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = product.category.uppercase(),
                                                    color = LuxuryGold,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp
                                                )
                                                if (product.videoUrl != null) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.PlayArrow,
                                                            contentDescription = "Video",
                                                            tint = LuxuryGold,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Text(
                                                            text = "VIDEO",
                                                            color = LuxuryGold,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                            Text(
                                                text = product.title,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = product.subtitle,
                                                color = Color.Gray,
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "ETB ${String.format("%,.2f", product.price)}",
                                                color = LuxuryGold,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        IconButton(
                                            onClick = { startEditing(product) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit",
                                                tint = Color.White.copy(alpha = 0.6f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // FORM VIEW (Add or Edit)
                val isEditing = editingProduct != null
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isEditing) {
                                if (currentLanguage == "AM") "ምርት ያሻሽሉ" else "EDIT PRODUCT DETAILS"
                            } else {
                                if (currentLanguage == "AM") "አዲስ ምርት ያክሉ" else "ADD NEW PRODUCT DETAILS"
                            },
                            color = LuxuryGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = formTitle,
                            onValueChange = { formTitle = it },
                            label = { Text("Product Name / Title", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedLabelColor = LuxuryGold,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = formSubtitle,
                            onValueChange = { formSubtitle = it },
                            label = { Text("Short Subtitle / Brand Note", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedLabelColor = LuxuryGold,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = formPrice,
                                onValueChange = { formPrice = it },
                                label = { Text("Price (ETB)", color = Color.Gray) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxuryGold,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                    focusedLabelColor = LuxuryGold,
                                    unfocusedLabelColor = Color.Gray,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Category Selector
                            var expanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.weight(1f).padding(top = 8.dp)) {
                                Button(
                                    onClick = { expanded = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = CardBg),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                    contentPadding = PaddingValues(horizontal = 14.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(horizontalAlignment = Alignment.Start) {
                                            Text("Category", color = Color.Gray, fontSize = 10.sp)
                                            Text(formCategory, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop", tint = LuxuryGold)
                                    }
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(CardBg)
                                ) {
                                    categories.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text(cat, color = Color.White) },
                                            onClick = {
                                                formCategory = cat
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = formDescription,
                            onValueChange = { formDescription = it },
                            label = { Text("Full Description (Handcrafting details, fabrics, engravings)", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedLabelColor = LuxuryGold,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            maxLines = 5,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        Column {
                            Text(
                                text = "Product Image Configuration",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Text(
                                text = "You can enter a remote image URL or choose one of our high-resolution design templates below.",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            OutlinedTextField(
                                value = formImageUrl,
                                onValueChange = { formImageUrl = it },
                                label = { Text("Remote Image URL (e.g. Cloudinary/Imgur link)", color = Color.Gray) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxuryGold,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                    focusedLabelColor = LuxuryGold,
                                    unfocusedLabelColor = Color.Gray,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Or select a template style:",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(localDrawables) { (res, label) ->
                                    val selected = formImageResSelection == res && formImageUrl.isEmpty()
                                    Card(
                                        modifier = Modifier
                                            .width(110.dp)
                                            .clickable {
                                                formImageResSelection = res
                                                formImageUrl = "" // clear url to prioritize selected resource
                                            }
                                            .border(
                                                width = if (selected) 2.dp else 0.5.dp,
                                                color = if (selected) LuxuryGold else Color.White.copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = CardBg)
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Image(
                                                painter = painterResource(id = res),
                                                contentDescription = label,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(65.dp)
                                                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Text(
                                                text = label,
                                                color = if (selected) LuxuryGold else Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
                                                maxLines = 1,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = formVideoUrl,
                            onValueChange = { formVideoUrl = it },
                            label = { Text("Product Video URL (Optional, MP4 link)", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedLabelColor = LuxuryGold,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (formTitle.trim().isEmpty() || formSubtitle.trim().isEmpty() || formPrice.trim().isEmpty() || formDescription.trim().isEmpty()) {
                                    Toast.makeText(context, "Please fill in all primary fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val priceVal = formPrice.toDoubleOrNull() ?: 0.0
                                val videoUrlVal = if (formVideoUrl.trim().isEmpty()) null else formVideoUrl.trim()
                                val imageUrlVal = if (formImageUrl.trim().isEmpty()) null else formImageUrl.trim()

                                if (isEditing) {
                                    editingProduct?.let { orig ->
                                        val updated = orig.copy(
                                            title = formTitle.trim(),
                                            subtitle = formSubtitle.trim(),
                                            price = priceVal,
                                            category = formCategory,
                                            description = formDescription.trim(),
                                            imageUrl = imageUrlVal,
                                            imageResId = if (imageUrlVal != null) 0 else formImageResSelection,
                                            videoUrl = videoUrlVal
                                        )
                                        viewModel.updateProduct(updated)
                                        Toast.makeText(context, "Product successfully updated!", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    val newProduct = GiftProduct(
                                        id = 0,
                                        title = formTitle.trim(),
                                        subtitle = formSubtitle.trim(),
                                        price = priceVal,
                                        rating = 4.8f,
                                        description = formDescription.trim(),
                                        imageUrl = imageUrlVal,
                                        imageResId = if (imageUrlVal != null) 0 else formImageResSelection,
                                        category = formCategory,
                                        videoUrl = videoUrlVal
                                    )
                                    viewModel.addProduct(newProduct)
                                    Toast.makeText(context, "Product successfully added!", Toast.LENGTH_LONG).show()
                                }

                                isAddingNew = false
                                editingProduct = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isEditing) {
                                    if (currentLanguage == "AM") "ማሻሻያውን አስቀምጥ" else "Save Product Updates"
                                } else {
                                    if (currentLanguage == "AM") "አዲሱን ምርት ፍጠር" else "Publish New Product"
                                },
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                isAddingNew = false
                                editingProduct = null
                            },
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = if (currentLanguage == "AM") "ሰርዝ" else "Cancel & Go Back",
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
