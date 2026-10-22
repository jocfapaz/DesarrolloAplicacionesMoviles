package com.example.comduocnotariapass.repository.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sesion",
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
data class SesionEntity(
    @PrimaryKey val id: Int = 1,
    val usuarioId: Long,
    val fechaInicio: Long = System.currentTimeMillis()
)
