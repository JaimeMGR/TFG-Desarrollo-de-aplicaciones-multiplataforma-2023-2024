package com.JaimeMGR.maxmanga.Cliente

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.JaimeMGR.maxmanga.Administrador.Constantes
import com.JaimeMGR.maxmanga.Administrador.MisFunciones
import com.JaimeMGR.maxmanga.LeerLibro
import com.JaimeMGR.maxmanga.Modelos.ModeloComentario
import com.JaimeMGR.maxmanga.R
import com.JaimeMGR.maxmanga.databinding.ActivityDetalleLibroBinding
import com.JaimeMGR.maxmanga.databinding.ActivityDetalleLibroClienteBinding
import com.JaimeMGR.maxmanga.databinding.ActivityLoginClienteBinding
import com.JaimeMGR.maxmanga.databinding.DialogAgregarComentarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream

class DetalleLibro_Cliente : AppCompatActivity() {

  private lateinit var binding: ActivityDetalleLibroClienteBinding
  private var idLibro = ""

    private var tituloLibro = ""
    private var urlLibro = ""

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private var esFavorito = false

    private lateinit var comentarioArrayList : ArrayList<ModeloComentario>
    private lateinit var adaptadorComentario: AdaptadorComentario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLibroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro = intent.getStringExtra("idLibro")!!

        MisFunciones.incrementarVistas(idLibro)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnLeerLibroC.setOnClickListener {
            val intent = Intent (this@DetalleLibro_Cliente, LeerLibro::class.java)
            intent.putExtra("idLibro", idLibro)
            startActivity(intent)
        }

        binding.BtnDescargarLibroC.setOnClickListener {
                descargarLibro()
        }

        binding.BtnFavoritosLibroC.setOnClickListener {
            if (esFavorito){
                MisFunciones.eliminarFavoritos(this@DetalleLibro_Cliente, idLibro)
            }else{
                agregarFavoritos()
            }
        }

        binding.IbAgregarComentario.setOnClickListener {
            dialogComentar()
        }

        comprobarFavorito()
        cargarDetalleLibro()
        listarComentario()
    }

    private fun listarComentario() {
        comentarioArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro).child("Comentarios")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comentarioArrayList.clear()
                    for (ds in snapshot.children){
                        val modelo = ds.getValue(ModeloComentario::class.java)
                        comentarioArrayList.add(modelo!!)
                    }
                    adaptadorComentario = AdaptadorComentario(this@DetalleLibro_Cliente, comentarioArrayList)
                    binding.RvComentarios.adapter = adaptadorComentario
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private var comentario = ""
    private fun dialogComentar() {
        val agregar_con_binding = DialogAgregarComentarioBinding.inflate(LayoutInflater.from(this))

        val builder = AlertDialog.Builder(this)
        builder.setView(agregar_con_binding.root)

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        agregar_con_binding.IbCerrar.setOnClickListener {
            alertDialog.dismiss()
        }

        agregar_con_binding.BtnComentar.setOnClickListener {
            comentario = agregar_con_binding.EtAgregarComentario.text.toString().trim()
            if (comentario.isEmpty()){
                Toast.makeText(applicationContext, "Agregue un comentario", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.dismiss()
                agregarComentario()
            }
        }
    }

    private fun agregarComentario() {
        progressDialog.setMessage("Agregando comentario")
        progressDialog.show()

        val tiempo = "${System.currentTimeMillis()}"

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$tiempo"
        hashMap["idLibro"] = "${idLibro}"
        hashMap["tiempo"] = "$tiempo"
        hashMap["comentario"] = "${comentario}"
        hashMap["uid"] = "${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro).child("Comentarios").child(tiempo)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Su comentario ha sido publicado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun comprobarFavorito() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idLibro)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    esFavorito = snapshot.exists()
                    if (esFavorito){
                        binding.BtnFavoritosLibroC.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_agregar_favorito,
                            0,
                            0
                        )
                        binding.BtnFavoritosLibroC.text = "Eliminar de favoritos"
                    }else{
                        binding.BtnFavoritosLibroC.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_no_favorito,
                            0,
                            0
                        )
                        binding.BtnFavoritosLibroC.text = "Agregar a favoritos"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun agregarFavoritos(){
        val tiempo = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = idLibro
        hashMap["tiempo"] = tiempo

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idLibro)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Libro añadido a favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(applicationContext, "No se agregó a favoritos debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun descargarLibro() {
        progressDialog.setMessage("Descargando libro")
        progressDialog.show()

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlLibro)
        storageReference.getBytes(Constantes.Maximo_bytes_pdf)
            .addOnSuccessListener {bytes->
                guardarLibroDisp(bytes)
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarLibroDisp(bytes : ByteArray) {
        val nombreLibro_extension = "$tituloLibro"+System.currentTimeMillis()+".pdf"
        try {
            val carpeta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            carpeta.mkdirs()

            val archivo_ruta = carpeta.path+"/"+nombreLibro_extension
            val out = FileOutputStream(archivo_ruta)
            out.write(bytes)
            out.close()

            Toast.makeText(applicationContext, "Libro guardado con éxito", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            incrementarNumDesc()
        }catch (e:Exception){
            Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }

    private fun cargarDetalleLibro() {
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Obtener información del libro a través del id
                    val categoria = "${snapshot.child("categoria").value}"
                    val contadorDescargas = "${snapshot.child("contadorDescargas").value}"
                    val contadorVistas = "${snapshot.child("contadorVistas").value}"
                    val descripcion = "${snapshot.child("descripcion").value}"
                    val tiempo = "${snapshot.child("tiempo").value}"
                    tituloLibro = "${snapshot.child("titulo").value}"
                    urlLibro = "${snapshot.child("url").value}"

                    //Formato del tiempo
                    val fecha = MisFunciones.formatoTiempo(tiempo.toLong())
                    //Cargar categoria del libro
                    MisFunciones.CargarCategoria(categoria, binding.categoriaD)
                    //Cargar la miniatura del libro y el contador de páginas
                    MisFunciones.CargarPdfUrl("$urlLibro", "$tituloLibro", binding.VisualizadorPDF, binding.progressBar,
                        binding.paginasD)
                    //Cargar tamaño
                    MisFunciones.CargarTamañoPdf("$urlLibro", "${tituloLibro}", binding.tamaOD)

                    //Seteamos informacion restante
                    binding.tituloLibroD.text = tituloLibro
                    binding.descripcionD.text = descripcion
                    binding.vistasD.text = contadorVistas
                    binding.descargasD.text = contadorDescargas
                    binding.fechaD.text = fecha
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun incrementarNumDesc(){
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var contDescarActual = "${snapshot.child("contadorDescargas").value}"

                    if (contDescarActual == "" || contDescarActual == null){
                        contDescarActual = "0"
                    }

                    val nuevaDes = contDescarActual.toLong() + 1

                    val hashMap = HashMap<String, Any>()
                    hashMap["contadorDescargas"] = nuevaDes

                    val BDRef = FirebaseDatabase.getInstance().getReference("Libros")
                    BDRef.child(idLibro)
                        .updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private val permisoAlmacenamiento = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){permiso_concedido->
        if (permiso_concedido){

        }else{
            Toast.makeText(applicationContext, "El permiso de almacenamiento ha sido denegado", Toast.LENGTH_SHORT).show()
        }
    }
}