package com.example.essalud.db.daos

import androidx.room.Embedded
import androidx.room.Relation
import com.example.essalud.db.entities.CentroSalud
import com.example.essalud.db.entities.Cita
import com.example.essalud.db.entities.Medico

data class CitaDetallada(
    @Embedded val cita: Cita,

    @Relation(
        parentColumn = "id_medico",
        entityColumn = "id_medico"
    )
    val medico: Medico,

    @Relation(
        parentColumn = "id_centro",
        entityColumn = "id_centro"
    )
    val centro: CentroSalud
)