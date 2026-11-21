package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compost
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Grid3x3
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary

data class StoryCategory(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val badgeColor: Color
)

@Composable
fun InstagramStoryBar(
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        StoryCategory("TODOS", "Tudo", Icons.Default.Grid3x3, GreenPrimary),
        StoryCategory("COMPRA_VENDA", "Feira Directa", Icons.Default.Yard, GreenPrimary),
        StoryCategory("DOACAO", "Doações", Icons.Default.VolunteerActivism, Color(0xFFE53935)),
        StoryCategory("RECICLAGEM_ORGANICA", "Reciclagem", Icons.Default.Compost, Color(0xFF795548))
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 6.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { cat ->
            val isSelected = selectedCategoryId == cat.id

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(68.dp)
                    .clickable { onCategorySelected(cat.id) }
                    .testTag("story_cat_${cat.id}")
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) cat.badgeColor else Color.LightGray,
                            shape = CircleShape
                        )
                        .background(if (isSelected) cat.badgeColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = cat.icon,
                        contentDescription = cat.title,
                        tint = if (isSelected) cat.badgeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cat.title,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) cat.badgeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
