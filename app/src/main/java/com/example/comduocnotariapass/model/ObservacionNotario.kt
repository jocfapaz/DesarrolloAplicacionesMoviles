package com.example.comduocnotariapass.model

// Comentario que el notario de prueba deja sobre el trámite observado
data class ComentarioNotario(
    val autor: String,
    val momento: String,
    val texto: String,
    val documento: String? = null
)

// Observación completa asociada a un trámite: comentarios y documentos afectados
data class ObservacionNotario(
    val comentarios: List<ComentarioNotario>,
    val documentosObservados: Set<String>
) {
    fun estaObservado(documento: Documento): Boolean = documento.id in documentosObservados
}

// Observación demo generada a partir de los documentos del formato elegido
fun observacionDemo(formato: FormatoTramite): ObservacionNotario {
    val principal = formato.documentosInclusos.getOrNull(0)
    val anexo = formato.documentosInclusos.getOrNull(1)

    val comentarios = buildList {
        if (principal != null) {
            add(
                ComentarioNotario(
                    autor = "Notaría demo",
                    momento = "Hoy · 10:05",
                    texto = "Revisa la fecha de ${principal.nombre}: no coincide con la fecha de la solicitud.",
                    documento = principal.nombre
                )
            )
        }
        if (anexo != null) {
            add(
                ComentarioNotario(
                    autor = "Notaría demo",
                    momento = "Hoy · 10:06",
                    texto = "Adjunta una versión legible de ${anexo.nombre}.",
                    documento = anexo.nombre
                )
            )
        }
        add(
            ComentarioNotario(
                autor = "Notaría demo",
                momento = "Hoy · 10:08",
                texto = "Completa la comuna del solicitante antes de reenviar."
            )
        )
    }

    return ObservacionNotario(
        comentarios = comentarios,
        documentosObservados = listOfNotNull(principal?.id, anexo?.id).toSet()
    )
}
