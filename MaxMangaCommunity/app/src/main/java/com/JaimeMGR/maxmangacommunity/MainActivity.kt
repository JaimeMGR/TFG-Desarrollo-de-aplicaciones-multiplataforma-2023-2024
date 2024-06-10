package com.JaimeMGR.maxmangacommunity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.JaimeMGR.maxmangacommunity.Fragmentos.FragmentoChats
import com.JaimeMGR.maxmangacommunity.Fragmentos.FragmentoUsuarios
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.JaimeMGR.maxmangacommunity.Modelo.Chat
import com.JaimeMGR.maxmangacommunity.Modelo.Usuario
import com.JaimeMGR.maxmangacommunity.Perfil.PerfilActivity

class MainActivity : AppCompatActivity() {

    var reference : DatabaseReference?=null
    var firebaseUser : FirebaseUser?=null
    private lateinit var nombre_usuario : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InicializarComponentes()
        ObtenerDato()
    }

    fun InicializarComponentes(){

        val toolbar : Toolbar = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(firebaseUser!!.uid)
        nombre_usuario = findViewById(R.id.Nombre_usuario)

        val tabLayout : TabLayout = findViewById(R.id.TabLayoutMain)
        val viewPager : ViewPager = findViewById(R.id.ViewPagerMain)

        /*val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewpagerAdapter.addItem(FragmentoUsuarios(),"Usuarios")
        viewpagerAdapter.addItem(FragmentoChats(), "Chats")

        viewPager.adapter = viewpagerAdapter
        tabLayout.setupWithViewPager(viewPager)*/

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
                var contMensajesNoLeidos = 0
                for (dataSnapshot in snapshot.children){
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.getReceptor().equals(firebaseUser!!.uid) && !chat.isVisto()){
                        contMensajesNoLeidos += 1
                    }
                }
                if (contMensajesNoLeidos == 0){
                    viewPagerAdapter.addItem(FragmentoChats(), "Chats")
                }
                else{
                    viewPagerAdapter.addItem(FragmentoChats(), "[$contMensajesNoLeidos] Chats")
                }
                viewPagerAdapter.addItem(FragmentoUsuarios(), "Usuarios")
                viewPager.adapter = viewPagerAdapter
                tabLayout.setupWithViewPager(viewPager)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun ObtenerDato(){
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val usuario : Usuario? = snapshot.getValue(Usuario::class.java)
                    nombre_usuario.text = usuario!!.getN_Usuario()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    class ViewPagerAdapter(fragmentManager : FragmentManager):FragmentPagerAdapter(fragmentManager) {

        private val listaFragmentos : MutableList<Fragment> = ArrayList()
        private val listaTitulos : MutableList<String> = ArrayList()



        override fun getCount(): Int {
            return listaFragmentos.size
        }

        override fun getItem(position: Int): Fragment {
            return listaFragmentos[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listaTitulos[position]
        }

        fun addItem(fragment: Fragment, titulo:String){
            listaFragmentos.add(fragment)
            listaTitulos.add(titulo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_perfil->{
                val intent = Intent(applicationContext, PerfilActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_acerca_de->{
                InfoApp()
                return true
            }
            R.id.menu_salir->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, Inicio::class.java)
                Toast.makeText(applicationContext, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                //finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun InfoApp(){
        val EntendidoInfo : Button
        val Ir_facebook : ImageView
        val Ir_instagram : ImageView
        val Ir_twitch : ImageView
        val Ir_twitter : ImageView


        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.cuadro_d_info_app)

        EntendidoInfo = dialog.findViewById(R.id.EntendidoInfo)
        Ir_facebook = dialog.findViewById(R.id.Ir_facebook)
        Ir_instagram = dialog.findViewById(R.id.Ir_instagram)
        Ir_twitch = dialog.findViewById(R.id.Ir_twitch)
        Ir_twitter = dialog.findViewById(R.id.Ir_twitter)

        Ir_facebook.setOnClickListener {
            val facebook = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100082250443596"))
            startActivity(facebook)
        }
        Ir_instagram.setOnClickListener {
            val instagram = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/jaimemgr_"))
            startActivity(instagram)
        }
        Ir_twitch.setOnClickListener {
            val youtube = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitch.tv/jaimemgr"))
            startActivity(youtube)
        }
        Ir_twitter.setOnClickListener {
            val twitter = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/Jaime_MGR"))
            startActivity(twitter)
        }

        EntendidoInfo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    private fun ActualizarEstado(estado : String){
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
