package com.example.essalud.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_medicos")
data class Medico(
    @PrimaryKey(autoGenerate = true)
    val id_medico: Int = 0,
    val nombre_medico: String,
    val id_especialidad: Int, // Llave foránea (Foreign Key)
    val id_centro: Int        // Llave foránea (Foreign Key)
)