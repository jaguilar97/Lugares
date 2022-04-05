package com.example.lugares.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Update
import com.example.lugares.data.LugarDao
import com.example.lugares.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {

    val getAllData : MutableLiveData<List<Lugar>> = lugarDao.getLugares()

    suspend fun addLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }

    suspend fun updateLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }

    suspend fun deleteLugar(lugar: Lugar){
        lugarDao.deleteLugar(lugar)
    }
}