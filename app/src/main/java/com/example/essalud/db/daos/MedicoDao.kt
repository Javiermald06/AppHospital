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

    // Busca el médico que coincida con la especialidad y el centro seleccionado
    @Query("SELECT * FROM tabla_medicos WHERE especialidad = :especialidad AND id_centro = :idCentro LIMIT 1")
    suspend fun obtenerMedicoPorEspecialidadYCentro(especialidad: String, idCentro: Int): Medico?

    @Query("SELECT * FROM tabla_medicos WHERE especialidad = :especialidad LIMIT 1")
    suspend fun obtenerMedicoPorEspecialidad(especialidad: String): Medico?

    @Query("SELECT * FROM tabla_medicos")
    suspend fun obtenerTodosLosMedicos(): List<Medico>

    // Consulta para autenticación del médico
    @Query("SELECT * FROM tabla_medicos WHERE dni = :dni AND contrasena = :contrasena LIMIT 1")
    suspend fun loginMedico(dni: String, contrasena: String): Medico?
}