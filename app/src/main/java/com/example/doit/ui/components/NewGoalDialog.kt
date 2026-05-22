package com.example.doit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Finestra di dialogo per l'inserimento di un nuovo obiettivo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGoalDialog(onDismiss: () -> Unit, onConfirm: (String, LocalDateTime?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(24.dp)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nuovo Obiettivo", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo di testo per il titolo
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Cosa vuoi raggiungere?") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Pulsante per aprire il selettore della data
                TextButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (selectedDate == null) "Aggiungi Scadenza" else "Scadenza: ${selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
                }

                // Selettore della data (Date Picker)
                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
                                }
                                showDatePicker = false
                            }) { Text("OK") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Pulsanti di azione per il dialogo
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Annulla") }
                    Button(
                        onClick = { if (title.isNotBlank()) onConfirm(title, selectedDate) },
                        enabled = title.isNotBlank()
                    ) { Text("Aggiungi") }
                }
            }
        }
    }
}
