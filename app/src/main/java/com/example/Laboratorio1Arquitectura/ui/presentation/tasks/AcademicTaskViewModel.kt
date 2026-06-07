package com.example.Laboratorio1Arquitectura.ui.presentation.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Laboratorio1Arquitectura.domain.repository.AcademicTaskRepository
import com.example.Laboratorio1Arquitectura.domain.usecase.AddTaskUseCase
import com.example.Laboratorio1Arquitectura.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AcademicTaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val repository: AcademicTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AcademicTaskUiState>(AcademicTaskUiState.Loading)
    val uiState: StateFlow<AcademicTaskUiState> = _uiState.asStateFlow()

    var currentScreen by mutableStateOf(ScreenType.LIST)
        private set

    var newTaskTitle by mutableStateOf("")
        private set

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            try {
                getTasksUseCase().collect { taskList ->
                    _uiState.value = AcademicTaskUiState.Success(taskList)
                }
            } catch (e: Exception) {
                _uiState.value = AcademicTaskUiState.Error("Error crítico al cargar el listado: ${e.localizedMessage}")
            }
        }
    }

    fun onTaskCheckedChange(taskId: String) {
        viewModelScope.launch {
            try {
                repository.toggleTaskCompletion(taskId)
            } catch (e: Exception) {
                _uiState.value = AcademicTaskUiState.Error("No se pudo actualizar el estado de la tarea.")
            }
        }
    }

    fun onNavigateToCreate() {
        newTaskTitle = ""
        //lista vacía temporalmente
        //muestra el formulario limpio
        _uiState.value = AcademicTaskUiState.Success(emptyList())
        currentScreen = ScreenType.CREATE
    }
    fun onNavigateToList() {
        currentScreen = ScreenType.LIST
        observeTasks() // Nos aseguramos de refrescar y limpiar estados de error previos
    }

    fun onTaskTitleChange(newTitle: String) {
        newTaskTitle = newTitle
    }

    fun onSaveTask() {
        if (newTaskTitle.isBlank()) return
        viewModelScope.launch {
            try {
                addTaskUseCase(newTaskTitle)
                newTaskTitle = ""
                currentScreen = ScreenType.LIST
            } catch (e: IllegalArgumentException) {
                _uiState.value = AcademicTaskUiState.Error(e.message ?: "Dato inválido")
            } catch (e: Exception) {
                _uiState.value = AcademicTaskUiState.Error("Error al guardar: ${e.localizedMessage}")
            }
        }
    }
}