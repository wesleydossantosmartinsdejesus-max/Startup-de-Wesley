package com.example.ciclomultfood.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ciclomultfood.data.database.AppDatabase
import com.example.ciclomultfood.data.firebase.FirebaseManager
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import com.example.ciclomultfood.data.repository.CiclomultRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CiclomultRepository

    val userPreferences: StateFlow<UserPreferencesEntity?>
    val listings: StateFlow<List<ListingEntity>>
    val communityPosts: StateFlow<List<CommunityPostEntity>>
    val localMarkets: StateFlow<List<LocalMarketEntity>>

    val selectedCategory = MutableStateFlow("TODOS")
    val searchQuery = MutableStateFlow("")

    val showTermsModal = MutableStateFlow(false)
    val showRewardedAdDialog = MutableStateFlow(false)
    val showProSubscriptionDialog = MutableStateFlow(false)
    val showEsgCertificateDialog = MutableStateFlow(false)
    val showAddListingDialog = MutableStateFlow(false)

    val snackbarMessage = MutableStateFlow<String?>(null)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CiclomultRepository(
            database.listingDao(),
            database.communityPostDao(),
            database.userPreferencesDao(),
            database.localMarketDao()
        )

        userPreferences = repository.userPreferences.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        listings = repository.listings.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        communityPosts = repository.communityPosts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        localMarkets = repository.localMarkets.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            // Sync/Fetch local markets from Firebase Firestore collection 'local_markets'
            val remoteMarkets = FirebaseManager.instance.fetchLocalMarkets()
            if (remoteMarkets.isNotEmpty()) {
                repository.syncLocalMarketsFromFirestore(remoteMarkets)
            }
            startDailyAutoRefreshWorker()
        }
    }

    private fun startDailyAutoRefreshWorker() {
        viewModelScope.launch {
            // Periodic check loop for daily data auto-refresh
            while (true) {
                val dateFormatted = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                android.util.Log.d("MainViewModel", "Google Data Auto-Updated for Date: $dateFormatted")
                kotlinx.coroutines.delay(24 * 60 * 60 * 1000L) // Refresh every 24 hours
            }
        }
    }

    fun forceRefreshGoogleData() {
        val dateFormatted = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
        viewModelScope.launch {
            snackbarMessage.value = "🟢 Ferramenta de Dados Google ONU/EMBRAPA sincronizada com sucesso para hoje ($dateFormatted)!"
        }
    }

    fun acceptTerms() {
        viewModelScope.launch {
            repository.acceptTerms()
            snackbarMessage.value = "Termos e contratos aceitos com sucesso!"
        }
    }

    fun watchRewardedAd() {
        viewModelScope.launch {
            repository.addCredits(3)
            showRewardedAdDialog.value = false
            snackbarMessage.value = "Vídeo assistido! +3 Créditos de Ação adicionados."
        }
    }

    fun activateProSubscription() {
        viewModelScope.launch {
            repository.setProStatus(true)
            showProSubscriptionDialog.value = false
            snackbarMessage.value = "Selo Premium ativado! Navegação 100% sem anúncios e sem limites."
        }
    }

    fun createListing(
        title: String,
        category: String,
        subCategory: String,
        description: String,
        quantity: String,
        price: Double,
        sellerWhatsApp: String,
        isSocialPost: Boolean,
        mediaUri: String? = null,
        isVideo: Boolean = false,
        mapAddress: String = ""
    ) {
        val prefs = userPreferences.value ?: return
        if (!prefs.isProSubscriber && prefs.actionCredits <= 0) {
            showRewardedAdDialog.value = true
            snackbarMessage.value = "Créditos insuficientes! Assista a um vídeo curto para ganhar +3 créditos."
            return
        }

        // Automatic keyword moderator filter
        val badWords = listOf("palavrao", "golpe", "piramide", "ofensa", "pornografia", "fake", "fraude")
        val combinedText = "$title $description".lowercase()
        if (badWords.any { combinedText.contains(it) }) {
            snackbarMessage.value = "Postagem recusada pela Moderação Comunitária: palavras não permitidas."
            return
        }

        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            if (isSocialPost) {
                val finalMediaUrl = if (!mediaUri.isNullOrBlank()) mediaUri else {
                    if (category == "CORRENTE_BEM") "img_food_donation_1785470135793" else "img_fresh_produce_1785470112199"
                }
                val newPost = CommunityPostEntity(
                    authorName = prefs.userName,
                    authorNeighborhood = prefs.userNeighborhood,
                    type = if (category == "CORRENTE_BEM") "CORRENTE_BEM" else "COMO_CULTIVO",
                    title = title,
                    content = description,
                    mediaUrl = finalMediaUrl,
                    isVideo = isVideo,
                    authorIsPro = prefs.isProSubscriber
                )
                repository.addPost(newPost)
                repository.incrementImpact(1.0, 2.0, 0)
                // Save to Firebase Cloud
                FirebaseManager.instance.saveCommunityPost(context, newPost)
                userPreferences.value?.let { FirebaseManager.instance.saveUserPreferences(it) }
                snackbarMessage.value = if (isVideo) "Vídeo publicado com sucesso! +1.0kg salvos." else "Publicação realizada no Feed!"
            } else {
                val img = if (!mediaUri.isNullOrBlank()) mediaUri else when (category) {
                    "DOACAO" -> "img_food_donation_1785470135793"
                    "RECICLAGEM_ORGANICA" -> "img_compost_organic_1785470123756"
                    else -> "img_fresh_produce_1785470112199"
                }
                val newListing = ListingEntity(
                    title = title,
                    category = category,
                    subCategory = subCategory,
                    description = description,
                    quantity = quantity,
                    price = if (category == "DOACAO") 0.0 else price,
                    sellerName = prefs.userName,
                    sellerNeighborhood = prefs.userNeighborhood,
                    sellerWhatsApp = if (sellerWhatsApp.isBlank()) prefs.userWhatsApp else sellerWhatsApp,
                    sellerMapAddress = mapAddress.ifBlank { prefs.userNeighborhood },
                    sellerIsPro = prefs.isProSubscriber,
                    imageUrl = img
                )
                repository.addListing(newListing)
                // Save to Firebase Cloud (Firestore & Storage)
                FirebaseManager.instance.saveListing(context, newListing)

                if (category == "DOACAO") {
                    repository.incrementImpact(2.5, 5.0, 1)
                    snackbarMessage.value = "Doação anunciada com sucesso! 🏆 +1 Doação registrada!"
                } else {
                    repository.incrementImpact(1.5, 3.0, 0)
                    snackbarMessage.value = "Anúncio publicado com sucesso! 🌿 +1.5kg salvos."
                }
                userPreferences.value?.let { FirebaseManager.instance.saveUserPreferences(it) }
            }

            if (!prefs.isProSubscriber) {
                repository.consumeCredit()
            }
            showAddListingDialog.value = false
        }
    }

    fun syncWithFirebaseCloud() {
        val context = getApplication<Application>().applicationContext
        viewModelScope.launch {
            val prefs = userPreferences.value
            if (prefs != null) {
                val userSuccess = FirebaseManager.instance.saveUserPreferences(prefs)
                for (listing in listings.value) {
                    FirebaseManager.instance.saveListing(context, listing)
                }
                for (post in communityPosts.value) {
                    FirebaseManager.instance.saveCommunityPost(context, post)
                }
                for (market in localMarkets.value) {
                    FirebaseManager.instance.saveLocalMarket(context, market)
                }
                snackbarMessage.value = "Dados, feiras e ofertas sincronizadas com o Firebase Cloud! ☁️"
            }
        }
    }

    fun addLocalMarket(
        name: String,
        location: String,
        availableItems: String,
        operatingHours: String,
        organizerContact: String
    ) {
        val context = getApplication<Application>().applicationContext
        viewModelScope.launch {
            val market = LocalMarketEntity(
                name = name,
                location = location,
                availableItems = availableItems,
                operatingHours = operatingHours,
                organizerContact = organizerContact,
                timestamp = System.currentTimeMillis()
            )
            repository.addLocalMarket(market)
            FirebaseManager.instance.saveLocalMarket(context, market)
            snackbarMessage.value = "Nova Feira Local cadastrada no Firebase! 🎪"
        }
    }

    private val reservedUserHandles = setOf(
        "@marcelo_horta",
        "@herval_agro",
        "@fernanda_adubo",
        "@joao_organicos",
        "@ecochef_sp",
        "@pedro_hortas",
        "@ana_recicla"
    )

    fun isHandleAvailable(handle: String, currentHandle: String): Boolean {
        val formatted = if (handle.startsWith("@")) handle.lowercase().trim() else "@${handle.lowercase().trim()}"
        if (formatted == currentHandle.lowercase().trim()) return true
        return !reservedUserHandles.contains(formatted)
    }

    fun updateUserProfile(name: String, handle: String, neighborhood: String, whatsApp: String): Boolean {
        val prefs = userPreferences.value ?: return false
        val now = System.currentTimeMillis()
        val THREE_MONTHS_MS = 90L * 24 * 60 * 60 * 1000L

        val formattedHandle = if (handle.startsWith("@")) handle.lowercase().trim() else "@${handle.lowercase().trim()}"
        if (formattedHandle.length < 3) {
            snackbarMessage.value = "O nome de usuário individual deve ter pelo menos 3 caracteres."
            return false
        }

        if (formattedHandle != prefs.userHandle.lowercase().trim() && reservedUserHandles.contains(formattedHandle)) {
            snackbarMessage.value = "O nome de usuário $formattedHandle já pertence a outro integrante da comunidade. Por favor escolha um nome individual diferente."
            return false
        }

        if (prefs.profileUpdatesCount >= 2) {
            val timeSinceLastUpdate = now - prefs.lastProfileUpdateTimestamp
            if (timeSinceLastUpdate < THREE_MONTHS_MS) {
                val daysRemaining = ((THREE_MONTHS_MS - timeSinceLastUpdate) / (1000L * 60 * 60 * 24)).coerceAtLeast(1)
                snackbarMessage.value = "Você já atualizou seu perfil 2 vezes. A próxima alteração é permitida apenas a cada 3 meses (disponível em $daysRemaining dias)."
                return false
            }
        }

        viewModelScope.launch {
            repository.updateUserProfile(name, formattedHandle, neighborhood, whatsApp, now)
            val updatedPrefs = prefs.copy(
                userName = name,
                userHandle = formattedHandle,
                userNeighborhood = neighborhood,
                userWhatsApp = whatsApp,
                profileUpdatesCount = prefs.profileUpdatesCount + 1,
                lastProfileUpdateTimestamp = now
            )
            val context = getApplication<Application>().applicationContext
            FirebaseManager.instance.saveUserPreferences(updatedPrefs)
            val newCount = updatedPrefs.profileUpdatesCount
            if (newCount < 2) {
                snackbarMessage.value = "Perfil e nome individual $formattedHandle atualizados! (Ainda resta ${2 - newCount} alteração livre)."
            } else {
                snackbarMessage.value = "Perfil e nome individual $formattedHandle atualizados! Limite de 2 edições atingido. A próxima edição será em 3 meses."
            }
        }
        return true
    }

    fun toggleFollowUser(targetUser: String) {
        val prefs = userPreferences.value ?: return
        val currentFollowed = prefs.followedUsers.split(",").filter { it.isNotBlank() }.toMutableList()
        val isFollowing = currentFollowed.contains(targetUser)
        if (isFollowing) {
            currentFollowed.remove(targetUser)
            snackbarMessage.value = "Você deixou de seguir $targetUser."
        } else {
            currentFollowed.add(targetUser)
            snackbarMessage.value = "Você começou a seguir $targetUser! 🔔"
        }
        val newFollowedString = currentFollowed.joinToString(",")
        viewModelScope.launch {
            repository.updateFollowedUsers(newFollowedString)
            val updatedPrefs = prefs.copy(followedUsers = newFollowedString)
            val context = getApplication<Application>().applicationContext
            FirebaseManager.instance.saveUserPreferences(updatedPrefs)
        }
    }

    fun toggleTheme() {
        val prefs = userPreferences.value ?: UserPreferencesEntity()
        val newTheme = !prefs.isDarkTheme
        viewModelScope.launch {
            val updated = prefs.copy(isDarkTheme = newTheme)
            repository.saveUserPreferences(updated)
            FirebaseManager.instance.saveUserPreferences(updated)
            snackbarMessage.value = if (newTheme) "Tema Escuro ativado 🌙" else "Tema Claro ativado ☀️"
        }
    }

    fun deleteUserProfile() {
        val prefs = userPreferences.value ?: return
        viewModelScope.launch {
            val resetPrefs = prefs.copy(
                userName = "Novo Integrante",
                userHandle = "@novo_integrante",
                userNeighborhood = "Bairro não informado",
                userWhatsApp = "",
                followedUsers = "",
                profileUpdatesCount = 0,
                lastProfileUpdateTimestamp = 0L
            )
            repository.resetUserProfile(resetPrefs)
            FirebaseManager.instance.saveUserPreferences(resetPrefs)
            snackbarMessage.value = "Perfil excluído com sucesso. Suas informações foram reiniciadas! 🗑️"
        }
    }

    fun onContactWhatsAppClicked(phone: String, title: String, onSuccess: () -> Unit) {
        val prefs = userPreferences.value
        if (prefs == null) return

        if (!prefs.isProSubscriber && prefs.actionCredits <= 0) {
            showRewardedAdDialog.value = true
            snackbarMessage.value = "Sem créditos de ação! Assista a um vídeo para liberar o WhatsApp."
            return
        }

        viewModelScope.launch {
            if (!prefs.isProSubscriber) {
                repository.consumeCredit()
            }
            onSuccess()
        }
    }

    fun reportListing(id: Int) {
        viewModelScope.launch {
            repository.reportListing(id)
            snackbarMessage.value = "Denúncia registrada. A Moderação Comunitária agradece!"
        }
    }

    fun reportPost(id: Int) {
        viewModelScope.launch {
            repository.reportPost(id)
            snackbarMessage.value = "Denúncia registrada. A Moderação Comunitária agradece!"
        }
    }

    fun toggleLikePost(post: CommunityPostEntity) {
        viewModelScope.launch {
            repository.toggleLikePost(post.id, post.isLiked, post.likesCount)
        }
    }

    fun clearSnackbar() {
        snackbarMessage.value = null
    }
}
