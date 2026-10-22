package com.example.comduocnotariapass.model

// Etapas de la línea de progreso del trámite
enum class PasoProgreso(
    val titulo: String,
    val descripcion: String,
    val responsable: String,
    val momento: String
) {
    DOCUMENTO_CARGADO(
        titulo = "Documento cargado",
        descripcion = "Se cargaron los documentos del formato y tus adjuntos propios.",
        responsable = "Sistema",
        momento = "Hoy · 09:10"
    ),
    ANTECEDENTES_VERIFICADOS(
        titulo = "Antecedentes verificados",
        descripcion = "Confirmaste tus datos y la revisión de antecedentes.",
        responsable = "Tú",
        momento = "Hoy · 09:18"
    ),
    ENVIADO_AL_NOTARIO(
        titulo = "Enviado al notario",
        descripcion = "La solicitud se envió al notario de prueba con la firma simulada.",
        responsable = "Sistema",
        momento = "Hoy · 09:22"
    ),
    EN_REVISION(
        titulo = "En revisión",
        descripcion = "El notario de prueba revisa los documentos y la representación de firma.",
        responsable = "Notaría demo",
        momento = "Hoy · 09:25"
    ),
    RESULTADO(
        titulo = "Observaciones/Finalizado",
        descripcion = "El notario publica observaciones o cierra el trámite.",
        responsable = "Notaría demo",
        momento = "Pendiente"
    )
}

// Resultado final que puede publicar el notario de prueba
enum class ResultadoRevision(
    val titulo: String,
    val etiqueta: String,
    val descripcion: String,
    val momento: String
) {
    OBSERVACIONES(
        titulo = "Observaciones",
        etiqueta = "Con observaciones",
        descripcion = "El notario de prueba pidió corregir la comuna y reemplazar el anexo demo.",
        momento = "Hoy · 09:30"
    ),
    FINALIZADO(
        titulo = "Finalizado",
        etiqueta = "Finalizado",
        descripcion = "El notario de prueba cerró el trámite y registró la firma simulada.",
        momento = "Hoy · 09:35"
    )
}

// Estado del trámite mostrado en el encabezado y en la línea de progreso
data class EstadoTramite(
    val id: String = "TRX-2048",
    val resultado: ResultadoRevision? = null
) {
    // Sin resultado el trámite queda "En revisión"; con resultado avanza al último paso
    val indiceActual: Int
        get() = if (resultado == null) {
            PasoProgreso.EN_REVISION.ordinal
        } else {
            PasoProgreso.RESULTADO.ordinal
        }

    val etiquetaEstado: String
        get() = resultado?.etiqueta ?: "En revisión"

    fun estaCompletado(paso: PasoProgreso): Boolean = paso.ordinal < indiceActual

    fun esActual(paso: PasoProgreso): Boolean = paso.ordinal == indiceActual

    fun esTocable(paso: PasoProgreso): Boolean = paso.ordinal <= indiceActual

    fun tituloDe(paso: PasoProgreso): String =
        if (paso == PasoProgreso.RESULTADO) resultado?.titulo ?: paso.titulo else paso.titulo

    fun descripcionDe(paso: PasoProgreso): String =
        if (paso == PasoProgreso.RESULTADO) {
            resultado?.descripcion ?: paso.descripcion
        } else {
            paso.descripcion
        }

    fun momentoDe(paso: PasoProgreso): String =
        if (paso == PasoProgreso.RESULTADO) resultado?.momento ?: paso.momento else paso.momento

    fun siguienteResultado(): ResultadoRevision = when (resultado) {
        null -> ResultadoRevision.OBSERVACIONES
        ResultadoRevision.OBSERVACIONES -> ResultadoRevision.FINALIZADO
        ResultadoRevision.FINALIZADO -> ResultadoRevision.OBSERVACIONES
    }

    fun textoBotonDemo(): String = when (resultado) {
        null -> "Simular observación del notario"
        ResultadoRevision.OBSERVACIONES -> "Simular finalización"
        ResultadoRevision.FINALIZADO -> "Simular observación"
    }
}
