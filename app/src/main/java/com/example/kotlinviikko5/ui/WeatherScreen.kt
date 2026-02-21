package com.example.kotlinviikko5.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinviikko5.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WeatherScreen(vm: WeatherViewModel = viewModel()) {
    var city by remember { mutableStateOf("") }

    val latest = vm.latestWeather.collectAsState(initial = null).value
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Sää (Room-välimuisti)", style = MaterialTheme.typography.headlineSmall)

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
            Text("Hae sää")
        }

        if (loading) {
            CircularProgressIndicator()
        }

        if (error != null) {
            Text("Virhe: $error", color = MaterialTheme.colorScheme.error)
        }

        if (latest != null) {
            Text("Kaupunki: ${latest.city}")
            Text("Lämpötila: ${latest.temp} °C")
            Text("Kuvaus: ${latest.description}")
        } else {
            Text("Ei tallennettua säätä vielä. Hae kaupunki.")
        }
    }
}