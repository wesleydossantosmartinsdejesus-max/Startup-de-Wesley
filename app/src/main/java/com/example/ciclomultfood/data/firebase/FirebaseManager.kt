package com.example.ciclomultfood.data.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseManager private constructor() {

    private val db: FirebaseFirestore? by lazy {
        runCatching { FirebaseFirestore.getInstance() }.getOrNull()
    }

    private val storage: FirebaseStorage? by lazy {
        runCatching { FirebaseStorage.getInstance() }.getOrNull()
    }

    private val auth: FirebaseAuth? by lazy {
        runCatching { FirebaseAuth.getInstance() }.getOrNull()
    }

    fun getCurrentUserId(): String {
        return auth?.currentUser?.uid ?: "user_ciclomult_local"
    }

    suspend fun saveUserPreferences(userPrefs: UserPreferencesEntity): Boolean {
        return runCatching {
            val firestore = db ?: return false
            val userId = getCurrentUserId()
            val userMap = hashMapOf(
                "userId" to userId,
                "name" to userPrefs.userName,
                "handle" to userPrefs.userHandle,
                "neighborhood" to userPrefs.userNeighborhood,
                "whatsApp" to userPrefs.userWhatsApp,
                "followedUsers" to userPrefs.followedUsers,
                "totalKgSaved" to userPrefs.totalKgSaved,
                "totalCo2EvadedKg" to userPrefs.totalCo2EvadedKg,
                "donationsCompleted" to userPrefs.donationsCompleted,
                "actionCredits" to userPrefs.actionCredits,
                "isProSubscriber" to userPrefs.isProSubscriber,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(userId).set(userMap).await()
            Log.d(TAG, "User profile saved to Firebase Firestore: $userId")
            true
        }.getOrElse {
            Log.w(TAG, "Failed saving user to Firestore: ${it.message}")
            false
        }
    }

    suspend fun saveListing(context: Context, listing: ListingEntity): String? {
        return runCatching {
            val firestore = db ?: return null
            var finalMediaUrl = listing.imageUrl

            // Upload image to Firebase Storage if mediaUri is a local content/file URI
            if (finalMediaUrl.startsWith("content://") || finalMediaUrl.startsWith("file://")) {
                val uploadedUrl = uploadMediaToStorage(context, Uri.parse(finalMediaUrl), "listings_media", false)
                if (uploadedUrl != null) {
                    finalMediaUrl = uploadedUrl
                }
            }

            val docId = if (listing.id > 0) listing.id.toString() else UUID.randomUUID().toString()
            val listingMap = hashMapOf(
                "id" to listing.id,
                "title" to listing.title,
                "category" to listing.category,
                "subCategory" to listing.subCategory,
                "description" to listing.description,
                "quantity" to listing.quantity,
                "price" to listing.price,
                "sellerName" to listing.sellerName,
                "sellerNeighborhood" to listing.sellerNeighborhood,
                "sellerWhatsApp" to listing.sellerWhatsApp,
                "sellerMapAddress" to listing.sellerMapAddress,
                "sellerIsPro" to listing.sellerIsPro,
                "imageUrl" to finalMediaUrl,
                "timestamp" to listing.timestamp,
                "sellerId" to getCurrentUserId()
            )

            firestore.collection("listings").document(docId).set(listingMap).await()
            Log.d(TAG, "Listing saved to Firebase Firestore: $docId")
            finalMediaUrl
        }.getOrElse {
            Log.w(TAG, "Error saving listing to Firebase: ${it.message}")
            null
        }
    }

    suspend fun saveCommunityPost(context: Context, post: CommunityPostEntity): String? {
        return runCatching {
            val firestore = db ?: return null
            var finalMediaUrl = post.mediaUrl

            if (finalMediaUrl.startsWith("content://") || finalMediaUrl.startsWith("file://")) {
                val uploadedUrl = uploadMediaToStorage(context, Uri.parse(finalMediaUrl), "posts_media", post.isVideo)
                if (uploadedUrl != null) {
                    finalMediaUrl = uploadedUrl
                }
            }

            val docId = if (post.id > 0) post.id.toString() else UUID.randomUUID().toString()
            val postMap = hashMapOf(
                "id" to post.id,
                "authorName" to post.authorName,
                "authorNeighborhood" to post.authorNeighborhood,
                "authorIsPro" to post.authorIsPro,
                "type" to post.type,
                "title" to post.title,
                "content" to post.content,
                "mediaUrl" to finalMediaUrl,
                "isVideo" to post.isVideo,
                "likesCount" to post.likesCount,
                "timestamp" to post.timestamp,
                "authorId" to getCurrentUserId()
            )

            firestore.collection("community_posts").document(docId).set(postMap).await()
            Log.d(TAG, "Community post saved to Firebase Firestore: $docId")
            finalMediaUrl
        }.getOrElse {
            Log.w(TAG, "Error saving post to Firebase: ${it.message}")
            null
        }
    }

    suspend fun saveLocalMarket(context: Context, market: LocalMarketEntity): String? {
        return runCatching {
            val firestore = db ?: return null
            var finalImageUrl = market.imageUrl

            if (finalImageUrl.startsWith("content://") || finalImageUrl.startsWith("file://")) {
                val uploadedUrl = uploadMediaToStorage(context, Uri.parse(finalImageUrl), "markets_media", false)
                if (uploadedUrl != null) {
                    finalImageUrl = uploadedUrl
                }
            }

            val docId = if (market.id > 0) market.id.toString() else UUID.randomUUID().toString()
            val marketMap = hashMapOf(
                "id" to market.id,
                "name" to market.name,
                "location" to market.location,
                "latitude" to market.latitude,
                "longitude" to market.longitude,
                "availableItems" to market.availableItems,
                "operatingHours" to market.operatingHours,
                "organizerContact" to market.organizerContact,
                "imageUrl" to finalImageUrl,
                "isVerified" to market.isVerified,
                "timestamp" to market.timestamp,
                "updatedBy" to getCurrentUserId()
            )

            firestore.collection("local_markets").document(docId).set(marketMap).await()
            Log.d(TAG, "Local market saved to Firebase Firestore collection 'local_markets': $docId")
            finalImageUrl
        }.getOrElse {
            Log.w(TAG, "Error saving local market to Firebase Firestore: ${it.message}")
            null
        }
    }

    suspend fun fetchLocalMarkets(): List<LocalMarketEntity> {
        return runCatching {
            val firestore = db ?: return emptyList()
            val snapshot = firestore.collection("local_markets").get().await()
            val list = mutableListOf<LocalMarketEntity>()
            for (doc in snapshot.documents) {
                val id = (doc.getLong("id") ?: 0L).toInt()
                val name = doc.getString("name") ?: ""
                val location = doc.getString("location") ?: ""
                val latitude = doc.getDouble("latitude") ?: 0.0
                val longitude = doc.getDouble("longitude") ?: 0.0
                val availableItems = doc.getString("availableItems") ?: ""
                val operatingHours = doc.getString("operatingHours") ?: ""
                val organizerContact = doc.getString("organizerContact") ?: ""
                val imageUrl = doc.getString("imageUrl") ?: ""
                val isVerified = doc.getBoolean("isVerified") ?: true
                val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()

                list.add(
                    LocalMarketEntity(
                        id = id,
                        name = name,
                        location = location,
                        latitude = latitude,
                        longitude = longitude,
                        availableItems = availableItems,
                        operatingHours = operatingHours,
                        organizerContact = organizerContact,
                        imageUrl = imageUrl,
                        isVerified = isVerified,
                        timestamp = timestamp
                    )
                )
            }
            Log.d(TAG, "Fetched ${list.size} local markets from Firebase Firestore collection 'local_markets'")
            list
        }.getOrElse {
            Log.w(TAG, "Error fetching local markets from Firebase Firestore: ${it.message}")
            emptyList()
        }
    }

    private suspend fun uploadMediaToStorage(
        context: Context,
        fileUri: Uri,
        folderName: String,
        isVideo: Boolean
    ): String? {
        return runCatching {
            val firebaseStorage = storage ?: return null
            val fileExtension = if (isVideo) "mp4" else "jpg"
            val fileName = "$folderName/${UUID.randomUUID()}_${System.currentTimeMillis()}.$fileExtension"
            val ref = firebaseStorage.reference.child(fileName)

            val inputStream = context.contentResolver.openInputStream(fileUri) ?: return null
            val uploadTask = ref.putStream(inputStream).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            Log.d(TAG, "Media successfully uploaded to Firebase Storage: $downloadUrl")
            downloadUrl
        }.getOrElse {
            Log.w(TAG, "Firebase Storage upload failed: ${it.message}")
            null
        }
    }

    companion object {
        private const val TAG = "FirebaseManager"
        val instance: FirebaseManager by lazy { FirebaseManager() }
    }
}
