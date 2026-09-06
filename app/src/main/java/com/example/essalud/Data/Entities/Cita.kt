package com.example.essalud.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_citas")
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val id_cita: Int = 0,
    val id_paciente: Int,     // Llave foránea
    val id_horario: Int,      // Al vincular el horario, ya sabemos Médico, Fecha y Hora
    val modalidad: String,    // "Presencial" o "Teleconsulta"
    val estado_cita: String,  // "Confirmada", "Atendida", "Cancelada"
    val fecha_creacion: Long  // Timestamp para saber cuándo hizo la reserva
)