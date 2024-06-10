package com.JaimeMGR.maxmanga.Cliente

import android.widget.Filter
import com.JaimeMGR.maxmanga.Modelos.ModeloCategoria

class FiltrarCategoria_Cliente : Filter{


    private var filtroLista : ArrayList<ModeloCategoria>
    private var adaptadorCategoriaCliente : AdaptadorCategoria_Cliente

    constructor(filtroLista: ArrayList<ModeloCategoria>, adaptadorCategoriaCliente: AdaptadorCategoria_Cliente) {
        this.filtroLista = filtroLista
        this.adaptadorCategoriaCliente = adaptadorCategoriaCliente
    }

    override fun performFiltering(categoria: CharSequence?): Filter.FilterResults {
        var categoria = categoria
        var resultados = Filter.FilterResults()

        if (categoria != null && categoria.isNotEmpty()){
            categoria = categoria.toString().uppercase()
            val modeloFiltrado : ArrayList<ModeloCategoria> = ArrayList()
            for (i in 0 until filtroLista.size)
                if (filtroLista[i].categoria.uppercase().contains(categoria)){
                    modeloFiltrado.add(filtroLista[i])
                }
            resultados.count = modeloFiltrado.size
            resultados.values = modeloFiltrado

        }
        else{
            resultados.count = filtroLista.size
            resultados.values = filtroLista
        }
        return resultados
    }

    override fun publishResults(p0: CharSequence?, resultados: Filter.FilterResults) {
        adaptadorCategoriaCliente.categoriaArrayList = resultados.values as ArrayList<ModeloCategoria>
        adaptadorCategoriaCliente.notifyDataSetChanged()
    }
}