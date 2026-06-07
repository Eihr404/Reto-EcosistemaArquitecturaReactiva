package com.example.Laboratorio1Arquitectura.ui.presentation.grades

import com.example.Laboratorio1Arquitectura.domain.model.AcademicGrade

sealed class GradeUiState {
    data object Loading : GradeUiState()

    data class Success(
        val grades: List<AcademicGrade>,
        val average: Double
    ) : GradeUiState()

    data class Error(
        val message: String
    ) : GradeUiState()
}