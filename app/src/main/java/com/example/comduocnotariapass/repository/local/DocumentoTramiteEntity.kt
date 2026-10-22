package com.example.comduocnotariapass.repository.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "documentos_tramite",
    foreignKeys = [
        ForeignKey(
            entity = TramiteEntity::class,
            parentColumns = ["id"],
            childColumns = ["tramiteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tramiteId")]
)
data class DocumentoTramiteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tramiteId: Long,
    val nombre: String,
    val mimeType: String,
    val tamanoBytes: Long,
    val uri: String?
)
