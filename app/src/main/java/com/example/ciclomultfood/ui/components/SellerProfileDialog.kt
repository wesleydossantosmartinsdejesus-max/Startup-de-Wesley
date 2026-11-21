package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CardBorder
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg
import com.example.ui.theme.WhatsAppGreen

data class SellerProfileData(
    val name: String,
    val handle: String = "",
    val neighborhood: String = "",
    val whatsApp: String = "",
    val isPro: Boolean = false,
    val totalKgSaved: Double = 18.5,
    val totalCo2Kg: Double = 37.0,
    val donationsCount: Int = 6,
    val esgLevel: String = "Nível Ouro 🥇",
    val bio: String = "Integrante ativo da rede local de produção consciente e impacto socioambiental."
)

@Composable
fun SellerProfileDialog(
    profile: SellerProfileData,
    isFollowing: Boolean,
    onToggleFollow: () -> Unit,
    onContactWhatsApp: () -> Unit,
    onDismiss: () -> Unit,
    userListings: List<ListingEntity> = emptyList(),
    userPosts: List<CommunityPostEntity> = emptyList()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("seller_profile_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top header with close icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Perfil & Selo ESG",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_seller_profile")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Profile Info Header Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = GreenPrimary.copy(alpha = 0.08f)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.2f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Large Avatar
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .background(GreenPrimary)
                                        .border(2.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profile.name.take(1).uppercase(),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = profile.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                    if (profile.isPro) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = ProBadgeBg,
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Verified,
                                                    contentDescription = "Pro",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "PRO",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }

                                val handleFormatted = if (profile.handle.isNotBlank()) profile.handle else "@${profile.name.lowercase().replace(" ", "")}"
                                Text(
                                    text = handleFormatted,
                                    fontSize = 13.sp,
                                    color = GreenPrimary,
                                    fontWeight = FontWeight.Medium
                                )

                                if (profile.neighborhood.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Place,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = profile.neighborhood,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = profile.bio,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Action buttons: Follow & WhatsApp
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = onToggleFollow,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .testTag("btn_toggle_follow_profile"),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isFollowing) MaterialTheme.colorScheme.surfaceVariant else GreenPrimary,
                                            contentColor = if (isFollowing) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isFollowing) "Seguindo" else "Seguir",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }

                                    if (profile.whatsApp.isNotBlank()) {
                                        Button(
                                            onClick = onContactWhatsApp,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp)
                                                .testTag("btn_whatsapp_profile"),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = WhatsAppGreen,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Chat,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "WhatsApp",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ESG Impact Metrics Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = GreenPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Pontuação & Métricas ESG",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Surface(
                                        color = AmberAccent.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = profile.esgLevel,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberAccent,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    EsgStatTile(
                                        icon = Icons.Default.Eco,
                                        value = "%.1f kg".format(profile.totalKgSaved),
                                        label = "Salvos"
                                    )
                                    EsgStatTile(
                                        icon = Icons.Default.Co2,
                                        value = "%.1f kg".format(profile.totalCo2Kg),
                                        label = "CO₂ Evitado"
                                    )
                                    EsgStatTile(
                                        icon = Icons.Default.VolunteerActivism,
                                        value = "${profile.donationsCount}",
                                        label = "Doações"
                                    )
                                }
                            }
                        }
                    }

                    // User Listings or Posts section
                    if (userListings.isNotEmpty()) {
                        item {
                            Text(
                                text = "Anúncios do Produtor (${userListings.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        items(userListings) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${item.category} • ${item.quantity}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = if (item.price == 0.0) "GRÁTIS" else "R$ %.2f".format(item.price),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (item.price == 0.0) AmberAccent else GreenPrimary
                                    )
                                }
                            }
                        }
                    }

                    if (userPosts.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Publicações Sociais (${userPosts.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        items(userPosts) { post ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = post.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = post.content,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EsgStatTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(GreenPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = GreenPrimary
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
