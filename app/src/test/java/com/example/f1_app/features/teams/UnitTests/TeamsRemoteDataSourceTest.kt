package com.example.f1_app.features.teams.UnitTests

import com.example.f1_app.features.teams.data.api.TeamsApiService
import com.example.f1_app.features.teams.data.api.dto.TeamDto
import com.example.f1_app.features.teams.data.api.dto.TeamsResponseWrapper
import com.example.f1_app.features.teams.data.datasource.TeamsRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class TeamsRemoteDataSourceTest {

    private val api = mock(TeamsApiService::class.java)
    private val dataSource = TeamsRemoteDataSource(api)

    @Test
    fun `getTeams returns only teams with non-null base`() = runTest { // Arrange
        val dtoList = listOf(
            TeamDto(1, "Ferrari", "logo1.png", "Maranello", "Vasseur"),
            TeamDto(2, "Mercedes", "logo2.png", null, "Wolff"), // Este debe ser filtrado
            TeamDto(3, "Red Bull", "logo3.png", "Milton Keynes", "Horner")
        )

        `when`(api.getTeams()).thenReturn(TeamsResponseWrapper(dtoList))

        // Act
        val result = dataSource.getTeams()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Ferrari" })
        assertTrue(result.any { it.name == "Red Bull" })
        assertFalse(result.any { it.name == "Mercedes" })

        verify(api).getTeams()
    }
}
