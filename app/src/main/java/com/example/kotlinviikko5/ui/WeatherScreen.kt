package com.example.kotlinviikko5.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinviikko5.viewmodel.WeatherUiState
import com.example.kotlinviikko5.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    vm: WeatherViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Sää (OpenWeather)", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Kaupunki") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { vm.fetchWeather(city) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hae")
        }

        when (val s = state) {
            WeatherUiState.Idle -> Text("Syötä kaupunki ja paina Hae.")
            WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Error -> Text(
                text = "Virhe: ${s.message}",
                color = MaterialTheme.colorScheme.error
            )
            is WeatherUiState.Success -> {
                val data = s.data
                Text("Kaupunki: ${data.name}")
                Text("Lämpötila: ${data.main.temp} °C")
                Text("Kuvaus: ${data.weather.firstOrNull()?.description ?: "-"}")
            }
        }
    }
}
