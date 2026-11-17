package com.example.comduocnotariapass.repository

import com.example.comduocnotariapass.remote.XanoDataSource
import com.example.comduocnotariapass.repository.local.SesionEntity
import com.example.comduocnotariapass.repository.local.SesionDao
import com.example.comduocnotariapass.repository.local.UsuarioDao
import com.example.comduocnotariapass.repository.local.UsuarioEntity
import retrofit2.HttpException
import java.io.IOException

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
    private val sesionDao: SesionDao,
    private val remote: XanoDataSource = XanoDataSource()
) {

    suspend fun iniciarSesion(correo: String, clave: String): ResultadoAuth {
        val correoLimpio = correo.trim().lowercase()
        if (correoLimpio.isBlank() || clave.isBlank()) {
            return ResultadoAuth.Error("Completa el correo y la contraseña.")
        }

        val respuesta = try {
            remote.login(correoLimpio, clave)
        } catch (error: HttpException) {
            if (error.code() == 401) {
                return ResultadoAuth.Error("Correo o contraseña incorrectos.")
            }
            null
        } catch (error: IOException) {
            null
        }
        if (respuesta != null) {
            val usuario = sincronizarUsuario(
                idRemoto = respuesta.id,
                nombre = respuesta.nombre,
                correo = correoLimpio,
                clave = clave
            )
            return ResultadoAuth.Exito(usuario)
        }

        return iniciarSesionLocal(correoLimpio, clave)
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

        val respuesta = try {
            remote.registrar(nombreLimpio, correoLimpio, clave)
        } catch (error: HttpException) {
            if (error.code() == 409) {
                return ResultadoAuth.Error("Ya existe una cuenta con ese correo.")
            }
            null
        } catch (error: IOException) {
            null
        }
        if (respuesta != null) {
            val usuario = sincronizarUsuario(
                idRemoto = respuesta.id,
                nombre = respuesta.nombre.ifBlank { nombreLimpio },
                correo = correoLimpio,
                clave = clave
            )
            return ResultadoAuth.Exito(usuario)
        }

        return registrarLocal(nombreLimpio, correoLimpio, clave)
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
        XanoDataSource.limpiarToken()
        sesionDao.eliminar()
    }

    fun entrarDemo(): ResultadoAuth = ResultadoAuth.Exito(
        UsuarioSesion(
            id = 0,
            nombre = "Persona Demo",
            correo = "demo@notariax.cl"
        )
    )

    private suspend fun sincronizarUsuario(
        idRemoto: Long,
        nombre: String,
        correo: String,
        clave: String
    ): UsuarioSesion {
        val existente = usuarioDao.buscarPorCorreo(correo)
        val idLocal = when {
            existente != null -> existente.id
            idRemoto > 0 -> usuarioDao.insertar(
                UsuarioEntity(id = idRemoto, nombre = nombre, correo = correo, clave = clave)
            ).let { idRemoto }
            else -> usuarioDao.insertar(
                UsuarioEntity(nombre = nombre, correo = correo, clave = clave)
            )
        }
        val usuario = UsuarioSesion(idLocal, nombre, correo)
        sesionDao.guardar(SesionEntity(usuarioId = idLocal))
        return usuario
    }

    private suspend fun iniciarSesionLocal(
        correoLimpio: String,
        clave: String
    ): ResultadoAuth {
        val usuario = usuarioDao.buscarPorCorreo(correoLimpio)
            ?: return ResultadoAuth.Error("Correo o contraseña incorrectos.")

        if (usuario.clave != clave) {
            return ResultadoAuth.Error("Correo o contraseña incorrectos.")
        }

        sesionDao.guardar(SesionEntity(usuarioId = usuario.id))
        return ResultadoAuth.Exito(usuario.aSesion())
    }

    private suspend fun registrarLocal(
        nombreLimpio: String,
        correoLimpio: String,
        clave: String
    ): ResultadoAuth {
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

    private fun UsuarioEntity.aSesion(): UsuarioSesion =
        UsuarioSesion(id = id, nombre = nombre, correo = correo)
}
