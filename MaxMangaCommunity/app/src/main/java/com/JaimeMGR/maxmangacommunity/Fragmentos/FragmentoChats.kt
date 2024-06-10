package com.JaimeMGR.maxmangacommunity.Fragmentos

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.JaimeMGR.maxmangacommunity.Adaptador.AdaptadorUsuario
import com.JaimeMGR.maxmangacommunity.Modelo.ListaChats
import com.JaimeMGR.maxmangacommunity.Modelo.Usuario
import com.JaimeMGR.maxmangacommunity.Notificaciones.Token
import com.JaimeMGR.maxmangacommunity.R

class FragmentoChats : Fragment() {

    private var usuarioAdaptador : AdaptadorUsuario?=null
    private var usuarioLista : List<Usuario>?=null
    private var usuarioListaChats : List<ListaChats>?= null

    lateinit var RV_ListaChats : RecyclerView
    private var firebaseUser : FirebaseUser?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_fragmento_chats, container, false)

        RV_ListaChats = view.findViewById(R.id.RV_ListaChats)
        RV_ListaChats.setHasFixedSize(true)
        RV_ListaChats.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        usuarioListaChats = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("ListaMensajes").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuarioListaChats as ArrayList).clear()
                for (dataSnapShot in snapshot.children){
                    val chatList = dataSnapShot.getValue(ListaChats::class.java)
                    (usuarioListaChats as ArrayList).add(chatList!!)
                }
                RecuperarListaChats()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { tarea->
                if (tarea.isSuccessful){
                    if (tarea.result != null && !TextUtils.isEmpty(tarea.result)){
                        val token : String = tarea.result!!
                        ActualizarToken(token)
                    }
                }
            }

        return view
    }

    private fun ActualizarToken(token: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Tokens")
        val token1 = Token(token!!)
        reference.child(firebaseUser!!.uid).setValue(token1)

    }

    private fun RecuperarListaChats(){
        usuarioLista = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios")
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuarioLista as ArrayList).clear()
                for (dataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(Usuario::class.java)
                    for (cadaLista in usuarioListaChats!!){
                        if (user!!.getUid().equals(cadaLista.getUid())){
                            (usuarioLista as ArrayList).add(user!!)
                        }
                    }
                    usuarioAdaptador = AdaptadorUsuario(context!!, (usuarioLista as ArrayList<Usuario>), true)
                    RV_ListaChats.adapter = usuarioAdaptador
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}