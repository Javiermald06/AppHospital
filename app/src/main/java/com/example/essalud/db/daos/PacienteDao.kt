package com.example.essalud.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.essalud.db.entities.Paciente

@Dao
interface PacienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPaciente(paciente: Paciente): Long

    @Query("SELECT * FROM tabla_pacientes WHERE id_paciente = :idPaciente")
    suspend fun obtenerPacientePorId(idPaciente: Int): Paciente?
}