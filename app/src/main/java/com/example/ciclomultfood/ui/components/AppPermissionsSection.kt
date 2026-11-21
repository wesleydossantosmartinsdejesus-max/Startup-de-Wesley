package com.example.ciclomultfood.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.GreenPrimary

@Composable
fun AppPermissionsSection(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var cameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    var locationGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    var notificationGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    // Launchers for dynamic permission requests
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraGranted = isGranted
        if (isGranted) {
            Toast.makeText(context, "Permissão da Câmera concedida com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permissão da Câmera negada.", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        locationGranted = fine || coarse
        if (locationGranted) {
            Toast.makeText(context, "Permissão de Localização GPS concedida!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permissão de Localização negada.", Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationGranted = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notificações ativadas com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notificações desativadas.", Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_permissions_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Permissões do App",
                        tint = GreenPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Column {
                        Text(
                            text = "Permissões do Aplicativo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Controle o acesso a recursos do seu celular",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Camera Permission Item
                PermissionRowItem(
                    title = "Câmera",
                    description = "Tire fotos dos alimentos, hortas e doações em tempo real",
                    icon = Icons.Default.CameraAlt,
                    isGranted = cameraGranted,
                    onRequestPermission = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    testTagSuffix = "camera"
                )

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Location Permission Item
                PermissionRowItem(
                    title = "Localização GPS",
                    description = "Encontre doações e feiras sustentáveis mais próximas no seu bairro",
                    icon = Icons.Default.LocationOn,
                    isGranted = locationGranted,
                    onRequestPermission = {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    testTagSuffix = "location"
                )

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Notifications Permission Item
                PermissionRowItem(
                    title = "Notificações",
                    description = "Receba alertas sobre novas doações, mensagens e recompensas",
                    icon = Icons.Default.Notifications,
                    isGranted = notificationGranted,
                    onRequestPermission = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            Toast.makeText(context, "Notificações já ativadas por padrão no Android.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    testTagSuffix = "notifications"
                )
            }
        }
    }
}

@Composable
private fun PermissionRowItem(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    onRequestPermission: () -> Unit,
    testTagSuffix: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("permission_item_$testTagSuffix"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = if (isGranted) GreenPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isGranted) GreenPrimary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (isGranted) {
                        Surface(
                            color = GreenPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Permitido",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Desativado",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (!isGranted) {
            Button(
                onClick = onRequestPermission,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_request_permission_$testTagSuffix")
            ) {
                Text("Permitir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            IconButton(
                onClick = onRequestPermission,
                modifier = Modifier.testTag("btn_granted_permission_$testTagSuffix")
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Concedido",
                    tint = GreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
