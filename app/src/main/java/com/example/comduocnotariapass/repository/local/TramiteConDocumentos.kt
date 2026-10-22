package com.example.comduocnotariapass.repository.local

import androidx.room.Embedded
import androidx.room.Relation

data class TramiteConDocumentos(
    @Embedded val tramite: TramiteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tramiteId"
    )
    val documentos: List<DocumentoTramiteEntity>
)
