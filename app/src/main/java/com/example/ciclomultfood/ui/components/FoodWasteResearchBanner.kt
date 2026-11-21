package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GreenPrimary

@Composable
fun FoodWasteResearchBanner(
    onOpenResearchDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .testTag("food_waste_research_banner"),
        color = GreenPrimary.copy(alpha = 0.06f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.22f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isCollapsed) {
                // Collapsed View - Compact horizontal bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isCollapsed = false },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = GreenPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = "Pesquisa Google ONU",
                                tint = GreenPrimary,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(14.dp)
                            )
                        }
                        Text(
                            text = "Central de Pesquisas • Google • ONU • Embrapa",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = GreenPrimary,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .clickable { onOpenResearchDialog() }
                                .testTag("btn_open_research_facts_collapsed")
                        ) {
                            Text(
                                text = "Abrir",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        IconButton(
                            onClick = { isCollapsed = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expandir Ferramenta",
                                tint = GreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            } else {
                // Expanded View - Clean, organized 3-part layout

                // Part 1: Header Row (Title, Status Badge, Collapse Arrow)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = GreenPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(16.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Central de Pesquisas & Ciência",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    color = GreenPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "🟢 Auto-Atualizado",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = { isCollapsed = true },
                        modifier = Modifier
                            .size(24.dp)
                            .testTag("btn_collapse_research_banner")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Encolher Ferramenta",
                            tint = GreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                HorizontalDivider(
                    color = GreenPrimary.copy(alpha = 0.15f),
                    thickness = 0.8.dp
                )

                // Part 2: Organized Source Badges (Google, ONU, Embrapa)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Fontes:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Source 1: Google
                    SourceBadge(
                        label = "Google",
                        icon = Icons.Default.Search,
                        badgeColor = Color(0xFF4285F4)
                    )

                    // Source 2: ONU / PNUMA
                    SourceBadge(
                        label = "ONU / PNUMA",
                        icon = Icons.Default.Public,
                        badgeColor = Color(0xFF009EDB)
                    )

                    // Source 3: EMBRAPA
                    SourceBadge(
                        label = "EMBRAPA",
                        icon = Icons.Default.Park,
                        badgeColor = GreenPrimary
                    )
                }

                // Part 3: Fact Highlight & Action Button
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.15f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenResearchDialog() }
                        .testTag("btn_open_research_facts")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "📊 Dado Atualizado (2024):",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                            Text(
                                text = "79 kg de alimentos desperdiçados por pessoa/ano no Brasil",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            color = GreenPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Ver Mais",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceBadge(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badgeColor: Color
) {
    Surface(
        color = badgeColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, badgeColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
        }
    }
}



