package com.JaimeMGR.maxmangacommunity.Modelo

class Chat {
    private var id_mensaje : String = ""
    private var emisor : String = ""
    private var receptor : String = ""
    private var mensaje : String = ""
    private var url : String = ""
    private var visto = false
    private var fecha = "00:00"

    constructor()
    constructor(
        id_mensaje: String,
        emisor: String,
        receptor: String,
        mensaje: String,
        url: String,
        visto: Boolean,
        fecha: String
    ) {
        this.id_mensaje = id_mensaje
        this.emisor = emisor
        this.receptor = receptor
        this.mensaje = mensaje
        this.url = url
        this.visto = visto
        this.fecha
    }

    //getters y setters
    fun getId_Mensaje() : String?{
        return id_mensaje
    }

    fun setId_Mensaje(id_mensaje : String?){
        this.id_mensaje = id_mensaje!!
    }

    fun getEmisor() : String?{
        return emisor
    }

    fun setEmisor(emisor : String?){
        this.emisor = emisor!!
    }

    fun getReceptor() : String?{
        return receptor
    }

    fun setReceptor(receptor : String?){
        this.receptor = receptor!!
    }

    fun getMensaje() : String?{
        return mensaje
    }

    fun setMensaje(mensaje : String?){
        this.mensaje = mensaje!!
    }

    fun getUrl() : String?{
        return url
    }

    fun setUrl(url : String?){
        this.url = url!!
    }

    fun isVisto() : Boolean{
        return visto
    }

    fun setIsVisto(visto : Boolean?){
        this.visto = visto!!
    }

    fun getFecha(): String {
        return this.fecha;
    }

    fun setFecha(fecha: String) {
        this.fecha = fecha;
    }

}