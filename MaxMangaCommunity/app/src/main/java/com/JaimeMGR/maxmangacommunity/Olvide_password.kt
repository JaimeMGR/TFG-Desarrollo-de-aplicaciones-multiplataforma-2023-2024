package com.JaimeMGR.maxmangacommunity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class Olvide_password : AppCompatActivity() {

    private lateinit var L_Et_email : EditText
    private lateinit var Btn_enviar_correo : MaterialButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvide_password)
        InicializarVistas()

        Btn_enviar_correo.setOnClickListener {
            ValidarInformacion()
        }
    }


    private fun InicializarVistas(){
        L_Et_email = findViewById(R.id.L_Et_email)
        Btn_enviar_correo = findViewById(R.id.Btn_enviar_correo)
        firebaseAuth = FirebaseAuth.getInstance()

        //Configuramos el progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun ValidarInformacion() {
        //Obtener el email
        email = L_Et_email.text.toString().trim()
        if (email.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese su correo", Toast.LENGTH_SHORT).show()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(applicationContext, "Correo no válido", Toast.LENGTH_SHORT).show()
        }else{
            RecuperarPassword()
        }

    }

    private fun RecuperarPassword() {
        progressDialog.setMessage("Enviando instrucciones de reestablecimiento de contraseña al correo $email")
        progressDialog.show()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                progressDialog.dismiss()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
            }
    }

}