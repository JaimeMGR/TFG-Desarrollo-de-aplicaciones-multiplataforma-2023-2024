package com.JaimeMGR.maxmangacommunity.Notificaciones

import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIdService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { tarea->
                if (tarea.isSuccessful){
                    if (tarea.result != null && !TextUtils.isEmpty(tarea.result)){
                        val mi_token : String = tarea.result!!
                        if (firebaseUser != null){
                            ActualizarToken(mi_token)
                        }
                    }
                }
            }
    }

    private fun ActualizarToken(miToken: String?) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference().child("Tokens")
        val token = Token(miToken!!)
        reference.child(firebaseUser!!.uid).setValue(token)

    }

}