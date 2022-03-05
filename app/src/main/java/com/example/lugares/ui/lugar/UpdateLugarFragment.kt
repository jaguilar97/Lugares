package com.example.lugares.ui.lugar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
        binding.tvLatitud.text=args.lugar.latitud.toString()
        binding.tvLongitud.text=args.lugar.longitud.toString()
        binding.tvAltura.text=args.lugar.altura.toString()

        binding.btUpdateLugar.setOnClickListener { actualizarLugar() }
        binding.btEmail.setOnClickListener { enviarCorreo() }
        binding.btPhone.setOnClickListener { hacerLlamada() }
        binding.btWhatsapp.setOnClickListener { enviarWhatsApp() }
        binding.btWeb.setOnClickListener { verWeb() }
        binding.btLocation.setOnClickListener { verMapa() }

        setHasOptionsMenu(true) //Este fragmento debe tener un menú adicional
        return binding.root
    }

    private fun enviarCorreo() {
        val correo = binding.etCorreo.text.toString() //Se extrae la cuenta de correo del lugar
        if (correo.isNotEmpty()){
            val intent = Intent(Intent.ACTION_SEND) //Se intenta enviar algo desde la app...
            intent.type = "message/rfc822" //Se va a enviar un correo electrónico

            //Se define el destinatario
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))

            //Se define el asunto
            intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.msg_saludos)+" "+binding.etNombre.text)

            //Se define el cuerpo del correo inicial
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.msg_mensaje_correo))

            //Se solicita el recurso de correo para que se envíe este
            startActivity(intent)

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun hacerLlamada(){
        val telefono = binding.etTelefono.text.toString() //Se extrae el número del lugar
        if (telefono.isNotEmpty()){
            val intent = Intent(Intent.ACTION_CALL) //Se intenta hacer la llamada...
            intent.data = Uri.parse("tel:$telefono") //Con ese prefijo siempre y el $ reemplaza la palabra telefono por el valor numero...

            //Se procede a validar si hay permisos por parte dell usuario para hacer la llamada
            if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){ //Si no tenemos permisos, hay que solicitarlos...

                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)

            } else { //Si se otorgaron los permisos...

                requireActivity().startActivity(intent) // Se hace la llamada

            }

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun enviarWhatsApp(){
        val telefono = binding.etTelefono.text.toString() //Se extrae el número del lugar
        if (telefono.isNotEmpty()){
            val intent = Intent(Intent.ACTION_VIEW) //Se intenta hacer la llamada...
            val uri="whatsapp://send?phone=506:$telefono&text="+
                    getString(R.string.msg_saludos) //Con ese prefijo siempre y el $ reemplaza la palabra telefono por el valor numero...

            //Se establece el App a usar
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(uri) //Se carga la info
            startActivity(intent)

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun verWeb(){
        val sitio = binding.etWeb.text.toString() //Se extrae el sitio web del lugar
        if (sitio.isNotEmpty()){
            val web = Uri.parse("http://$sitio")
            val intent = Intent(Intent.ACTION_VIEW,web) //Se intenta abrir el sitio web...

            startActivity(intent)  //Se abre el visor web y se muestra el sitio web

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun verMapa(){
        val latitud = binding.tvLatitud.text.toString().toDouble() //Se extrae la latitud del lugar
        val longitud = binding.tvLongitud.text.toString().toDouble() //Se extrae la longitud del lugar
        if (latitud.isFinite() && longitud.isFinite()){
            val location = Uri.parse("geo:$latitud,$longitud?z=18")
            val intent = Intent(Intent.ACTION_VIEW,location) //Se ve el mapa del lugar...

            startActivity(intent)  //Se abre el visor del mapa y se ve el lugar

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
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