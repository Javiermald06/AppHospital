package com.example.essalud.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.essalud.db.daos.CentroSaludDao
import com.example.essalud.db.daos.CitaDao
import com.example.essalud.db.daos.MedicoDao
import com.example.essalud.db.daos.PacienteDao
import com.example.essalud.db.entities.CentroSalud
import com.example.essalud.db.entities.Cita
import com.example.essalud.db.entities.Medico
import com.example.essalud.db.entities.Paciente

@Database(
    entities = [Paciente::class, CentroSalud::class, Medico::class, Cita::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pacienteDao(): PacienteDao
    abstract fun centroSaludDao(): CentroSaludDao
    abstract fun medicoDao(): MedicoDao
    abstract fun citaDao(): CitaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "essalud_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}