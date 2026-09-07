package com.example.essalud

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.essalud.databinding.ActivityAtencionesRealizadasBinding
import com.example.essalud.db.AppDatabase
import com.example.essalud.db.daos.CitaDetallada
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AtencionesRealizadas : AppCompatActivity() {

    private lateinit var binding: ActivityAtencionesRealizadasBinding
    private val idPacienteJavier = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtencionesRealizadasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVolver.setOnClickListener { finish() }

        cargarAtenciones()
    }

    private fun cargarAtenciones() {
        val db = AppDatabase.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            val lista = db.citaDao().obtenerCitasPorEstado(idPacienteJavier, "ATENDIDO")

            withContext(Dispatchers.Main) {
                if (lista.isEmpty()) {
                    binding.txtSinAtenciones.visibility = View.VISIBLE
                } else {
                    binding.txtSinAtenciones.visibility = View.GONE
                    binding.contenedorCitas.removeAllViews()

                    for (item in lista) {
                        agregarTarjetaCita(item)
                    }
                }
            }
        }
    }

    private fun agregarTarjetaCita(item: CitaDetallada) {
        val card = MaterialCardView(this).apply {
            radius = 32f
            cardElevation = 4f
            setCardBackgroundColor(Color.WHITE)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 30)
            layoutParams = params
        }

        val layoutInterno = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        // Fila Especialidad y Estado
        val filaSuperior = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val txtEspecialidad = TextView(this).apply {
            text = item.medico.especialidad
            textSize = 18f
            setTextColor(Color.parseColor("#1E293B"))
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val txtEstado = TextView(this).apply {
            text = "  ${item.cita.estado_cita}  "
            textSize = 12f
            setTextColor(Color.parseColor("#166534"))
            setBackgroundColor(Color.parseColor("#DCFCE7"))
            typeface = Typeface.DEFAULT_BOLD
        }

        filaSuperior.addView(txtEspecialidad)
        filaSuperior.addView(txtEstado)

        // Médico
        val txtMedico = TextView(this).apply {
            text = item.medico.nombre_medico
            textSize = 15f
            setTextColor(Color.parseColor("#475569"))
            setPadding(0, 10, 0, 0)
        }

        // Centro
        val txtCentro = TextView(this).apply {
            text = item.centro.nombre_centro
            textSize = 13f
            setTextColor(Color.parseColor("#64748B"))
            setPadding(0, 4, 0, 0)
        }

        // Línea divisoria
        val linea = View(this).apply {
            val p = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            p.setMargins(0, 20, 0, 20)
            layoutParams = p
            setBackgroundColor(Color.parseColor("#F1F5F9"))
        }

        // Fecha y Hora
        val txtFechaHora = TextView(this).apply {
            text = "Atendido el: ${item.cita.fecha} — ${item.cita.hora}"
            textSize = 13f
            setTextColor(Color.parseColor("#007ED2"))
            typeface = Typeface.DEFAULT_BOLD
        }

        layoutInterno.addView(filaSuperior)
        layoutInterno.addView(txtMedico)
        layoutInterno.addView(txtCentro)
        layoutInterno.addView(linea)
        layoutInterno.addView(txtFechaHora)

        card.addView(layoutInterno)
        binding.contenedorCitas.addView(card)
    }
}