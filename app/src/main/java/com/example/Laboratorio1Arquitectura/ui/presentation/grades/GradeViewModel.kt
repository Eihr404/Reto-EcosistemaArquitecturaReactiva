package com.example.Laboratorio1Arquitectura.ui.presentation.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Laboratorio1Arquitectura.domain.usecase.AddGradeUseCase
import com.example.Laboratorio1Arquitectura.domain.usecase.GetGradesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GradeViewModel(
    private val getGradesUseCase: GetGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GradeUiState>(GradeUiState.Loading)
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    private var lastSuccessState: GradeUiState.Success? = null

    private val _currentScreen = MutableStateFlow(GradeScreen.List)
    val currentScreen: StateFlow<GradeScreen> = _currentScreen.asStateFlow()

    init {
        observeGrades()
    }

    private fun observeGrades() {
        viewModelScope.launch {
            getGradesUseCase().collect { grades ->

                val average = if (grades.isNotEmpty()) {
                    grades.map { it.grade }.average()
                } else {
                    0.0
                }

                val successState = GradeUiState.Success(
                    grades = grades,
                    average = average
                )

                lastSuccessState = successState
                _uiState.value = successState


            }
        }
    }

    fun showForm() {
        _currentScreen.value = GradeScreen.Form
    }

    fun showList() {
        if (_uiState.value is GradeUiState.Error) {
            lastSuccessState?.let { successState ->
                _uiState.value = successState
            }
        }

        _currentScreen.value = GradeScreen.List
    }

    fun saveGrade(
        activityName: String,
        subject: String,
        gradeText: String
    ) {
        viewModelScope.launch {
            try {
                val gradeValue = gradeText.toDoubleOrNull()
                    ?: throw IllegalArgumentException("La nota debe ser un número válido.")

                addGradeUseCase(
                    activityName = activityName,
                    subject = subject,
                    gradeValue = gradeValue
                )

                _currentScreen.value = GradeScreen.List

            } catch (error: IllegalArgumentException) {
                _uiState.value = GradeUiState.Error(
                    message = error.message ?: "Error al registrar la calificación."
                )
            }
        }
    }
}