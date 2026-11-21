package com.example.ciclomultfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.ui.components.AdMobNativeCard
import com.example.ciclomultfood.ui.components.CommunityPostCard
import com.example.ciclomultfood.ui.components.FoodWasteResearchBanner

@Composable
fun CommunityFeedScreen(
    posts: List<CommunityPostEntity>,
    onLikePost: (CommunityPostEntity) -> Unit,
    onReportPost: (Int) -> Unit,
    isProUser: Boolean,
    onOpenResearchDialog: () -> Unit,
    followedUsers: String = "",
    onToggleFollow: ((String) -> Unit)? = null,
    onOpenAuthorProfile: ((CommunityPostEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Research Banner at top of social feed
        FoodWasteResearchBanner(
            onOpenResearchDialog = onOpenResearchDialog
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("community_feed_list"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            itemsIndexed(posts) { index, post ->
                val isFollowing = followedUsers.split(",").contains(post.authorName)
                CommunityPostCard(
                    post = post,
                    onLikePost = onLikePost,
                    onReportPost = onReportPost,
                    isFollowingAuthor = isFollowing,
                    onToggleFollow = onToggleFollow,
                    onOpenAuthorProfile = onOpenAuthorProfile
                )

                // Interspersed Passive AdMob Native Banner every 2 posts for non-pro users
                if (!isProUser && (index + 1) % 2 == 0) {
                    AdMobNativeCard()
                }
            }
        }
    }
}
