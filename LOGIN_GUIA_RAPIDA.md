# 🏎️ Login con Firebase - Guía Rápida

## ✅ Lo que se implementó

He añadido un **sistema completo de login con Firebase Authentication** a tu app de F1. La pantalla de login es ahora la **pantalla inicial** de la aplicación.

## 🎯 Características Principales

1. **Pantalla de Login Profesional**
   - Diseño moderno con Material 3
   - Validaciones en tiempo real
   - Mensajes de error claros en español

2. **Opción de Saltar** ⚠️ (Como solicitaste)
   - Botón **X** en la esquina superior derecha
   - Botón **"Continuar sin iniciar sesión"** en la parte inferior
   - Ambos llevan directamente a la app principal

3. **Validaciones Implementadas**
   - ✅ Email: formato válido requerido
   - ✅ Contraseña: mínimo 6 caracteres
   - ✅ Campos no vacíos

4. **Integración con Firebase Auth**
   - Login funcional con email/password
   - Manejo de errores de Firebase
   - Sesión persistente

## 🚀 Para Probar la App

### Paso 1: Sincronizar Gradle
En Android Studio:
```
File → Sync Project with Gradle Files
```

O desde terminal:
```bash
./gradlew build
```

### Paso 2: Habilitar Authentication en Firebase

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Selecciona tu proyecto
3. Ve a **Authentication** → **Sign-in method**
4. Habilita **Email/Password**

### Paso 3: Crear un Usuario de Prueba

En Firebase Console:
1. Ve a **Authentication** → **Users**
2. Click en **Add user**
3. Agrega:
   - Email: `test@example.com`
   - Password: `123456`

### Paso 4: Ejecutar la App

1. Conecta tu dispositivo o inicia el emulador
2. Click en **Run** (▶️) en Android Studio

## 📱 Flujo de la Aplicación

```
App inicia
    ↓
┌─────────────────────┐
│  Pantalla de Login  │
│                     │
│  [Email]            │
│  [Password]         │
│                     │
│  [Iniciar Sesión]   │
│                     │
│  Continuar sin...   │
└─────────────────────┘
    ↓ (login exitoso o skip)
    ↓
┌─────────────────────┐
│  Pantalla Principal │
│                     │
│  [Home] [Calendar]  │
│  [Teams] [Drivers]  │
│  [Results]          │
└─────────────────────┘
```

## 🧪 Casos de Prueba

### Test 1: Login Exitoso
1. Abre la app
2. Ingresa: `test@example.com`
3. Ingresa: `123456`
4. Click en **Iniciar Sesión**
5. ✅ Deberías ver la pantalla principal

### Test 2: Validaciones
1. Abre la app
2. Intenta login sin datos
3. ✅ Deberías ver errores en rojo

### Test 3: Skip Login (X)
1. Abre la app
2. Click en la **X** arriba a la derecha
3. ✅ Deberías ir directo a la app principal

### Test 4: Skip Login (Botón)
1. Abre la app
2. Click en **"Continuar sin iniciar sesión"**
3. ✅ Deberías ir directo a la app principal

## 📁 Archivos Nuevos Creados

```
features/auth/
├── domain/
│   ├── model/
│   │   └── User.kt ✅
│   ├── repository/
│   │   └── AuthRepository.kt ✅
│   └── usecase/
│       └── LoginUseCase.kt ✅
├── data/
│   ├── datasource/
│   │   └── AuthRemoteDataSource.kt ✅
│   └── repository/
│       └── AuthRepositoryImpl.kt ✅
├── framework/
│   └── FirebaseAuthRemoteDataSource.kt ✅
├── presentation/
│   ├── LoginViewModel.kt ✅
│   └── LoginScreen.kt ✅
└── di/
    └── AuthModule.kt ✅
```

## 📝 Archivos Modificados

- ✅ `app/build.gradle.kts` - Añadido Firebase Auth
- ✅ `gradle/libs.versions.toml` - Añadida versión de Firebase Auth
- ✅ `MainActivity.kt` - Navegación con login inicial
- ✅ `App.kt` - Módulo de auth en Koin

## ⚠️ Importante

- **NO se modificó** ninguna funcionalidad existente
- **NO se eliminó** ningún código
- **SOLO se añadió** la funcionalidad de login
- El login **es opcional** (se puede saltar)

## 🔧 Si algo no funciona

### Error: "Unresolved reference FirebaseAuth"
**Solución:** Sincroniza Gradle (File → Sync Project with Gradle Files)

### Error al hacer login: "INVALID_LOGIN_CREDENTIALS"
**Solución:** 
1. Verifica que Email/Password esté habilitado en Firebase Console
2. Crea un usuario de prueba en Firebase

### La app no compila
**Solución:**
```bash
./gradlew clean
./gradlew build
```

## 📞 Próximos Pasos Opcionales

Si quieres extender la funcionalidad:
- Añadir pantalla de registro (SignUp)
- Recuperación de contraseña
- Login con Google
- Guardar estado de sesión
- Perfil de usuario

## ✨ Resumen

**¡Todo listo!** 🎉

Tu app ahora tiene:
- ✅ Login funcional con Firebase
- ✅ Pantalla de login como inicio
- ✅ Opción de saltar (X y botón)
- ✅ Validaciones completas
- ✅ UI profesional
- ✅ Arquitectura limpia

Solo necesitas **sincronizar Gradle** y **habilitar Email/Password en Firebase** para probarla.

