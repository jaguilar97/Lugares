package com.example.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lugares.databinding.LugarFilaBinding
import com.example.lugares.model.Lugar
import com.example.lugares.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //Una lista de los lugares a mostrar...

    private var listaLugares = emptyList<Lugar>()

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
            fun bind(lugar : Lugar){
                itemBinding.tvNombre.text = lugar.nombre
                itemBinding.tvCorreo.text = lugar.correo
                itemBinding.tvTelefono.text = lugar.telefono
                itemBinding.tvWeb.text = lugar.web
                itemBinding.vistaFila.setOnClickListener {
                    val  accion = LugarFragmentDirections.actionNavLugarToUpdateLugarFragment(lugar)
                    itemView.findNavController().navigate(accion)
                }
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
        parent, false) //iflate toma toda la info y construye la tarjeta a nivel de XML
        return LugarViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        //Aquí se dibujan los lugares
        val lugar = listaLugares[position] //Recupero el lugar a "pintar"
        holder.bind(lugar) //Se invoca la función definida en el inner class
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setData(lugares : List<Lugar>){
        this.listaLugares = lugares
        notifyDataSetChanged()
    }

}