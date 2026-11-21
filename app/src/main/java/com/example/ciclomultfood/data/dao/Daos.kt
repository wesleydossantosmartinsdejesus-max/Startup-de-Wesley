package com.example.ciclomultfood.data.dao

import androidx.room.*
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Query("SELECT * FROM listings WHERE isHidden = 0 ORDER BY timestamp DESC")
    fun getAllVisibleListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE category = :category AND isHidden = 0 ORDER BY timestamp DESC")
    fun getListingsByCategory(category: String): Flow<List<ListingEntity>>

    @Query("SELECT COUNT(*) FROM listings")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listings: List<ListingEntity>)

    @Query("UPDATE listings SET reportsCount = reportsCount + 1, isHidden = CASE WHEN reportsCount + 1 >= 3 THEN 1 ELSE isHidden END WHERE id = :id")
    suspend fun reportListing(id: Int)
}

@Dao
interface CommunityPostDao {
    @Query("SELECT * FROM community_posts WHERE isHidden = 0 ORDER BY timestamp DESC")
    fun getAllVisiblePosts(): Flow<List<CommunityPostEntity>>

    @Query("SELECT COUNT(*) FROM community_posts")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<CommunityPostEntity>)

    @Query("UPDATE community_posts SET isLiked = :isLiked, likesCount = :likesCount WHERE id = :id")
    suspend fun updateLike(id: Int, isLiked: Boolean, likesCount: Int)

    @Query("UPDATE community_posts SET reportsCount = reportsCount + 1, isHidden = CASE WHEN reportsCount + 1 >= 3 THEN 1 ELSE isHidden END WHERE id = :id")
    suspend fun reportPost(id: Int)
}

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    suspend fun getUserPreferencesSync(): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserPreferencesEntity)

    @Query("UPDATE user_preferences SET acceptedTerms = :accepted WHERE id = 1")
    suspend fun setAcceptedTerms(accepted: Boolean)

    @Query("UPDATE user_preferences SET actionCredits = actionCredits + :amount WHERE id = 1")
    suspend fun addCredits(amount: Int)

    @Query("UPDATE user_preferences SET actionCredits = CASE WHEN actionCredits > 0 THEN actionCredits - 1 ELSE 0 END WHERE id = 1")
    suspend fun consumeCredit()

    @Query("UPDATE user_preferences SET isProSubscriber = :isPro WHERE id = 1")
    suspend fun setProStatus(isPro: Boolean)

    @Query("UPDATE user_preferences SET totalKgSaved = totalKgSaved + :kgSaved, totalCo2EvadedKg = totalCo2EvadedKg + :co2Evaded, donationsCompleted = donationsCompleted + :donationsCount WHERE id = 1")
    suspend fun incrementImpact(kgSaved: Double, co2Evaded: Double, donationsCount: Int)

    @Query("UPDATE user_preferences SET userName = :name, userHandle = :handle, userNeighborhood = :neighborhood, userWhatsApp = :whatsApp, profileUpdatesCount = profileUpdatesCount + 1, lastProfileUpdateTimestamp = :timestamp WHERE id = 1")
    suspend fun updateUserProfile(name: String, handle: String, neighborhood: String, whatsApp: String, timestamp: Long)

    @Query("UPDATE user_preferences SET followedUsers = :followed WHERE id = 1")
    suspend fun updateFollowedUsers(followed: String)
}

@Dao
interface LocalMarketDao {
    @Query("SELECT * FROM local_markets ORDER BY id ASC")
    fun getAllLocalMarkets(): Flow<List<LocalMarketEntity>>

    @Query("SELECT COUNT(*) FROM local_markets")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarket(market: LocalMarketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(markets: List<LocalMarketEntity>)
}

