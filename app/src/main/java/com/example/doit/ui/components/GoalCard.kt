package com.example.doit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doit.Goal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Rappresentazione visiva di un singolo obiettivo nella lista.
 */
@Composable
fun GoalCard(
    goal: Goal, 
    onToggleClick: () -> Unit, 
    onDeleteClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    isFirst: Boolean,
    isLast: Boolean
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    // Colori dinamici in base allo stato di completamento
    val cardBg = if (goal.isCompleted) Color(0xFFF5F5F5) else Color(0xFF2D2D2D)
    val contentColor = if (goal.isCompleted) Color.Black else Color.White

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Frecce per spostare l'obiettivo nell'ordine della lista
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onMoveUp, enabled = !isFirst, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = contentColor.copy(alpha = if (isFirst) 0.1f else 0.5f))
                }
                IconButton(onClick = onMoveDown, enabled = !isLast, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = contentColor.copy(alpha = if (isLast) 0.1f else 0.5f))
                }
            }

            // Pulsante per segnare l'obiettivo come fatto/non fatto
            IconButton(onClick = onToggleClick) {
                Icon(
                    imageVector = if (goal.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (goal.isCompleted) Color(0xFF4CAF50) else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            // Titolo e dettagli dell'obiettivo (data di completamento o scadenza)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                Text(
                    text = goal.title,
                    textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else null,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    fontSize = 17.sp
                )
                
                if (goal.isCompleted && goal.completionDate != null) {
                    Text(
                        text = "Raggiunto il: ${goal.completionDate.format(dateFormatter)}",
                        fontSize = 11.sp,
                        color = contentColor.copy(alpha = 0.6f)
                    )
                } else if (!goal.isCompleted && goal.targetDate != null) {
                    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.targetDate.toLocalDate())
                    Text(
                        text = "Scadenza: ${goal.targetDate.format(dateFormatter)} ($daysLeft gg)",
                        fontSize = 11.sp,
                        color = Color(0xFFFFCC80)
                    )
                }
            }

            // Pulsante per eliminare l'obiettivo
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.DeleteOutline, 
                    contentDescription = null, 
                    tint = if (goal.isCompleted) Color.Red.copy(alpha = 0.6f) else Color(0xFFFF8A80)
                )
            }
        }
    }
}
