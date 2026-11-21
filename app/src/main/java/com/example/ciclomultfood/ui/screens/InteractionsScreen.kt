package com.example.ciclomultfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclomultfood.ui.components.SellerProfileData
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CardBorder
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg
import com.example.ui.theme.WhatsAppGreen

data class CommunityMember(
    val name: String,
    val handle: String,
    val neighborhood: String,
    val isPro: Boolean,
    val whatsApp: String,
    val totalKgSaved: Double,
    val totalCo2Kg: Double,
    val esgBadge: String,
    val bio: String,
    val isFollower: Boolean = true // follows current user
)

val sampleCommunityMembers = listOf(
    CommunityMember(
        name = "Seu Zé Hortaliças",
        handle = "@seuze_horta",
        neighborhood = "Capão Redondo, SP",
        isPro = true,
        whatsApp = "5511988887777",
        totalKgSaved = 28.5,
        totalCo2Kg = 57.0,
        esgBadge = "Ouro 🥇",
        bio = "Agricultor urbano e produtor de orgânicos sem veneno na Zona Sul.",
        isFollower = true
    ),
    CommunityMember(
        name = "Ana Paula Cultivos",
        handle = "@anapaula_cultivo",
        neighborhood = "Heliópolis, SP",
        isPro = false,
        whatsApp = "5511977776666",
        totalKgSaved = 14.2,
        totalCo2Kg = 28.4,
        esgBadge = "Prata 🥈",
        bio = "Cultivadora comunitária em Heliópolis. Apaixonada por vegetais e ervas medicinais.",
        isFollower = true
    ),
    CommunityMember(
        name = "Sacolão do Bairro",
        handle = "@sacolao_bairro",
        neighborhood = "Jardim Ângela, SP",
        isPro = true,
        whatsApp = "5511966665555",
        totalKgSaved = 45.0,
        totalCo2Kg = 90.0,
        esgBadge = "Diamante 💎",
        bio = "Ponto de doação de frutas e hortaliças frescas para ONGs e cozinhas solidárias.",
        isFollower = true
    ),
    CommunityMember(
        name = "Padaria Comunitária Esperança",
        handle = "@padaria_esperanca",
        neighborhood = "Capão Redondo, SP",
        isPro = true,
        whatsApp = "5511955554444",
        totalKgSaved = 32.0,
        totalCo2Kg = 64.0,
        esgBadge = "Ouro 🥇",
        bio = "Doação diária de pães e broas excedentes do dia para famílias cadastradas.",
        isFollower = false
    ),
    CommunityMember(
        name = "Hortifruti São José",
        handle = "@hortifruti_saojose",
        neighborhood = "Grajaú, SP",
        isPro = false,
        whatsApp = "5511944443333",
        totalKgSaved = 19.8,
        totalCo2Kg = 39.6,
        esgBadge = "Prata 🥈",
        bio = "Matéria orgânica e cascas para compostagem local no Grajaú.",
        isFollower = true
    ),
    CommunityMember(
        name = "Sítio Recanto Verde",
        handle = "@recanto_verde",
        neighborhood = "Parelheiros, SP",
        isPro = true,
        whatsApp = "5511933332222",
        totalKgSaved = 52.0,
        totalCo2Kg = 104.0,
        esgBadge = "Diamante 💎",
        bio = "Adubação e esterco natural curtido diretamente do sítio em Parelheiros.",
        isFollower = false
    ),
    CommunityMember(
        name = "João do Cultivo Urbano",
        handle = "@joao_cultivourbano",
        neighborhood = "Brasilândia, SP",
        isPro = true,
        whatsApp = "5511922221111",
        totalKgSaved = 22.0,
        totalCo2Kg = 44.0,
        esgBadge = "Ouro 🥇",
        bio = "Especialista em hortas em caixotes de feira e pequenas áreas urbanas.",
        isFollower = true
    ),
    CommunityMember(
        name = "Cozinha Comunitária Solidária",
        handle = "@cozinha_solidaria",
        neighborhood = "Paraisópolis, SP",
        isPro = true,
        whatsApp = "5511911110000",
        totalKgSaved = 60.0,
        totalCo2Kg = 120.0,
        esgBadge = "Diamante 💎",
        bio = "Marmitas e lanches gratuitos distribuídos diariamente em Paraisópolis.",
        isFollower = true
    )
)

@Composable
fun InteractionsScreen(
    followedUsers: String,
    onToggleFollow: (String) -> Unit,
    onOpenProfile: (SellerProfileData) -> Unit,
    onContactWhatsApp: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Seguindo, 1 = Seguidores
    var searchQuery by remember { mutableStateOf("") }

    val followedList = remember(followedUsers) {
        followedUsers.split(",").map { it.trim() }.filter { it.isNotBlank() }
    }

    // Filter members for "Seguindo" tab vs "Seguidores" tab
    val currentTabMembers = remember(selectedTabIndex, followedList, searchQuery) {
        val baseList = if (selectedTabIndex == 0) {
            // "Seguindo" tab: users in followedList + default followed ones
            sampleCommunityMembers.filter { member ->
                followedList.contains(member.name) || followedList.contains(member.handle) || (followedList.isEmpty() && member.name.contains("Seu Zé"))
            }
        } else {
            // "Seguidores" tab: members who follow the user
            sampleCommunityMembers.filter { it.isFollower }
        }

        if (searchQuery.isBlank()) baseList
        else baseList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.neighborhood.contains(searchQuery, ignoreCase = true) ||
                    it.handle.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Title Bar Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Diversity3,
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Interações Comunitárias",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Acompanhe quem você segue e os integrantes que seguem sua horta & doações.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar produtor, doador ou bairro...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = GreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpar"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("search_interactions"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Switcher (Seguindo vs Seguidores)
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = GreenPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = GreenPrimary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                text = "Seguindo (${if (followedList.isEmpty()) 1 else followedList.size})",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == 0) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.testTag("tab_following")
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                text = "Seguidores (${sampleCommunityMembers.count { it.isFollower }})",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == 1) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.testTag("tab_followers")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Members List
        if (currentTabMembers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.GroupOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (selectedTabIndex == 0) "Você ainda não segue ninguém." else "Nenhum seguidor encontrado com esse termo.",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    if (selectedTabIndex == 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toque em 'Seguir' nos anúncios ou no Feed Social para se conectar!",
                            fontSize = 12.sp,
                            color = GreenPrimary
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(currentTabMembers) { member ->
                    val isFollowing = followedList.contains(member.name) || followedList.contains(member.handle) || (followedList.isEmpty() && member.name.contains("Seu Zé"))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("member_card_${member.handle}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorder))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Avatar
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(GreenPrimary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = member.name.take(1).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = GreenPrimary
                                        )
                                    }

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = member.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (member.isPro) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(
                                                    imageVector = Icons.Default.Verified,
                                                    contentDescription = "Pro",
                                                    tint = ProBadgeBg,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "${member.handle} • ${member.neighborhood}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                // ESG Badge
                                Surface(
                                    color = AmberAccent.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = member.esgBadge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberAccent,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Bio preview
                            Text(
                                text = member.bio,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onOpenProfile(
                                            SellerProfileData(
                                                name = member.name,
                                                handle = member.handle,
                                                neighborhood = member.neighborhood,
                                                whatsApp = member.whatsApp,
                                                isPro = member.isPro,
                                                totalKgSaved = member.totalKgSaved,
                                                totalCo2Kg = member.totalCo2Kg,
                                                esgLevel = member.esgBadge,
                                                bio = member.bio
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("btn_view_esg_profile_${member.handle}"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GreenPrimary.copy(alpha = 0.12f),
                                        contentColor = GreenPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Eco,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Ver Perfil & ESG",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onToggleFollow(member.name) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("btn_toggle_follow_${member.handle}"),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (isFollowing) MaterialTheme.colorScheme.onSurfaceVariant else GreenPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isFollowing) "Seguindo" else "Seguir",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }

                                if (member.whatsApp.isNotBlank()) {
                                    IconButton(
                                        onClick = { onContactWhatsApp(member.whatsApp, "Olá ${member.name}, vi seu perfil no Ciclomult_Food!") },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(WhatsAppGreen)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Chat,
                                            contentDescription = "WhatsApp",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
