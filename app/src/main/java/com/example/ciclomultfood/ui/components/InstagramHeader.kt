package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg

@Composable
fun InstagramHeader(
    creditsCount: Int,
    isPro: Boolean,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onCreditsClicked: () -> Unit,
    onEsgClicked: () -> Unit,
    onAddClicked: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Brand Name & Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ciclomult_logo_icon_1786159101385),
                        contentDescription = "Logo Ciclomult_Food",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Ciclomult",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GreenPrimary
                        )
                        Text(
                            text = "_Food",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Economia Circular & Zero Fome",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Right Actions: Theme Toggle, ESG Certificate, Credits badge, Add post button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Small Theme Toggle Button
                Surface(
                    modifier = Modifier
                        .height(26.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onToggleTheme() }
                        .testTag("btn_toggle_theme_header"),
                    color = if (isDarkTheme) Color(0xFF2C2C2C) else GreenPrimary.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color.Gray else GreenPrimary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Tema",
                            tint = if (isDarkTheme) Color(0xFFFFD54F) else GreenPrimary,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = if (isDarkTheme) "Escuro" else "Claro",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else GreenPrimary
                        )
                    }
                }

                // ESG Impact Button
                IconButton(
                    onClick = onEsgClicked,
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("btn_esg_impact")
                ) {
                    Icon(
                        imageVector = Icons.Default.CardMembership,
                        contentDescription = "Certificado ESG",
                        tint = GreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Credits Badge / Pro Status
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onCreditsClicked() }
                        .testTag("btn_credits_badge"),
                    color = if (isPro) ProBadgeBg.copy(alpha = 0.15f) else AmberAccent.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        if (isPro) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "PRO",
                                tint = ProBadgeBg,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "PRO",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = ProBadgeBg
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Créditos",
                                tint = AmberAccent,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "$creditsCount Cr.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = AmberAccent
                            )
                        }
                    }
                }

                // Add New Listing Button (optional)
                if (onAddClicked != null) {
                    IconButton(
                        onClick = onAddClicked,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary)
                            .testTag("btn_add_listing")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Novo Anúncio",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
