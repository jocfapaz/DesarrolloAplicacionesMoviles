# Notaria Pass

Aplicación móvil Android para la gestión digital de trámites notariales, desarrollada con **Kotlin**, **Jetpack Compose** (Material 3) y arquitectura **MVVM**, integrando persistencia local mediante **Room** y servicios remotos con **Retrofit** y **Xano**.

---

## 1. Integrantes del Proyecto

- **Josefa Roca**
- **Ignacia Aravena**

---

## 2. Descripción General

**Notaria Pass** permite a los usuarios gestionar trámites notariales de manera digital desde sus dispositivos móviles, facilitando el registro de solicitudes, carga de documentos, validación de identidad mediante distintos tipos de firma y seguimiento en tiempo real del estado de revisión por parte del notario.

---

## 3. Funcionalidades Principales

- **Autenticación y Gestión de Sesión:** Registro de nuevos usuarios e inicio de sesión seguro mediante tokens JWT (`Bearer Token`), manteniendo persistencia local de sesión con Room.
- **Catálogo de Formatos Notariales:** Consulta dinámica de formatos disponibles (Poder Simple, Declaración Jurada, Autorización de Viaje, entre otros) con sus documentos base incluidos.
- **Creación y Edición de Trámites:** Flujo guiado para ingresar los datos del solicitante (nombre, identificador, rol) y generar borradores o solicitudes formales.
- **Gestión Documental:** Adjuntar archivos PDF o imágenes propios del trámite, con validación de metadatos (`nombre`, `mime_type`, `tamano_bytes`).
- **Tipos de Firma y Verificación:** Soporte para firma **Simple**, **Avanzada** y **Notariada** (simulando mecanismos como e-Token y verificación biométrica).
- **Seguimiento y Resolución en Tiempo Real:** Visualización del progreso del trámite (`Borrador`, `En Revisión`, `Con Observaciones`, `Finalizado`), revisión de observaciones del notario y notificaciones.
- **Búsqueda de Notarías:** Consulta de notarías disponibles filtradas por comuna, incluyendo dirección y coordenadas geográficas (`lat`, `lng`).
- **Persistencia Híbrida (Offline-First / Sync):** Almacenamiento local con Room (`NotariaDatabase`) sincronizado con el backend remoto en Xano.

---

## 4. Endpoints Utilizados (API Externa y Microservicios)

La aplicación se conecta al backend desarrollado en **Xano** mediante **Retrofit 2** y **OkHttp**.

- **URL Base:** `https://x8ki-letl-twmt.n7.xano.io/api:JSDg7Jzb/`
- **Autenticación:** Header `Authorization: Bearer <token>` inyectado automáticamente mediante interceptor HTTP.

### Microservicio de Autenticación (`/auth`)

| Método | Endpoint | Descripción | Payload / Parámetros |
| :--- | :--- | :--- | :--- |
| `POST` | `auth/register` | Registra un nuevo usuario y retorna el token de acceso. | `nombre`, `correo`, `clave` |
| `POST` | `auth/login` | Autentica al usuario y obtiene el `authToken`. | `correo`, `clave` |

### Microservicio de Trámites y Formatos (`/formato`, `/tramite`)

| Método | Endpoint | Descripción | Payload / Parámetros |
| :--- | :--- | :--- | :--- |
| `GET` | `formato` | Obtiene el listado de formatos de trámites activos y sus documentos incluidos. | — |
| `GET` | `tramite` | Lista todos los trámites asociados a un usuario. | Query: `usuario_id` |
| `POST` | `tramite` | Crea un nuevo trámite en el sistema. | `usuario_id`, `formato_id`, `solicitante_nombre`, `solicitante_id`, `solicitante_rol` |
| `GET` | `tramite/{id}` | Obtiene el detalle completo de un trámite y sus documentos adjuntos. | Path: `id` |
| `POST` | `tramite/{id}/documentos` | Asocia un documento adjunto a un trámite específico. | Path: `id` / Body: `nombre`, `mime_type`, `tamano_bytes` |
| `POST` | `tramite/{id}/enviar` | Envía el trámite a revisión notarial especificando el tipo de firma. | Path: `id` / Body: `tipo_firma` |
| `GET` | `tramite/{id}/estado` | Consulta el estado y resultado actual de revisión del trámite. | Path: `id` |
| `POST` | `tramite/{id}/resolucion` | Registra la resolución u observación del notario sobre el trámite. | Path: `id` / Body: `resultado`, `comentario` |

### Microservicio de Notarías y Notificaciones (`/notaria`, `/notificaciones`)

| Método | Endpoint | Descripción | Payload / Parámetros |
| :--- | :--- | :--- | :--- |
| `GET` | `notaria` | Obtiene las notarías disponibles filtradas por comuna (incluye geolocalización). | Query: `comuna` |
| `POST` | `notificaciones` | Envía o registra una notificación asociada a un trámite. | `tramite_id`, `mensaje` |

---

## 5. Arquitectura y Tecnologías

- **Lenguaje:** Kotlin
- **Interfaz de Usuario:** Jetpack Compose + Material 3 + Material Icons Extended
- **Arquitectura:** MVVM (Model - View - ViewModel)
- **Red y Serialización:** Retrofit 2, OkHttp Logging Interceptor, Gson
- **Persistencia Local:** Room Database (con KSP)
- **Pruebas Unitarias:** JUnit 4 (`EstadoTramiteTest`, `ReglasDeNegocioModelTest`, `DtoMappingTest`, `XanoPayloadTest`)

### Configuración del Módulo Android
- **Namespace / Application ID:** `com.example.comduocnotariapass`
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Compile SDK:** 37
- **Versión:** 1.0 (`versionCode 1`)

---

## 6. Pasos para Ejecutar el Proyecto

### Prerrequisitos
- **Android Studio** (Hedgehog, Iguana, Jellyfish o superior)
- **JDK 11** o superior configurado en el entorno
- **Android SDK** con API 24+ instalada
- Conexión a internet para consumir los servicios de Xano

### Instrucciones de Instalación
1. **Clonar el repositorio:**
   ```bash
   git clone <url-del-repositorio>
   cd DesarrolloAplicacionesMoviles
   ```
2. **Abrir en Android Studio:**
   - Seleccionar `File > Open` y elegir la carpeta raíz del proyecto.
3. **Sincronizar dependencias:**
   - Esperar a que Gradle descargue las dependencias y compile las anotaciones de Room (`KSP`). Si no inicia automáticamente, presionar **Sync Project with Gradle Files**.
4. **Ejecutar la aplicación:**
   - Seleccionar un emulador Android (API 24+) o conectar un dispositivo físico con la depuración USB activada.
   - Presionar el botón **Run** (`Shift + F10`).

### Ejecución mediante Terminal (Opcional)
- **Compilar APK de depuración:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Ejecutar pruebas unitarias:**
  ```bash
  ./gradlew testDebugUnitTest
  ```

---

## 7. Evidencia de Compilación: APK Firmado y Keystore (`.jks`)

> **Nota:** Reemplaza las rutas de las imágenes a continuación con las capturas almacenadas en tu carpeta de evidencias (por ejemplo, `docs/evidencias/`).

### Generación del Keystore (`.jks`)
Archivo de firma digital generado desde Android Studio (`Build > Generate Signed Bundle / APK`) para la distribución de la aplicación:

![Evidencia Keystore .jks](docs/evidencias/captura_jks.png)

### APK Firmado (`app-release.apk`)
Evidencia de la generación exitosa del paquete de instalación firmado listo para despliegue:

