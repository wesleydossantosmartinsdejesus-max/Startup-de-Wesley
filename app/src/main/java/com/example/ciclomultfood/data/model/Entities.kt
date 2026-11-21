package com.example.ciclomultfood.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // COMPRA_VENDA, DOACAO, RECICLAGEM_ORGANICA
    val subCategory: String, // Hortaliças, Frutas, Resíduos, etc.
    val description: String,
    val quantity: String,
    val price: Double,
    val sellerName: String,
    val sellerNeighborhood: String,
    val sellerWhatsApp: String,
    val sellerMapAddress: String = "",
    val sellerIsPro: Boolean = false,
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val reportsCount: Int = 0,
    val isHidden: Boolean = false
)

@Entity(tableName = "community_posts")
data class CommunityPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authorName: String,
    val authorAvatarUrl: String = "",
    val authorNeighborhood: String,
    val authorIsPro: Boolean = false,
    val type: String, // COMO_CULTIVO, CORRENTE_BEM
    val title: String,
    val content: String,
    val mediaUrl: String = "",
    val isVideo: Boolean = false,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val reportsCount: Int = 0,
    val isHidden: Boolean = false
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val acceptedTerms: Boolean = false,
    val isProSubscriber: Boolean = false,
    val actionCredits: Int = 5,
    val totalKgSaved: Double = 14.5,
    val totalCo2EvadedKg: Double = 28.0,
    val donationsCompleted: Int = 4,
    val userName: String = "Dona Maria",
    val userHandle: String = "@donamaria_horta",
    val userNeighborhood: String = "Jardim periferia, SP",
    val userWhatsApp: String = "5511987654321",
    val profileUpdatesCount: Int = 0,
    val lastProfileUpdateTimestamp: Long = 0L,
    val followedUsers: String = "",
    val isDarkTheme: Boolean = false
)

@Entity(tableName = "local_markets")
data class LocalMarketEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val location: String,
    val latitude: Double = -23.5505,
    val longitude: Double = -46.6333,
    val availableItems: String,
    val operatingHours: String,
    val organizerContact: String = "5511987654321",
    val imageUrl: String = "",
    val isVerified: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
