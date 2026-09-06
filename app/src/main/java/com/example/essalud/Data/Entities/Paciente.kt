package com.example.essalud.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_pacientes")
data class Paciente (
    @PrimaryKey(autoGenerate = true)
    val id_paciente: Int = 0,
    val nombre: String,
    val dni: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)