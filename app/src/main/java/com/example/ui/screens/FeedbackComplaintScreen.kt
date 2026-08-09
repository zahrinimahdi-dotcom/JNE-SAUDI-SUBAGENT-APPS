package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SubAgentViewModel
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun FeedbackComplaintScreen(
    shipmentId: String,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val shipments by viewModel.shipments.collectAsState()
    val shipment = shipments.firstOrNull { it.shipmentId == shipmentId || it.awbNumber == shipmentId } ?: shipments.first()

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
                        text = "Customer Review & Support Tickets",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AWB: ${shipment.awbNumber}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Rating & Review Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Customer Delivery Feedback", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("5.0 / 5.0", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = JneNavy)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"${shipment.feedbackComment ?: "Very fast pickup at hotel lobby and dates arrived safely in Jakarta!"}\"",
                        fontSize = 12.sp,
                        color = JneTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text("— ${shipment.sender.fullName} (${shipment.sender.whatsappNumber})", fontSize = 11.sp, color = JneTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Admin Support Ticket Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = JneNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Support Ticket Resolution Log", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        }
                        StatusChip(text = "Resolved", backgroundColor = JneGreenLight, textColor = JneGreen)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Ticket ID: TKT-2026-042 • AWB: ${shipment.awbNumber}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Customer: ${shipment.sender.fullName}", fontSize = 12.sp, color = JneTextSecondary)
                    Text("Subject: Delivery Address Clarification in Jakarta", fontSize = 12.sp, color = JneTextPrimary)

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(JneGrayBackground, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Admin Resolution Note:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            Text("Package located at Jakarta Gateway Hub and delivered safely to recipient on Aug 8. Case closed.", fontSize = 11.sp, color = JneTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
