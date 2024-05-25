package com.tutorials.flows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorials.flows.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
   val dispatcher: DispatcherProvider
) : ViewModel() {

    val countdownTimer = flow<Int> {
        val startingTime = 10
        var currentTime = startingTime
        while (currentTime > 0) {
            emit(currentTime)
            delay(1000L)
            currentTime--
        }
    }.flowOn(dispatcher.main)

    init {
        collectFlow()
        collectFlowBuffer()
    }

    fun collectFlow() {
        viewModelScope.launch(dispatcher.main) {//collect is a suspend fun, so it's called in a launch block
            countdownTimer
                .filter { time -> time % 2 == 0 }
                .map { time -> time + 1 }
                .collect { time -> println("the current time is $time") }
        }
    }

    fun concurrentCalls() {
        viewModelScope.launch {
            coldFlow()

        }
        viewModelScope.launch {
            hotFlow()
        }
        viewModelScope.launch {
            async { mutableSharedFlowExample() }
            async { mutableStateFlowExample() }
        }
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler() { context, throwable ->
                println("exception occurred in job 2")
            }
            val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

            val job1 = scope.launch {
                delay(1000)
                println("concurrentCalls Job1")
            }
            job1.invokeOnCompletion {
                println("scope is ${scope.isActive}")
            }

            val job2 = scope.launch {
                delay(1000)
                throw Error("job 2 error")
                println("concurrentCalls Job 2")
            }
            job2.invokeOnCompletion {
                println("scope is ${scope.isActive}")
            }
            job1.join()
            job2.join()

            concurrentCalls1()

        }
    }

    suspend fun concurrentCalls1() {
//        runBlocking {

        val scope = CoroutineScope(Dispatchers.IO + CoroutineName("Test"))

        val result1 = scope.async { task1() }
        val result2 = scope.async { task2() }

        var res: String? = null
        println("task 1: ${result1.await()}")
        try {
            res = result2.await()
        } catch (e: Exception) {
            println("Exception in Task 2 occurred")
        }
        println("task 2: $res")

//        }
    }

    suspend fun task1(): String {
        delay(1000)
        return "Task 1 Complete"
    }


    suspend fun task2(): String {
        delay(1000)
        throw Exception("Error in Task 2")
        return "Task 2 Complete"
    }

    suspend fun coldFlow() {
        val flow = flow {
            emit(0)
            emit(1)
        }

        flow.collect {
            println("cold flow collector received: $it")
        }
    }

    suspend fun hotFlow() {
        coroutineScope {
            val flow = MutableSharedFlow<Int>()
            flow.emit(0)
            flow.emit(1)
            flow.emit(2)

            launch {
                flow.collect {
                    println("hot flow collector received: $it")
                }
            }
            flow.emit(3)
            flow.collect {
                println("hot flow collector received: $it")
            }
        }
    }

    suspend fun mutableStateFlowExample(): Unit = coroutineScope {
//        slower observers might not receive some intermediate state changes.
        val state = MutableStateFlow('X')

        launch {
            for (c in 'A'..'E') {
                delay(300)
                state.value = c
                // or state.emit(c)
            }
        }

        state.collect {
            delay(1000)
            println("mutableStateFlow: $it")
        }
    }

    suspend fun mutableSharedFlowExample(): Unit = coroutineScope {
        val mutableSharedFlow = MutableSharedFlow<String>(replay = 3) // caches 3 emissions in flow for new collectors
        // or MutableSharedFlow<String>()

        launch {
            mutableSharedFlow.collect {
                println("first flow collector received mutableSharedFlow #1 received $it")
            }
        }
        // for another collector, launch another coroutine scope
        launch {
            mutableSharedFlow.collect {
                println("second flow collector received mutableSharedFlow #2 received $it")
            }
        }

        delay(1000)
        mutableSharedFlow.emit("Message1") //this will suspend as long as all the collectors need to process the events
        mutableSharedFlow.emit("Message2")
    }

    fun collectFlowBuffer() {
        val flow1 = flow<String> {
            delay(200)
            emit("Appetizer")
            delay(500)
            emit("Main Dish")
            delay(100)
            emit("Dessert")

        }
        viewModelScope.launch(dispatcher.main) {
            flow1.onEach { println("Buffer: Delivered $it") }
//        .buffer()
                .conflate() //  collector always gets the most recent value emitted
                .collectLatest { // if there is a new emission, the previous one is ignored
//        .collect {
                    println("Buffer: Now eating $it")
                    delay(1400)
                    println("Buffer: Finished eating $it")
                }
        }
    }
}