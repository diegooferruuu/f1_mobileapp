//package com.example.f1_app.features.home.data.repository
//
//import com.example.f1_app.features.home.data.datasource.HomeRemoteDataSource
//import com.example.f1_app.features.home.domain.model.HomeOverview
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class HomeRepositoryImplTest {
//
//    private lateinit var homeRemoteDataSource: HomeRemoteDataSource
//    private lateinit var repository: HomeRepositoryImpl
//
//    @Before
//    fun setup() {
//        homeRemoteDataSource = mockk()
//        repository = HomeRepositoryImpl(homeRemoteDataSource)
//    }
//
//    @Test
//    fun `getOverview should return data from remote data source`() = runTest {
//        // Given
//        val homeOverview = HomeOverview(racesCompleted = 5)
//        coEvery { homeRemoteDataSource.getHomeOverview() } returns homeOverview
//
//        // When
//        val result = repository.getOverview()
//
//        // Then
//        assertEquals(homeOverview, result)
//    }
//}
