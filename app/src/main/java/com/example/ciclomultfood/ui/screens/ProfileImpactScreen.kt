package com.example.ciclomultfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import com.example.ciclomultfood.ui.components.AppPermissionsSection
import com.example.ciclomultfood.ui.components.UserMiniBadgesRow
import com.example.ciclomultfood.ui.components.UserProfileBadgesSection
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg

@Composable
fun ProfileImpactScreen(
    userPrefs: UserPreferencesEntity?,
    onOpenRewardedAd: () -> Unit,
    onOpenProSubscription: () -> Unit,
    onOpenEsgCertificate: () -> Unit,
    onSyncFirebase: (() -> Unit)? = null,
    onUpdateProfile: ((String, String, String, String) -> Boolean)? = null,
    isHandleAvailable: ((String, String) -> Boolean)? = null,
    onDeleteProfile: (() -> Unit)? = null,
    onToggleTheme: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val prefs = userPrefs ?: UserPreferencesEntity()
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showDeleteProfileDialog by remember { mutableStateOf(false) }

    val followedCount = prefs.followedUsers.split(",").filter { it.isNotBlank() }.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // User Profile Header Card - Organized layout with clean spacing
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("user_profile_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Top Row: Avatar + Name + Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GreenPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = GreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = prefs.userName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (prefs.isProSubscriber) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Selo Pro",
                                        tint = ProBadgeBg,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = prefs.userHandle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GreenPrimary
                            )
                        }
                    }

                    // Clean Top-Right Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .height(26.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .clickable { onToggleTheme?.invoke() }
                                .testTag("btn_toggle_theme_profile"),
                            color = if (prefs.isDarkTheme) Color(0xFF2C2C2C) else GreenPrimary.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (prefs.isDarkTheme) Color.Gray else GreenPrimary.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = if (prefs.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = "Tema",
                                    tint = if (prefs.isDarkTheme) Color(0xFFFFD54F) else GreenPrimary,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = if (prefs.isDarkTheme) "Escuro" else "Claro",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (prefs.isDarkTheme) Color.White else GreenPrimary
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_edit_profile_header")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Perfil",
                                tint = GreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = { showDeleteProfileDialog = true },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_delete_profile_header")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Deletar Perfil",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                // Details Row: Location, Contact & Following Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = prefs.userNeighborhood,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "WhatsApp: ${prefs.userWhatsApp}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        color = GreenPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = "Seguindo",
                                tint = GreenPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Seguindo: $followedCount",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        }
                    }
                }

                UserMiniBadgesRow(userPrefs = prefs)
            }
        }

        // User Reward Badges Section (Zero Waste Hero, Top Donor, etc.)
        UserProfileBadgesSection(
            userPrefs = prefs
        )

        // Action Credits Card - First box after profile
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AmberAccent.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AmberAccent))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Créditos",
                            tint = AmberAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "Saldo de Créditos de Ação",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (prefs.isProSubscriber) "Acesso Ilimitado (Selo Pro)" else "${prefs.actionCredits} Créditos disponíveis",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (!prefs.isProSubscriber) {
                        Button(
                            onClick = onOpenRewardedAd,
                            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_get_more_credits")
                        ) {
                            Text("+3 Créditos", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Text(
                    text = "Créditos de ação são usados para publicar novos anúncios ou liberar o contato no WhatsApp de vendedores locais. 100% gratuito ao assistir um vídeo AdMob!",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ESG Social Impact Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seu Impacto Social e Ambiental",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(onClick = onOpenEsgCertificate) {
                        Icon(
                            imageVector = Icons.Default.CardMembership,
                            contentDescription = "Certificado",
                            tint = GreenPrimary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricBox("Alimentos Salvos", "%.1f kg".format(prefs.totalKgSaved), Icons.Default.Eco)
                    MetricBox("CO2 Evitado", "%.1f kg".format(prefs.totalCo2EvadedKg), Icons.Default.Co2)
                    MetricBox("Doações", "${prefs.donationsCompleted}", Icons.Default.VolunteerActivism)
                }

                Button(
                    onClick = onOpenEsgCertificate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_open_esg_dialog"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.CardMembership, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gerar Certificado de Impacto ESG", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Pro Seller Subscription Banner
        if (!prefs.isProSubscriber) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ProBadgeBg.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ProBadgeBg))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Pro",
                            tint = ProBadgeBg,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Assinatura Profissional",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "Vende grandes volumes de produtos ou resíduos? Assine o Selo Premium por boleto/Pix (R$ 29,90/mês) para anúncios ilimitados e zero publicidade.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = onOpenProSubscription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_open_pro_subscription"),
                        colors = ButtonDefaults.buttonColors(containerColor = ProBadgeBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.QrCode, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ativar Selo Premium via Pix/Boleto", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Hardware & System Permissions Management (Camera, Location GPS, Notifications) - Moved to bottom
        AppPermissionsSection()
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            userPrefs = prefs,
            onDismiss = { showEditProfileDialog = false },
            isHandleAvailable = isHandleAvailable,
            onSave = { name, handle, neighborhood, whatsapp ->
                val success = onUpdateProfile?.invoke(name, handle, neighborhood, whatsapp) ?: true
                if (success) {
                    showEditProfileDialog = false
                }
            }
        )
    }

    if (showDeleteProfileDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteProfileDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text("Deletar Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Text(
                    "Tem certeza de que deseja deletar seus dados de perfil (nome, @handle e contatos)? Suas informações serão reiniciadas para as configurações iniciais.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProfile?.invoke()
                        showDeleteProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("btn_confirm_delete_profile")
                ) {
                    Text("Deletar Perfil", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteProfileDialog = false },
                    modifier = Modifier.testTag("btn_cancel_delete_profile")
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun EditProfileDialog(
    userPrefs: UserPreferencesEntity,
    onDismiss: () -> Unit,
    isHandleAvailable: ((String, String) -> Boolean)? = null,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(userPrefs.userName) }
    var handle by remember { mutableStateOf(userPrefs.userHandle) }
    var neighborhood by remember { mutableStateOf(userPrefs.userNeighborhood) }
    var whatsApp by remember { mutableStateOf(userPrefs.userWhatsApp) }

    val cleanHandle = if (handle.startsWith("@")) handle.trim() else "@${handle.trim()}"
    val isHandleUnique = isHandleAvailable?.invoke(handle, userPrefs.userHandle) ?: true

    val now = System.currentTimeMillis()
    val THREE_MONTHS_MS = 90L * 24 * 60 * 60 * 1000L
    val updatesCount = userPrefs.profileUpdatesCount
    val timeSinceLastUpdate = now - userPrefs.lastProfileUpdateTimestamp
    val isLimitReached = updatesCount >= 2
    val isCooldownActive = isLimitReached && (timeSinceLastUpdate < THREE_MONTHS_MS)
    val daysRemaining = if (isCooldownActive) {
        ((THREE_MONTHS_MS - timeSinceLastUpdate) / (1000L * 60 * 60 * 24)).coerceAtLeast(1)
    } else 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = GreenPrimary
                )
                Text("Atualizar Dados do Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = if (isCooldownActive) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                            else GreenPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCooldownActive) MaterialTheme.colorScheme.error else GreenPrimary
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isCooldownActive) Icons.Default.LockClock else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isCooldownActive) MaterialTheme.colorScheme.error else GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            if (isCooldownActive) {
                                Text(
                                    text = "Bloqueio Temporário (3 Meses)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "Você já atualizou seu perfil 2 vezes. A próxima edição será permitida apenas a cada 3 meses (disponível em $daysRemaining dias).",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else if (isLimitReached) {
                                Text(
                                    text = "Período de 3 Meses Concluído",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GreenPrimary
                                )
                                Text(
                                    text = "Você já utilizou suas 2 edições iniciais. O intervalo de 3 meses se passou e uma nova alteração está liberada agora.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else {
                                Text(
                                    text = "Regra de Atualização ($updatesCount/2 efetuadas)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GreenPrimary
                                )
                                Text(
                                    text = "Você pode atualizar seus dados e seu nome individual (@handle) até 2 vezes gratuitamente.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome de Exibição") },
                    singleLine = true,
                    enabled = !isCooldownActive,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_profile_name")
                )

                // Unique Username Field (@handle)
                Column {
                    OutlinedTextField(
                        value = handle,
                        onValueChange = { handle = it },
                        label = { Text("Nome de Usuário Único (@handle)") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = "Handle", tint = GreenPrimary)
                        },
                        trailingIcon = {
                            if (cleanHandle.length >= 3) {
                                Icon(
                                    imageVector = if (isHandleUnique) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (isHandleUnique) GreenPrimary else MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true,
                        enabled = !isCooldownActive,
                        isError = !isHandleUnique && cleanHandle.length >= 3,
                        modifier = Modifier.fillMaxWidth().testTag("input_edit_profile_handle")
                    )

                    if (cleanHandle.length >= 3) {
                        Text(
                            text = if (isHandleUnique) "🟢 Nome de usuário $cleanHandle disponível!"
                            else "🔴 Nome $cleanHandle já está em uso por outro integrante.",
                            fontSize = 11.sp,
                            color = if (isHandleUnique) GreenPrimary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = neighborhood,
                    onValueChange = { neighborhood = it },
                    label = { Text("Bairro / Região") },
                    singleLine = true,
                    enabled = !isCooldownActive,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_profile_neighborhood")
                )

                OutlinedTextField(
                    value = whatsApp,
                    onValueChange = { whatsApp = it },
                    label = { Text("WhatsApp de Contato") },
                    singleLine = true,
                    enabled = !isCooldownActive,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_profile_whatsapp")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && cleanHandle.length >= 3 && isHandleUnique && neighborhood.isNotBlank() && whatsApp.isNotBlank()) {
                        onSave(name.trim(), cleanHandle, neighborhood.trim(), whatsApp.trim())
                    }
                },
                enabled = !isCooldownActive && name.isNotBlank() && cleanHandle.length >= 3 && isHandleUnique && neighborhood.isNotBlank() && whatsApp.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier.testTag("btn_save_profile_dialog")
            ) {
                Text("Salvar Alterações")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_profile_dialog")
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun MetricBox(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = GreenPrimary)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
