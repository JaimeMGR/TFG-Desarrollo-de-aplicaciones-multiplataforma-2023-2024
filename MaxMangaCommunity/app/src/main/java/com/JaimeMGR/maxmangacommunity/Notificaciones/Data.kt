package com.JaimeMGR.maxmangacommunity.Notificaciones

class Data {
    private var usuario : String = ""
    private var icono = 0
    private var cuerpo : String = ""
    private var titulo : String = ""
    private var enviado : String = ""

    constructor(){

    }

    constructor(usuario: String, icono: Int, cuerpo: String, titulo: String, enviado: String) {
        this.usuario = usuario
        this.icono = icono
        this.cuerpo = cuerpo
        this.titulo = titulo
        this.enviado = enviado
    }


    fun getUsuario(): String?{
        return usuario
    }

    fun setUsuario(usuario: String){
        this.usuario = usuario
    }

    fun getIcono(): Int{
        return icono
    }

    fun setIcono(icono: Int){
        this.icono = icono
    }

    fun getCuerpo(): String?{
        return cuerpo
    }

    fun setCuerpo(cuerpo: String){
        this.cuerpo = cuerpo
    }

    fun getTitulo(): String?{
        return titulo
    }

    fun setTitulo(titulo: String){
        this.titulo = titulo
    }

    fun getEnviado(): String?{
        return enviado
    }

    fun setEnviado(enviado: String){
        this.enviado = enviado
    }



}