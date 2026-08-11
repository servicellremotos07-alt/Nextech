package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.DeviceEntity
import com.example.ui.DeviceViewModel
import com.example.ui.theme.LockedRed
import com.example.ui.theme.UnlockedGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    deviceId: Long,
    viewModel: DeviceViewModel,
    onBack: () -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val device = devices.find { it.id == deviceId }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (device == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de Dispositivo") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Dispositivo no encontrado o eliminado.")
            }
        }
        return
    }

    val statusColor = if (device.status == "LOCKED") LockedRed else UnlockedGreen
    val statusText = if (device.status == "LOCKED") "DISPOSITIVO BLOQUEADO 🔒" else "DISPOSITIVO LIBRE 🔓"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(device.model, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.testTag("delete_device_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Registro", tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Banner Card
            Card(
                colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = statusText,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (device.status == "LOCKED") "El equipo está restringido y muestra el mensaje de bloqueo." else "Operación normal habilitada.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { viewModel.toggleLockStatus(device) },
                        colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                        modifier = Modifier.testTag("toggle_lock_action")
                    ) {
                        Text(if (device.status == "LOCKED") "Desbloquear" else "Bloquear")
                    }
                }
            }

            // Customer & Device Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Información General", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Divider()

                    InfoRow(label = "IMEI / Serie", value = device.imei)
                    InfoRow(label = "Modelo", value = device.model)
                    InfoRow(label = "Propietario", value = device.customerName)
                    InfoRow(label = "Teléfono", value = device.customerPhone)
                    InfoRow(label = "PIN Técnico", value = device.lockPin)
                }
            }

            // Financing & Balance Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Financiamiento y Pagos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Divider()

                    InfoRow(label = "Saldo Pendiente", value = "$${String.format("%.2f", device.remainingBalance)}")
                    InfoRow(label = "Progreso de Cuotas", value = "${device.paidInstallments} de ${device.totalInstallments} pagadas")

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (device.paidInstallments < device.totalInstallments) {
                                    val newPaid = device.paidInstallments + 1
                                    val newBalance = kotlin.math.max(0.0, device.remainingBalance - (device.remainingBalance / (device.totalInstallments - device.paidInstallments).coerceAtLeast(1)))
                                    viewModel.updateDevice(device.copy(paidInstallments = newPaid, remainingBalance = newBalance))
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Registrar Cuota")
                        }

                        Button(
                            onClick = {
                                viewModel.updateDevice(device.copy(remainingBalance = 0.0, paidInstallments = device.totalInstallments, status = "UNLOCKED"))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Liquidar Total")
                        }
                    }
                }
            }

            // Lock Screen Simulator Preview Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.PhoneLocked, contentDescription = null, tint = LockedRed, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Vista Previa de Pantalla Bloqueada (Cliente)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔒 EQUIPO BLOQUEADO", color = LockedRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = device.lockMessage,
                                color = Color.White,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Registro") },
            text = { Text("¿Está seguro de eliminar el registro de este equipo en Bloqueo Nextech?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteDevice(device.id)
                        showDeleteDialog = false
                        onBack()
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
