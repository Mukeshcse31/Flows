package com.tutorials.flows.viewModel

import app.cash.turbine.test
import com.tutorials.flows.TestDispatcher
import com.tutorials.flows.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    lateinit var testDispatcher: TestDispatcher
    lateinit var viewModel: MainViewModel
    @Before
    fun setup(){
        testDispatcher = com.tutorials.flows.TestDispatcher()
        viewModel = MainViewModel(testDispatcher)
    }

    @Test
    fun `countdownTimer emits correctly`() = runBlocking {
        viewModel.countdownTimer.test {

            for(i in 10 downTo 1) {
                val emission = awaitItem()
                testDispatcher.testDispatcher.scheduler.apply { advanceTimeBy(1000L); runCurrent() }
                Assert.assertEquals(i, emission)
            }
                cancelAndConsumeRemainingEvents()
        }
    }
}