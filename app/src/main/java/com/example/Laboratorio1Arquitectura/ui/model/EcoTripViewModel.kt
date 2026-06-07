package com.example.Laboratorio1Arquitectura.ui.model

import com.example.Laboratorio1Arquitectura.PreferenciaViaje

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.Laboratorio1Arquitectura.data.PreferenciasRepositorio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Llaves de almacenamiento para el SavedStateHandle
private const val DESTINO_KEY = "destino_viaje"
private const val DURACION_KEY = "duracion_dias"

class EcoTripViewModel(
    private val repositorio: PreferenciasRepositorio,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 1. Estado Permanente (DataStore)
    // Convertimos el Flow del repositorio en un StateFlow reactivo para Jetpack Compose
    val preferenciasFlow: StateFlow<PreferenciaViaje> = repositorio.preferenciasViajeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PreferenciaViaje() // Valor inicial mientras carga el disco
        )

    // 2. Estado Temporal Blindado (SavedStateHandle)
    // Enlazamos el destino y duración temporal para mitigar la muerte del proceso
    val destinoViaje: StateFlow<String> = savedStateHandle.getStateFlow(DESTINO_KEY, "")
    val duracionDias: StateFlow<String> = savedStateHandle.getStateFlow(DURACION_KEY, "")

    // 3. Eventos de UI (UDF: Eventos suben, Estados bajan)

    fun actualizarDestino(nuevoDestino: String) {
        savedStateHandle[DESTINO_KEY] = nuevoDestino
    }

    fun actualizarDuracion(nuevaDuracion: String) {
        // Mantenemos como String en el formulario para facilitar la escritura,
        // la conversión estricta se hará al navegar.
        savedStateHandle[DURACION_KEY] = nuevaDuracion
    }

    fun guardarPreferenciasGlobales(preferencia: PreferenciaViaje) {
        viewModelScope.launch {
            repositorio.guardarPreferencias(preferencia)
        }
    }

    // 4. Factory para instanciar el ViewModel
    // Permite inyectar el repositorio y el SavedStateHandle sin usar librerías complejas como Hilt
    companion object {
        fun provideFactory(
            repositorio: PreferenciasRepositorio
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Obtenemos el SavedStateHandle automáticamente de los extras de Compose
                val savedStateHandle = extras.createSavedStateHandle()
                return EcoTripViewModel(repositorio, savedStateHandle) as T
            }
        }
    }
}