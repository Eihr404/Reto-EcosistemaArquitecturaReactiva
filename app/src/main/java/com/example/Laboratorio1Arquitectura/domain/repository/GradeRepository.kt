package com.example.Laboratorio1Arquitectura.domain.repository
import com.example.Laboratorio1Arquitectura.domain.model.AcademicGrade
import kotlinx.coroutines.flow.Flow


interface GradeRepository{
    fun getGrades(): Flow<List<AcademicGrade>>

    suspend fun addGrade(grade:AcademicGrade)
}