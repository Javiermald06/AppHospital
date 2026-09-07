package com.example.essalud.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tabla_medicos",
    foreignKeys = [
        ForeignKey(
            entity = CentroSalud::class,
            parentColumns = ["id_centro"],
            childColumns = ["id_centro"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_centro"])]
)
data class Medico(
    @PrimaryKey(autoGenerate = true)
    val id_medico: Int = 0,
    val nombre_medico: String,
    val especialidad: String,
    val id_centro: Int,
    val dia_atencion: String,
    val dni: String,
    val contrasena: String
)