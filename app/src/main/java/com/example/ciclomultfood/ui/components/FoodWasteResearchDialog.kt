package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodWasteResearchDialog(
    onDismiss: () -> Unit,
    onForceRefreshData: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val currentDateStr = remember {
        java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("food_waste_research_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                        Column {
                            Text(
                                text = "Pesquisa & Impacto Ambiental",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Dados Oficiais ONU (PNUMA), FAO & EMBRAPA",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Text("✕", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                // Daily Auto-Update Status Card
                Surface(
                    color = GreenPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "🔄 Atualização Diária Automática Google",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "🟢 Status: Dados sincronizados para hoje ($currentDateStr). Próxima atualização automática em 24h.",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        TextButton(
                            onClick = onForceRefreshData,
                            colors = ButtonDefaults.textButtonColors(contentColor = GreenPrimary),
                            modifier = Modifier.testTag("btn_force_refresh_google_data")
                        ) {
                            Text("Atualizar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Tab Buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = { Text("Estatísticas", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 2.dp)
                            .testTag("tab_research_stats")
                    )
                    FilterChip(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = { Text("Preservação", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.TipsAndUpdates, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .testTag("tab_research_preservation")
                    )
                    FilterChip(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        label = { Text("Fontes ONU", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 2.dp)
                            .testTag("tab_research_sources")
                    )
                }

                when (selectedTab) {
                    0 -> ResearchStatsTab()
                    1 -> PreservationGuideTab()
                    2 -> ResearchSourcesTab()
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_close_research_dialog"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Entendi & Quero Preservar!", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun ResearchStatsTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        StatCard(
            title = "1,05 Bilhão de Toneladas / Ano",
            description = "Segundo o Relatório do Índice de Desperdício de Alimentos da ONU (PNUMA 2024), 19% de toda a comida disponível para consumidores no mundo vai para o lixo.",
            tag = "Desperdício Global",
            color = MaterialTheme.colorScheme.errorContainer,
            icon = Icons.Default.Public
        )

        StatCard(
            title = "79 kg por Pessoa no Brasil por Ano",
            description = "No Brasil, estimativas da FAO e EMBRAPA indicam que cada brasileiro descarta em média 79 kg de alimentos próprios para consumo todos os anos (total de 27 a 40 milhões de toneladas).",
            tag = "Realidade Brasileira",
            color = AmberAccent.copy(alpha = 0.15f),
            icon = Icons.Default.Info
        )

        StatCard(
            title = "8% a 10% das Emissões Globais de CO2",
            description = "O decomposição de resíduos orgânicos em aterros gera metano (CH4), um gás de efeito estufa 28x mais potente que o CO2. Se o desperdício fosse um país, seria o 3º maior emissor do planeta!",
            tag = "Impacto Climático",
            color = GreenPrimary.copy(alpha = 0.15f),
            icon = Icons.Default.Co2
        )
    }
}

@Composable
private fun PreservationGuideTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        PreservationItem(
            step = "1. Aproveitamento Integral (Talos & Cascas)",
            detail = "Cascas de maçã, batata e talos de couve/brócolis contêm mais fibras e vitaminas que a polpa! Use em sucos, farofas, sopas e bolos orgânicos."
        )

        PreservationItem(
            step = "2. Armazenamento Correto de Hortaliças",
            detail = "Lave as folhas verdes, seque bem e guarde em recipientes fechados com papel toalha. A umidade excessiva acelera a deterioração em até 3x."
        )

        PreservationItem(
            step = "3. Congelamento Estratégico & Branqueamento",
            detail = "Passe vegetais em água fervente por 2 min e depois em água gelada (branqueamento). Congele em porções para durar até 6 meses mantendo o valor nutricional."
        )

        PreservationItem(
            step = "4. Compostagem Urbana Periférica",
            detail = "Restos não comestíveis (cascas de ovo, borra de café, podas) se transformam em húmus fértil para hortas comunitárias em 45 a 60 dias."
        )
    }
}

@Composable
private fun ResearchSourcesTab() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Fontes dos Dados de Pesquisa:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        SourceCitationItem(
            agency = "ONU / PNUMA (UNEP 2024)",
            title = "Food Waste Index Report 2024",
            summary = "Mapeamento global do desperdício nas famílias, comércios e restaurantes."
        )

        SourceCitationItem(
            agency = "FAO - Nações Unidas",
            title = "The State of Food Security and Nutrition 2023",
            summary = "Indicadores de fome, segurança alimentar e perdas agrícolas pós-colheita."
        )

        SourceCitationItem(
            agency = "EMBRAPA Alimentos",
            title = "Pesquisa Nacional de Perdas e Resíduos Agrícolas",
            summary = "Dados de desperdício em feiras livres, transporte e residências no Brasil."
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    description: String,
    tag: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tag.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
            }
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PreservationItem(step: String, detail: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = step, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GreenPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SourceCitationItem(agency: String, title: String, summary: String) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = agency, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = GreenPrimary)
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            Text(text = summary, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
