package com.example.essalud

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.essalud.databinding.ActivityAgendarCitaBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar


class AgendarCitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgendarCitaBinding

    // Variables para simular y retener las elecciones del usuario
    private var especialidadSeleccionada: String = "Medicina General"
    private var centroSeleccionado: String = "Hospital III Daniel Alcides Carrión"
    private var fechaSeleccionada: String = "15 de septiembre de 2026"
    private var horaSeleccionada: String = "10:30"
    private var medicoAsignado: String = "Dr. Carlos Ramírez"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendarCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listas de vistas para control visual
        val puntos = listOf(binding.punto1, binding.punto2, binding.punto3, binding.punto4)
        val lineas = listOf(binding.linea1, binding.linea2, binding.linea3)

        val cardsEspecialidad = listOf(
            binding.cardMedicinaGeneral,
            binding.cardCardiologia,
            binding.cardTraumatologia
        )

        val cardsCentro = listOf(
            binding.cardCentro1,
            binding.cardCentro2
        )

        val botonesHora = listOf(
            binding.btn0900,
            binding.btn0930,
            binding.btn1000,
            binding.btn1030,
            binding.btn1100,
            binding.btn1130
        )

        // Función para cambiar de paso y actualizar el indicador superior
        fun mostrarPaso(paso: Int) {
            binding.paso1.visibility = if (paso == 1) View.VISIBLE else View.GONE
            binding.paso2.visibility = if (paso == 2) View.VISIBLE else View.GONE
            binding.paso3.visibility = if (paso == 3) View.VISIBLE else View.GONE
            binding.paso4.visibility = if (paso == 4) View.VISIBLE else View.GONE

            // Círculos numerados
            puntos.forEachIndexed { index, textView ->
                val numPaso = index + 1
                if (numPaso <= paso) {
                    textView.setBackgroundResource(R.drawable.bg_step_active)
                    textView.setTextColor(Color.WHITE)
                } else {
                    textView.setBackgroundResource(R.drawable.bg_step_inactive)
                    textView.setTextColor(Color.parseColor("#64748B"))
                }
            }

            // Líneas conectoras
            lineas.forEachIndexed { index, view ->
                if (index < paso - 1) {
                    view.setBackgroundColor(Color.parseColor("#007ED2"))
                } else {
                    view.setBackgroundColor(Color.parseColor("#CBD5E1"))
                }
            }
        }

        // ==========================================
        // PASO 1: SELECCIÓN DE ESPECIALIDAD
        // ==========================================
        fun marcarEspecialidad(cardSeleccionada: MaterialCardView, especialidad: String, medico: String) {
            cardsEspecialidad.forEach { card ->
                card.strokeWidth = 0
                card.strokeColor = Color.TRANSPARENT
                card.setCardBackgroundColor(Color.WHITE)
            }
            cardSeleccionada.strokeWidth = 5
            cardSeleccionada.strokeColor = Color.parseColor("#007ED2")
            cardSeleccionada.setCardBackgroundColor(Color.parseColor("#EFF6FF"))

            especialidadSeleccionada = especialidad
            medicoAsignado = medico
        }

        binding.cardMedicinaGeneral.setOnClickListener {
            marcarEspecialidad(binding.cardMedicinaGeneral, "Medicina General", "Dr. Carlos Ramírez")
        }
        binding.cardCardiologia.setOnClickListener {
            marcarEspecialidad(binding.cardCardiologia, "Cardiología", "Dra. Elena Ramos")
        }
        binding.cardTraumatologia.setOnClickListener {
            marcarEspecialidad(binding.cardTraumatologia, "Traumatología", "Dr. Marco Véliz")
        }

        // Marcar la primera especialidad por defecto
        marcarEspecialidad(binding.cardMedicinaGeneral, "Medicina General", "Dr. Carlos Ramírez")

        binding.btnSiguiente1.setOnClickListener {
            mostrarPaso(2)
        }

        // ==========================================
        // PASO 2: SELECCIÓN DE CENTRO ASISTENCIAL
        // ==========================================
        fun marcarCentro(cardSeleccionada: MaterialCardView, centro: String) {
            cardsCentro.forEach { card ->
                card.strokeWidth = 0
                card.strokeColor = Color.TRANSPARENT
                card.setCardBackgroundColor(Color.WHITE)
            }
            cardSeleccionada.strokeWidth = 5
            cardSeleccionada.strokeColor = Color.parseColor("#007ED2")
            cardSeleccionada.setCardBackgroundColor(Color.parseColor("#EFF6FF"))

            centroSeleccionado = centro
        }

        binding.cardCentro1.setOnClickListener {
            marcarCentro(binding.cardCentro1, "Hospital III Daniel Alcides Carrión")
        }
        binding.cardCentro2.setOnClickListener {
            marcarCentro(binding.cardCentro2, "Centro Médico Metropolitano")
        }

        // Marcar el primer centro por defecto
        marcarCentro(binding.cardCentro1, "Hospital III Daniel Alcides Carrión")

        binding.btnAnterior2.setOnClickListener {
            mostrarPaso(1)
        }
        binding.btnSiguiente2.setOnClickListener {
            mostrarPaso(3)
        }

        // ==========================================
        // PASO 3: FECHA Y HORA
        // ==========================================
        binding.cardFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val selectorFecha = DatePickerDialog(
                this,
                { _, anioSelec, mesSelec, diaSelec ->
                    val nombresMeses = arrayOf(
                        "enero", "febrero", "marzo", "abril", "mayo", "junio",
                        "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
                    )
                    fechaSeleccionada = "$diaSelec de ${nombresMeses[mesSelec]} de $anioSelec"
                    binding.txtFecha.text = fechaSeleccionada
                },
                anio,
                mes,
                dia
            )
            selectorFecha.show()
        }

        fun marcarHora(botonSeleccionado: MaterialButton, hora: String) {
            botonesHora.forEach { boton ->
                boton.setBackgroundColor(Color.WHITE)
                boton.setTextColor(Color.parseColor("#007ED2"))
                boton.strokeColor = getColorStateList(android.R.color.holo_blue_dark)
            }
            botonSeleccionado.setBackgroundColor(Color.parseColor("#007ED2"))
            botonSeleccionado.setTextColor(Color.WHITE)

            horaSeleccionada = hora
        }

        botonesHora.forEach { boton ->
            boton.setOnClickListener {
                marcarHora(boton, boton.text.toString())
            }
        }

        // Marcar la hora predeterminada
        marcarHora(binding.btn1030, "10:30")

        binding.btnAnterior3.setOnClickListener {
            mostrarPaso(2)
        }

        // Volcar datos al resumen antes de pasar al paso 4
        binding.btnSiguiente3.setOnClickListener {
            binding.txtConfirmEspecialidad.text = especialidadSeleccionada
            binding.txtConfirmCentro.text = centroSeleccionado
            binding.txtConfirmFecha.text = fechaSeleccionada
            binding.txtConfirmHora.text = "$horaSeleccionada hrs"
            binding.txtConfirmMedico.text = medicoAsignado

            mostrarPaso(4)
        }

        // ==========================================
        // PASO 4: CONFIRMACIÓN
        // ==========================================
        binding.btnAnterior4.setOnClickListener {
            mostrarPaso(3)
        }

        binding.btnConfirmar.setOnClickListener {
            Toast.makeText(
                this,
                "¡Cita con $medicoAsignado reservada para el $fechaSeleccionada!",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

        // Botón superior de retorno
        binding.btnVolver.setOnClickListener {
            finish()
        }

        // Iniciar en el paso 1
        mostrarPaso(1)

    }
}