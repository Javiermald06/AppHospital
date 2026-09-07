package com.example.essalud.db.entities
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
//porsiaca

@Entity(
    tableName = "tabla_citas",
    foreignKeys = [
        ForeignKey(
            entity = Paciente::class,
            parentColumns = ["id_paciente"],
            childColumns = ["id_paciente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Medico::class,
            parentColumns = ["id_medico"],
            childColumns = ["id_medico"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CentroSalud::class,
            parentColumns = ["id_centro"],
            childColumns = ["id_centro"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_paciente"]),
        Index(value = ["id_medico"]),
        Index(value = ["id_centro"])
    ]
)
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val id_cita: Int = 0,
    val id_paciente: Int,
    val id_medico: Int,
    val id_centro: Int,
    val fecha: String = "Pendiente",
    val hora: String = "Pendiente",
    val estado_cita: String = "EN_ESPERA", // "EN_ESPERA", "POR_CONFIRMAR", "ACEPTADA", "CANCELADA", "ATENDIDA"
    val fecha_creacion: Long = System.currentTimeMillis()
)