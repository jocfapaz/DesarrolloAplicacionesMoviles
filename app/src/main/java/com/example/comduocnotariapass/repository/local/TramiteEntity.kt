package com.example.comduocnotariapass.repository.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tramites",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("usuarioId")]
)
data class TramiteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val codigo: String,
    val formatoId: String,
    val formatoNombre: String,
    val fechaCreacion: Long,
    val estado: String,
    val resultado: String? = null,
    val tipoFirma: String,
    val solicitanteNombre: String,
    val solicitanteId: String,
    val solicitanteRol: String
)
