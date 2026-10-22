package com.example.comduocnotariapass.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TramiteDao {

    @Transaction
    @Query("SELECT * FROM tramites WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    suspend fun listarPorUsuario(usuarioId: Long): List<TramiteConDocumentos>

    @Transaction
    @Query("SELECT * FROM tramites WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): TramiteConDocumentos?

    @Insert
    suspend fun insertar(tramite: TramiteEntity): Long

    @Update
    suspend fun actualizar(tramite: TramiteEntity)

    @Query("DELETE FROM tramites WHERE id = :id")
    suspend fun eliminar(id: Long)

    @Query("DELETE FROM documentos_tramite WHERE tramiteId = :tramiteId")
    suspend fun eliminarDocumentos(tramiteId: Long)

    @Insert
    suspend fun insertarDocumentos(documentos: List<DocumentoTramiteEntity>)

    @Transaction
    suspend fun guardar(
        tramite: TramiteEntity,
        documentos: List<DocumentoTramiteEntity>
    ): Long {
        val id = if (tramite.id == 0L) {
            insertar(tramite)
        } else {
            actualizar(tramite)
            tramite.id
        }

        eliminarDocumentos(id)
        if (documentos.isNotEmpty()) {
            insertarDocumentos(documentos.map { it.copy(tramiteId = id) })
        }
        return id
    }
}
