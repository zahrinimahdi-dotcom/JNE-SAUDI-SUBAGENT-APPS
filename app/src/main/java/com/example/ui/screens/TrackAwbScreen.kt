package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SubAgentViewModel
import com.example.ui.components.ShipmentStatusChip
import com.example.ui.theme.*

@Composable
fun TrackAwbScreen(
    initialAwb: String? = null,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val shipments by viewModel.shipments.collectAsState()

    var awbInput by remember { mutableStateOf(initialAwb ?: "SA123456785SA") }
    var searchedAwb by remember { mutableStateOf(awbInput) }

    val matchedShipment = shipments.firstOrNull { it.awbNumber.equals(searchedAwb.trim(), ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
        // Top Bar
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
                Text(
                    text = "Track Any JNE AWB",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // AWB Input Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Enter JNE Saudi Tracking Number", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = awbInput,
                        onValueChange = { awbInput = it },
                        label = { Text("AWB Number") },
                        placeholder = { Text("e.g. SA123456785SA or JNE88990011SA") },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.showMessage("Simulating camera barcode scan...") }) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode", tint = JneNavy)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("input_track_awb"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { searchedAwb = awbInput },
                        colors = ButtonDefaults.buttonColors(containerColor = JneRed),
                        modifier = Modifier.fillMaxWidth().testTag("btn_execute_track")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Search Tracking Status", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Test History Chips
                    Text("Quick Sample AWBs:", fontSize = 11.sp, color = JneTextSecondary)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        SuggestionChip(
                            onClick = { awbInput = "SA123456785SA"; searchedAwb = "SA123456785SA" },
                            label = { Text("Owned AWB (SA123...)", fontSize = 10.sp) }
                        )
                        SuggestionChip(
                            onClick = { awbInput = "JNE88990011SA"; searchedAwb = "JNE88990011SA" },
                            label = { Text("External JNE AWB", fontSize = 10.sp) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tracking Result Display
            if (matchedShipment != null) {
                val shipment = matchedShipment

                // Privacy Indicator
                if (!shipment.isOwnedBySubAgent) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Public Tracking View: Private customer personal data, invoice, and commission details are hidden for non-owned external JNE shipments.",
                            fontSize = 11.sp,
                            color = Color(0xFF1E40AF)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

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
                            Text("AWB: ${shipment.awbNumber}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = JneNavy)
                            ShipmentStatusChip(status = shipment.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (shipment.isOwnedBySubAgent) {
                            Text("Sender: ${shipment.sender.fullName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Receiver: ${shipment.receiver.fullName} (${shipment.receiver.city})", fontSize = 12.sp, color = JneTextSecondary)
                        } else {
                            Text("Route: Saudi Arabia ➔ Indonesia", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Destination Hub: ${shipment.receiver.city}, Indonesia", fontSize = 12.sp, color = JneTextSecondary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = JneBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Tracking Milestone Log", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Spacer(modifier = Modifier.height(8.dp))

                        shipment.trackingEvents.forEach { event ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JneGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(event.statusTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("${event.location} • ${event.timestamp}", fontSize = 10.sp, color = JneTextSecondary)
                                }
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SearchOff, contentDescription = null, tint = JneTextSecondary, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No Tracking Result Found", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Verify the JNE Saudi AWB number and try again.", fontSize = 12.sp, color = JneTextSecondary)
                    }
                }
            }
        }
    }
}
