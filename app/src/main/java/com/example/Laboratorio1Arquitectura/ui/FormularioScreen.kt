package com.example.Laboratorio1Arquitectura.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.Laboratorio1Arquitectura.ResumenRuta
import com.example.Laboratorio1Arquitectura.TipoTransporte
import com.example.Laboratorio1Arquitectura.ui.model.EcoTripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    viewModel: EcoTripViewModel,
    onNavegarResumen: (ResumenRuta) -> Unit
) {
    // 1. Observación Reactiva de Estados (Flujo Unidireccional de Datos)
    val preferencias by viewModel.preferenciasFlow.collectAsState()
    val destino by viewModel.destinoViaje.collectAsState()
    val duracion by viewModel.duracionDias.collectAsState()

    // Estado local simple para mostrar mensajes de error si los campos están vacíos
    var errorMensaje by remember { mutableStateOf("") }

    // 2. Estructura Base Scaffold solicitada en el examen
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Viaje") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // 3. Validación estricta usando toIntOrNull()
                    val diasValidados = duracion.toIntOrNull()

                    if (preferencias.nombreViajero.isBlank() || destino.isBlank() || diasValidados == null || diasValidados <= 0) {
                        errorMensaje = "Por favor, completa todos los campos correctamente."
                    } else {
                        errorMensaje = ""

                        // Si todo es válido, construimos el objeto complejo y navegamos
                        // (La distancia y huella son simuladas aquí para cumplir el contrato de ResumenRuta)
                        val resumen = ResumenRuta(
                            origen = "Quito",
                            destino = destino,
                            distanciaKm = if (preferencias.tipoTransporte == TipoTransporte.TREN) 350.0 else 120.0,
                            huellaCarbonoKg = if (preferencias.soloBajaHuellaCarbono) 12.5 else 45.8
                        )
                        onNavegarResumen(resumen)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Ir al resumen"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CAMPOS PERSISTENTES EN DISCO (DataStore) ---
            OutlinedTextField(
                value = preferencias.nombreViajero,
                onValueChange = { nuevoNombre ->
                    viewModel.guardarPreferenciasGlobales(preferencias.copy(nombreViajero = nuevoNombre))
                },
                label = { Text("Nombre del Viajero") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- CAMPOS TEMPORALES BLINDADOS (SavedStateHandle) ---
            OutlinedTextField(
                value = destino,
                onValueChange = { viewModel.actualizarDestino(it) },
                label = { Text("Destino del Viaje") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = duracion,
                onValueChange = { viewModel.actualizarDuracion(it) },
                label = { Text("Duración (días)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- COMPONENTES DE SELECCIÓN ---
            Text("Medio de Transporte", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TipoTransporte.entries.forEach { tipo ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = preferencias.tipoTransporte == tipo,
                            onClick = {
                                viewModel.guardarPreferenciasGlobales(preferencias.copy(tipoTransporte = tipo))
                            }
                        )
                        // Capitalizamos el texto del Enum para mejor estética
                        Text(tipo.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            // --- INTERRUPTOR DE HUELLA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Exclusivo baja huella de carbono")
                Switch(
                    checked = preferencias.soloBajaHuellaCarbono,
                    onCheckedChange = { nuevaPreferencia ->
                        viewModel.guardarPreferenciasGlobales(preferencias.copy(soloBajaHuellaCarbono = nuevaPreferencia))
                    }
                )
            }

            if (errorMensaje.isNotEmpty()) {
                Text(text = errorMensaje, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}