package com.example.Laboratorio1Arquitectura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.Laboratorio1Arquitectura.data.repository.InMemoryTaskRepository
import com.example.Laboratorio1Arquitectura.domain.usecase.AddTaskUseCase
import com.example.Laboratorio1Arquitectura.domain.usecase.GetTasksUseCase
import com.example.Laboratorio1Arquitectura.ui.presentation.tasks.AcademicTaskApp
import com.example.Laboratorio1Arquitectura.ui.presentation.tasks.AcademicTaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // orquestación de dependencias de infraestructura y negocio
        val repository = InMemoryTaskRepository()
        val getTasksUseCase = GetTasksUseCase(repository)
        val addTaskUseCase = AddTaskUseCase(repository)

        // creación del ViewModel inyectando sus colaboradores requeridos
        val viewModel = AcademicTaskViewModel(getTasksUseCase, addTaskUseCase, repository)

        // vista declarativa limpia
        setContent {
            AcademicTaskApp(viewModel = viewModel)
        }
    }
}