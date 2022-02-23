package com.example.lugares.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lugares.R
import com.example.lugares.adapter.LugarAdapter
//import com.example.lugares.databinding.FragmentHomeBinding
import com.example.lugares.databinding.FragmentLugarBinding
import com.example.lugares.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentLugarBinding.inflate(inflater, container, false)

        //Doy vida a la simulación de navegación entre páginas
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }

        //Activar el Recycler View
        val lugarAdapter = LugarAdapter()  //Se crea un objeto adapter
        val reciclador=binding.reciclador //Se recupera el recycler view de la vista

        reciclador.adapter = lugarAdapter //Se asigna lugarAdapter como el adapter de re
        reciclador.layoutManager = LinearLayoutManager(requireContext())

        //Se crea un "observador"para mostrar la info de la tabla lugares... se actualiza cuando cambió el set de datos
        lugarViewModel.getAllData.observe(viewLifecycleOwner) { lugares ->
            lugarAdapter.setData(lugares)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}