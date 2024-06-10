package com.JaimeMGR.maxmanga.Cliente

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.JaimeMGR.maxmanga.MainActivityCliente
import com.JaimeMGR.maxmanga.R
import com.JaimeMGR.maxmanga.databinding.ActivityLoginClienteBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login_Cliente : AppCompatActivity() {

    private lateinit var binding : ActivityLoginClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        //Regresar a la actividad anterior
        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnLoginAdmin.setOnClickListener {
            validaInformacion()
        }
    }





    private var email = ""
    private var password = ""
    private fun validaInformacion() {
        //Obtener las credenciales
        email = binding.EtEmailCl.text.toString().trim()
        password = binding.EtPasswordCl.text.toString().trim()

        //Validar
        if (email.isEmpty()){
            binding.EtEmailCl.error = "Ingrese correo"
            binding.EtEmailCl.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EtEmailCl.error = "Correo no válido"
            binding.EtEmailCl.requestFocus()
        }
        else if (password.isEmpty()){
            binding.EtPasswordCl.error = "Ingrese contraseña"
            binding.EtPasswordCl.requestFocus()
        }else{
            loginCliente()
        }
    }

    private fun loginCliente() {
        progressDialog.setMessage("Iniciando sesión")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this@Login_Cliente, MainActivityCliente::class.java))
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "No se pudo iniciar sesión debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}