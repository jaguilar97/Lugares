package com.example.lugares.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lugares.model.Lugar

@Dao
interface LugarDao {

    //Función para obtener la lista de lugares
    @Query( "select * from LUGAR")
    fun getAllData() : LiveData<List<Lugar>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar: Lugar)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateLugar(lugar: Lugar)

    @Delete
    suspend fun deleteLugar(lugar: Lugar)

}