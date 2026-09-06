package com.example.essalud.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_centros")
data class CentroSalud(
    @PrimaryKey(autoGenerate = true)
    val id_centro: Int = 0,
    val nombre_centro: String,
    val direccion: String
)