package com.example.comduocnotariapass.repository

import com.example.comduocnotariapass.repository.local.SesionEntity
import com.example.comduocnotariapass.repository.local.SesionDao
import com.example.comduocnotariapass.repository.local.UsuarioDao
import com.example.comduocnotariapass.repository.local.UsuarioEntity

data class UsuarioSesion(
    val id: Long,
    val nombre: String,
    val correo: String
)

sealed interface ResultadoAuth {
    data class Exito(val usuario: UsuarioSesion) : ResultadoAuth
    data class Error(val mensaje: String) : ResultadoAuth
}

class AuthRepository(
    private val usuarioDao: UsuarioDao,
    private val sesionDao: SesionDao
) {

    suspend fun iniciarSesion(correo: String, clave: String): ResultadoAuth {
        val correoLimpio = correo.trim().lowercase()
        if (correoLimpio.isBlank() || clave.isBlank()) {
            return ResultadoAuth.Error("Completa el correo y la contraseña.")
        }

        val usuario = usuarioDao.buscarPorCorreo(correoLimpio)
            ?: return ResultadoAuth.Error("Correo o contraseña incorrectos.")

        if (usuario.clave != clave) {
            return ResultadoAuth.Error("Correo o contraseña incorrectos.")
        }

        sesionDao.guardar(SesionEntity(usuarioId = usuario.id))
        return ResultadoAuth.Exito(usuario.aSesion())
    }

    suspend fun registrar(nombre: String, correo: String, clave: String): ResultadoAuth {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim().lowercase()

        if (nombreLimpio.isBlank() || correoLimpio.isBlank() || clave.isBlank()) {
            return ResultadoAuth.Error("Completa todos los campos.")
        }
        if (!correoLimpio.contains("@") || !correoLimpio.contains(".")) {
            return ResultadoAuth.Error("Ingresa un correo válido.")
        }
        if (clave.length < 4) {
            return ResultadoAuth.Error("La contraseña debe tener al menos 4 caracteres.")
        }
        if (usuarioDao.buscarPorCorreo(correoLimpio) != null) {
            return ResultadoAuth.Error("Ya existe una cuenta con ese correo.")
        }

        val id = usuarioDao.insertar(
            UsuarioEntity(
                nombre = nombreLimpio,
                correo = correoLimpio,
                clave = clave
            )
        )
        sesionDao.guardar(SesionEntity(usuarioId = id))
        return ResultadoAuth.Exito(UsuarioSesion(id, nombreLimpio, correoLimpio))
    }

    suspend fun sesionActiva(): UsuarioSesion? {
        val sesion = sesionDao.obtener() ?: return null
        val usuario = usuarioDao.buscarPorId(sesion.usuarioId)
        if (usuario == null) {
            sesionDao.eliminar()
            return null
        }
        return usuario.aSesion()
    }

    suspend fun cerrarSesion() {
        sesionDao.eliminar()
    }

    fun entrarDemo(): ResultadoAuth = ResultadoAuth.Exito(
        UsuarioSesion(
            id = 0,
            nombre = "Persona Demo",
            correo = "demo@notariax.cl"
        )
    )

    private fun UsuarioEntity.aSesion(): UsuarioSesion =
        UsuarioSesion(id = id, nombre = nombre, correo = correo)
}
