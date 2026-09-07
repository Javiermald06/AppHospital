package com.example.essalud.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.essalud.db.entities.Cita

@Dao
interface CitaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCita(cita: Cita): Long

    // Citas activas del paciente
    @Transaction
    @Query("SELECT * FROM tabla_citas WHERE id_paciente = :pacienteId AND estado_cita IN ('EN_ESPERA', 'POR_CONFIRMAR', 'ACEPTADA') ORDER BY id_cita DESC")
    suspend fun obtenerCitasActivas(pacienteId: Int): List<CitaDetallada>

    // Citas completadas / pasadas
    @Transaction
    @Query("SELECT * FROM tabla_citas WHERE id_paciente = :pacienteId AND estado_cita = 'ATENDIDA' ORDER BY id_cita DESC")
    suspend fun obtenerAtencionesRealizadas(pacienteId: Int): List<CitaDetallada>

    // Respuestas del paciente
    @Query("UPDATE tabla_citas SET estado_cita = 'ACEPTADA' WHERE id_cita = :idCita")
    suspend fun aceptarCita(idCita: Int)

    @Query("UPDATE tabla_citas SET estado_cita = 'EN_ESPERA', fecha = 'Pendiente', hora = 'Pendiente' WHERE id_cita = :idCita")
    suspend fun rechazarCita(idCita: Int)

    @Query("UPDATE tabla_citas SET estado_cita = 'CANCELADA' WHERE id_cita = :idCita")
    suspend fun cancelarCita(idCita: Int)

    // Acciones del médico / admin
    @Transaction
    @Query("SELECT * FROM tabla_citas WHERE id_medico = :idMedico AND estado_cita = 'EN_ESPERA' ORDER BY fecha_creacion ASC")
    suspend fun obtenerCitasEnEsperaPorMedico(idMedico: Int): List<CitaDetallada>

    @Transaction
    @Query("SELECT * FROM tabla_citas WHERE estado_cita = 'EN_ESPERA' ORDER BY fecha_creacion ASC")
    suspend fun obtenerTodasCitasEnEspera(): List<CitaDetallada>

    @Query("UPDATE tabla_citas SET fecha = :fecha, hora = :hora, estado_cita = 'POR_CONFIRMAR' WHERE id_cita = :idCita")
    suspend fun asignarTurno(idCita: Int, fecha: String, hora: String)

    // Comprueba disponibilidad para evitar duplicados en los turnos
    @Query("SELECT COUNT(*) FROM tabla_citas WHERE id_medico = :idMedico AND fecha = :fecha AND hora = :hora AND estado_cita IN ('POR_CONFIRMAR', 'ACEPTADA')")
    suspend fun contarTurnosOcupados(idMedico: Int, fecha: String, hora: String): Int
}