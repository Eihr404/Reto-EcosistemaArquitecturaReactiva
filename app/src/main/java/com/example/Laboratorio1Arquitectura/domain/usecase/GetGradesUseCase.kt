package com.example.Laboratorio1Arquitectura.domain.usecase
import com.example.Laboratorio1Arquitectura.domain.repository.GradeRepository


class GetGradesUseCase (
    private val repository: GradeRepository
)
{
    operator fun invoke() = repository.getGrades()
}
