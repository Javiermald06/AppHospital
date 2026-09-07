package com.example.essalud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.essalud.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Botón para Agendar Cita
        binding.cardAgendarCita.setOnClickListener {
            startActivity(Intent(this, AgendarCitaActivity::class.java))
        }

        // 2. Botón para Citas Programadas (¡Este es el que faltaba!)
        binding.cardCitasProgramadas.setOnClickListener {
            startActivity(Intent(this, CitasProgramadasActivity::class.java))
        }

        // 3. Botón para Atenciones Realizadas
        binding.cardAtenciones.setOnClickListener {
            startActivity(Intent(this, AtencionesRealizadas::class.java))
        }
    }
}