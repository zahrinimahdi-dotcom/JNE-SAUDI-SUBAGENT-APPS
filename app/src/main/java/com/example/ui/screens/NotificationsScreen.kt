package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationChannel
import com.example.ui.SubAgentViewModel
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    var selectedChannelFilter by remember { mutableStateOf<NotificationChannel?>(null) }

    val filteredNotifs = notifications.filter {
        selectedChannelFilter == null || it.channel == selectedChannelFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
        // Header
        Surface(color = JneNavy, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Notification Center",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sub-Agent Push & Customer WhatsApp Activity Logs",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Channel Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedChannelFilter == null,
                onClick = { selectedChannelFilter = null },
                label = { Text("All Logs", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedChannelFilter == NotificationChannel.PUSH,
                onClick = { selectedChannelFilter = NotificationChannel.PUSH },
                label = { Text("Push (Sub-Agent)", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedChannelFilter == NotificationChannel.WHATSAPP,
                onClick = { selectedChannelFilter = NotificationChannel.WHATSAPP },
                label = { Text("WhatsApp (Customer)", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedChannelFilter == NotificationChannel.ACTION_REQUIRED,
                onClick = { selectedChannelFilter = NotificationChannel.ACTION_REQUIRED },
                label = { Text("Action Required", fontSize = 11.sp) }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredNotifs) { record ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("notif_card_${record.id}"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(record.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            StatusChip(
                                text = record.channel.name,
                                backgroundColor = when (record.channel) {
                                    NotificationChannel.PUSH -> Color(0xFFE0F2FE)
                                    NotificationChannel.WHATSAPP -> JneGreenLight
                                    NotificationChannel.ACTION_REQUIRED -> JneOrangeLight
                                },
                                textColor = when (record.channel) {
                                    NotificationChannel.PUSH -> Color(0xFF0284C7)
                                    NotificationChannel.WHATSAPP -> JneGreen
                                    NotificationChannel.ACTION_REQUIRED -> JneOrange
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(record.message, fontSize = 11.sp, color = JneTextPrimary)

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Recipient: ${record.recipient}", fontSize = 10.sp, color = JneTextSecondary)
                            Text(record.timestamp, fontSize = 10.sp, color = JneTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
