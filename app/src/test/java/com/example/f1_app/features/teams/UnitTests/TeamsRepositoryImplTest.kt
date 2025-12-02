package com.example.f1_app.features.teams.UnitTests

import com.example.f1_app.features.teams.data.datasource.TeamsRemoteDataSource
import com.example.f1_app.features.teams.data.api.dto.TeamDto
import com.example.f1_app.features.teams.data.repository.TeamsRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class TeamsRepositoryImplTest {

    // 1. Usamos mockk() en lugar de mock()
    private val remote = mockk<TeamsRemoteDataSource>()
    private val repository = TeamsRepositoryImpl(remote)

    @Test
    fun `repository maps dto to domain model correctly`() = runTest {
        // Arrange
        val dtoList = listOf(
            TeamDto(1, "Ferrari", "logo.png", "Maranello", "Vasseur")
        )

        // 2. Usamos coEvery { ... } returns ... en lugar de when(...).thenReturn(...)
        // "coEvery" se usa porque getTeams es una función suspendida (corutina)
        coEvery { remote.getTeams() } returns dtoList

        // Act
        val result = repository.getTeams()

        // Assert
        assertEquals(1, result.size)
        val team = result[0]

        assertEquals(1, team.id)
        assertEquals("Ferrari", team.name)
        assertEquals("logo.png", team.logo)
        assertEquals("Maranello", team.base)
        assertEquals("Vasseur", team.director)

        // 3. Usamos coVerify en lugar de verify
        coVerify { remote.getTeams() }
    }

    @Test
    fun `repository applies default values when fields are null`() = runTest {
        // Arrange
        val dtoList = listOf(
            TeamDto(null, null, null, null, null)
        )

        coEvery { remote.getTeams() } returns dtoList

        // Act
        val result = repository.getTeams()

        // Assert
        val team = result[0]

        assertEquals(0, team.id)
        assertEquals("Unknown", team.name)
        assertEquals("", team.logo)
        assertEquals("N/A", team.base)
        assertEquals("N/A", team.director)

        coVerify { remote.getTeams() }
    }
}
