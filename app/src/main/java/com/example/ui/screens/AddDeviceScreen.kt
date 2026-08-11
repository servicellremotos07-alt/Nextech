package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DeviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(
    viewModel: DeviceViewModel,
    onBack: () -> Unit
) {
    var imei by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("LOCKED") }
    var lockMessage by remember { mutableStateOf("Dispositivo bloqueado por falta de pago. Comuníquese al centro de servicio.") }
    var lockPin by remember { mutableStateOf("1234") }
    var remainingBalance by remember { mutableStateOf("500.00") }
    var totalInstallments by remember { mutableStateOf("12") }
    var paidInstallments by remember { mutableStateOf("0") }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Dispositivo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (showError) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            OutlinedTextField(
                value = imei,
                onValueChange = { imei = it },
                label = { Text("IMEI o Número de Serie *") },
                modifier = Modifier.fillMaxWidth().testTag("input_imei"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modelo del Dispositivo (ej. iPhone 15) *") },
                modifier = Modifier.fillMaxWidth().testTag("input_model"),
                singleLine = true
            )

            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Nombre del Cliente *") },
                modifier = Modifier.fillMaxWidth().testTag("input_customer"),
                singleLine = true
            )

            OutlinedTextField(
                value = customerPhone,
                onValueChange = { customerPhone = it },
                label = { Text("Teléfono de Contacto") },
                modifier = Modifier.fillMaxWidth().testTag("input_phone"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Status selection
            Text("Estado Inicial", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = status == "LOCKED",
                    onClick = { status = "LOCKED" },
                    label = { Text("Bloqueado 🔒") }
                )
                FilterChip(
                    selected = status == "UNLOCKED",
                    onClick = { status = "UNLOCKED" },
                    label = { Text("Libre 🔓") }
                )
                FilterChip(
                    selected = status == "PENDING",
                    onClick = { status = "PENDING" },
                    label = { Text("Pendiente ⏳") }
                )
            }

            OutlinedTextField(
                value = lockMessage,
                onValueChange = { lockMessage = it },
                label = { Text("Mensaje de Bloqueo en Pantalla") },
                modifier = Modifier.fillMaxWidth().testTag("input_lock_msg"),
                maxLines = 3
            )

            OutlinedTextField(
                value = lockPin,
                onValueChange = { lockPin = it },
                label = { Text("PIN de Desbloqueo Técnico (4 dígitos)") },
                modifier = Modifier.fillMaxWidth().testTag("input_pin"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = remainingBalance,
                    onValueChange = { remainingBalance = it },
                    label = { Text("Saldo Restante ($)") },
                    modifier = Modifier.weight(1f).testTag("input_balance"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = totalInstallments,
                    onValueChange = { totalInstallments = it },
                    label = { Text("Cuotas Totales") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = paidInstallments,
                    onValueChange = { paidInstallments = it },
                    label = { Text("Cuotas Pagas") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (imei.isBlank() || model.isBlank() || customerName.isBlank()) {
                        showError = true
                        errorMessage = "Por favor complete los campos obligatorios (IMEI, Modelo, Cliente)."
                        return@Button
                    }
                    val balanceVal = remainingBalance.toDoubleOrNull() ?: 0.0
                    val totalInstVal = totalInstallments.toIntOrNull() ?: 1
                    val paidInstVal = paidInstallments.toIntOrNull() ?: 0

                    viewModel.addDevice(
                        imei = imei.trim(),
                        model = model.trim(),
                        customerName = customerName.trim(),
                        customerPhone = customerPhone.trim(),
                        status = status,
                        lockMessage = lockMessage.trim(),
                        lockPin = lockPin.trim(),
                        remainingBalance = balanceVal,
                        totalInstallments = totalInstVal,
                        paidInstallments = paidInstVal
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_device_button")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar y Registrar Dispositivo", fontSize = 16.sp)
            }
        }
    }
}
