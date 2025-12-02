# Guía: Agregar Funcionalidad de Logout

## Cómo Implementar el Botón de Cerrar Sesión

Para permitir que los usuarios cierren sesión, puedes agregar un botón en cualquier pantalla (recomendado: HomeScreen o una pantalla de perfil).

---

## Opción 1: Agregar Logout en HomeScreen

### Paso 1: Crear LogoutUseCase

Crear archivo: `/features/auth/domain/usecase/LogoutUseCase.kt`

```kotlin
package com.example.f1_app.features.auth.domain.usecase

import com.example.f1_app.features.auth.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}
```

### Paso 2: Actualizar AuthModule

En `/features/auth/di/AuthModule.kt`, agregar:

```kotlin
// En los imports
import com.example.f1_app.features.auth.domain.usecase.LogoutUseCase

// En la sección de Use cases
factory {
    LogoutUseCase(get())
}
```

### Paso 3: Actualizar HomeViewModel

En `/features/home/presentation/HomeViewModel.kt`:

```kotlin
// Agregar en el constructor
class HomeViewModel(
    private val getSeasonOverviewUseCase: GetSeasonOverviewUseCase,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val logoutUseCase: LogoutUseCase  // ← Agregar esto
) : ViewModel() {

    // ...código existente...

    // Agregar esta función
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val result = logoutUseCase()
                if (result.isSuccess) {
                    onLogoutSuccess()
                }
            } catch (e: Exception) {
                // Manejar error si es necesario
            }
        }
    }
}
```

### Paso 4: Actualizar HomeModule

En `/features/home/di/HomeModule.kt`:

```kotlin
// En el viewModel
viewModel {
    HomeViewModel(
        get(),
        get(),
        get()  // ← LogoutUseCase
    )
}
```

### Paso 5: Agregar Botón en HomeScreen

En `/features/home/presentation/HomeScreen.kt`:

```kotlin
// Agregar estos imports
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController

// Modificar la función HomeScreen para recibir navController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,  // ← Agregar parámetro
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()
    val title = viewModel.title.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.value) },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.logout {
                                // Navegar al login después del logout
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                // ...resto del código
        ) {
            // ...contenido existente...
        }
    }
}
```

---

## Opción 2: Logout con Diálogo de Confirmación

Para una mejor UX, puedes mostrar un diálogo de confirmación:

```kotlin
@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar Sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sí, cerrar sesión")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// En HomeScreen
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    // ...resto del código con IconButton que muestra el diálogo
    IconButton(onClick = { showLogoutDialog = true }) {
        Icon(Icons.Default.ExitToApp, "Cerrar Sesión")
    }
}
```

---

## Opción 3: Pantalla de Perfil con Logout

Crear una nueva pantalla de perfil completa:

```kotlin
// ProfileScreen.kt
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Información del usuario
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nombre: ${user?.displayName ?: "Usuario"}")
                Text("Email: ${user?.email ?: ""}")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de logout
        Button(
            onClick = {
                viewModel.logout {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}
```

---

## Actualizar BottomNavGraph

Si usas la Opción 3, actualiza el `BottomNavGraph.kt` para incluir la ruta de perfil:

```kotlin
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainNavController: NavController  // ← Agregar para navegación al login
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { 
            HomeScreen(mainNavController) 
        }
        composable("profile") { 
            ProfileScreen(mainNavController) 
        }
        // ...otras rutas
    }
}
```

---

## Flujo Completo del Logout

```
1. Usuario presiona botón "Cerrar Sesión"
   ↓
2. (Opcional) Se muestra diálogo de confirmación
   ↓
3. Usuario confirma
   ↓
4. Se llama a logoutUseCase()
   ↓
5. Firebase elimina el token de autenticación
   ↓
6. Se navega a la pantalla de login
   ↓
7. Próxima vez que abra la app → Verá el login
```

---

## Testing del Logout

### Test 1: Logout Básico
1. Iniciar sesión en la app
2. Navegar a donde esté el botón de logout
3. Presionar "Cerrar Sesión"
4. Deberías ver la pantalla de Login

### Test 2: Persistencia después del Logout
1. Hacer logout (Test 1)
2. Cerrar completamente la app
3. Volver a abrir la app
4. **Resultado esperado**: Deberías ver la pantalla de Login (no la principal)

### Test 3: Login después del Logout
1. Hacer logout
2. Iniciar sesión nuevamente
3. Deberías poder entrar normalmente

---

## Recomendación

Para la mejor experiencia de usuario, te recomiendo:

1. **Opción 1** si quieres algo rápido y simple
2. **Opción 2** si quieres evitar logouts accidentales
3. **Opción 3** si planeas tener una pantalla de perfil completa

La **Opción 2** (con diálogo) es la más profesional y previene cierres de sesión accidentales.

---

## Código Mínimo para Logout Rápido

Si solo quieres probar el logout rápidamente, agrega esto temporalmente en cualquier pantalla:

```kotlin
Button(
    onClick = {
        Firebase.auth.signOut()
        // Navegar manualmente al login
    }
) {
    Text("LOGOUT (TEMP)")
}
```

Pero te recomiendo usar el approach con use cases para mantener la arquitectura limpia.

