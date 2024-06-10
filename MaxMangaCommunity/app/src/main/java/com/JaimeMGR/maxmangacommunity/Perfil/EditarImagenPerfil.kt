package com.JaimeMGR.maxmangacommunity.Perfil

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.JaimeMGR.maxmangacommunity.R

class EditarImagenPerfil : AppCompatActivity() {

    private lateinit var ImagenPerfilActualizar: ImageView
    private lateinit var BtnElegirImagen: Button
    private lateinit var BtnActualizarImagen: Button
    private var imageUri: Uri? = null

    private lateinit var firebaseAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_imagen_perfil)

        ImagenPerfilActualizar = findViewById(R.id.ImagenPerfilActualizar)
        BtnElegirImagen = findViewById(R.id.BtnElegirImagenDe)
        BtnActualizarImagen = findViewById(R.id.BtnActualizarImagen)

        progressDialog = ProgressDialog(this@EditarImagenPerfil)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser


        BtnElegirImagen.setOnClickListener {
            //Toast.makeText(applicationContext,"Seleccionar imagen de", Toast.LENGTH_SHORT).show()
            MostrarDialog()
        }

        BtnActualizarImagen.setOnClickListener {
            //Toast.makeText(applicationContext,"Actualizar imagen", Toast.LENGTH_SHORT).show()
            ValidarImagen()
        }
    }

    private fun ValidarImagen() {
        if (imageUri == null) {
            Toast.makeText(applicationContext, "Es necesario una imagen", Toast.LENGTH_SHORT).show()
        } else {
            SubirImagen()
        }
    }

    private fun SubirImagen() {
        progressDialog.setMessage("Actualizando imagen... ")
        progressDialog.show()
        val rutaImagen = "Perfil_usuario/" + firebaseAuth.uid
        val referenceStorage = FirebaseStorage.getInstance().getReference(rutaImagen)
        referenceStorage.putFile(imageUri!!).addOnSuccessListener { tarea ->
            val uriTarea: Task<Uri> = tarea.storage.downloadUrl
            while (!uriTarea.isSuccessful);
            val urlImagen = "${uriTarea.result}"
            ActualizarImagenBD(urlImagen)

        }.addOnFailureListener { e ->
            Toast.makeText(
                applicationContext,
                "No se ha podido subir la imagen debido a: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun ActualizarImagenBD(urlImagen: String) {
        progressDialog.setMessage("Actualizando imagen de perfil")
        val hashmap: HashMap<String, Any> = HashMap()
        if (imageUri != null) {
            hashmap["imagen"] = urlImagen
        }

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(firebaseAuth.uid!!).updateChildren(hashmap).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(applicationContext, "Su imagen a sido actualizada", Toast.LENGTH_SHORT)
                .show()

        }.addOnFailureListener { e ->
            Toast.makeText(
                applicationContext,
                "No se ha actualizado su imagen debido a: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun AbrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galeriaActivityResultLauncher.launch(intent)
    }

    private val galeriaActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val data = resultado.data
                imageUri = data!!.data
                ImagenPerfilActualizar.setImageURI(imageUri)
            } else {
                Toast.makeText(applicationContext, "Cancelado por el usuario", Toast.LENGTH_SHORT)
                    .show()

            }

        }
    )


    private val requestCameraPermiso =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { Permiso_concedido ->
            if (Permiso_concedido) {
                AbrirCamara()
            } else {
                Toast.makeText(applicationContext,"El permiso para acceder a la cámara no ha sido concedido",Toast.LENGTH_SHORT).show()
            }

        }

    private fun AbrirCamara() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Titulo")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        camaraActivityResultLauncher.launch(intent)
    }


    private val camaraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado_camara ->
            if (resultado_camara.resultCode == RESULT_OK) {
                ImagenPerfilActualizar.setImageURI(imageUri)
            } else {
                Toast.makeText(applicationContext, "Cancelado por el usuario", Toast.LENGTH_SHORT)
                    .show()
            }
        }



    private fun MostrarDialog() {
        val Btn_abrir_galeria: Button
        val Btn_abrir_camara: Button

        val dialog = Dialog(this@EditarImagenPerfil)

        dialog.setContentView(R.layout.cuadro_d_seleccionar)

        Btn_abrir_galeria = dialog.findViewById(R.id.Btn_abrir_galeria)
        Btn_abrir_camara = dialog.findViewById(R.id.Btn_abrir_camara)

        Btn_abrir_galeria.setOnClickListener {
            //Toast.makeText(applicationContext, "Abrir galería", Toast.LENGTH_SHORT).show()
                AbrirGaleria()
                dialog.dismiss()
        }

        Btn_abrir_camara.setOnClickListener {
            //Toast.makeText(applicationContext, "Abrir cámara", Toast.LENGTH_SHORT).show()
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                AbrirCamara()
                dialog.dismiss()
            } else {
                requestCameraPermiso.launch(Manifest.permission.CAMERA)
            }

        }

        dialog.show()

    }


    private fun ActualizarEstado(estado: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios")
            .child(firebaseUser!!.uid)
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