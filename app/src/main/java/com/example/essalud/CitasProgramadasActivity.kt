package com.example.essalud

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.essalud.databinding.ActivityCitasProgramadasBinding
import com.example.essalud.db.AppDatabase
import com.example.essalud.db.daos.CitaDetallada
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CitasProgramadasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitasProgramadasBinding
    private lateinit var db: AppDatabase
    private val idPacienteJavier = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitasProgramadasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        binding.btnVolver.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        cargarCitas()
    }

    private fun cargarCitas() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Buscamos solo las que están en espera usando el DAO
            val lista = db.citaDao().obtenerCitasPorEstado(idPacienteJavier, "EN_ESPERA")

            withContext(Dispatchers.Main) {
                binding.contenedorCitas.removeAllViews()

                if (lista.isEmpty()) {
                    binding.txtSinCitas.visibility = View.VISIBLE
                } else {
                    binding.txtSinCitas.visibility = View.GONE
                    for (item in lista) {
                        agregarTarjetaCita(item)
                    }
                }
            }
        }
    }

    private fun agregarTarjetaCita(item: CitaDetallada) {
        // Inflamos el XML de la tarjeta
        val view = layoutInflater.inflate(R.layout.item_cita_programada, binding.contenedorCitas, false)

        // Conectamos las vistas
        val txtEspecialidadCabecera = view.findViewById<TextView>(R.id.txtEspecialidadCabecera)
        val txtEstado = view.findViewById<TextView>(R.id.txtEstado)
        val txtFechaHora = view.findViewById<TextView>(R.id.txtFechaHora)
        val txtCentro = view.findViewById<TextView>(R.id.txtCentro)
        val btnVerDetalles = view.findViewById<LinearLayout>(R.id.btnVerDetalles)

        // Llenamos con los datos del objeto CitaDetallada
        txtEspecialidadCabecera.text = "TIENES UNA CITA EN ${item.medico.especialidad.uppercase()}"
        txtEstado.text = item.cita.estado_cita
        txtFechaHora.text = "${item.cita.fecha} - ${item.cita.hora}"
        txtCentro.text = item.centro.nombre_centro

        // Acción del botón
        btnVerDetalles.setOnClickListener {
            Toast.makeText(this, "Doctor asignado: ${item.medico.nombre_medico}", Toast.LENGTH_SHORT).show()
        }

        // Agregamos la tarjeta al contenedor
        binding.contenedorCitas.addView(view)
    }
}