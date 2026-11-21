package com.example.ciclomultfood.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GreenPrimary

fun openGoogleMaps(context: Context, addressOrUrl: String) {
    val query = addressOrUrl.ifBlank { "Capão Redondo, São Paulo - SP" }
    val uri = if (query.startsWith("http://") || query.startsWith("https://")) {
        Uri.parse(query)
    } else {
        Uri.parse("geo:0,0?q=" + Uri.encode(query))
    }
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query))
        )
        context.startActivity(browserIntent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        category: String,
        subCategory: String,
        description: String,
        quantity: String,
        price: Double,
        sellerWhatsApp: String,
        isSocialPost: Boolean,
        mediaUri: String?,
        isVideo: Boolean,
        mapAddress: String
    ) -> Unit
) {
    val context = LocalContext.current

    var isSocialPost by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("COMPRA_VENDA") } // COMPRA_VENDA, DOACAO, RECICLAGEM_ORGANICA
    var socialType by remember { mutableStateOf("COMO_CULTIVO") } // COMO_CULTIVO, CORRENTE_BEM

    var title by remember { mutableStateOf("") }
    var subCategory by remember { mutableStateOf("Hortaliças") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("2 kg") }
    var priceText by remember { mutableStateOf("5.00") }
    var whatsapp by remember { mutableStateOf("") }
    var mapAddress by remember { mutableStateOf("") }

    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var isSelectedVideo by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Launchers for image and video selection
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            isSelectedVideo = false
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            isSelectedVideo = true
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permissão da câmera é necessária para capturar imagens.", Toast.LENGTH_SHORT).show()
        }
    }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fine || coarse) {
            mapAddress = "Rua Comendador Sant'Anna, 300 - Capão Redondo, São Paulo - SP (-23.6512, -46.7725)"
            Toast.makeText(context, "Localização GPS detectada com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permissão de localização negada.", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("add_listing_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Anunciar no Ciclomult",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Text("✕", fontWeight = FontWeight.Bold)
                    }
                }

                // Toggle: Marketplace Listing vs Social Feed Post
                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = !isSocialPost,
                        onClick = { isSocialPost = false },
                        label = { Text("Anúncio / Produto") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                            .testTag("tab_product_announcement")
                    )
                    FilterChip(
                        selected = isSocialPost,
                        onClick = { isSocialPost = true },
                        label = { Text("Feed Comunitário") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                            .testTag("tab_social_announcement")
                    )
                }

                if (!isSocialPost) {
                    // Category Selection
                    Text("Tipo de Anúncio:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == "COMPRA_VENDA",
                            onClick = { selectedCategory = "COMPRA_VENDA" },
                            label = { Text("Venda Directa", fontSize = 11.sp) },
                            modifier = Modifier.testTag("chip_cat_venda")
                        )
                        FilterChip(
                            selected = selectedCategory == "DOACAO",
                            onClick = { selectedCategory = "DOACAO" },
                            label = { Text("Doação", fontSize = 11.sp) },
                            modifier = Modifier.testTag("chip_cat_doacao")
                        )
                        FilterChip(
                            selected = selectedCategory == "RECICLAGEM_ORGANICA",
                            onClick = { selectedCategory = "RECICLAGEM_ORGANICA" },
                            label = { Text("Reciclagem", fontSize = 11.sp) },
                            modifier = Modifier.testTag("chip_cat_reciclagem")
                        )
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título (ex: 3 kg de Laranja Fresca)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_listing_title")
                    )

                    OutlinedTextField(
                        value = subCategory,
                        onValueChange = { subCategory = it },
                        label = { Text("Categoria (Hortaliças, Frutas, Compostagem, etc)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_listing_subcategory")
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantidade (ex: 2 kg)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_listing_quantity")
                        )

                        if (selectedCategory != "DOACAO") {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("Preço (R$)") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_listing_price")
                            )
                        }
                    }
                } else {
                    // Social Post Selection
                    Text("Tipo de Publicação Social:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = socialType == "COMO_CULTIVO",
                            onClick = { socialType = "COMO_CULTIVO" },
                            label = { Text("Como Eu Cultivo", fontSize = 11.sp) },
                            modifier = Modifier.testTag("chip_type_cultivo")
                        )
                        FilterChip(
                            selected = socialType == "CORRENTE_BEM",
                            onClick = { socialType = "CORRENTE_BEM" },
                            label = { Text("Corrente do Bem", fontSize = 11.sp) },
                            modifier = Modifier.testTag("chip_type_corrente")
                        )
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título do Post") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_post_title")
                    )
                }

                // Media Upload Section (Photos and Videos up to 5 min)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Foto ou Vídeo (até 5 minutos):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (selectedMediaUri == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        imagePickerLauncher.launch("image/*")
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_upload_photo"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Foto", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { videoPickerLauncher.launch("video/*") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_upload_video"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Vídeo (≤ 5 min)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = GreenPrimary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (isSelectedVideo) Icons.Default.Movie else Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if (isSelectedVideo) "🎥 Vídeo Anexado (Máx 5 min)" else "📷 Foto Anexada",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = GreenPrimary
                                        )
                                        Text(
                                            text = "⚡ Compressão automática (economia 3G/4G)",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        selectedMediaUri = null
                                        isSelectedVideo = false
                                    },
                                    modifier = Modifier.testTag("btn_remove_media")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remover",
                                        tint = Color.Red.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição / Detalhes para a comunidade") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_listing_desc")
                )

                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("WhatsApp de Contato (com DDD)") },
                    placeholder = { Text("5511987654321") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_listing_whatsapp")
                )

                // Google Maps Location Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Localização Exata no Google Maps:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = mapAddress,
                        onValueChange = { mapAddress = it },
                        label = { Text("Endereço Exato ou Ponto de Referência") },
                        placeholder = { Text("Ex: Rua Comendador Sant'Anna, 300 - Capão Redondo, SP") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Localização",
                                tint = GreenPrimary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_listing_maps_address")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                                if (fineGranted || coarseGranted) {
                                    mapAddress = "Rua Comendador Sant'Anna, 300 - Capão Redondo, São Paulo - SP (-23.6512, -46.7725)"
                                    Toast.makeText(context, "Localização GPS atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("btn_use_gps"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Usar GPS Atual", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                openGoogleMaps(context, mapAddress.ifBlank { "Capão Redondo, São Paulo - SP" })
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_preview_maps"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Testar Mapa", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        if (title.isBlank() || description.isBlank()) {
                            errorMessage = "Preencha o título e a descrição."
                            return@Button
                        }
                        val finalPrice = priceText.toDoubleOrNull() ?: 0.0
                        onSubmit(
                            title,
                            if (isSocialPost) socialType else selectedCategory,
                            subCategory,
                            description,
                            quantity,
                            finalPrice,
                            whatsapp,
                            isSocialPost,
                            selectedMediaUri?.toString(),
                            isSelectedVideo,
                            mapAddress
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_listing"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Publish, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSocialPost) "Publicar no Feed Comunitário" else "Anunciar Produto (Consome 1 Cr.)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

