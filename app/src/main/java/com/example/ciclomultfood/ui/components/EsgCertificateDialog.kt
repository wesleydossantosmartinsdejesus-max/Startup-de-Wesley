package com.example.ciclomultfood.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import com.example.ciclomultfood.util.EsgCertificateExporter
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimary

@Composable
fun EsgCertificateDialog(
    userPrefs: UserPreferencesEntity?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val kgSaved = userPrefs?.totalKgSaved ?: 14.5
    val co2Saved = userPrefs?.totalCo2EvadedKg ?: 28.0
    val donations = userPrefs?.donationsCompleted ?: 4

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("esg_certificate_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(GreenPrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardMembership,
                        contentDescription = "Certificado ESG",
                        tint = GreenPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Certificado de Impacto Social",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Atestado oficial de combate ao desperdício e impacto ambiental gerado pelo usuário no Ciclomult_Food.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Impact Metrics Grid
                Surface(
                    color = GreenPrimary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GreenPrimary)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ImpactMetric("Alimentos Salvos", "%.1f kg".format(kgSaved), Icons.Default.Eco)
                        ImpactMetric("CO2 Evitado", "%.1f kg".format(co2Saved), Icons.Default.Co2)
                        ImpactMetric("Doações", "$donations", Icons.Default.VolunteerActivism)
                    }
                }

                // ESG Corporate Sponsors
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "PATROCÍNIO CORPORATIVO - COTAS ESG",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "🏆 Supermercados VerdeMais ESG • BioAdubo Brasil",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                    }
                }

                // Export Options (PDF & Image)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val prefs = userPrefs ?: UserPreferencesEntity()
                            EsgCertificateExporter.exportAsPdf(context, prefs)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_export_esg_pdf"),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Emitir PDF",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val prefs = userPrefs ?: UserPreferencesEntity()
                            EsgCertificateExporter.exportAsImage(context, prefs)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_export_esg_png"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Emitir Imagem",
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Share Text Button
                TextButton(
                    onClick = {
                        val shareText = "🌱 Veja meu Certificado de Impacto Social no Ciclomult_Food!\n\n" +
                                "👤 Integrante: ${userPrefs?.userName ?: "Dona Maria"} (${userPrefs?.userHandle ?: "@donamaria_horta"})\n" +
                                "✅ %.1f kg de alimentos salvos do lixo\n".format(kgSaved) +
                                "🌍 %.1f kg de CO2 evitados\n".format(co2Saved) +
                                "❤️ $donations doações realizadas na minha comunidade!\n\n" +
                                "Patrocinadores ESG: Supermercados VerdeMais & BioAdubo Brasil."
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Compartilhar Texto do Certificado"))
                    },
                    modifier = Modifier.testTag("btn_share_esg_whatsapp")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Compartilhar texto no WhatsApp",
                        fontWeight = FontWeight.SemiBold,
                        color = GreenPrimary,
                        fontSize = 12.sp
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text("Fechar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun ImpactMetric(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = GreenPrimary)
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
