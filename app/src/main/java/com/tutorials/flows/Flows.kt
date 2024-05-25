package com.tutorials.flows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun snapshotFlow() {
    val count = remember { mutableIntStateOf(0) }
    val countFlow = snapshotFlow {
        count.intValue
    }
    /*
    snapshotFlow creates a Flow that runs block when collected and emits the result, recording any snapshot state that was accessed.
     While collection continues, if a new Snapshot is applied that changes state accessed by block, the flow will run block again, re-recording the snapshot state that was accessed.
     If the result of block is not equal to the previous result, the flow will emit that new result.
     */
    LaunchedEffect(key1 = Unit) {
        countFlow.collect {
            println("the count changes to ${it}")
        }
    }
    Button(onClick = {
        count.intValue = count.intValue.inc()
    }) {
        Text(
            modifier = Modifier.semantics { contentDescription = "Increment" },
            text = "${count.intValue}: Click to increment",
            fontSize = 32.sp
        )
    }
}

@Composable
fun DerivedStateFlow() {

    val clickCount = remember { mutableStateOf(0) }
    val sum = remember { derivedStateOf { clickCount.value in 5..9 } }

    SideEffect {
        // Called on every recomposition
        println("Count is ${clickCount.value}")
    }
    Column(
        modifier = Modifier.padding(all = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sum ${sum.value}",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            clickCount.value += 1
        }) {
            SideEffect {
                // for logging and analytics
                // for non-UI related work like database, n/w operations
                println("Inner Count is ${clickCount.value}")
            }
            Text(
                text = "Add a Number ${clickCount.value}",
                fontSize = 32.sp
            )
        }
    }
}