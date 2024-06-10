package com.JaimeMGR.maxmanga.Cliente

import android.widget.Filter
import com.JaimeMGR.maxmanga.Modelos.Modelopdf

class FiltrarPdfCliente : Filter{

    var filtroList : ArrayList<Modelopdf>
    var adaptadorPdfCliente : AdaptadorPdfCliente

    constructor(filtroList: ArrayList<Modelopdf>, adaptadorPdfCliente: AdaptadorPdfCliente) {
        this.filtroList = filtroList
        this.adaptadorPdfCliente = adaptadorPdfCliente
    }

    override fun performFiltering(libro: CharSequence?): Filter.FilterResults {
        var libro : CharSequence?= libro
        val resultados = Filter.FilterResults()
        if (libro!=null && libro.isNotEmpty()){
            libro = libro.toString().lowercase()
            val modeloFiltrado : ArrayList<Modelopdf> = ArrayList()
            for (i in filtroList.indices){
                if (filtroList[i].titulo.lowercase().contains(libro)){
                    modeloFiltrado.add(filtroList[i])
                }
            }
            resultados.count = modeloFiltrado.size
            resultados.values = modeloFiltrado
        }
        else{
            resultados.count = filtroList.size
            resultados.values = filtroList
        }
        return resultados
    }

    override fun publishResults(constraint: CharSequence?, resultados: Filter.FilterResults) {
        adaptadorPdfCliente.pdfArrayList = resultados.values as ArrayList<Modelopdf>
        adaptadorPdfCliente.notifyDataSetChanged()
    }
}