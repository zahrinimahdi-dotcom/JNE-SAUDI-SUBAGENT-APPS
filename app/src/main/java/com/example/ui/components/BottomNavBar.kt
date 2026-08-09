package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            NavItem(
                label = "Home",
                icon = Icons.Default.Home,
                isSelected = currentScreen is Screen.Home,
                testTag = "nav_home",
                onClick = { onNavigate(Screen.Home) }
            )

            // 2. My Shipments
            NavItem(
                label = "My Shipments",
                icon = Icons.Default.LocalShipping,
                isSelected = currentScreen is Screen.MyShipments,
                testTag = "nav_my_shipments",
                onClick = { onNavigate(Screen.MyShipments) }
            )

            // 3. Request Pickup (Center Red FAB)
            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .size(56.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(JneRed)
                    .clickable { onNavigate(Screen.RequestPickup) }
                    .testTag("nav_request_pickup"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Request Pickup",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // 4. Wallet
            NavItem(
                label = "Wallet",
                icon = Icons.Default.AccountBalanceWallet,
                isSelected = currentScreen is Screen.Wallet,
                testTag = "nav_wallet",
                onClick = { onNavigate(Screen.Wallet) }
            )

            // 5. Profile
            NavItem(
                label = "Profile",
                icon = Icons.Default.Person,
                isSelected = currentScreen is Screen.Profile,
                testTag = "nav_profile",
                onClick = { onNavigate(Screen.Profile) }
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val color = if (isSelected) JneRed else JneTextSecondary
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = color
        )
    }
}
