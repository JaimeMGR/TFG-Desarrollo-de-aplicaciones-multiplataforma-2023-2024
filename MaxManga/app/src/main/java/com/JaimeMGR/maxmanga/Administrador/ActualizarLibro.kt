package com.JaimeMGR.maxmanga.Administrador

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.JaimeMGR.maxmanga.R
import com.JaimeMGR.maxmanga.databinding.ActivityActualizarLibroBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActualizarLibro : AppCompatActivity() {

    private lateinit var binding : ActivityActualizarLibroBinding

    private var idLibro = ""

    private lateinit var progressDialog: ProgressDialog

    //Titulos
    private lateinit var catTituloArrayList: ArrayList<String>
    //Id
    private lateinit var catIdArrayList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        idLibro = intent.getStringExtra("idLibro")!!


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarCategorias()
        cargarInformacion()

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.TvCategoriaLibro.setOnClickListener {
            dialogCategoria()
        }
        binding.BtnActualizarLibro.setOnClickListener {
            validarinformacion()
        }
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Obtener la información en tiempo real del libro seleccionado
                    val titulo = snapshot.child("titulo").value.toString()
                    val descripcion = snapshot.child("descripcion").value.toString()
                    id_seleccionado = snapshot.child("categoria").value.toString()

                    //Seteamos en las vistas
                    binding.EtTituloLibro.setText(titulo)
                    binding.EtDescripcionLibro.setText(descripcion)

                    val refCategoria = FirebaseDatabase.getInstance().getReference("Categorias")
                    refCategoria.child(id_seleccionado)
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot){
                                //Obtener la categoria
                                val categoria = snapshot.child("categoria").value
                                //Seteamos en el textview
                                binding.TvCategoriaLibro.text = categoria.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private var titulo = ""
    private var descripcion = ""
    private fun validarinformacion() {
        //obtener los datos igresados
        titulo = binding.EtTituloLibro.text.toString().trim()
        descripcion = binding.EtDescripcionLibro.text.toString().trim()

        if (titulo.isEmpty()){
            Toast.makeText(this, "Ingrese título", Toast.LENGTH_SHORT).show()
        }else if (descripcion.isEmpty()){
            Toast.makeText(this, "Ingrese descripción", Toast.LENGTH_SHORT).show()
        }else if (id_seleccionado.isEmpty()) {
            Toast.makeText(this, "Seleccione una categoría", Toast.LENGTH_SHORT).show()
        }else{
            actualizarInformacion()
        }

    }

    private fun actualizarInformacion() {
        progressDialog.setMessage("Actualizando información")
        progressDialog.show()
        val hashMap = HashMap<String, Any>()
        hashMap["titulo"] = "$titulo"
        hashMap["descripcion"] = "$descripcion"
        hashMap["categoria"] = "$id_seleccionado"

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "La actualización fué exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "La actualización falló debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private var id_seleccionado = ""
    private var titulo_seleccionado = ""
    private fun dialogCategoria() {
        val categoriaArray = arrayOfNulls<String>(catTituloArrayList.size)
        for (i in catTituloArrayList.indices){
            categoriaArray[i] = catTituloArrayList[i]
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione una categoría")
            .setItems(categoriaArray){dialog, posicion->
                id_seleccionado = catIdArrayList[posicion]
                titulo_seleccionado = catTituloArrayList[posicion]

                binding.TvCategoriaLibro.text = titulo_seleccionado
            }
            .show()
    }

    private fun cargarCategorias() {
        catTituloArrayList = ArrayList()
        catIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                catTituloArrayList.clear()
                catIdArrayList.clear()
                for (ds in snapshot.children){
                    val id = ""+ds.child("id")
                    val categoria = ""+ds.child("categoria").value

                    catTituloArrayList.add(categoria)
                    catIdArrayList.add(id)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}