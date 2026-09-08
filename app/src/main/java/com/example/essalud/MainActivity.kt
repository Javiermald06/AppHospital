package com.example.essalud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.essalud.databinding.ActivityMainBinding
import com.example.essalud.db.AppDatabase
import com.example.essalud.db.entities.CentroSalud
import com.example.essalud.db.entities.Cita
import com.example.essalud.db.entities.Medico
import com.example.essalud.db.entities.Paciente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa la base de datos y la llena si está vacía
        val db = AppDatabase.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            precargarDatosIniciales(db)
        }

        binding.btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, IntroVideoActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun precargarDatosIniciales(db: AppDatabase) {
        val totalCentros = db.centroSaludDao().obtenerTodosLosCentros().size
        if (totalCentros > 0) return // Si ya hay datos creados, no duplica

        // 1. Paciente base Javier con DNI y contraseña 12345678
        db.pacienteDao().insertarPaciente(
            Paciente(
                id_paciente = 1,
                nombre = "Javier",
                dni = "12345678",
                telefono = "952123456",
                correo = "javier@essalud.gob.pe",
                contrasena = "12345678"
            )
        )

        // 2. Centros de salud del formulario
        db.centroSaludDao().insertarCentro(
            CentroSalud(
                id_centro = 1,
                nombre_centro = "Hospital III Daniel Alcides Carrión",
                direccion = "Av. Bolognesi 123"
            )
        )
        db.centroSaludDao().insertarCentro(
            CentroSalud(
                id_centro = 2,
                nombre_centro = "Centro Médico Metropolitano",
                direccion = "Calle San Martín 456"
            )
        )

        // 3. Médicos con su especialidad y día de atención
        // SEDE 1: Hospital III Daniel Alcides Carrión (id_centro = 1)
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 1,
                nombre_medico = "Dr. Carlos Ramírez",
                especialidad = "Medicina General",
                id_centro = 1,
                dia_atencion = "LUNES",
                dni = "10000001",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 2,
                nombre_medico = "Dra. Elena Ramos",
                especialidad = "Cardiología",
                id_centro = 1,
                dia_atencion = "MARTES",
                dni = "10000002",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 3,
                nombre_medico = "Dr. Marco Véliz",
                especialidad = "Traumatología",
                id_centro = 1,
                dia_atencion = "MIÉRCOLES",
                dni = "10000003",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 4,
                nombre_medico = "Dr. Luis Morales",
                especialidad = "Dermatología",
                id_centro = 1,
                dia_atencion = "VIERNES",
                dni = "10000004",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 5,
                nombre_medico = "Dr. Andrés Gómez",
                especialidad = "Pediatría",
                id_centro = 1,
                dia_atencion = "JUEVES",
                dni = "10000005",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 6,
                nombre_medico = "Dra. Carmen Ríos",
                especialidad = "Neurología",
                id_centro = 1,
                dia_atencion = "LUNES",
                dni = "10000006",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 7,
                nombre_medico = "Dr. Fernando Quispe",
                especialidad = "Gastroenterología",
                id_centro = 1,
                dia_atencion = "MIÉRCOLES",
                dni = "10000007",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 8,
                nombre_medico = "Dr. Víctor Salazar",
                especialidad = "Oftalmología",
                id_centro = 1,
                dia_atencion = "VIERNES",
                dni = "10000008",
                contrasena = "medico123"
            )
        )

// --- SEDE 2: Centro Médico Metropolitano (id_centro = 2) ---
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 9,
                nombre_medico = "Dr. Raúl Paredes",
                especialidad = "Medicina General",
                id_centro = 2,
                dia_atencion = "LUNES",
                dni = "20000001",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 10,
                nombre_medico = "Dr. Hugo Benítez",
                especialidad = "Cardiología",
                id_centro = 2,
                dia_atencion = "MARTES",
                dni = "20000002",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 11,
                nombre_medico = "Dra. Claudia Fuentes",
                especialidad = "Traumatología",
                id_centro = 2,
                dia_atencion = "MIÉRCOLES",
                dni = "20000003",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 12,
                nombre_medico = "Dra. Natalia Vega",
                especialidad = "Dermatología",
                id_centro = 2,
                dia_atencion = "VIERNES",
                dni = "20000004",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 13,
                nombre_medico = "Dra. Sofía Castro",
                especialidad = "Pediatría",
                id_centro = 2,
                dia_atencion = "JUEVES",
                dni = "20000005",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 14,
                nombre_medico = "Dra. Patricia Mendoza",
                especialidad = "Neurología",
                id_centro = 2,
                dia_atencion = "LUNES",
                dni = "20000006",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 15,
                nombre_medico = "Dr. Manuel Delgado",
                especialidad = "Gastroenterología",
                id_centro = 2,
                dia_atencion = "MIÉRCOLES",
                dni = "20000007",
                contrasena = "medico123"
            )
        )
        db.medicoDao().insertarMedico(
            Medico(
                id_medico = 16,
                nombre_medico = "Dra. Lucía Valdivia",
                especialidad = "Oftalmología",
                id_centro = 2,
                dia_atencion = "VIERNES",
                dni = "20000008",
                contrasena = "medico123"
            )
        )// ==============================================================
        // 4. SIMULACIÓN: 2 CITAS YA ATENDIDAS PARA JAVIER (id_paciente = 1)
        // ==============================================================

        // Cita 1: Dr. Carlos Ramírez (Medicina General - Lunes)
        // Fecha simulada pasada: Lunes 24/08/2026
        db.citaDao().insertarCita(
            Cita(
                id_cita = 1,
                id_paciente = 1,
                id_medico = 1,
                id_centro = 1,
                fecha = "24/08/2026",
                hora = "09:00 AM",
                estado_cita = "ATENDIDO",
                fecha_creacion = "20/08/2026 10:15"
            )
        )

        // Cita 2: Dra. Elena Ramos (Cardiología - Martes)
        // Fecha simulada pasada: Martes 01/09/2026
        db.citaDao().insertarCita(
            Cita(
                id_cita = 2,
                id_paciente = 1,
                id_medico = 2,
                id_centro = 1,
                fecha = "01/09/2026",
                hora = "10:30 AM",
                estado_cita = "ATENDIDO",
                fecha_creacion = "28/08/2026 16:40"
            )
        )

        Log.d("SIMULACION", "2 Citas simuladas con estado ATENDIDO insertadas correctamente.")
    }
}