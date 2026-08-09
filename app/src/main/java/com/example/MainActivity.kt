package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.JneTopBar
import com.example.ui.screens.*
import com.example.ui.theme.JneSubAgentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JneSubAgentTheme {
                SubAgentApp()
            }
        }
    }
}

@Composable
fun SubAgentApp(
    viewModel: SubAgentViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    // Show Toast when ViewModel emits a user message
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearUserMessage()
        }
    }

    var isAuthenticated by remember { mutableStateOf(true) }

    if (!isAuthenticated) {
        AuthScreen(
            viewModel = viewModel,
            onLoginSuccess = { isAuthenticated = true }
        )
    } else {
        val showBottomBar = currentScreen in listOf(
            Screen.Home,
            Screen.QrBooking,
            Screen.RequestPickup,
            Screen.MyShipments,
            Screen.Profile
        )

        val showHeader = currentScreen == Screen.Home

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (showHeader) {
                    JneTopBar(
                        profile = profile,
                        unreadNotificationCount = notifications.size,
                        onNavigateToNotifications = { viewModel.navigateTo(Screen.Notifications) },
                        onNavigateToTrackAwb = { viewModel.navigateTo(Screen.TrackAwb()) },
                        onOpenAuthSwitcher = { viewModel.navigateTo(Screen.Auth) }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavBar(
                        currentScreen = currentScreen,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = if (showHeader) innerPadding.calculateTopPadding() else 0.dp,
                        bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp
                    )
            ) {
                Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                    when (screen) {
                        is Screen.Auth -> AuthScreen(
                            viewModel = viewModel,
                            onLoginSuccess = {
                                isAuthenticated = true
                                viewModel.navigateTo(Screen.Home)
                            }
                        )

                        is Screen.Home -> HomeScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        is Screen.QrBooking -> QrBookingScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() }
                        )

                        is Screen.RequestPickup -> RequestPickupScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() }
                        )

                        is Screen.PickupDetail -> PickupDetailScreen(
                            requestId = screen.requestId,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        is Screen.DriverVerificationPreview -> DriverVerificationScreen(
                            shipmentId = screen.shipmentId,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        is Screen.InvoiceInsurance -> InvoiceInsuranceScreen(
                            shipmentId = screen.shipmentId,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        is Screen.Wallet -> WalletScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() }
                        )

                        is Screen.MyShipments -> MyShipmentsScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        is Screen.ShipmentDetail -> ShipmentDetailScreen(
                            shipmentId = screen.shipmentId,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        is Screen.TrackAwb -> TrackAwbScreen(
                            initialAwb = screen.initialAwb,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() }
                        )

                        is Screen.Notifications -> NotificationsScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() }
                        )

                        is Screen.Profile -> ProfileScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.navigateTo(it) },
                            onLogout = { isAuthenticated = false }
                        )

                        is Screen.FeedbackComplaint -> FeedbackComplaintScreen(
                            shipmentId = screen.shipmentId,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateBack() }
                        )
                    }
                }
            }
        }
    }
}
