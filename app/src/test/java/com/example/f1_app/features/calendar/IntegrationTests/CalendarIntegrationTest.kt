package com.example.f1_app.features.calendar.IntegrationTests

import androidx.compose.foundation.layout.size
import com.example.f1_app.features.calendar.domain.model.Meeting
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository
import com.example.f1_app.features.calendar.domain.usecase.GetMeetingsUseCase
import com.example.f1_app.features.calendar.presentation.CalendarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.OffsetDateTime

// 1. Usa KoinTest para facilitar la inyección con `inject()`
@OptIn(ExperimentalCoroutinesApi::class)
class CalendarIntegrationTest : KoinTest {

    // 2. Inyecta el ViewModel directamente desde el grafo de Koin
    private val viewModel: CalendarViewModel by inject()
    private val testDispatcher = StandardTestDispatcher()

    // Datos de prueba
    private val mockMeeting = Meeting(
        meetingKey = 1,
        circuitShortName = "SPA",
        meetingCode = "BEL",
        location = "Spa",
        countryCode = "BE",
        countryName = "Belgium",
        meetingName = "Belgian GP",
        dateStart = OffsetDateTime.parse("2025-08-25T14:00:00Z"),
        year = 2025
    )

    private val fakeRepository = object : CalendarRepository {
        override suspend fun getMeetings(year: Int): List<Meeting> {
            // Devuelve los datos de prueba cuando se le llame
            return listOf(mockMeeting)
        }
    }

    @Before
    fun setUp() {
        // Establece el dispatcher principal para corutinas de UI
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(module {
                // 4. Provee las dependencias correctamente
                single<CalendarRepository> { fakeRepository }
                single { GetMeetingsUseCase(get()) } // Koin inyectará `fakeRepository` aquí
                factory { CalendarViewModel(get()) }   // Koin inyectará `GetMeetingsUseCase` aquí
            })
        }
    }

    @After
    fun tearDown() {
        // Limpia Koin y el dispatcher después de cada prueba
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `ViewModel integrates correctly with UseCase and returns one month`() = runTest {
        // 5. Llama a la función a probar en el ViewModel inyectado por Koin
        viewModel.load(2025)

        // Asegura que todas las corutinas pendientes se ejecuten
        testDispatcher.scheduler.advanceUntilIdle()

        // 6. Realiza las aserciones sobre el estado del ViewModel
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.months.size) // La aserción original
        assertEquals("AUGUST", state.months.keys.first().name)
        assertEquals(1, state.months.values.first().size)
        assertEquals("Belgian GP", state.months.values.first().first().meetingName)
    }
}
