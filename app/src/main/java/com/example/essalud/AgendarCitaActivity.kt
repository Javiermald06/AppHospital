package com.example.essalud
//ay

import android.graphics.Color       //para el cambio de color
import android.os.Bundle            //iniciar interfaz
import android.util.Log             //para mostrar mensaje temporal
import android.view.View                //manejar los wiews del activity
import android.widget.ArrayAdapter         //para modificar el layout y adaptarlo
import android.widget.Toast                 //mensaje corto
import androidx.appcompat.app.AlertDialog   //para mensajes de alerta
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope        //ejecutar en segundo plano
import com.example.essalud.databinding.ActivityAgendarCitaBinding     //conexion con activity
import com.example.essalud.db.AppDatabase       //conexion al SQLite mediante ROOM
import com.example.essalud.db.daos.EspecialidadDiaDto       //conexion con una interfaz Dao
import com.example.essalud.db.entities.Cita        //conexion a la tabla de cita
import com.example.essalud.db.entities.Medico       //conexion a la tabla de medico
import com.google.android.material.card.MaterialCardView        //contenedor de formas
import kotlinx.coroutines.Dispatchers       //para que no se cuelgue caundo se haga varias ejecuciones en segundo plano
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat   //formato fecha simple
import java.util.Date           //fecha
import java.util.Locale         //del lugar

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

    //crea la interfaz db prepara la conexion a SQLite por ROOM y se alistan 4 eventos e iniciamos con mostrar paso 1 del layot
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

    //esta funcion cumple con mostrar las secciones divididas pro scrooll desde el activity

    private fun mostrarPaso(paso: Int) {
        binding.paso1.visibility = if (paso == 1) View.VISIBLE else View.GONE
        binding.paso2.visibility = if (paso == 2) View.VISIBLE else View.GONE
        binding.paso3.visibility = if (paso == 3) View.VISIBLE else View.GONE

        val puntos = listOf(binding.punto1, binding.punto2, binding.punto3)
        val lineas = listOf(binding.linea1, binding.linea2)

        //estos forEachIndexed ejecuta a cada uno del contenido de puntos y lineas
        puntos.forEachIndexed { index, textView ->
            val numPaso = index + 1
            //numPaso = 1      la variable paso va por la funcion mostrarPaso como iniciamos con 1
            // cambiara la seccion de color del texto y con bg step active se le asgina el color que esta en al carpeta drawable
            if (numPaso <= paso) {
                textView.setBackgroundResource(R.drawable.bg_step_active)
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setBackgroundResource(R.drawable.bg_step_inactive)
                textView.setTextColor(Color.parseColor("#64748B"))
            }
        }
        //en este caso es el mismo que arriba
        lineas.forEachIndexed { index, view ->
            if (index < paso - 1) {
                view.setBackgroundColor(Color.parseColor("#007ED2"))
            } else {
                view.setBackgroundColor(Color.parseColor("#CBD5E1"))
            }
        }
    }

    //esta funcion tendra el objetivo de volver al activity anterior
    private fun configurarIndicadorPasos() {
        binding.btnVolver.setOnClickListener { finish() }
    }

    // Consulta la base de datos en segundo plano y enlaza al desplegable
    private fun configurarDesplegableUnico() {
        //ejecutara todo esto en segundo plano todo lo que este een lifeccleScope
        lifecycleScope.launch(Dispatchers.IO) {
            // esta variable obtendra la consulta que necesita edel MedicoDao para obtener la especialidad y dia
            val listaOpcionesDb = db.medicoDao().obtenerEspecialidadesConDia()

            //Esta seccion actua como un permiso para adaptar la interfaze del activit
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(
                    //se ejecutara en este mismo activity y se colocara un deño predeterminado de android
                    this@AgendarCitaActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    //donde almacenara cada elemento dentro de la lista de opciones que obtuvimso de la consulta
                    listaOpcionesDb
                )
                binding.dropdownEspecialidad.setAdapter(adapter)
                //aca le decimos que lo pocisione justo donde esta este identificador
                //posteriormente la variable seleccion utilizara el dataclass de MedicoDao para almacenar especialidadseleccionada
                //y dia atencion seleccionado, pero primero limpiamos los datos,
                binding.dropdownEspecialidad.setOnItemClickListener { parent, _, position, _ ->
                    val seleccion = parent.getItemAtPosition(position) as EspecialidadDiaDto
                    especialidadSeleccionada = seleccion.especialidad.trim()
                    diaAtencionSeleccionado = seleccion.dia_atencion.trim()
                }
            }
        }
    }
    //en esta funcion configuramos ejecutamos otra ademas de la que ya tiene,
    private fun configurarSeleccionCentro() {
        //mmarcarCentro solo actuara para cambiar de color o diseño al momento de interactuar con el paso 2, como son el el diseño del boton
        //cambio pequeño del fondo, etc
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

            //por ultimo al mismo tiempo marcarCentro toma los datas que se selecciono su id y su nombre
            centroSeleccionadoId = idCentro
            centroSeleccionadoNombre = nombreCentro
        }
        //por ultimo fuera de esa funcion marcarCentro agarra todo el contenido de la primera carta el texto  Hospital III Daniel Alcides Carrión"

        binding.cardCentro1.setOnClickListener {
            //aqui se obtiene el id y el nombre que solicita la anterior funcion
            marcarCentro(binding.cardCentro1, 1, "Hospital III Daniel Alcides Carrión")
        }

        binding.cardCentro2.setOnClickListener {
            marcarCentro(binding.cardCentro2, 2, "Centro Médico Metropolitano")
        }
        //por defecto estara marcado la primera opcion
        marcarCentro(binding.cardCentro1, 1, "Hospital III Daniel Alcides Carrión")
    }

    private fun configurarBotonesNavegacionYConfirmacion() {
        // Paso 1 -> Paso 2
        //en esta funcion recien le decimos que avance al siguiente paso pero si esta vacio saldra un mensaje con Toast
        binding.btnSiguiente1.setOnClickListener {
            if (especialidadSeleccionada.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona una especialidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mostrarPaso(2)
        }
        //este boton sera para volver a la vista anterior
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
                // en esta funcion se realiza todo esto en segundo plano, lo primero que hace es que crea la variable medio
                // para saber el medico que tenga tanto su especialidad como centro id

                //por ultimo le damos permisos para que vuelva a interactuar con el activity del layouy
                withContext(Dispatchers.Main) {
                    //aca le decimos que el medico asignado sera el mismo que cumpla con los requisitos anteriores
                    medicoAsignado = medico
                    //luego modificara las siguientes lineas de texto por un dato real que pertenesca ala base de datos o que esten guardados en al variable
                    binding.txtConfirmEspecialidad.text = especialidadSeleccionada
                    binding.txtConfirmDiaAtencion.text = "Día de atención: $diaAtencionSeleccionado"
                    binding.txtConfirmCentro.text = centroSeleccionadoNombre

                    //Por ultimo si la variable de medico no esta vacio
                    if (medico != null) {
                        //esto hara que se coloque el siguiente texto en ese identificador
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
                        id_cita = 0,
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