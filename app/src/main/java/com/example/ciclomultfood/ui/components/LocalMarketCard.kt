package com.example.ciclomultfood.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ui.theme.GreenPrimary

@Composable
fun LocalMarketCard(
    market: LocalMarketEntity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("card_local_market_${market.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Header: Icon + Name + Firestore Synced Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = GreenPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = market.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Coleção Firestore: local_markets",
                            fontSize = 10.sp,
                            color = GreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    color = GreenPrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "Firestore ok",
                            tint = GreenPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Ativa",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                thickness = 0.8.dp
            )

            // Location
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Localização",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                )
                Column {
                    Text(
                        text = "Localização",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = market.location,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Operating Hours
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Horário",
                    tint = GreenPrimary,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                )
                Column {
                    Text(
                        text = "Horário de Funcionamento",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = market.operatingHours,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Available Items
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Itens disponíveis",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                )
                Column {
                    Text(
                        text = "Itens Disponíveis",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = market.availableItems,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Contact WhatsApp Button
            if (market.organizerContact.isNotBlank()) {
                Button(
                    onClick = {
                        val uri = Uri.parse("https://api.whatsapp.com/send?phone=${market.organizerContact}&text=Olá! Vi a feira local '${market.name}' no app Ciclomult Food e gostaria de mais informações.")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        runCatching { context.startActivity(intent) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("btn_contact_market_${market.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Falar com Organizador da Feira",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
