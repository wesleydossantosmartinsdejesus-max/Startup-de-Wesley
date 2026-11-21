package com.example.ciclomultfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ciclomultfood.ui.components.AdMobNativeCard
import com.example.ciclomultfood.ui.components.InstagramStoryBar
import com.example.ciclomultfood.ui.components.ListingCard
import com.example.ciclomultfood.ui.components.ListingGridCard
import com.example.ciclomultfood.ui.components.LocalMarketCard
import com.example.ui.theme.GreenPrimary
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Storefront

@Composable
fun MarketplaceScreen(
    listings: List<ListingEntity>,
    localMarkets: List<LocalMarketEntity> = emptyList(),
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onContactWhatsAppClicked: (ListingEntity) -> Unit,
    onReportListing: (Int) -> Unit,
    isProUser: Boolean,
    onOpenResearchDialog: () -> Unit,
    onAddLocalMarketClicked: () -> Unit = {},
    onOpenSellerProfile: ((ListingEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isGridView by remember { mutableStateOf(true) }
    var selectedTabMode by remember { mutableStateOf(0) } // 0: Alimentos/Ofertas, 1: Feiras Locais (Firestore)

    val filteredListings = remember(listings, selectedCategory, searchQuery) {
        listings.filter { item ->
            val matchesCategory = when (selectedCategory) {
                "TODOS" -> true
                else -> item.category == selectedCategory
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true) ||
                item.sellerNeighborhood.contains(searchQuery, ignoreCase = true)
            }
            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Instagram Story Filter Bar (Compact)
        InstagramStoryBar(
            selectedCategoryId = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        // Search Bar & View Mode Toggle (Grid 2 col / Grid 3 col / List)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = {
                    Text(
                        "Buscar hortaliças, doações, bairro...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = GreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChanged("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpar busca",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("input_marketplace_search"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true
            )

            // View Mode Toggle Button
            Surface(
                color = GreenPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(48.dp)
            ) {
                IconButton(
                    onClick = { isGridView = !isGridView },
                    modifier = Modifier.testTag("btn_toggle_view_mode")
                ) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = if (isGridView) "Ver em Lista" else "Ver em Grade Ampliada",
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Local Fair Header Status Bar & Mode Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterChip(
                    selected = selectedTabMode == 0,
                    onClick = { selectedTabMode = 0 },
                    label = { Text("🛒 Ofertas (${filteredListings.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreenPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_tab_listings")
                )
                FilterChip(
                    selected = selectedTabMode == 1,
                    onClick = { selectedTabMode = 1 },
                    label = { Text("🎪 Feiras Locais (${localMarkets.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreenPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_tab_local_markets")
                )
            }

            if (selectedTabMode == 1) {
                SmallFloatingActionButton(
                    onClick = onAddLocalMarketClicked,
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("btn_add_market_dialog")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Nova Feira", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (selectedTabMode == 1) {
            // Render Local Markets list (Firestore collection local_markets)
            if (localMarkets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhuma Feira Local encontrada.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cadastre a feira agroecológica do seu bairro no Firestore!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(
                        items = localMarkets,
                        key = { _, market -> market.id }
                    ) { _, market ->
                        LocalMarketCard(market = market)
                    }
                }
            }
        } else if (filteredListings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nenhum anúncio encontrado.",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Seja o primeiro a publicar nesta categoria!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (isGridView) {
            // 2-Column Grid view (Visão Maximizada para a Feira Local)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 6.dp, end = 6.dp, top = 2.dp, bottom = 72.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(
                    items = filteredListings,
                    key = { _, item -> item.id }
                ) { index, listing ->
                    ListingGridCard(
                        listing = listing,
                        onContactWhatsApp = { onContactWhatsAppClicked(it) },
                        onReportListing = onReportListing,
                        onOpenSellerProfile = onOpenSellerProfile
                    )
                }
            }
        } else {
            // 1-Column List view
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                itemsIndexed(filteredListings) { index, listing ->
                    ListingCard(
                        listing = listing,
                        onContactWhatsApp = { item ->
                            onContactWhatsAppClicked(item)
                        },
                        onReportListing = onReportListing,
                        onOpenSellerProfile = onOpenSellerProfile
                    )

                    // Interspersed Passive AdMob Native Banner every 4 items for non-pro users
                    if (!isProUser && (index + 1) % 4 == 0) {
                        AdMobNativeCard()
                    }
                }
            }
        }
    }
}
