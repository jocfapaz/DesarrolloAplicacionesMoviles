# NotariaPass

Aplicación móvil Android para la gestión digital de trámites notariales, desarrollada con **Jetpack Compose**, **Kotlin** y arquitectura **MVVM** con **Room** para persistencia local.

## Descripción

NotariaPass permite a los usuarios gestionar trámites notariales desde un dispositivo móvil, con funcionalidades para autenticación, seguimiento del estado de trámites, gestión de documentos y más. La aplicación utiliza una base de datos local con Room para almacenar usuarios, trámites, sesiones y documentos.

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Base de datos local**: Room (con KSP)
- **Componentes**: AndroidX Lifecycle, Activity Compose
- **Iconos**: Material Icons Extended
- **Build**: Gradle (Kotlin DSL)

## Estructura del Proyecto

```
app/src/main/java/com/example/comduocnotariapass/
├── MainActivity.kt           # Punto de entrada de la aplicación
├── model/                    # Modelos de datos
│   ├── EstadoTramite.kt
│   ├── EstadoPersistido.kt
│   ├── ObservacionNotario.kt
│   ├── TramiteDraft.kt
│   └── TramiteModel.kt
├── repository/               # Repositorios y lógica de datos
│   ├── AuthRepository.kt
│   ├── TramiteRepository.kt
│   └── local/               # Capa de persistencia con Room
│       ├── NotariaDatabase.kt
│       ├── TramiteDao.kt
│       ├── UsuarioDao.kt
│       ├── SesionDao.kt
│       └── Entidades (UsuarioEntity, TramiteEntity, etc.)
├── ui/                      # Pantallas y componentes Compose
├── util/                    # Utilidades y helpers
└── viewmodel/               # ViewModels
```

## Requisitos

- Android Studio (recomendado Hedgehog o superior)
- JDK 11
- Android SDK 24+ (minSdk) / 36 (targetSdk)
- Gradle

## Instalación y Ejecución

1. Clonar el repositorio:
```bash
git clone <url-del-repositorio>
cd DesarrolloAplicacionesMoviles
```

2. Abrir el proyecto con Android Studio

3. Sincronizar Gradle (Android Studio lo hará automáticamente)

4. Ejecutar la aplicación en un emulador o dispositivo físico

También puedes compilar desde línea de comandos:
```bash
./gradlew assembleDebug
```

## Configuración

- **Namespace**: `com.example.comduocnotariapass`
- **Application ID**: `com.example.comduocnotariapass`
- **Versión**: 1.0 (versionCode 1)
- **Compile SDK**: 37
- **Min SDK**: 24
- **Target SDK**: 36

## Características

- Autenticación de usuarios
- Gestión y seguimiento de trámites notariales
- Almacenamiento local con Room
- Interfaz moderna con Jetpack Compose y Material 3
- Arquitectura MVVM para separación de responsabilidades

## Licencia

Este proyecto es de uso académico.
