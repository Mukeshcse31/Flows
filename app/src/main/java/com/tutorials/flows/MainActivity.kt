package com.tutorials.flows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.tutorials.flows.service.ApiHelperImpl
import com.tutorials.flows.ui.theme.FlowsTheme
import com.tutorials.flows.viewmodel.GraphQlViewModel
import com.tutorials.flows.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel(DefaultDispatcher())
        viewModel.concurrentCalls()

        val apiHelper = ApiHelperImpl(Util.apiService)
        val graphQlViewModel = GraphQlViewModel(apiHelper)
        graphQlViewModel.getGraphRes()
        lifecycleScope.launch {
            graphQlViewModel.state.collect{
                println("GraphQl response view: $it")

            }
        }

        setContent {
            FlowsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val time = viewModel.countdownTimer.collectAsState(initial = 10).value
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text(text = time.toString())
                            snapshotFlow()
                            DerivedStateFlow()
                        }
                    }
//                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlowsTheme {
        Greeting("Android")
    }
}