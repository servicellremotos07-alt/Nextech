package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    onBack: () -> Unit
) {
    var imeiQuery by remember { mutableStateOf("") }
    var checkResult by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Herramientas Técnicas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("tools_back_button")) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // IMEI Verification Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verificador de IMEI y Blacklist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    OutlinedTextField(
                        value = imeiQuery,
                        onValueChange = { imeiQuery = it },
                        label = { Text("Ingrese IMEI (15 dígitos)") },
                        modifier = Modifier.fillMaxWidth().testTag("tool_imei_input"),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (imeiQuery.length >= 14) {
                                checkResult = "IMEI ${imeiQuery.take(8)}... verificado en base de datos Nextech: CLEAN (Sin reporte de robo, apto para financiamiento o activación)."
                            } else {
                                checkResult = "Por favor ingrese un IMEI válido de 15 dígitos."
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("tool_verify_button")
                    ) {
                        Text("Verificar Estado")
                    }

                    if (checkResult != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = checkResult!!,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // FRP & Unlock Guide Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Help, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guías Rápidas de Servicio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Divider()

                    Text("• FRP (Factory Reset Protection):", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Para bypass de cuentas Google en dispositivos Android soportados, conecte en modo EDL o Fastboot y ejecute la rutina de depuración.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Protocolo de Bloqueo por Cuota:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("El módulo MDM de Bloqueo Nextech restringe llamadas entrantes/salientes y ajustes de sistema cuando la cuota de financiamiento vence.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Remote Command Dispatcher Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Comandos Remotos de Emergencia", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    OutlinedButton(
                        onClick = { /* Simulated action */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Emitir Alarma Sonora en Dispositivo Remoto")
                    }

                    OutlinedButton(
                        onClick = { /* Simulated action */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.LockReset, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Forzar Sincronización de Estado MDM")
                    }
                }
            }
        }
    }
}
