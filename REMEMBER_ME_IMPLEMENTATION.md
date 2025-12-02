# Implementación de Persistencia de Sesión (Remember Me)

## Resumen
Se ha implementado la funcionalidad de persistencia de sesión para que los usuarios permanezcan autenticados después de cerrar y volver a abrir la aplicación.

## ¿Cómo Funciona?

Firebase Authentication automáticamente persiste la sesión del usuario. Cuando un usuario inicia sesión exitosamente, Firebase guarda el token de autenticación de forma segura en el dispositivo. Este token se mantiene hasta que:
- El usuario cierra sesión explícitamente
- El token expira (poco común en Firebase)
- Se desinstala la aplicación

## Componentes Implementados

### 1. Nuevos Use Cases

#### `IsUserLoggedInUseCase.kt`
```kotlin
class IsUserLoggedInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}
```
- Verifica si hay un usuario autenticado actualmente
- Retorna `true` si el usuario está logueado, `false` en caso contrario

#### `GetCurrentUserUseCase.kt`
```kotlin
class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): User? {
        return repository.getCurrentUser()
    }
}
```
- Obtiene la información del usuario actualmente autenticado
- Retorna `null` si no hay usuario autenticado

### 2. MainViewModel

**Ubicación**: `/app/src/main/java/com/example/f1_app/presentation/MainViewModel.kt`

Este ViewModel gestiona el estado de autenticación al iniciar la aplicación:

```kotlin
class MainViewModel(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            try {
                _isUserLoggedIn.value = isUserLoggedInUseCase()
            } catch (e: Exception) {
                _isUserLoggedIn.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}
```

**Estados manejados**:
- `isLoading`: Indica si se está verificando el estado de autenticación
- `isUserLoggedIn`: Indica si el usuario está autenticado

### 3. Actualización del MainActivity

La función `AppNavigationHost()` ahora:

1. **Obtiene el ViewModel**:
```kotlin
val viewModel: MainViewModel = koinViewModel()
val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()
```

2. **Muestra una pantalla de carga** mientras verifica el estado:
```kotlin
if (isLoading) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
    return
}
```

3. **Determina la pantalla inicial** basándose en el estado de autenticación:
```kotlin
val startDestination = if (isUserLoggedIn) "main" else "login"
```

### 4. Actualización del AuthModule

Se agregaron los nuevos componentes al módulo de inyección de dependencias:

```kotlin
// Use cases
factory {
    IsUserLoggedInUseCase(get())
}
factory {
    GetCurrentUserUseCase(get())
}

// ViewModels
viewModel {
    MainViewModel(get())
}
```

## Flujo de Usuario

### Primera vez (Sin sesión guardada)
1. Usuario abre la app
2. `MainViewModel` verifica el estado de autenticación
3. No hay usuario logueado → Muestra pantalla de Login
4. Usuario ingresa credenciales y hace login
5. Firebase guarda el token de autenticación
6. Usuario navega a la pantalla principal

### Abriendo la app de nuevo (Con sesión guardada)
1. Usuario abre la app
2. `MainViewModel` verifica el estado de autenticación
3. Firebase detecta el token guardado → Usuario está logueado
4. **Saltea automáticamente la pantalla de login**
5. Usuario navega directamente a la pantalla principal

### Cerrar sesión
Para cerrar sesión, el usuario puede:
1. Usar un botón de "Cerrar Sesión" (puede implementarse en cualquier pantalla)
2. Esto llamará a `AuthRepository.logout()`
3. Firebase eliminará el token guardado
4. La próxima vez que abra la app, verá la pantalla de login

## Ventajas de esta Implementación

1. **Experiencia de Usuario Mejorada**: Los usuarios no necesitan iniciar sesión cada vez que abren la app
2. **Seguridad**: Firebase maneja el almacenamiento seguro de tokens
3. **Simplicidad**: La lógica es clara y fácil de mantener
4. **Escalabilidad**: Fácil de extender con funcionalidades adicionales (biometría, etc.)

## Notas Técnicas

- **Persistencia Automática**: Firebase Auth maneja automáticamente la persistencia del token
- **Verificación Asíncrona**: La verificación del estado de autenticación es asíncrona para no bloquear la UI
- **Loading State**: Se muestra un indicador de carga durante la verificación inicial

## Próximos Pasos Recomendados

1. **Agregar botón de logout**: Implementar en la pantalla de perfil o configuración
2. **Timeout de sesión**: Configurar un tiempo máximo de inactividad (opcional)
3. **Refresco de token**: Firebase lo maneja automáticamente, pero se puede personalizar
4. **Biometría**: Agregar autenticación biométrica para mayor seguridad

