package com.example.f1_app.features.home.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.f1_app.features.home.domain.model.HomeOverview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysDataFromViewModel() {
        // Given
        val homeOverview = HomeOverview(
            racesCompleted = 10,
            driversOnPodium = 5,
            safetyCars = 2,
            nextRace = "Monaco GP",
            weather = "Sunny",
            firstPractice = "Friday 10:00"
        )
        val title = "F1 Test Title"

        val stateFlow = MutableStateFlow(homeOverview)
        val titleFlow = MutableStateFlow(title)

        // When
        composeTestRule.setContent {
            HomeScreen(
                state = stateFlow,
                title = titleFlow,
                onCardClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText("10").assertExists()
        composeTestRule.onNodeWithText("5").assertExists()
        composeTestRule.onNodeWithText("2").assertExists()
        composeTestRule.onNodeWithText("Next Race: Monaco GP").assertExists()
        composeTestRule.onNodeWithText("Weather: Sunny").assertExists()
        composeTestRule.onNodeWithText("First Practice: Friday 10:00").assertExists()
    }
}
