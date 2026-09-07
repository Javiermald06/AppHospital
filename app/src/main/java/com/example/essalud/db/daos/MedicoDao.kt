package com.example.essalud.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.essalud.db.entities.Medico

@Dao
interface MedicoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMedico(medico: Medico): Long

    @Query("SELECT * FROM tabla_medicos WHERE especialidad = :especialidad LIMIT 1")
    suspend fun obtenerMedicoPorEspecialidad(especialidad: String): Medico?

    @Query("SELECT * FROM tabla_medicos")
    suspend fun obtenerTodosLosMedicos(): List<Medico>
}