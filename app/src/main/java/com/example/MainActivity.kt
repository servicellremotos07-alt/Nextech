package com.example.MainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.ui.DeviceViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: DeviceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
                var selectedDeviceId by remember { mutableStateOf<Long?>(null) }

                when (val screen = currentScreen) {
                    is AppScreen.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToAdd = { currentScreen = AppScreen.AddDevice },
                            onNavigateToDetail = { id ->
                                selectedDeviceId = id
                                currentScreen = AppScreen.Detail
                            },
                            onNavigateToTools = { currentScreen = AppScreen.Tools },
                            onNavigateToSettings = { currentScreen = AppScreen.Settings }
                        )
                    }
                    is AppScreen.AddDevice -> {
                        AddDeviceScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = AppScreen.Home }
                        )
                    }
                    is AppScreen.Detail -> {
                        selectedDeviceId?.let { id ->
                            DeviceDetailScreen(
                                deviceId = id,
                                viewModel = viewModel,
                                onBack = { currentScreen = AppScreen.Home }
                            )
                        } ?: run {
                            currentScreen = AppScreen.Home
                        }
                    }
                    is AppScreen.Tools -> {
                        ToolsScreen(
                            onBack = { currentScreen = AppScreen.Home }
                        )
                    }
                    is AppScreen.Settings -> {
                        SettingsScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = AppScreen.Home }
                        )
                    }
                }
            }
        }
    }
}

sealed class AppScreen {
    object Home : AppScreen()
    object AddDevice : AppScreen()
    object Detail : AppScreen()
    object Tools : AppScreen()
    object Settings : AppScreen()
}
