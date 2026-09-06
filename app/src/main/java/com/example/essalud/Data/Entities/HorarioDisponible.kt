package com.example.essalud.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_horarios")
data class HorarioDisponible(
    @PrimaryKey(autoGenerate = true)
    val id_horario: Int = 0,
    val id_medico: Int,       // Llave foránea (Foreign Key)
    val fecha: String,        // Ej. "12/09/2026"
    val hora_inicio: String,  // Ej. "09:30"
    val hora_fin: String,     // Ej. "10:00"
    val estado_disponible: Boolean // true = Libre, false = Ocupado
)