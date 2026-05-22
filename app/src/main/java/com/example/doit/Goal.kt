package com.example.doit

import java.time.LocalDateTime

/**
 * Classe dati che rappresenta un obiettivo (Goal).
 * @param id Identificativo univoco dell'obiettivo, generato dal timestamp corrente.
 * @param title Il titolo o la descrizione dell'obiettivo.
 * @param isCompleted Indica se l'obiettivo è stato completato o meno.
 * @param completionDate La data e l'ora in cui l'obiettivo è stato completato (opzionale).
 * @param targetDate La data di scadenza prevista per l'obiettivo (opzionale).
 */
data class Goal(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val isCompleted: Boolean = false,
    val completionDate: LocalDateTime? = null,
    val targetDate: LocalDateTime? = null
)
