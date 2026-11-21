package com.example.ciclomultfood.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary
import kotlinx.coroutines.delay

@Composable
fun RewardedAdDialog(
    onDismiss: () -> Unit,
    onWatchCompleted: () -> Unit
) {
    var isWatching by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(10) }

    LaunchedEffect(isWatching) {
        if (isWatching) {
            while (secondsLeft > 0) {
                delay(1000L)
                secondsLeft--
            }
            onWatchCompleted()
        }
    }

    Dialog(onDismissRequest = { if (!isWatching) onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("rewarded_ad_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(AmberAccent.copy(alpha = 0.15f), shape = RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Vídeo Premiado",
                        tint = AmberAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Vídeo Premiado AdMob",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!isWatching) {
                    Text(
                        text = "Assista a um vídeo curto de 10 a 15 segundos para liberar gratuitamente +3 Créditos de Ação no aplicativo!",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = { isWatching = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_start_rewarded_video"),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Assistir Vídeo por Recompensa (+3 Cr.)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    TextButton(onClick = onDismiss) {
                        Text("Agora Não", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    // Video playback simulation screen
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, shape = RoundedCornerShape(12.dp))
                            .padding(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Vídeo",
                            tint = AmberAccent,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Exibindo Anúncio Patrocinado...",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        LinearProgressIndicator(
                            progress = { (10f - secondsLeft.toFloat()) / 10f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = AmberAccent,
                            trackColor = Color.DarkGray
                        )
                        Text(
                            text = "Aguarde $secondsLeft seg. para receber +3 Créditos",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
