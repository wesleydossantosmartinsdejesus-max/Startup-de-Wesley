package com.example.ciclomultfood.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CardBorder
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg

import com.example.ui.theme.WhatsAppGreen

@Composable
fun ListingCard(
    listing: ListingEntity,
    onContactWhatsApp: (ListingEntity) -> Unit,
    onReportListing: (Int) -> Unit,
    onOpenSellerProfile: ((ListingEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    val categoryColor = when (listing.category) {
        "DOACAO" -> AmberAccent
        "RECICLAGEM_ORGANICA" -> Color(0xFF795548)
        else -> GreenPrimary
    }

    val categoryText = when (listing.category) {
        "DOACAO" -> "Doação • Zero Fome"
        "RECICLAGEM_ORGANICA" -> "Reciclagem Orgânica"
        else -> "Compra & Venda Direta"
    }

    val drawableRes = when {
        listing.imageUrl.contains("fresh_produce") -> R.drawable.img_fresh_produce_1785470112199
        listing.imageUrl.contains("compost") -> R.drawable.img_compost_organic_1785470123756
        listing.imageUrl.contains("donation") -> R.drawable.img_food_donation_1785470135793
        listing.category == "DOACAO" -> R.drawable.img_food_donation_1785470135793
        listing.category == "RECICLAGEM_ORGANICA" -> R.drawable.img_compost_organic_1785470123756
        else -> R.drawable.img_fresh_produce_1785470112199
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("listing_card_${listing.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorder)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Header: User avatar, seller name, neighborhood, Selo Pro badge & Options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onOpenSellerProfile?.invoke(listing) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(categoryColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = listing.sellerName.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = categoryColor,
                            fontSize = 16.sp
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = listing.sellerName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (listing.sellerIsPro) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Selo Pro",
                                    tint = ProBadgeBg,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Local",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = listing.sellerNeighborhood,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 3-dot Menu for Moderation & Profile
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opções",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Compartilhar Anúncio") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    tint = GreenPrimary
                                )
                            },
                            onClick = {
                                showMenu = false
                                shareListingNative(context, listing)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ver Perfil & ESG do Anunciante") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = GreenPrimary
                                )
                            },
                            onClick = {
                                showMenu = false
                                onOpenSellerProfile?.invoke(listing)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Denunciar Anúncio (Moderação)") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ReportProblem,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                showMenu = false
                                onReportListing(listing.id)
                            }
                        )
                    }
                }
            }

            // Image Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = listing.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category Tag Overlay
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    color = categoryColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = categoryText,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Price Badge Overlay
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomEnd),
                    color = if (listing.price == 0.0) AmberAccent else GreenPrimary,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (listing.price == 0.0) "GRÁTIS (DOAÇÃO)" else "R$ %.2f".format(listing.price),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Title & Details
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listing.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Qtd: ${listing.quantity}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = listing.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                val context = LocalContext.current
                val displayLocation = if (!listing.sellerMapAddress.isNullOrBlank()) listing.sellerMapAddress else listing.sellerNeighborhood

                OutlinedButton(
                    onClick = { openGoogleMaps(context, displayLocation) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("btn_maps_${listing.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = GreenPrimary.copy(alpha = 0.06f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Maps",
                        tint = GreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "📍 $displayLocation",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GreenPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // WhatsApp Direct Contact Button
                Button(
                    onClick = {
                        onContactWhatsApp(listing)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_whatsapp_${listing.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Combinar Retirada via WhatsApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

fun openWhatsAppDirect(context: Context, phone: String, productTitle: String) {
    val cleanPhone = phone.replace(Regex("[^0-9]"), "")
    val message = "Olá! Vi seu anúncio '$productTitle' no aplicativo Ciclomult_Food e gostaria de combinar a retirada."
    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$cleanPhone"))
        context.startActivity(fallbackIntent)
    }
}

@Composable
fun ListingGridCard(
    listing: ListingEntity,
    onContactWhatsApp: (ListingEntity) -> Unit,
    onReportListing: (Int) -> Unit,
    onOpenSellerProfile: ((ListingEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (listing.category) {
        "DOACAO" -> AmberAccent
        "RECICLAGEM_ORGANICA" -> Color(0xFF795548)
        else -> GreenPrimary
    }

    val drawableRes = when {
        listing.imageUrl.contains("fresh_produce") -> R.drawable.img_fresh_produce_1785470112199
        listing.imageUrl.contains("compost") -> R.drawable.img_compost_organic_1785470123756
        listing.imageUrl.contains("donation") -> R.drawable.img_food_donation_1785470135793
        listing.category == "DOACAO" -> R.drawable.img_food_donation_1785470135793
        listing.category == "RECICLAGEM_ORGANICA" -> R.drawable.img_compost_organic_1785470123756
        else -> R.drawable.img_fresh_produce_1785470112199
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("listing_grid_card_${listing.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorder)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(98.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = listing.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Price Badge
                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                    color = if (listing.price == 0.0) AmberAccent else GreenPrimary,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (listing.price == 0.0) "GRÁTIS" else "R$ %.2f".format(listing.price),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = listing.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable { onOpenSellerProfile?.invoke(listing) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(9.dp)
                    )
                    Text(
                        text = "${listing.sellerName} • ${listing.sellerNeighborhood}",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                Button(
                    onClick = { onContactWhatsApp(listing) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .testTag("btn_grid_whatsapp_${listing.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Combinar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
