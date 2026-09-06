package com.example.essalud

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class IntroVideoActivity : AppCompatActivity() {

    private var yaNavego = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar directamente el XML sin depender de View Binding
        setContentView(R.layout.activity_intro_video)

        val videoIntro = findViewById<VideoView>(R.id.videoIntro)
        val btnSaltar = findViewById<MaterialButton>(R.id.btnSaltar)

        // Ruta al video en res/raw/i ntro.mp4
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.intro}")
        videoIntro.setVideoURI(videoUri)

        // Arranca al cargar
        videoIntro.setOnPreparedListener {
            videoIntro.start()
        }

        // Al terminar el video -> Home
        videoIntro.setOnCompletionListener {
            irAHome()
        }

        // En caso de error -> Home directo
        videoIntro.setOnErrorListener { _, _, _ ->
            irAHome()
            true
        }

        // Botón Omitir
        btnSaltar.setOnClickListener {
            irAHome()
        }
    }

    private fun irAHome() {
        if (!yaNavego) {
            yaNavego = true
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        findViewById<VideoView>(R.id.videoIntro)?.pause()
    }
}