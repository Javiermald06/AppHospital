package com.example.essalud

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.essalud.databinding.ActivityAgendarCitaBinding
import com.example.essalud.db.AppDatabase
import com.example.essalud.db.entities.Cita
import com.example.essalud.db.entities.Medico
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgendarCitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgendarCitaBinding
    private lateinit var db: AppDatabase

    private var especialidadSeleccionada: String = ""
    private var diaAtencionSeleccionado: String = ""
    private var centroSeleccionadoId: Int = 1
    private var centroSeleccionadoNombre: String = "Hospital III Daniel Alcides Carrión"
    private var medicoAsignado: Medico? = null

    // ID del paciente Javier (precargado en MainActivity)
    private val idPacienteJavier: Int = 1

    private data class OpcionEspecialidad(val especialidad: String, val dia: String) {
        override fun toString(): String = "$especialidad — (Disponible: $dia)"
    }

    private val listaOpciones = listOf(
        OpcionEspecialidad("Medicina General", "LUNES"),
        OpcionEspecialidad("Neurología", "LUNES"),
        OpcionEspecialidad("Cardiología", "MARTES"),
        OpcionEspecialidad("Traumatología", "MIÉRCOLES"),
        OpcionEspecialidad("Gastroenterología", "MIÉRCOLES"),
        OpcionEspecialidad("Pediatría", "JUEVES"),
        OpcionEspecialidad("Dermatología", "VIERNES"),
        OpcionEspecialidad("Oftalmología", "VIERNES")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendarCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        configurarIndicadorPasos()
        configurarDesplegableUnico()
        configurarSeleccionCentro()
        configurarBotonesNavegacionYConfirmacion()

        mostrarPaso(1)
    }

    private fun mostrarPaso(paso: Int) {
        binding.paso1.visibility = if (paso == 1) View.VISIBLE else View.GONE
        binding.paso2.visibility = if (paso == 2) View.VISIBLE else View.GONE
        binding.paso3.visibility = if (paso == 3) View.VISIBLE else View.GONE

        val puntos = listOf(binding.punto1, binding.punto2, binding.punto3)
        val lineas = listOf(binding.linea1, binding.linea2)

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

        lineas.forEachIndexed { index, view ->
            if (index < paso - 1) {
                view.setBackgroundColor(Color.parseColor("#007ED2"))
            } else {
                view.setBackgroundColor(Color.parseColor("#CBD5E1"))
            }
        }
    }

    private fun configurarIndicadorPasos() {
        binding.btnVolver.setOnClickListener { finish() }
    }

    private fun configurarDesplegableUnico() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listaOpciones
        )
        binding.dropdownEspecialidad.setAdapter(adapter)

        binding.dropdownEspecialidad.setOnItemClickListener { parent, _, position, _ ->
            val seleccion = parent.getItemAtPosition(position) as OpcionEspecialidad
            especialidadSeleccionada = seleccion.especialidad.trim()
            diaAtencionSeleccionado = seleccion.dia.trim()
        }
    }

    private fun configurarSeleccionCentro() {
        fun marcarCentro(cardSeleccionada: MaterialCardView, idCentro: Int, nombreCentro: String) {
            val cards = listOf(binding.cardCentro1, binding.cardCentro2)
            cards.forEach { card ->
                card.strokeWidth = 0
                card.strokeColor = Color.TRANSPARENT
                card.setCardBackgroundColor(Color.WHITE)
            }
            cardSeleccionada.strokeWidth = 5
            cardSeleccionada.strokeColor = Color.parseColor("#007ED2")
            cardSeleccionada.setCardBackgroundColor(Color.parseColor("#EFF6FF"))

            centroSeleccionadoId = idCentro
            centroSeleccionadoNombre = nombreCentro
        }

        binding.cardCentro1.setOnClickListener {
            marcarCentro(binding.cardCentro1, 1, "Hospital III Daniel Alcides Carrión")
        }
        binding.cardCentro2.setOnClickListener {
            marcarCentro(binding.cardCentro2, 2, "Centro Médico Metropolitano")
        }

        marcarCentro(binding.cardCentro1, 1, "Hospital III Daniel Alcides Carrión")
    }

    private fun configurarBotonesNavegacionYConfirmacion() {
        // Paso 1 -> Paso 2
        binding.btnSiguiente1.setOnClickListener {
            if (especialidadSeleccionada.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona una especialidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mostrarPaso(2)
        }

        // Paso 2 -> Paso 1
        binding.btnAnterior2.setOnClickListener {
            mostrarPaso(1)
        }

        // Paso 2 -> Paso 3
        binding.btnSiguiente2.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val medico = db.medicoDao().obtenerMedicoPorEspecialidadYCentro(
                    especialidadSeleccionada,
                    centroSeleccionadoId
                )

                withContext(Dispatchers.Main) {
                    medicoAsignado = medico
                    binding.txtConfirmEspecialidad.text = especialidadSeleccionada
                    binding.txtConfirmDiaAtencion.text = "Día de atención: $diaAtencionSeleccionado"
                    binding.txtConfirmCentro.text = centroSeleccionadoNombre

                    if (medico != null) {
                        binding.txtConfirmMedico.text = "${medico.nombre_medico} (${medico.especialidad})"
                        binding.btnConfirmar.isEnabled = true
                    } else {
                        binding.txtConfirmMedico.text = "No hay médico asignado para esta sede"
                        binding.btnConfirmar.isEnabled = false
                        Toast.makeText(this@AgendarCitaActivity, "No se encontró médico disponible", Toast.LENGTH_SHORT).show()
                    }

                    mostrarPaso(3)
                }
            }
        }

        // Paso 3 -> Paso 2
        binding.btnAnterior3.setOnClickListener {
            mostrarPaso(2)
        }

        // Confirmar y Guardar en SQLite
        binding.btnConfirmar.setOnClickListener {
            val medico = medicoAsignado
            if (medico == null) {
                Toast.makeText(this, "Error: No hay médico asignado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnConfirmar.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

                    val nuevaCita = Cita(
                        id_cita = 0, // 0 para que Room y SQLite apliquen autoGenerate
                        id_paciente = idPacienteJavier,
                        id_medico = medico.id_medico,
                        id_centro = centroSeleccionadoId,
                        fecha = "Pendiente",
                        hora = "Pendiente",
                        estado_cita = "EN_ESPERA",
                        fecha_creacion = fechaFormateada
                    )

                    val idInsertado = db.citaDao().insertarCita(nuevaCita)
                    Log.d("ROOM_CITA", "ID Cita generada exitosamente: $idInsertado")

                    withContext(Dispatchers.Main) {
                        if (idInsertado > 0) {
                            AlertDialog.Builder(this@AgendarCitaActivity)
                                .setTitle("¡Cita Solicitada!")
                                .setMessage("Tu cita se registró con éxito en la base de datos (ID: #$idInsertado) para ${medico.nombre_medico}. Su estado es EN ESPERA.")
                                .setPositiveButton("Aceptar") { _, _ -> finish() }
                                .setCancelable(false)
                                .show()
                        } else {
                            binding.btnConfirmar.isEnabled = true
                            Toast.makeText(this@AgendarCitaActivity, "No se pudo insertar la cita en la base de datos.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ROOM_CITA", "Error al insertar cita", e)
                    withContext(Dispatchers.Main) {
                        binding.btnConfirmar.isEnabled = true
                        AlertDialog.Builder(this@AgendarCitaActivity)
                            .setTitle("Error de SQLite")
                            .setMessage("Detalle: ${e.localizedMessage}")
                            .setPositiveButton("Cerrar", null)
                            .show()
                    }
                }
            }
        }
    }
}