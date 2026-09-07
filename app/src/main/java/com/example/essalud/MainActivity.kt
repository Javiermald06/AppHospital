package com.example.essalud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.essalud.databinding.ActivityMainBinding
import com.example.essalud.db.AppDatabase
import com.example.essalud.db.entities.CentroSalud
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
        db.medicoDao().insertarMedico(
            Medico(id_medico = 1, nombre_medico = "Dr. Carlos Ramírez", especialidad = "Medicina General", id_centro = 1, dia_atencion = "LUNES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 2, nombre_medico = "Dra. Elena Ramos", especialidad = "Cardiología", id_centro = 1, dia_atencion = "MARTES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 3, nombre_medico = "Dr. Marco Véliz", especialidad = "Traumatología", id_centro = 1, dia_atencion = "MIÉRCOLES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 4, nombre_medico = "Dr. Luis Morales", especialidad = "Dermatología", id_centro = 1, dia_atencion = "VIERNES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 5, nombre_medico = "Dra. Sofía Castro", especialidad = "Pediatría", id_centro = 2, dia_atencion = "JUEVES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 6, nombre_medico = "Dra. Patricia Mendoza", especialidad = "Neurología", id_centro = 2, dia_atencion = "LUNES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 7, nombre_medico = "Dr. Fernando Quispe", especialidad = "Gastroenterología", id_centro = 1, dia_atencion = "MIÉRCOLES")
        )
        db.medicoDao().insertarMedico(
            Medico(id_medico = 8, nombre_medico = "Dra. Lucía Valdivia", especialidad = "Oftalmología", id_centro = 2, dia_atencion = "VIERNES")
        )
    }
}