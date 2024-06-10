package com.JaimeMGR.maxmanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.JaimeMGR.maxmanga.Administrador.Login_Admin
import com.JaimeMGR.maxmanga.Administrador.Registrar_Admin
import com.JaimeMGR.maxmanga.Cliente.Registro_Cliente
import com.JaimeMGR.maxmanga.databinding.ActivityElegirRolBinding

class Elegir_rol : AppCompatActivity() {

    private lateinit var binding : ActivityElegirRolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnRolAdministrador.setOnClickListener{
            //Toast.makeText(applicationContext, "Rol administrador", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Elegir_rol, Login_Admin::class.java))
        }

        binding.BtnRolCliente.setOnClickListener{
            startActivity(Intent(this@Elegir_rol, Registro_Cliente::class.java))
        }
    }
}