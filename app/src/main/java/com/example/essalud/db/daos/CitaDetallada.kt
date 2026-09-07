package com.example.essalud.db.daos
//ay
import androidx.room.Embedded
import androidx.room.Relation
import com.example.essalud.db.entities.CentroSalud
import com.example.essalud.db.entities.Cita
import com.example.essalud.db.entities.Medico
import com.example.essalud.db.entities.Paciente

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
    val centro: CentroSalud,

    @Relation(
        parentColumn = "id_paciente",
        entityColumn = "id_paciente"
    )
    val paciente: Paciente
)