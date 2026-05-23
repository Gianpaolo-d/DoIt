package com.example.doit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doit.Goal
import com.example.doit.GoalManager
import com.example.doit.ui.components.CelebrationDialog
import com.example.doit.ui.components.GoalCard
import com.example.doit.ui.components.NewGoalDialog

/**
 * Composable principale che gestisce l'interfaccia dell'applicazione.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalApp(viewModel: GoalManager = viewModel()) {
    // Osserva la lista degli obiettivi dal Manager
    val goals by viewModel.goals.collectAsState()
    
    // Stati per gestire la visibilità dei dialoghi e le azioni sugli obiettivi
    var showAddDialog by remember { mutableStateOf(false) }
    var goalToToggle by remember { mutableStateOf<Goal?>(null) }
    var goalToDelete by remember { mutableStateOf<Goal?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF121212), // Sfondo scuro per l'intera schermata
        topBar = {
            // Barra superiore con il titolo dell'app
            CenterAlignedTopAppBar(
                title = { 
                    Text("DOIT", fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = Color.White)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF121212)
                )
            )
        },
        floatingActionButton = {
            // Pulsante fluttuante per aggiungere un nuovo obiettivo
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Mostra un messaggio se non ci sono obiettivi, altrimenti mostra la lista
            if (goals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Ancora nessun obiettivo.\nClicca + per iniziare!",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            } else {
                // Lista a scorrimento verticale per gli obiettivi
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    itemsIndexed(goals, key = { _, goal -> goal.id }) { index, goal ->
                        GoalCard(
                            goal = goal,
                            onToggleClick = { goalToToggle = goal },
                            onDeleteClick = { goalToDelete = goal },
                            onMoveUp = { viewModel.moveGoal(goal, -1) },
                            onMoveDown = { viewModel.moveGoal(goal, 1) },
                            isFirst = index == 0,
                            isLast = index == goals.size - 1
                        )
                    }
                }
            }
        }

        // --- Gestione dei Dialoghi ---

        // Dialogo per la creazione di un nuovo obiettivo
        if (showAddDialog) {
            NewGoalDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, date ->
                    viewModel.addGoal(title, date)
                    showAddDialog = false
                }
            )
        }

        // Conferma per cambiare lo stato di completamento
        goalToToggle?.let { goal ->
            AlertDialog(
                onDismissRequest = { goalToToggle = null },
                title = { Text(if (goal.isCompleted) "Vuoi annullare?" else "E' stato completato?") },
                text = { Text("Vuoi cambiare lo stato di questo obiettivo?") },
                confirmButton = {
                    Button(onClick = {
                        val becomingCompleted = !goal.isCompleted
                        viewModel.toggleGoalCompletion(goal)
                        if (becomingCompleted) showCelebration = true
                        goalToToggle = null
                    }) { Text("Sì") }
                },
                dismissButton = { TextButton(onClick = { goalToToggle = null }) { Text("No") } }
            )
        }

        // Conferma per l'eliminazione di un obiettivo
        goalToDelete?.let { goal ->
            AlertDialog(
                onDismissRequest = { goalToDelete = null },
                title = { Text("Elimina") },
                text = { Text("Sei sicuro di voler eliminare '${goal.title}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteGoal(goal)
                            goalToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Elimina") }
                },
                dismissButton = { TextButton(onClick = { goalToDelete = null }) { Text("Annulla") } }
            )
        }

        // Dialogo di celebrazione al completamento di un obiettivo
        if (showCelebration) {
            CelebrationDialog(onDismiss = { showCelebration = false })
        }
    }
}
