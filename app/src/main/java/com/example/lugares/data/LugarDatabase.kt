package com.example.lugares.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lugares.model.Lugar

@Database(entities=[Lugar::class], version = 1, exportSchema = false)
abstract class LugarDatabase : RoomDatabase() /* : = Hereda*/ {

    abstract fun lugarDao() : LugarDao

    companion object /*es el equivalente al static en java*/ {

        @Volatile
        private var INSTANCE: LugarDatabase? = null

        fun getDatabase(context: android.content.Context) : LugarDatabase {

            var instance = INSTANCE
            if (instance != null){
                return instance
            }
            synchronized(this){
                //Se crea el esquema de la base de datos -físico-
                val basedatos = Room.databaseBuilder(
                    context.applicationContext,
                    LugarDatabase::class.java,
                    "Lugar_database"
                ).build()
                INSTANCE = basedatos
                return basedatos
            }
        }

    }

}