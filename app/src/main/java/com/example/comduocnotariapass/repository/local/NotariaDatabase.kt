package com.example.comduocnotariapass.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UsuarioEntity::class,
        SesionEntity::class,
        TramiteEntity::class,
        DocumentoTramiteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NotariaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun sesionDao(): SesionDao

    abstract fun tramiteDao(): TramiteDao

    companion object {
        @Volatile
        private var instancia: NotariaDatabase? = null

        fun obtener(context: Context): NotariaDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotariaDatabase::class.java,
                    "notariapass.db"
                ).build().also { instancia = it }
            }
    }
}
