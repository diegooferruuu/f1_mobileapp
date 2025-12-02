# 🔥 Firebase Remote Config - Implementación Completa

## ✅ Implementación Realizada

Se ha implementado **Firebase Remote Config** para gestionar el título "F1 Hub" de forma dinámica desde Firebase Console, siguiendo Clean Architecture.

---

## 📁 Estructura Creada

### Archivos Nuevos (5 archivos)

```
core/config/
├── domain/
│   ├── RemoteConfigRepository.kt ✅
│   └── usecase/
│       ├── GetRemoteConfigStringUseCase.kt ✅
│       └── FetchRemoteConfigUseCase.kt ✅
├── data/
│   └── RemoteConfigRepositoryImpl.kt ✅
└── di/
    └── RemoteConfigModule.kt ✅
```

### Archivos Modificados (4 archivos)

```
✅ App.kt - Agregado remoteConfigModule
✅ HomeViewModel.kt - Integrado Remote Config
✅ HomeScreen.kt - Usa título del ViewModel
✅ di/modules.kt - Actualizados parámetros del ViewModel
✅ app/build.gradle.kts - Dependencia firebase-config
✅ gradle/libs.versions.toml - Versión de Remote Config
```

---

## 🎯 Cómo Funciona

### 1. Flujo de Datos

```
Firebase Remote Config (Cloud)
        ↓
RemoteConfigRepositoryImpl
        ↓
GetRemoteConfigStringUseCase
        ↓
HomeViewModel
        ↓
HomeScreen (UI)
```

### 2. Proceso de Actualización

```kotlin
1. App inicia
2. HomeViewModel se crea
3. loadRemoteConfig() ejecuta:
   - fetchRemoteConfigUseCase() → Obtiene valores de Firebase
   - getRemoteConfigStringUseCase("title") → Lee el valor
4. _title.value se actualiza
5. HomeScreen muestra el nuevo título
```

---

## 🔧 Configuración

### Valores por Defecto

En `RemoteConfigRepositoryImpl.kt`:
```kotlin
remoteConfig.setDefaultsAsync(
    mapOf(
        "title" to "F1 Hub"
    )
)
```

**Valor por defecto:** "F1 Hub" (si no hay conexión o Firebase no responde)

### Intervalo de Fetch

```kotlin
minimumFetchIntervalInSeconds = 3600 // 1 hora
```

**Para desarrollo, cambiar a:**
```kotlin
minimumFetchIntervalInSeconds = 0 // Fetch inmediato
```

---

## 🚀 Configuración en Firebase Console

### Paso 1: Ir a Firebase Console
```
https://console.firebase.google.com/
```

### Paso 2: Seleccionar tu Proyecto
```
f1_mobileapp (o el nombre de tu proyecto)
```

### Paso 3: Ir a Remote Config
```
Menú lateral → Remote Config
```

### Paso 4: Crear Parámetro
```
1. Click en "Agregar parámetro"
2. Nombre del parámetro: title
3. Tipo: String
4. Valor por defecto: F1 Hub
5. Click en "Agregar parámetro"
```

### Paso 5: Publicar
```
Click en "Publicar cambios"
```

---

## 🧪 Pruebas

### Test 1: Valor por Defecto
```
1. No configurar nada en Firebase
2. Abrir la app
3. Ver HomeScreen
✅ Debe mostrar "F1 Hub"
```

### Test 2: Valor desde Firebase
```
1. Configurar en Firebase:
   - title = "Formula 1 Hub"
2. Publicar cambios
3. Cerrar y abrir la app
4. Ver HomeScreen
✅ Debe mostrar "Formula 1 Hub"
```

### Test 3: Cambio en Vivo
```
1. App corriendo con título actual
2. Cambiar título en Firebase:
   - title = "F1 Racing Hub"
3. Publicar cambios
4. Cerrar y reabrir la app
✅ Debe mostrar "F1 Racing Hub"
```

### Test 4: Sin Conexión
```
1. Activar modo avión
2. Abrir la app
✅ Debe mostrar último valor cacheado
   o valor por defecto "F1 Hub"
```

---

## 📝 Valores que Puedes Configurar

### Parámetro: `title`

**Tipo:** String  
**Ubicación en código:** HomeScreen.kt  
**Uso:** Título principal de la pantalla Home

**Ejemplos de valores:**
```
- "F1 Hub"
- "Formula 1 Hub"
- "F1 Racing Central"
- "🏎️ F1 Hub"
- "F1 Companion"
```

### Agregar Más Parámetros

Si quieres agregar más valores configurables:

```kotlin
// 1. Agregar valor por defecto en RemoteConfigRepositoryImpl:
remoteConfig.setDefaultsAsync(
    mapOf(
        "title" to "F1 Hub",
        "subtitle" to "Welcome to F1",  // Nuevo
        "show_banner" to true            // Nuevo
    )
)

// 2. Crear use case si es necesario
// 3. Usar en ViewModel
// 4. Mostrar en UI
```

---

## 🎨 Características Implementadas

### ✅ Clean Architecture
```
✅ Domain Layer - Repository + Use Cases
✅ Data Layer - Implementation
✅ DI Layer - Koin Module
✅ Presentation Layer - ViewModel + UI
```

### ✅ Funcionalidades
```
✅ Fetch and activate automático
✅ Valores por defecto
✅ Caché de valores
✅ Manejo de errores
✅ Coroutines para async
✅ StateFlow para reactive UI
```

### ✅ Repository Pattern
```kotlin
interface RemoteConfigRepository {
    suspend fun getString(key: String): String
    suspend fun getBoolean(key: String): Boolean
    suspend fun getLong(key: String): Long
    suspend fun getDouble(key: String): Double
    suspend fun fetchAndActivate(): Boolean
}
```

**Métodos disponibles:**
- `getString()` - Para textos
- `getBoolean()` - Para flags
- `getLong()` - Para números enteros
- `getDouble()` - Para números decimales
- `fetchAndActivate()` - Para actualizar valores

---

## 🔍 Debugging

### Ver Logs de Remote Config

```kotlin
// En RemoteConfigRepositoryImpl, agregar logs:
override suspend fun fetchAndActivate(): Boolean {
    return try {
        val success = remoteConfig.fetchAndActivate().await()
        android.util.Log.d("RemoteConfig", "Fetch success: $success")
        success
    } catch (e: Exception) {
        android.util.Log.e("RemoteConfig", "Fetch error", e)
        false
    }
}
```

### Ver Valor Actual

```kotlin
// En HomeViewModel:
private fun loadRemoteConfig() {
    viewModelScope.launch {
        fetchRemoteConfigUseCase()
        val remoteTitle = getRemoteConfigStringUseCase("title")
        android.util.Log.d("HomeViewModel", "Title from Remote Config: $remoteTitle")
        _title.value = remoteTitle
    }
}
```

---

## 💡 Mejores Prácticas

### 1. Valores por Defecto Siempre
```kotlin
// ✅ Bien - Con valores por defecto
remoteConfig.setDefaultsAsync(
    mapOf("title" to "F1 Hub")
)

// ❌ Mal - Sin valores por defecto
// Si falla el fetch, no hay fallback
```

### 2. Fetch al Inicio
```kotlin
// ✅ Bien - En init del ViewModel
init {
    loadRemoteConfig()
}

// ❌ Mal - Cada vez que se renderiza
@Composable
fun Screen() {
    viewModel.loadRemoteConfig() // No hacer esto
}
```

### 3. Intervalo de Fetch
```kotlin
// Desarrollo
minimumFetchIntervalInSeconds = 0

// Producción
minimumFetchIntervalInSeconds = 3600 // 1 hora
```

### 4. Manejo de Errores
```kotlin
// ✅ Bien - Con try-catch
override suspend fun fetchAndActivate(): Boolean {
    return try {
        remoteConfig.fetchAndActivate().await()
    } catch (e: Exception) {
        false // Usa valores por defecto
    }
}
```

---

## 🎯 Casos de Uso Comunes

### 1. A/B Testing
```kotlin
// Firebase Console:
title = "F1 Hub" (50% usuarios)
title = "Formula 1 Central" (50% usuarios)
```

### 2. Feature Flags
```kotlin
// En Remote Config:
show_new_feature = true/false

// En código:
val showFeature = getRemoteConfigBooleanUseCase("show_new_feature")
if (showFeature) {
    // Mostrar feature
}
```

### 3. Mensajes de Mantenimiento
```kotlin
// En Remote Config:
maintenance_message = "Mantenimiento en curso"
is_maintenance = true

// En código:
if (getRemoteConfigBooleanUseCase("is_maintenance")) {
    val message = getRemoteConfigStringUseCase("maintenance_message")
    // Mostrar mensaje
}
```

### 4. URLs Dinámicas
```kotlin
// En Remote Config:
api_base_url = "https://api.example.com/v2/"

// En código:
val baseUrl = getRemoteConfigStringUseCase("api_base_url")
// Usar en Retrofit
```

---

## 📊 Resumen de Implementación

### Archivos Creados: 5
```
✅ RemoteConfigRepository.kt
✅ RemoteConfigRepositoryImpl.kt
✅ GetRemoteConfigStringUseCase.kt
✅ FetchRemoteConfigUseCase.kt
✅ RemoteConfigModule.kt
```

### Archivos Modificados: 5
```
✅ App.kt
✅ HomeViewModel.kt
✅ HomeScreen.kt
✅ di/modules.kt
✅ build.gradle.kts
```

### Dependencias Agregadas: 1
```
✅ firebase-config:23.0.1
```

---

## 🎉 Estado Final

```
┌─────────────────────────────────┐
│   Firebase Remote Config        │
│                                 │
│  ✅ Configuración completa      │
│  ✅ Clean Architecture          │
│  ✅ Valores por defecto         │
│  ✅ Fetch automático            │
│  ✅ Cache habilitado            │
│  ✅ Manejo de errores           │
│  ✅ UI reactiva                 │
│                                 │
│  ⏳ Falta: Gradle Sync          │
│  ⏳ Falta: Configurar Firebase  │
└─────────────────────────────────┘
```

---

## 🚀 Próximos Pasos

### 1. Sincronizar Gradle
```
File → Sync Project with Gradle Files
```

### 2. Configurar en Firebase Console
```
1. Ir a Remote Config
2. Crear parámetro "title"
3. Establecer valor
4. Publicar
```

### 3. Probar
```
1. Ejecutar app
2. Ver título en HomeScreen
3. Cambiar valor en Firebase
4. Reiniciar app
5. Ver nuevo título
```

---

## ✅ ¡Implementación Completa!

Firebase Remote Config está completamente integrado y listo para usar. Solo necesitas:
1. Sincronizar Gradle
2. Configurar el parámetro en Firebase Console
3. ¡Disfrutar de configuración remota! 🎉

