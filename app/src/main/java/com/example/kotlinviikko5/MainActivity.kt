package com.example.kotlinviikko5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kotlinviikko5.ui.WeatherScreen
import com.example.kotlinviikko5.ui.theme.KotlinViikko5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinViikko5Theme {
                WeatherScreen()
            }
        }
    }
}
