package com.example.Laboratorio1Arquitectura.data
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.Laboratorio1Arquitectura.PreferenciaViaje
import com.example.Laboratorio1Arquitectura.TipoTransporte
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Instancia única (Singleton) de DataStore delegada al Contexto de la app
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ecotrip_preferencias")

// 2. Clase de gestión de preferencias
class PreferenciasRepositorio(private val dataStore: DataStore<Preferences>) {

    // Definición de las claves tipadas para guardar los datos en disco
    private object PreferencesKeys {
        val NOMBRE_VIAJERO = stringPreferencesKey("nombre_viajero")
        val TIPO_TRANSPORTE = stringPreferencesKey("tipo_transporte")
        val SOLO_BAJA_HUELLA = booleanPreferencesKey("solo_baja_huella")
    }

    // 3. Flujo (Flow) reactivo que lee los datos y reconstruye nuestra Data Class
    val preferenciasViajeFlow: Flow<PreferenciaViaje> = dataStore.data.map { preferences ->
        val nombre = preferences[PreferencesKeys.NOMBRE_VIAJERO] ?: ""

        // Recuperamos el transporte guardado como texto y lo convertimos a Enum de forma segura
        val transporteString = preferences[PreferencesKeys.TIPO_TRANSPORTE] ?: TipoTransporte.BICICLETA.name
        val transporte = try {
            TipoTransporte.valueOf(transporteString)
        } catch (e: IllegalArgumentException) {
            TipoTransporte.BICICLETA
        }

        val bajaHuella = preferences[PreferencesKeys.SOLO_BAJA_HUELLA] ?: false

        // Retornamos el objeto inmutable con las preferencias actuales
        PreferenciaViaje(nombre, transporte, bajaHuella)
    }

    // 4. Función suspendida para guardar asíncronamente los datos desde la UI/ViewModel
    suspend fun guardarPreferencias(preferencia: PreferenciaViaje) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOMBRE_VIAJERO] = preferencia.nombreViajero
            preferences[PreferencesKeys.TIPO_TRANSPORTE] = preferencia.tipoTransporte.name
            preferences[PreferencesKeys.SOLO_BAJA_HUELLA] = preferencia.soloBajaHuellaCarbono
        }
    }
}