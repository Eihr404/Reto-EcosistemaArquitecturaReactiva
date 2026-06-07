package com.example.Laboratorio1Arquitectura.data.repository

import com.example.Laboratorio1Arquitectura.domain.model.AcademicGrade
import com.example.Laboratorio1Arquitectura.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryGradeRepository: GradeRepository {
    private val grades= MutableStateFlow(
        listOf(
            AcademicGrade(
                id = "1",
                activityName = "Examen Parcial",
                subject = "Matematicas",
                grade = 8.5
            ),
            AcademicGrade(
                id = "2",
                activityName = "Tarea 1",
                subject = "Programacion",
                grade = 9.0
            )
        )
    )

    override fun getGrades(): Flow<List<AcademicGrade>>{
        return grades.asStateFlow()
    }

    override suspend fun addGrade(grade:AcademicGrade){
        grades.value = grades.value + grade
    }
}