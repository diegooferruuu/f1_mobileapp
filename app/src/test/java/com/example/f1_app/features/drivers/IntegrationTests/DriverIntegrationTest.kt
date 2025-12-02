package com.example.f1_app.features.drivers.IntegrationTests

// Asegúrate de tener este import, si no lo tienes, el IDE te lo pedirá
import com.example.f1_app.features.drivers.domain.model.Driver
import com.example.f1_app.features.drivers.domain.repository.DriverRepository
import com.example.f1_app.features.drivers.domain.usecase.GetDriversUseCase
import com.example.f1_app.features.drivers.presentation.DriverViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class DriverIntegrationTest : KoinTest {

    // Datos de prueba
    private val mockDriver = Driver(
        id = 44,
        name = "Lewis Hamilton",
        image = "https://example.com/hamilton.jpg",
        nationality = "British",
        abbr = "HAM",
        number = 44,
        teamName = "Mercedes"
    )

    // 1. SOLUCIÓN: Crear un Repositorio Falso.
    // Esto cumple con lo que 'GetDriversUseCase' espera recibir.
    private val fakeRepository = object : DriverRepository {
        override suspend fun getDrivers(year: Int): List<Driver> {
            return listOf(mockDriver)
        }

        // Si la interfaz DriverRepository tiene más métodos (ej. getDriverById),
        // tendrás que implementarlos aquí también, aunque sea devolviendo null o datos vacíos.
    }

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val testModule = module {
            // 2. Inyectamos el repositorio falso
            single<DriverRepository> { fakeRepository }
            // 3. El UseCase recibe el repositorio falso automáticamente gracias a Koin
            single { GetDriversUseCase(get()) }
            factory { DriverViewModel(get()) }
        }

        startKoin { modules(testModule) }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `ViewModel integrates with GetDriversUseCase`() = runTest {
        // 4. Obtenemos el ViewModel ya construido por Koin (más limpio que hacerlo a mano)
        val vm: DriverViewModel by inject()

        vm.load(year = 2025)

        // Avanzamos el tiempo para que se ejecute la corrutina
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value

        // Verificaciones
        assertEquals(1, state.drivers.size)
        assertEquals("Lewis Hamilton", state.drivers.first().name)
    }
}
