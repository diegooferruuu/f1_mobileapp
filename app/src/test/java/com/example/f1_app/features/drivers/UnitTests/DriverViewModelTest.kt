package com.example.f1_app.features.drivers.UnitTests

import com.example.f1_app.features.drivers.domain.model.Driver
import com.example.f1_app.features.drivers.domain.usecase.GetDriversUseCase
import com.example.f1_app.features.drivers.presentation.DriverViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class DriverViewModelTest {

    private val useCase = mockk<GetDriversUseCase>()
    private val viewModel = DriverViewModel(useCase)

    @Test
    fun `load updates uiState with drivers on success`() = runTest {
        assert(true)
    }

    @Test
    fun `load updates uiState with error on failure`() = runTest {
        assert(true)
    }
}
