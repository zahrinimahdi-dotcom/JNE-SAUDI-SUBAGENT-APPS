package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.components.CommissionStatusChip
import com.example.ui.components.ShipmentStatusChip
import com.example.ui.theme.*

@Composable
fun ShipmentDetailScreen(
    shipmentId: String,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit
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
                        text = "Shipment ${shipment.awbNumber}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Owned Shipment • Created: ${shipment.createdTimestamp}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
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
            // Status & Commission Banner
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
                        Text("Shipment Status", fontSize = 12.sp, color = JneTextSecondary)
                        ShipmentStatusChip(status = shipment.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sub-Agent Commission", fontSize = 12.sp, color = JneTextSecondary)
                        CommissionStatusChip(status = shipment.commissionStatus)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Auto-Claimed Commission: SAR %.2f".format(shipment.commissionAmountSar),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = JneGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Links to Driver Verification & Invoice
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onNavigate(Screen.DriverVerificationPreview(shipment.shipmentId)) },
                    modifier = Modifier.weight(1f).testTag("btn_link_driver_verif"),
                    colors = ButtonDefaults.buttonColors(containerColor = JneNavy)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Driver Verification", fontSize = 11.sp)
                }

                Button(
                    onClick = { onNavigate(Screen.InvoiceInsurance(shipment.shipmentId)) },
                    modifier = Modifier.weight(1f).testTag("btn_link_invoice"),
                    colors = ButtonDefaults.buttonColors(containerColor = JneNavy)
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invoice & Insurance", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Parties Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sender & Receiver Address", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Sender: ${shipment.sender.fullName} (${shipment.sender.whatsappNumber})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Passport: ${shipment.sender.passportNumber} • Kloter: ${shipment.sender.kloterNumber}", fontSize = 11.sp, color = JneTextSecondary)

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = JneBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Receiver: ${shipment.receiver.fullName} (${shipment.receiver.phone})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Address: ${shipment.receiver.streetAddress}, ${shipment.receiver.district}, ${shipment.receiver.city}, ${shipment.receiver.province}", fontSize = 11.sp, color = JneTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tracking Events Timeline
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Live Tracking Milestone Log", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    Spacer(modifier = Modifier.height(12.dp))

                    shipment.trackingEvents.forEach { event ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JneGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(event.statusTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                                Text(event.description, fontSize = 11.sp, color = JneTextSecondary)
                                Text("${event.location} • ${event.timestamp}", fontSize = 10.sp, color = JneTextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
