package com.JaimeMGR.maxmangacommunity.Perfil

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.JaimeMGR.maxmangacommunity.Modelo.Usuario
import com.JaimeMGR.maxmangacommunity.R
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PerfilActivity : AppCompatActivity() {

    private lateinit var P_imagen: ImageView
    private lateinit var P_n_usuario: TextView
    private lateinit var P_email: TextView
    private lateinit var P_proveedor: TextView
    private lateinit var P_nombres: EditText
    private lateinit var P_apellidos: EditText
    private lateinit var P_profesion: EditText
    private lateinit var P_domicilio: EditText
    private lateinit var P_edad: EditText
    private lateinit var P_telefono: TextView
    private lateinit var Btn_guardar: Button
    private lateinit var Editar_imagen: ImageView
    private lateinit var Editar_Telefono: ImageView

    private lateinit var Btn_verificar: MaterialButton

    var user: FirebaseUser? = null
    var reference: DatabaseReference? = null

    private var codigoTel = ""
    private var numeroTel = ""
    private var codigo_numero_Tel = ""

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        InicializarVariables()
        ObtenerDatos()
        EstadoCuenta()

        Btn_guardar.setOnClickListener {
            ActualizarInformacion()
        }

        Editar_imagen.setOnClickListener {
            val intent = Intent(applicationContext, EditarImagenPerfil::class.java)
            startActivity(intent)
        }

        Editar_Telefono.setOnClickListener {
            EstablecerNumTel()
        }

        Btn_verificar.setOnClickListener {
            if (user!!.isEmailVerified) {
                //Usuario verificado
                //Toast.makeText(applicationContext, "Usuario verificado", Toast.LENGTH_SHORT).show()
                CuentaVerificada()
            } else {
                //Usuario no está verificado
                ConfirmarEnvio()
            }
        }

    }

    private fun ConfirmarEnvio() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Verificar cuenta")
            .setMessage("¿Estás seguro(a) de enviar instrucciones de verificación a su correo electrónico? ${user!!.email}")
            .setPositiveButton("Enviar") { d, e ->
                EnviarEmailConfirmacion()

            }
            .setNegativeButton("Cancelar") { d, e ->
                d.dismiss()
            }
            .show()
    }

    private fun EnviarEmailConfirmacion() {
        progressDialog.setMessage("Enviando instrucciones de verificación a su correo electrónico ${user!!.email}")
        progressDialog.show()

        user!!.sendEmailVerification()
            .addOnSuccessListener {
                //Envio fue exitoso
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Instrucciones enviadas, revise la bandeja de su correo ${user!!.email}",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->
                //Envio no fue exitoso
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "La operación falló debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun EstadoCuenta() {
        if (user!!.isEmailVerified) {
            Btn_verificar.text = "Verificado"
        } else {
            Btn_verificar.text = "No verificado"
        }
    }

    private fun EstablecerNumTel() {

        /*Declarar las vistas del CD*/
        val Establecer_Telefono: EditText
        //val SelectorCodigoPais : CountryCodePicker
        val Btn_aceptar_Telefono: MaterialButton

        val dialog = Dialog(this@PerfilActivity)

        /*Realizar la conexión con el diseño*/
        dialog.setContentView(R.layout.cuadro_d_establecer_tel)

        /*Inicializar las vistas*/
        Establecer_Telefono = dialog.findViewById(R.id.Establecer_Telefono)
        //SelectorCodigoPais = dialog.findViewById(R.id.SelectorCodigoPais)
        Btn_aceptar_Telefono = dialog.findViewById(R.id.Btn_aceptar_Telefono)

        /*Asignar un evento al botón*/
        Btn_aceptar_Telefono.setOnClickListener {
            //codigoTel = SelectorCodigoPais.selectedCountryCodeWithPlus
            numeroTel = Establecer_Telefono.text.toString().trim()
            codigo_numero_Tel = codigoTel + numeroTel
            if (numeroTel.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Ingrese un número telefónico",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            } else {
                P_telefono.text = codigo_numero_Tel
                dialog.dismiss()
            }
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)


    }

    private fun InicializarVariables() {
        P_imagen = findViewById(R.id.P_imagen)
        P_n_usuario = findViewById(R.id.P_n_usuario)
        P_proveedor = findViewById(R.id.P_proveedor)
        P_email = findViewById(R.id.P_email)
        P_nombres = findViewById(R.id.P_nombres)
        P_apellidos = findViewById(R.id.P_apellidos)
        P_profesion = findViewById(R.id.P_profesion)
        P_domicilio = findViewById(R.id.P_domicilio)
        P_edad = findViewById(R.id.P_edad)
        P_telefono = findViewById(R.id.P_telefono)
        Btn_guardar = findViewById(R.id.Btn_Guardar)
        Editar_imagen = findViewById(R.id.Editar_imagen)
        Editar_Telefono = findViewById(R.id.Editar_Telefono)
        Btn_verificar = findViewById(R.id.Btn_verificar)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(user!!.uid)

    }

    private fun ObtenerDatos() {
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //Obtenemos los datos de Firebase
                    val usuario: Usuario? = snapshot.getValue(Usuario::class.java)
                    val str_n_usuario = usuario!!.getN_Usuario()
                    val str_email = usuario.getEmail()
                    val str_proveedor = usuario.getProveedor()
                    val str_nombres = usuario.getNombres()
                    val str_apellidos = usuario.getApellidos()
                    val str_profesion = usuario.getProfesion()
                    val str_domicilio = usuario.getDomicilio()
                    val str_edad = usuario.getEdad()
                    val str_telefono = usuario.getTelefono()

                    //Seteamos la información en las vistas
                    P_n_usuario.text = str_n_usuario
                    P_email.text = str_email
                    P_proveedor.text = str_proveedor
                    P_nombres.setText(str_nombres)
                    P_apellidos.setText(str_apellidos)
                    P_profesion.setText(str_profesion)
                    P_domicilio.setText(str_domicilio)
                    P_edad.setText(str_edad)
                    P_telefono.setText(str_telefono)
                    Glide.with(applicationContext).load(usuario.getImagen())
                        .placeholder(R.drawable.ic_item_usuario).into(P_imagen)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun ActualizarInformacion() {
        val str_nombres = P_nombres.text.toString()
        val str_apellidos = P_apellidos.text.toString()
        val str_profesion = P_profesion.text.toString()
        val str_domicilio = P_domicilio.text.toString()
        val str_edad = P_edad.text.toString()
        val str_telefono = P_telefono.text.toString()

        val hashmap = HashMap<String, Any>()
        hashmap["nombres"] = str_nombres
        hashmap["apellidos"] = str_apellidos
        hashmap["profesion"] = str_profesion
        hashmap["domicilio"] = str_domicilio
        hashmap["edad"] = str_edad
        hashmap["telefono"] = str_telefono

        reference!!.updateChildren(hashmap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Se han actualizado los datos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "No se han actualizado los datos",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }.addOnFailureListener { e ->
            Toast.makeText(
                applicationContext,
                "Ha ocurrido un error ${e.message}",
                Toast.LENGTH_SHORT
            ).show()

        }


    }

    private fun CuentaVerificada() {

        val BtnEntendidoVerificado: MaterialButton
        val dialog = Dialog(this@PerfilActivity)

        dialog.setContentView(R.layout.cuadro_d_cuenta_verificada)

        BtnEntendidoVerificado = dialog.findViewById(R.id.BtnEntendidoVerificado)
        BtnEntendidoVerificado.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

    }


    private fun ActualizarEstado(estado: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios")
            .child(user!!.uid)
        val hashMap = HashMap<String, Any>()
        hashMap["estado"] = estado
        reference!!.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        ActualizarEstado("online")
    }

    override fun onPause() {
        super.onPause()
        ActualizarEstado("offline")
    }
}












