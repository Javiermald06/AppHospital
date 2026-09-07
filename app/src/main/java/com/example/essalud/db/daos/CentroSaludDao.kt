package com.example.essalud.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.essalud.db.entities.CentroSalud

@Dao
interface CentroSaludDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCentro(centro: CentroSalud): Long

    @Query("SELECT * FROM tabla_centros")
    suspend fun obtenerTodosLosCentros(): List<CentroSalud>
}