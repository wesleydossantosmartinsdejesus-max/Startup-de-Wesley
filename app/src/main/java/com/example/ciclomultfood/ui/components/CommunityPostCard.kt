package com.example.ciclomultfood.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg

@Composable
fun CommunityPostCard(
    post: CommunityPostEntity,
    onLikePost: (CommunityPostEntity) -> Unit,
    onReportPost: (Int) -> Unit,
    isFollowingAuthor: Boolean = false,
    onToggleFollow: ((String) -> Unit)? = null,
    onOpenAuthorProfile: ((CommunityPostEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var isVideoPlaying by remember { mutableStateOf(false) }

    val typeColor = if (post.type == "CORRENTE_BEM") Color(0xFFE91E63) else Color(0xFF00897B)
    val typeLabel = if (post.type == "CORRENTE_BEM") "Corrente do Bem" else "Como Eu Cultivo"

    val drawableRes = when {
        post.mediaUrl.contains("fresh_produce") -> R.drawable.img_fresh_produce_1785470112199
        post.mediaUrl.contains("compost") -> R.drawable.img_compost_organic_1785470123756
        post.mediaUrl.contains("donation") -> R.drawable.img_food_donation_1785470135793
        post.type == "CORRENTE_BEM" -> R.drawable.img_food_donation_1785470135793
        else -> R.drawable.img_fresh_produce_1785470112199
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header (Compact layout)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onOpenAuthorProfile?.invoke(post) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(typeColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = typeColor,
                            fontSize = 13.sp
                        )
                    }

                    val isLongName = post.authorName.length > 15

                    Column(verticalArrangement = Arrangement.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            if (post.authorIsPro) {
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Selo Pro",
                                    tint = ProBadgeBg,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            if (onToggleFollow != null && !isLongName) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (isFollowingAuthor) GreenPrimary.copy(alpha = 0.15f) else GreenPrimary,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onToggleFollow(post.authorName) }
                                        .testTag("btn_follow_post_author_${post.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFollowingAuthor) Icons.Default.Check else Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            tint = if (isFollowingAuthor) GreenPrimary else Color.White,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Text(
                                            text = if (isFollowingAuthor) "Seguindo" else "Seguir",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isFollowingAuthor) GreenPrimary else Color.White
                                        )
                                    }
                                }
                            }
                        }

                        if (isLongName && onToggleFollow != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = post.authorNeighborhood,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Surface(
                                    color = if (isFollowingAuthor) GreenPrimary.copy(alpha = 0.15f) else GreenPrimary,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onToggleFollow(post.authorName) }
                                        .testTag("btn_follow_post_author_${post.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFollowingAuthor) Icons.Default.Check else Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            tint = if (isFollowingAuthor) GreenPrimary else Color.White,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Text(
                                            text = if (isFollowingAuthor) "Seguindo" else "Seguir",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isFollowingAuthor) GreenPrimary else Color.White
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = post.authorNeighborhood,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opções",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver Perfil & ESG do Autor") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = GreenPrimary
                                )
                            },
                            onClick = {
                                showMenu = false
                                onOpenAuthorProfile?.invoke(post)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Denunciar Post (Moderação)") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ReportProblem,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                showMenu = false
                                onReportPost(post.id)
                            }
                        )
                    }
                }
            }

            // Media Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(Color.Black)
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = post.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category Tag Overlay (Compact)
                Surface(
                    color = typeColor,
                    shape = RoundedCornerShape(bottomEnd = 10.dp, topEnd = 10.dp),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = typeLabel,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (post.isVideo) {
                    if (!isVideoPlaying) {
                        // Data saving overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .clickable { isVideoPlaying = true }
                                    .testTag("btn_play_video_${post.id}"),
                                color = GreenPrimary,
                                tonalElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White
                                    )
                                    Column {
                                        Text(
                                            text = "▶ Reproduzir Vídeo",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "Economia de Dados (3G/4G)",
                                            fontSize = 10.sp,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Playing indicator
                        Surface(
                            modifier = Modifier
                                .padding(12.dp)
                                .align(Alignment.TopEnd),
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "🔴 Vídeo em Reprodução (HD)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Post Content & Actions
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = post.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    text = post.content,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = { onLikePost(post) },
                            modifier = Modifier.testTag("btn_like_${post.id}")
                        ) {
                            Icon(
                                imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Curtir",
                                tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${post.likesCount} curtidas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = {
                                val shareMessage = "🌱 Confira esta publicação no Ciclomult_Food!\n\n" +
                                        "👤 Autor: ${post.authorName} (${post.authorNeighborhood})\n" +
                                        "📌 Categoria: $typeLabel\n" +
                                        "📌 ${post.title}\n\n" +
                                        "\"${post.content}\"\n\n" +
                                        "Baixe o Ciclomult_Food e conecte-se com produtores e hortas comunitárias!"

                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Compartilhar publicação")
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("btn_share_post_${post.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartilhar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
