package com.example.doit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.doit.ui.GoalApp
import com.example.doit.ui.theme.DoItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Abilita il design edge-to-edge (opzionale, attualmente commentato)
        //enableEdgeToEdge()
        setContent {
            DoItTheme {
                // Punto di ingresso principale dell'applicazione, ora definito in ui/GoalApp.kt
                GoalApp()
            }
        }
    }
}
