package com.example.essalud.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.essalud.db.entities.Cita

@Dao
interface CitaDao {

    // 1. INSERTAR CITA (se crea con estado PENDIENTE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCita(cita: Cita): Long

    // 2. MOSTRAR TODAS LAS CITAS DE UN PACIENTE
    @Transaction
    @Query("SELECT * FROM tabla_citas WHERE id_paciente = :pacienteId ORDER BY id_cita DESC")
    suspend fun obtenerCitasPorPaciente(pacienteId: Int): List<CitaDetallada>

    // 3. MOSTRAR CITAS SEGÚN SU ESTADO (PENDIENTE, ATENDIDO, CANCELADO)
    @Transaction
    @Query("SELECT * FROM tabla_citas WHERE id_paciente = :pacienteId AND estado_cita = :estado ORDER BY id_cita DESC")
    suspend fun obtenerCitasPorEstado(pacienteId: Int, estado: String): List<CitaDetallada>

    // 4. CANCELAR CITA (acción del usuario)
    @Query("UPDATE tabla_citas SET estado_cita = 'CANCELADO' WHERE id_cita = :idCita")
    suspend fun cancelarCita(idCita: Int)

    // 5. MARCAR COMO ATENDIDO (cuando pase la fecha o se complete la cita)
    @Query("UPDATE tabla_citas SET estado_cita = 'ATENDIDO' WHERE id_cita = :idCita")
    suspend fun marcarComoAtendido(idCita: Int)
}