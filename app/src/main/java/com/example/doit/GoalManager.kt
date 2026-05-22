package com.example.doit

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDateTime
import java.util.Collections

/**
 * Manager che gestisce la logica di business e la persistenza dei dati per gli obiettivi.
 */
class GoalManager(application: Application) : AndroidViewModel(application) {
    // StateFlow per gestire la lista degli obiettivi in modo reattivo
    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()
    
    // Nome del file per il salvataggio dei dati in formato JSON
    private val fileName = "goals_data.json"

    init {
        // Carica gli obiettivi salvati all'avvio
        loadGoals()
    }

    private fun getContext() = getApplication<Application>().applicationContext

    /**
     * Aggiunge un nuovo obiettivo alla lista.
     */
    fun addGoal(title: String, targetDate: LocalDateTime? = null) {
        val newGoal = Goal(id = System.nanoTime(), title = title, targetDate = targetDate)
        // Aggiunge l'obiettivo in cima alla lista
        _goals.value = listOf(newGoal) + _goals.value
        saveGoals()
    }

    /**
     * Cambia lo stato di completamento di un obiettivo.
     */
    fun toggleGoalCompletion(goal: Goal) {
        _goals.value = _goals.value.map {
            if (it.id == goal.id) {
                val isCompleted = !it.isCompleted
                // Se completato, imposta la data corrente, altrimenti la rimuove
                it.copy(
                    isCompleted = isCompleted,
                    completionDate = if (isCompleted) LocalDateTime.now() else null
                )
            } else it
        }
        saveGoals()
    }

    /**
     * Elimina un obiettivo dalla lista.
     */
    fun deleteGoal(goal: Goal) {
        _goals.value = _goals.value.filter { it.id != goal.id }
        saveGoals()
    }

    /**
     * Sposta un obiettivo su o giù nella lista per cambiarne l'ordine.
     */
    fun moveGoal(goal: Goal, direction: Int) {
        val currentList = _goals.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == goal.id }
        val newIndex = index + direction
        // Verifica che il nuovo indice sia valido prima di scambiare
        if (newIndex in currentList.indices) {
            Collections.swap(currentList, index, newIndex)
            _goals.value = currentList
            saveGoals()
        }
    }

    /**
     * Salva la lista corrente degli obiettivi in un file JSON.
     */
    private fun saveGoals() {
        try {
            val jsonArray = JSONArray()
            _goals.value.forEach { goal ->
                val jsonObject = JSONObject()
                jsonObject.put("id", goal.id)
                jsonObject.put("title", goal.title)
                jsonObject.put("isCompleted", goal.isCompleted)
                jsonObject.put("completionDate", goal.completionDate?.toString() ?: "null")
                jsonObject.put("targetDate", goal.targetDate?.toString() ?: "null")
                jsonArray.put(jsonObject)
            }
            getContext().openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(jsonArray.toString().toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Carica gli obiettivi dal file JSON salvato localmente.
     */
    private fun loadGoals() {
        try {
            val file = File(getContext().filesDir, fileName)
            if (!file.exists()) return

            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)
            val loadedGoals = mutableListOf<Goal>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                loadedGoals.add(Goal(
                    id = obj.getLong("id"),
                    title = obj.getString("title"),
                    isCompleted = obj.getBoolean("isCompleted"),
                    completionDate = if (obj.has("completionDate") && obj.getString("completionDate") != "null") 
                        LocalDateTime.parse(obj.getString("completionDate")) else null,
                    targetDate = if (obj.has("targetDate") && obj.getString("targetDate") != "null") 
                        LocalDateTime.parse(obj.getString("targetDate")) else null
                ))
            }
            _goals.value = loadedGoals
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
