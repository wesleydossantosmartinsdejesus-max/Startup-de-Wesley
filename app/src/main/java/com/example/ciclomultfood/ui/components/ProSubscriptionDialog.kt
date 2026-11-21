package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.ProBadgeBg

@Composable
fun ProSubscriptionDialog(
    onDismiss: () -> Unit,
    onActivatePro: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("pro_subscription_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(ProBadgeBg.copy(alpha = 0.15f), shape = RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Selo Premium",
                        tint = ProBadgeBg,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Selo Premium - Produtor Profissional",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Para produtores urbanos e comércios.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProBenefitRow("Selo de Verificação Azul no perfil e anúncios")
                        ProBenefitRow("Anúncios ilimitados sem consumir créditos")
                        ProBenefitRow("Experiência 100% livre de publicidade")
                        ProBenefitRow("Pagamento fácil via Pix ou Boleto (R$ 29,90/mês)")
                    }
                }

                Button(
                    onClick = onActivatePro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_simulate_pix_boleto"),
                    colors = ButtonDefaults.buttonColors(containerColor = ProBadgeBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Simular Pagamento Pix/Boleto (R$ 29,90)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text("Continuar Grátis com Anúncios", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun ProBenefitRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = ProBadgeBg,
            modifier = Modifier.size(16.dp)
        )
        Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
