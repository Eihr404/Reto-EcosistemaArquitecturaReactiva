package com.example.Laboratorio1Arquitectura

import kotlinx.serialization.Serializable

/**
 * Representa la pantalla del Formulario de Registro de Viaje.
 * Al ser un objeto, no transporta parámetros iniciales.
 */
@Serializable
object FormularioRuta

/**
 * Representa la pantalla de Resumen de Ruta.
 * Los parámetros declarados aquí definen estrictamente el contrato de datos
 * requerido para construir la UI de destino sin usar Bundles manuales.
 */
@Serializable
data class ResumenRuta(
    val origen: String,
    val destino: String,
    val distanciaKm: Double,
    val huellaCarbonoKg: Double
)

// Usamos Enum para el transporte, así garantizamos el Type - Safety en las opciones)

enum class TipoTransporte {
    TREN, BICICLETA, VEHICULO_ELECTRICO
}
/**
 * Data Class pura para el manejo de preferencias globales persistentes en disco.
 */
data class PreferenciaViaje(
    val nombreViajero: String = "",
    val tipoTransporte: TipoTransporte = TipoTransporte.BICICLETA,
    val soloBajaHuellaCarbono: Boolean = false
)