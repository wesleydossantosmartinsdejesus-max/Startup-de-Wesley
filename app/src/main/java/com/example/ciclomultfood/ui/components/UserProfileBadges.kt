package com.example.ciclomultfood.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val levelName: String, // Nível Ouro, Diamante, Platina
    val icon: ImageVector,
    val badgeColor: Color,
    val isUnlocked: Boolean,
    val currentProgress: Double,
    val targetProgress: Double,
    val progressUnit: String,
    val rewardPerk: String
)

@Composable
fun UserProfileBadgesSection(
    userPrefs: UserPreferencesEntity,
    modifier: Modifier = Modifier
) {
    var selectedBadgeForDialog by remember { mutableStateOf<BadgeItem?>(null) }

    // Calculate badges based on real user activity metrics
    val badges = remember(userPrefs) {
        listOf(
            BadgeItem(
                id = "zero_waste_hero",
                title = "Zero Waste Hero",
                description = "Salva alimentos próprios para consumo do desperdício e promove a economia circular no bairro.",
                levelName = "Nível Ouro 🏆",
                icon = Icons.Default.WorkspacePremium,
                badgeColor = AmberAccent,
                isUnlocked = userPrefs.totalKgSaved >= 10.0,
                currentProgress = userPrefs.totalKgSaved,
                targetProgress = 10.0,
                progressUnit = "kg salvos",
                rewardPerk = "+3 Créditos de Ação de bônus todo mês e destaque de perfil no feed!"
            ),
            BadgeItem(
                id = "top_donor",
                title = "Top Donor",
                description = "Líder de doações diretas de cestas, hortaliças e comida na Corrente do Bem da periferia.",
                levelName = "Nível Diamante 💎",
                icon = Icons.Default.VolunteerActivism,
                badgeColor = Color(0xFF00B0FF),
                isUnlocked = userPrefs.donationsCompleted >= 3,
                currentProgress = userPrefs.donationsCompleted.toDouble(),
                targetProgress = 3.0,
                progressUnit = "doações",
                rewardPerk = "Selo Verificado de Doador Comunitário e prioridade nas buscas por WhatsApp."
            ),
            BadgeItem(
                id = "cultivador_organico",
                title = "Cultivador Urbano",
                description = "Fomenta a produção agrícola sustentável e a preservação do meio ambiente.",
                levelName = "Nível Prata 🌿",
                icon = Icons.Default.Park,
                badgeColor = GreenPrimary,
                isUnlocked = userPrefs.totalCo2EvadedKg >= 15.0,
                currentProgress = userPrefs.totalCo2EvadedKg,
                targetProgress = 15.0,
                progressUnit = "kg CO2 evitado",
                rewardPerk = "Acesso exclusivo a guias da Embrapa e trocas de sementes crioulas."
            ),
            BadgeItem(
                id = "guardiao_esg",
                title = "Guardião ESG",
                description = "Membro engajado da Rede Ciclomult Food com certificado de responsabilidade social.",
                levelName = if (userPrefs.isProSubscriber) "Nível Pro Elite 🌟" else "Nível Iniciante 🛡️",
                icon = Icons.Default.Shield,
                badgeColor = ProBadgeBg,
                isUnlocked = userPrefs.isProSubscriber || userPrefs.actionCredits >= 5,
                currentProgress = if (userPrefs.isProSubscriber) 1.0 else (userPrefs.actionCredits / 5.0).coerceAtMost(1.0),
                targetProgress = 1.0,
                progressUnit = if (userPrefs.isProSubscriber) "Assinante Pro" else "Créditos de engajamento",
                rewardPerk = "Isenção total de taxas de anúncio e relatório de metas ESG mensal."
            )
        )
    }

    val unlockedCount = badges.count { it.isUnlocked }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_profile_badges_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
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
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Badges & Conquistas",
                        tint = AmberAccent,
                        modifier = Modifier.size(26.dp)
                    )
                    Column {
                        Text(
                            text = "Badges & Selos de Conquista",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$unlockedCount de ${badges.size} Selos Desbloqueados pelo Engajamento",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = GreenPrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Sistema de Pontos",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Grid of Badges
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BadgeCard(
                        badge = badges[0],
                        onClick = { selectedBadgeForDialog = badges[0] },
                        modifier = Modifier.weight(1f)
                    )
                    BadgeCard(
                        badge = badges[1],
                        onClick = { selectedBadgeForDialog = badges[1] },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BadgeCard(
                        badge = badges[2],
                        onClick = { selectedBadgeForDialog = badges[2] },
                        modifier = Modifier.weight(1f)
                    )
                    BadgeCard(
                        badge = badges[3],
                        onClick = { selectedBadgeForDialog = badges[3] },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    // Badge Details Modal Dialog
    if (selectedBadgeForDialog != null) {
        BadgeDetailDialog(
            badge = selectedBadgeForDialog!!,
            onDismiss = { selectedBadgeForDialog = null }
        )
    }
}

@Composable
private fun BadgeCard(
    badge: BadgeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alphaVal = if (badge.isUnlocked) 1f else 0.55f

    Surface(
        onClick = onClick,
        modifier = modifier
            .testTag("badge_card_${badge.id}"),
        shape = RoundedCornerShape(16.dp),
        color = if (badge.isUnlocked) badge.badgeColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = androidx.compose.foundation.BorderStroke(
            width = if (badge.isUnlocked) 1.5.dp else 1.dp,
            color = if (badge.isUnlocked) badge.badgeColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.isUnlocked) {
                            Brush.linearGradient(listOf(badge.badgeColor.copy(alpha = 0.25f), badge.badgeColor.copy(alpha = 0.1f)))
                        } else {
                            Brush.linearGradient(listOf(Color.Gray.copy(alpha = 0.2f), Color.Gray.copy(alpha = 0.1f)))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badge.icon,
                    contentDescription = badge.title,
                    tint = if (badge.isUnlocked) badge.badgeColor else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                if (!badge.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Text(
                text = badge.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            Surface(
                color = if (badge.isUnlocked) badge.badgeColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if (badge.isUnlocked) badge.levelName else "🔒 Bloqueado",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (badge.isUnlocked) badge.badgeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Progress Bar
            val progressRatio = (badge.currentProgress / badge.targetProgress).coerceIn(0.0, 1.0).toFloat()
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progressRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (badge.isUnlocked) badge.badgeColor else Color.Gray,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (badge.isUnlocked) "Concluído" else "%.1f / %.1f %s".format(badge.currentProgress, badge.targetProgress, badge.progressUnit),
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BadgeDetailDialog(
    badge: BadgeItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("dialog_badge_details"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(badge.badgeColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = badge.icon,
                        contentDescription = badge.title,
                        tint = badge.badgeColor,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = badge.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = badge.levelName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = badge.badgeColor
                    )
                }

                Surface(
                    color = if (badge.isUnlocked) GreenPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (badge.isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (badge.isUnlocked) GreenPrimary else MaterialTheme.colorScheme.error
                        )
                        Column {
                            Text(
                                text = if (badge.isUnlocked) "STATUS: DESBLOQUEADO!" else "STATUS: EM PROGRESSO",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (badge.isUnlocked) GreenPrimary else MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = if (badge.isUnlocked) "Você é um exemplo ativo de sustentabilidade na comunidade." else "Continue doando e reciclando alimentos para conquistar este selo.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Descrição da Conquista:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(text = badge.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Benefícios & Recompensas:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GreenPrimary)
                    Text(text = badge.rewardPerk, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_close_badge_dialog"),
                    colors = ButtonDefaults.buttonColors(containerColor = badge.badgeColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Fechar & Continuar Engajado", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun UserMiniBadgesRow(
    userPrefs: UserPreferencesEntity,
    modifier: Modifier = Modifier
) {
    val isZeroWasteHero = userPrefs.totalKgSaved >= 10.0
    val isTopDonor = userPrefs.donationsCompleted >= 3

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isZeroWasteHero) {
            Surface(
                color = AmberAccent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Zero Waste Hero",
                        tint = AmberAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Zero Waste Hero",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                }
            }
        }

        if (isTopDonor) {
            Surface(
                color = Color(0xFF00B0FF).copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00B0FF).copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolunteerActivism,
                        contentDescription = "Top Donor",
                        tint = Color(0xFF00B0FF),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Top Donor",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00B0FF)
                    )
                }
            }
        }
    }
}
