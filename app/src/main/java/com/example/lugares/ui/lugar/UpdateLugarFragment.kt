package com.example.lugares.ui.lugar

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lugares.R
import com.example.lugares.databinding.FragmentUpdateLugarBinding
import com.example.lugares.model.Lugar
import com.example.lugares.viewmodel.LugarViewModel

class UpdateLugarFragment : Fragment() {


    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateLugarFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //Obtengo la info del lugar y la coloco en el fragmento
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)

        binding.btUpdateLugar.setOnClickListener {
            actualizarLugar()
        }

        setHasOptionsMenu(true) //Este fragmento debe tener un menú adicional
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Si es delete...
        if (item.itemId==R.id.delete_menu){
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actualizarLugar() {
        val nombre=binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) {
            val correo=binding.etCorreo.text.toString()
            val telefono=binding.etTelefono.text.toString()
            val web=binding.etWeb.text.toString()
            val lugar=Lugar(args.lugar.id, nombre, correo, telefono, web, 0.0,0.0,0.0,"","")
            lugarViewModel.updateLugar(lugar)
            Toast.makeText(requireContext(),
                getString(R.string.msg_lugar_update),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
    }
    private fun deleteLugar(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.menu_delete)
        builder.setMessage(getString(R.string.msg_seguroBorrar)+ " ${args.lugar.nombre}?")
        builder.setNegativeButton(getString(R.string.no)) {_,_->}
        builder.setPositiveButton(getString(R.string.si)) {_,_->
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}