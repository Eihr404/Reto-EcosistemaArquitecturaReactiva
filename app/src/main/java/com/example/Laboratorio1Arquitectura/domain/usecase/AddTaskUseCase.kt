package com.example.Laboratorio1Arquitectura.domain.usecase

import com.example.Laboratorio1Arquitectura.domain.repository.AcademicTaskRepository

class AddTaskUseCase(private val repository: AcademicTaskRepository) {
    suspend operator fun invoke(title: String) {
        val trimmedTitle = title.trim()

        // Validación base del laboratorio
        if (trimmedTitle.isBlank()) {
            throw IllegalArgumentException("El título de la tarea académica no puede estar vacío.")
        }

        // Validación obligatoria del Desafío Extra
        if (trimmedTitle.length < 5) {
            throw IllegalArgumentException("La regla de dominio exige un mínimo de 5 caracteres.")
        }

        repository.addTask(trimmedTitle)
    }
}