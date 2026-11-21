package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.GreenPrimary

@Composable
fun TermsDialog(
    onAcceptTerms: () -> Unit
) {
    Dialog(
        onDismissRequest = { /* Mandatory gate - cannot dismiss without accepting */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("terms_acceptance_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(GreenPrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = "Termos",
                        tint = GreenPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Termos de Uso e Contrato",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Bem-vindo ao Ciclomult_Food! Para utilizar nossa plataforma social e marketplace de economia circular, você concorda com as cláusulas abaixo:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TermRow(
                            title = "1. Isenção de Responsabilidade de Produtos",
                            detail = "O Ciclomult_Food atua estritamente como ponte digital geolocalizada via WhatsApp. A plataforma NÃO se responsabiliza pela qualidade, conservação ou entrega de alimentos e produtos negociados."
                        )
                        HorizontalDivider()
                        TermRow(
                            title = "2. Conformidade com Diretrizes AdMob",
                            detail = "A aplicação é 100% gratuita para o usuário comum, monetizada via banners estáticos passivos e vídeos premiados por créditos, em estrita conformidade com as exigências da Google AdMob."
                        )
                        HorizontalDivider()
                        TermRow(
                            title = "3. Moderação e Segurança Comunitária",
                            detail = "Fica proibido anúncios enganosos, golpes ou linguagem ofensiva. 3 denúncias de usuários ocultam a publicação automaticamente."
                        )
                    }
                }

                Button(
                    onClick = onAcceptTerms,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_accept_terms"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Li e Aceito os Termos e Contratos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TermRow(title: String, detail: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = GreenPrimary,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
