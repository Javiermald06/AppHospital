package com.example.essalud.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_especialidades")
data class Especialidad(
    @PrimaryKey(autoGenerate = true)
    val id_especialidad: Int = 0,
    val nombre_especialidad: String
)