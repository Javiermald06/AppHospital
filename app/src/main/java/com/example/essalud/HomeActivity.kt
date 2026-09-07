package com.example.essalud
//ay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.essalud.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Usar binding directamente sin findViewById
        binding.cardAgendarCita.setOnClickListener {
            val intent = Intent(this, AgendarCitaActivity::class.java)
            startActivity(intent)
        }
    }
}