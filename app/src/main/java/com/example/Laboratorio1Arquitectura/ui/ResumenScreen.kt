package com.example.Laboratorio1Arquitectura.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.Laboratorio1Arquitectura.ResumenRuta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    datos: ResumenRuta,
    onVolverInicio: () -> Unit // Función callback para aislar la lógica de navegación
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del Viaje") },
                navigationIcon = {
                    IconButton(onClick = onVolverInicio) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                // Aplicación estricta de Material You (Color Dinámico)
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )
        }
    ) { paddingValues ->
        Card(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Detalles de tu Ruta Ecológica",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Uso de plantillas de cadena idiomáticas de Kotlin
                Text(text = "🌍 Origen: ${datos.origen}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "📍 Destino: ${datos.destino}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "📏 Distancia Total: ${datos.distanciaKm} km", style = MaterialTheme.typography.bodyLarge)
                Text(text = "🍃 Huella de Carbono: ${datos.huellaCarbonoKg} kg CO2", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}