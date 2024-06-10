package com.JaimeMGR.maxmangacommunity.Modelo

class Usuario {
    private var uid : String = ""
    private var n_usuario : String = ""
    private var email : String = ""
    private var proveedor : String = ""
    private var telefono : String = ""
    private var imagen : String = ""
    private var buscar : String = ""
    private var nombres : String = ""
    private var apellidos : String = ""
    private var edad : String = ""
    private var profesion : String = ""
    private var domicilio : String = ""
    private var estado : String = ""

    constructor()

    constructor(
        uid: String,
        n_usuario: String,
        email: String,
        proveedor : String,
        telefono: String,
        imagen: String,
        buscar: String,
        nombres: String,
        apellidos: String,
        edad: String,
        profesion: String,
        domicilio: String,
        estado : String
    ) {
        this.uid = uid
        this.n_usuario = n_usuario
        this.email = email
        this.proveedor = proveedor
        this.telefono = telefono
        this.imagen = imagen
        this.buscar = buscar
        this.nombres = nombres
        this.apellidos = apellidos
        this.edad = edad
        this.profesion = profesion
        this.domicilio = domicilio
        this.estado = estado
    }

    //getters y setters
    fun getUid() : String?{
        return uid
    }

    fun setUid(uid : String){
        this.uid = uid
    }

    fun getN_Usuario() : String?{
        return n_usuario
    }

    fun setN_Usuario(n_usuario : String){
        this.n_usuario = n_usuario
    }

    fun getEmail() : String?{
        return email
    }

    fun setEmail(email : String){
        this.email = email
    }

    fun getProveedor () : String? {
        return proveedor
    }

    fun setProveedor (proveedor: String){
        this.proveedor = proveedor
    }

    fun getTelefono() : String?{
        return telefono
    }

    fun setTelefono(telefono : String){
        this.telefono = telefono
    }

    fun getImagen() : String?{
        return imagen
    }

    fun setImagen(imagen : String){
        this.imagen = imagen
    }

    fun getBuscar() : String?{
        return buscar
    }

    fun setBuscar(buscar : String){
        this.buscar = buscar
    }

    fun getNombres() : String?{
        return nombres
    }

    fun setNombres(nombres : String){
        this.nombres = nombres
    }

    fun getApellidos() : String?{
        return apellidos
    }

    fun setApellidos(apellidos : String){
        this.apellidos = apellidos
    }

    fun getEdad() : String?{
        return edad
    }

    fun setEdad(edad : String){
        this.edad = edad
    }

    fun getProfesion() : String?{
        return profesion
    }

    fun setProfesion(profesion : String){
        this.profesion = profesion
    }

    fun getDomicilio() : String?{
        return domicilio
    }

    fun setDomicilio(domicilio : String){
        this.domicilio = domicilio
    }

    fun getEstado() : String?{
        return estado
    }

    fun setEstado(estado : String){
        this.estado = estado
    }

}