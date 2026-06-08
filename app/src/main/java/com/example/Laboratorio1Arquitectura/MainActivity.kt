package com.example.Laboratorio1Arquitectura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Laboratorio1Arquitectura.data.repository.InMemoryGradeRepository
import com.example.Laboratorio1Arquitectura.domain.usecase.AddGradeUseCase
import com.example.Laboratorio1Arquitectura.domain.usecase.GetGradesUseCase
import com.example.Laboratorio1Arquitectura.ui.presentation.grades.GradeTrackerApp
import com.example.Laboratorio1Arquitectura.ui.presentation.grades.GradeViewModel

class MainActivity : ComponentActivity() {

    private val gradeViewModel: GradeViewModel by viewModels {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = InMemoryGradeRepository()

                val getGradesUseCase = GetGradesUseCase(repository)
                val addGradeUseCase = AddGradeUseCase(repository)

                return GradeViewModel(
                    getGradesUseCase = getGradesUseCase,
                    addGradeUseCase = addGradeUseCase
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                GradeTrackerApp(
                    viewModel = gradeViewModel
                )
            }
        }
    }
}