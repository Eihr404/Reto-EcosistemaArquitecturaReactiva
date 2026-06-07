package com.example.Laboratorio1Arquitectura.domain.usecase

import com.example.Laboratorio1Arquitectura.domain.model.AcademicTask
import com.example.Laboratorio1Arquitectura.domain.repository.AcademicTaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: AcademicTaskRepository) {
    operator fun invoke(): Flow<List<AcademicTask>> {
        return repository.getTasksStream()
    }
}