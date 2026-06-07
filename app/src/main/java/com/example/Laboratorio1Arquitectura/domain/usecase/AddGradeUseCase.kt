package com.example.Laboratorio1Arquitectura.domain.usecase

import com.example.Laboratorio1Arquitectura.domain.repository.GradeRepository
import com.example.Laboratorio1Arquitectura.domain.model.AcademicGrade

class AddGradeUseCase (private val repository: GradeRepository){
    suspend operator fun invoke(
        activityName: String,
        subject: String,
        gradeValue:Double
    ){
        if(activityName.isBlank()){
            throw IllegalArgumentException("El nombre de la actividad no puede estar vacio")
        }

        if(subject.isBlank()){
            throw IllegalArgumentException("El nombre de la materia no puede estar vacio")
        }

        if(gradeValue < 0.0 || gradeValue > 10.0){
            throw IllegalArgumentException("La calificacion debe estar entre 0 y 10")
        }

        val newGrade = AcademicGrade(
            id = System.currentTimeMillis().toString(),
            activityName = activityName.trim(),
            subject = subject.trim(),
            grade = gradeValue
        )

        repository.addGrade(newGrade)


    }
}