package com.JaimeMGR.maxmanga.Modelos

class ModeloComentario {

    var id = ""
    var idLibro = ""
    var tiempo = ""
    var comentario = ""
    var uid = ""

    constructor()
    constructor(id: String, idLibro: String, tiempo: String, comentario: String, uid: String) {
        this.id = id
        this.idLibro = idLibro
        this.tiempo = tiempo
        this.comentario = comentario
        this.uid = uid
    }

}