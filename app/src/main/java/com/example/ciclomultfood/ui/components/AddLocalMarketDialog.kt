package com.example.ciclomultfood.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GreenPrimary

@Composable
fun AddLocalMarketDialog(
    onDismiss: () -> Unit,
    onSubmitMarket: (name: String, location: String, availableItems: String, operatingHours: String, organizerContact: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var availableItems by remember { mutableStateOf("") }
    var operatingHours by remember { mutableStateOf("") }
    var organizerContact by remember { mutableStateOf("5511987654321") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Cadastrar Feira Local",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Coleção Firestore: local_markets",
                        fontSize = 11.sp,
                        color = GreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Feira") },
                    placeholder = { Text("Ex: Feira Orgânica da Vila") },
                    leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_market_name"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Localização (Endereço e Bairro)") },
                    placeholder = { Text("Ex: Praça Central - R. das Flores, 100") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_market_location"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = availableItems,
                    onValueChange = { availableItems = it },
                    label = { Text("Itens Disponíveis") },
                    placeholder = { Text("Ex: Hortaliças, Frutas, Mel, Adubo orgânico") },
                    leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_market_items"),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = operatingHours,
                    onValueChange = { operatingHours = it },
                    label = { Text("Horário de Funcionamento") },
                    placeholder = { Text("Ex: Sábados e Domingos, 07:00 às 13:00") },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_market_hours"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = organizerContact,
                    onValueChange = { organizerContact = it },
                    label = { Text("WhatsApp do Organizador") },
                    placeholder = { Text("5511987654321") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_market_contact"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank() || location.isBlank() || availableItems.isBlank() || operatingHours.isBlank()) {
                        errorMessage = "Por favor, preencha todos os campos obrigatórios."
                        return@Button
                    }
                    onSubmitMarket(name, location, availableItems, operatingHours, organizerContact)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_submit_market")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Salvar no Firestore", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_market")
            ) {
                Text("Cancelar")
            }
        }
    )
}
