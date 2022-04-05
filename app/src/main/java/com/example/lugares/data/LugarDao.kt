package com.example.lugares.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lugares.model.Lugar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase

class LugarDao {

    private val coleccion1 = "lugaresAPP"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private val coleccion2 = "misLugares"
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //Función para obtener la lista de lugares
    //Mutable permite modificar la info desde cualquier celular
    fun getLugares() : MutableLiveData<List<Lugar>> {
        val listaLugares = MutableLiveData<List<Lugar>>()

        firestore.collection(coleccion1).document(usuario).collection(coleccion2)
            .addSnapshotListener{ instantanea, e ->
                if (e != null){ //Validamos si se da un error recuperando la info de los documentos...
                    return@addSnapshotListener
                }

                if(instantanea != null){ //Si se pudo recuperar la información entonces...
                    val lista = ArrayList<Lugar>()

                    //Recorro todos los documentos de la ruta
                    instantanea.documents.forEach{
                        val lugar = it.toObject(Lugar::class.java)
                        if (lugar!= null){ //Si se pudo convertit el documento en un lugar
                            lista.add(lugar) //Se agrega el lugar de la lista temporal
                        }
                    }
                    listaLugares.value = lista
                }
            }

        return listaLugares

    }

    suspend fun saveLugar(lugar: Lugar){

        val documento: DocumentReference

        if (lugar.id.isEmpty()){
            documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document()
            lugar.id = documento.id
        }else{
            documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id)
        }
        //Efectivamente se actualiza la información del lugar en Firestore
        documento.set(lugar)
            .addOnSuccessListener { Log.d("saveLugar","Lugar agregado/modificado") }
            .addOnCanceledListener { Log.e("saveLugar","Error: Lugar NO agregado/modificado") }

    }


    suspend fun deleteLugar(lugar: Lugar){
        //Validamos si el lugar existe en la coleccion... si tiene id
        if (lugar.id.isNotEmpty()){
            firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id).delete()
                .addOnSuccessListener { Log.d("deleteLugar","Lugar eliminado") }
                .addOnCanceledListener { Log.e("deleteLugar","Error: Lugar NO eliminado") }
        }

    }

}