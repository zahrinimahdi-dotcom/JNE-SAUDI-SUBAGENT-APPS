package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShipmentStatus
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.components.ShipmentStatusChip
import com.example.ui.theme.*

@Composable
fun MyShipmentsScreen(
    viewModel: SubAgentViewModel,
    onNavigate: (Screen) -> Unit
) {
    val shipments by viewModel.shipments.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf<ShipmentStatus?>(null) }

    // Filter owned shipments
    val ownedShipments = shipments.filter { it.isOwnedBySubAgent }
    val filteredList = ownedShipments.filter { shipment ->
        val matchesSearch = searchQuery.isEmpty() ||
                shipment.awbNumber.contains(searchQuery, ignoreCase = true) ||
                shipment.sender.fullName.contains(searchQuery, ignoreCase = true) ||
                shipment.receiver.fullName.contains(searchQuery, ignoreCase = true)

        val matchesStatus = selectedStatusFilter == null || shipment.status == selectedStatusFilter
        matchesSearch && matchesStatus
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
        // Header Bar
        Surface(color = JneNavy, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = "My Owned Shipments",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track & manage all customer shipments connected to Al Buraq Cargo",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by AWB, Sender, or Receiver name...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = JneNavy) },
                    modifier = Modifier.fillMaxWidth().testTag("input_shipment_search"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Status Filter Chips Row (Supporting 20+ statuses)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedStatusFilter == null,
                    onClick = { selectedStatusFilter = null },
                    label = { Text("All (${ownedShipments.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = JneNavy, selectedLabelColor = Color.White)
                )
            }

            items(ShipmentStatus.values()) { status ->
                val count = ownedShipments.count { it.status == status }
                FilterChip(
                    selected = selectedStatusFilter == status,
                    onClick = { selectedStatusFilter = if (selectedStatusFilter == status) null else status },
                    label = { Text("${status.displayName} ($count)", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = JneNavy, selectedLabelColor = Color.White)
                )
            }
        }

        // Shipment Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList) { shipment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Screen.ShipmentDetail(shipment.shipmentId)) }
                        .testTag("my_shipment_card_${shipment.shipmentId}"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AWB: ${shipment.awbNumber}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = JneNavy
                            )
                            ShipmentStatusChip(status = shipment.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Sender: ${shipment.sender.fullName} (${shipment.sender.whatsappNumber})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Receiver: ${shipment.receiver.fullName} • ${shipment.receiver.city}, ${shipment.receiver.province}",
                            fontSize = 12.sp,
                            color = JneTextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Weight: ${shipment.verifiedWeightKg} kg • Comm: SAR %.2f".format(shipment.commissionAmountSar),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = JneGreen
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Details", fontSize = 11.sp, color = JneRed, fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = JneRed, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
