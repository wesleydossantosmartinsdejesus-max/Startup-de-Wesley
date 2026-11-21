package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.ui.components.*
import com.example.ciclomultfood.ui.screens.CommunityFeedScreen
import com.example.ciclomultfood.ui.screens.InteractionsScreen
import com.example.ciclomultfood.ui.screens.MarketplaceScreen
import com.example.ciclomultfood.ui.screens.ProfileImpactScreen
import com.example.ciclomultfood.ui.viewmodel.MainViewModel
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userPrefs by viewModel.userPreferences.collectAsStateWithLifecycle()
            val isDarkTheme = userPrefs?.isDarkTheme == true

            MyApplicationTheme(darkTheme = isDarkTheme) {
                val context = LocalContext.current

                val listings by viewModel.listings.collectAsStateWithLifecycle()
                val communityPosts by viewModel.communityPosts.collectAsStateWithLifecycle()
                val localMarkets by viewModel.localMarkets.collectAsStateWithLifecycle()

                val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

                val showTermsModal by viewModel.showTermsModal.collectAsStateWithLifecycle()
                val showRewardedAdDialog by viewModel.showRewardedAdDialog.collectAsStateWithLifecycle()
                val showProSubscriptionDialog by viewModel.showProSubscriptionDialog.collectAsStateWithLifecycle()
                val showEsgCertificateDialog by viewModel.showEsgCertificateDialog.collectAsStateWithLifecycle()
                val showAddListingDialog by viewModel.showAddListingDialog.collectAsStateWithLifecycle()

                var showFoodWasteResearchDialog by remember { mutableStateOf(false) }
                var showAddLocalMarketDialog by remember { mutableStateOf(false) }
                var selectedSellerProfileData by remember { mutableStateOf<SellerProfileData?>(null) }

                val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                var currentTab by remember { mutableIntStateOf(0) } // 0: Feira/Marketplace, 1: Social, 2: Interações, 3: Perfil
                var fabOffsetX by remember { mutableFloatStateOf(0f) }
                var fabOffsetY by remember { mutableFloatStateOf(0f) }

                val isTermsAccepted = userPrefs?.acceptedTerms == true
                val isProUser = userPrefs?.isProSubscriber == true
                val creditsCount = userPrefs?.actionCredits ?: 5

                // Trigger snackbar on message
                LaunchedEffect(snackbarMessage) {
                    snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    topBar = {
                        InstagramHeader(
                            creditsCount = creditsCount,
                            isPro = isProUser,
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = { viewModel.toggleTheme() },
                            onCreditsClicked = { viewModel.showRewardedAdDialog.value = true },
                            onEsgClicked = { viewModel.showEsgCertificateDialog.value = true }
                        )
                    },
                    bottomBar = {
                        val unselectedColor = if (isDarkTheme) Color.White else Color.Black
                        val navItemColors = NavigationBarItemDefaults.colors(
                            indicatorColor = GreenPrimary.copy(alpha = 0.25f),
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            unselectedIconColor = unselectedColor.copy(alpha = 0.7f),
                            unselectedTextColor = unselectedColor.copy(alpha = 0.7f)
                        )

                        NavigationBar(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .testTag("bottom_navigation_bar"),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavigationBarItem(
                                selected = currentTab == 0,
                                onClick = { currentTab = 0 },
                                icon = { Icon(Icons.Default.Storefront, contentDescription = "Feira") },
                                label = { Text("Feira Local", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("nav_item_marketplace")
                            )
                            NavigationBarItem(
                                selected = currentTab == 1,
                                onClick = { currentTab = 1 },
                                icon = { Icon(Icons.Default.Diversity3, contentDescription = "Social") },
                                label = { Text("Feed Social", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("nav_item_social")
                            )
                            NavigationBarItem(
                                selected = currentTab == 2,
                                onClick = { currentTab = 2 },
                                icon = { Icon(Icons.Default.People, contentDescription = "Interações") },
                                label = { Text("Interações", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("nav_item_interactions")
                            )
                            NavigationBarItem(
                                selected = currentTab == 3,
                                onClick = { currentTab = 3 },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                                label = { Text("Perfil & ESG", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("nav_item_profile")
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { viewModel.showAddListingDialog.value = true },
                            containerColor = GreenPrimary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .offset { IntOffset(fabOffsetX.roundToInt(), fabOffsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        fabOffsetX += dragAmount.x
                                        fabOffsetY += dragAmount.y
                                    }
                                }
                                .testTag("fab_add_listing")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Publicar")
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            0 -> MarketplaceScreen(
                                listings = listings,
                                localMarkets = localMarkets,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { viewModel.selectedCategory.value = it },
                                searchQuery = searchQuery,
                                onSearchQueryChanged = { viewModel.searchQuery.value = it },
                                onContactWhatsAppClicked = { item ->
                                    viewModel.onContactWhatsAppClicked(
                                        phone = item.sellerWhatsApp,
                                        title = item.title,
                                        onSuccess = {
                                            openWhatsAppDirect(context, item.sellerWhatsApp, item.title)
                                        }
                                    )
                                },
                                onReportListing = { viewModel.reportListing(it) },
                                isProUser = isProUser,
                                onOpenResearchDialog = { showFoodWasteResearchDialog = true },
                                onAddLocalMarketClicked = { showAddLocalMarketDialog = true },
                                onOpenSellerProfile = { listing ->
                                    selectedSellerProfileData = SellerProfileData(
                                        name = listing.sellerName,
                                        handle = "@${listing.sellerName.lowercase().replace(" ", "").replace("á","a").replace("é","e").replace("ã","a")}",
                                        neighborhood = listing.sellerNeighborhood,
                                        whatsApp = listing.sellerWhatsApp,
                                        isPro = listing.sellerIsPro,
                                        totalKgSaved = if (listing.category == "DOACAO") 25.0 else 18.5,
                                        totalCo2Kg = if (listing.category == "DOACAO") 50.0 else 37.0,
                                        esgLevel = if (listing.sellerIsPro) "Nível Ouro 🥇" else "Nível Prata 🥈",
                                        bio = "Produtor local ativo na rede sustentável Ciclomult_Food."
                                    )
                                }
                            )

                            1 -> CommunityFeedScreen(
                                posts = communityPosts,
                                onLikePost = { viewModel.toggleLikePost(it) },
                                onReportPost = { viewModel.reportPost(it) },
                                isProUser = isProUser,
                                onOpenResearchDialog = { showFoodWasteResearchDialog = true },
                                followedUsers = userPrefs?.followedUsers ?: "",
                                onToggleFollow = { viewModel.toggleFollowUser(it) },
                                onOpenAuthorProfile = { post ->
                                    selectedSellerProfileData = SellerProfileData(
                                        name = post.authorName,
                                        handle = "@${post.authorName.lowercase().replace(" ", "").replace("á","a").replace("é","e").replace("ã","a")}",
                                        neighborhood = post.authorNeighborhood,
                                        whatsApp = "",
                                        isPro = post.authorIsPro,
                                        totalKgSaved = 22.0,
                                        totalCo2Kg = 44.0,
                                        esgLevel = if (post.authorIsPro) "Nível Ouro 🥇" else "Nível Prata 🥈",
                                        bio = "Membro engajado do Feed Social Ciclomult_Food."
                                    )
                                }
                            )

                            2 -> InteractionsScreen(
                                followedUsers = userPrefs?.followedUsers ?: "",
                                onToggleFollow = { viewModel.toggleFollowUser(it) },
                                onOpenProfile = { profile -> selectedSellerProfileData = profile },
                                onContactWhatsApp = { phone, title ->
                                    openWhatsAppDirect(context, phone, title)
                                }
                            )

                            3 -> ProfileImpactScreen(
                                userPrefs = userPrefs,
                                onOpenRewardedAd = { viewModel.showRewardedAdDialog.value = true },
                                onOpenProSubscription = { viewModel.showProSubscriptionDialog.value = true },
                                onOpenEsgCertificate = { viewModel.showEsgCertificateDialog.value = true },
                                onSyncFirebase = { viewModel.syncWithFirebaseCloud() },
                                onUpdateProfile = { name, handle, neighborhood, whatsapp ->
                                    viewModel.updateUserProfile(name, handle, neighborhood, whatsapp)
                                },
                                isHandleAvailable = { handle, current ->
                                    viewModel.isHandleAvailable(handle, current)
                                },
                                onDeleteProfile = {
                                    viewModel.deleteUserProfile()
                                },
                                onToggleTheme = {
                                    viewModel.toggleTheme()
                                }
                            )
                        }
                    }
                }

                // Mandatory Terms Acceptance Gate on initial launch
                if (!isTermsAccepted) {
                    TermsDialog(
                        onAcceptTerms = { viewModel.acceptTerms() }
                    )
                }

                // Rewarded Ad Dialog
                if (showRewardedAdDialog) {
                    RewardedAdDialog(
                        onDismiss = { viewModel.showRewardedAdDialog.value = false },
                        onWatchCompleted = { viewModel.watchRewardedAd() }
                    )
                }

                // Pro Subscription (Pix/Boleto) Dialog
                if (showProSubscriptionDialog) {
                    ProSubscriptionDialog(
                        onDismiss = { viewModel.showProSubscriptionDialog.value = false },
                        onActivatePro = { viewModel.activateProSubscription() }
                    )
                }

                // ESG Impact Certificate Dialog
                if (showEsgCertificateDialog) {
                    EsgCertificateDialog(
                        userPrefs = userPrefs,
                        onDismiss = { viewModel.showEsgCertificateDialog.value = false }
                    )
                }

                // Add Listing or Post Dialog
                if (showAddListingDialog) {
                    AddListingDialog(
                        onDismiss = { viewModel.showAddListingDialog.value = false },
                        onSubmit = { title, category, subCategory, description, quantity, price, sellerWhatsApp, isSocialPost, mediaUri, isVideo, mapAddress ->
                            viewModel.createListing(
                                title = title,
                                category = category,
                                subCategory = subCategory,
                                description = description,
                                quantity = quantity,
                                price = price,
                                sellerWhatsApp = sellerWhatsApp,
                                isSocialPost = isSocialPost,
                                mediaUri = mediaUri,
                                isVideo = isVideo,
                                mapAddress = mapAddress
                            )
                        }
                    )
                }

                // Food Waste Research & Impact Dialog (ONU, FAO, EMBRAPA)
                if (showFoodWasteResearchDialog) {
                    FoodWasteResearchDialog(
                        onDismiss = { showFoodWasteResearchDialog = false },
                        onForceRefreshData = { viewModel.forceRefreshGoogleData() }
                    )
                }

                // Add Local Market Dialog (Firestore collection: local_markets)
                if (showAddLocalMarketDialog) {
                    AddLocalMarketDialog(
                        onDismiss = { showAddLocalMarketDialog = false },
                        onSubmitMarket = { name, location, availableItems, operatingHours, organizerContact ->
                            viewModel.addLocalMarket(
                                name = name,
                                location = location,
                                availableItems = availableItems,
                                operatingHours = operatingHours,
                                organizerContact = organizerContact
                            )
                        }
                    )
                }

                // Advertiser / Community Author Profile & ESG Dialog
                selectedSellerProfileData?.let { profile ->
                    val followedList = (userPrefs?.followedUsers ?: "").split(",").map { it.trim() }
                    val isFollowing = followedList.contains(profile.name) || followedList.contains(profile.handle)
                    SellerProfileDialog(
                        profile = profile,
                        isFollowing = isFollowing,
                        onToggleFollow = { viewModel.toggleFollowUser(profile.name) },
                        onContactWhatsApp = {
                            openWhatsAppDirect(context, profile.whatsApp, "Olá ${profile.name}, vi seu perfil no Ciclomult_Food!")
                        },
                        onDismiss = { selectedSellerProfileData = null },
                        userListings = listings.filter { it.sellerName == profile.name },
                        userPosts = communityPosts.filter { it.authorName == profile.name }
                    )
                }
            }
        }
    }
}
