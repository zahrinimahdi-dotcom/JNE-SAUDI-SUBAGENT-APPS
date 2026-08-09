package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.data.model.RequestStatus
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.components.RequestStatusChip
import com.example.ui.theme.*

@Composable
fun PickupDetailScreen(
    requestId: String,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val requests by viewModel.pickupRequests.collectAsState()
    val request = requests.firstOrNull { it.requestId == requestId } ?: requests.firstOrNull()

    var showCancelDialog by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("Customer requested schedule change") }

    if (request == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pickup Request Not Found")
        }
        return
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
                        text = "Pickup Request ${request.requestId}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Submitted: ${request.submissionTimestamp}",
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
            // Status Summary Card
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
                        Text("Current Request Status", fontSize = 12.sp, color = JneTextSecondary)
                        RequestStatusChip(status = request.status)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Sender: ${request.sender.fullName}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("WhatsApp: ${request.sender.whatsappNumber} (Verified)", fontSize = 12.sp, color = JneGreen)
                    Text("Pickup: ${request.pickupAddress}", fontSize = 12.sp, color = JneTextSecondary)
                    Text("Scheduled: ${request.requestedDate} (${request.requestedTimeSlot})", fontSize = 12.sp, color = JneNavy, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated Total: SAR %.2f".format(request.estimatedTotalSar), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Est. Commission: SAR %.2f".format(request.estimatedCommissionSar), fontSize = 12.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Operational Timeline
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Request Operational Timeline", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    Spacer(modifier = Modifier.height(12.dp))

                    TimelineStep("1. Request Submitted", "Customer/Sub-Agent created request", request.submissionTimestamp, true)
                    TimelineStep("2. Admin Operational Review", "Customs regulation screening passed", "2026-08-08 11:00", request.status != RequestStatus.WAITING_ADMIN_REVIEW)
                    TimelineStep("3. Driver Assignment", request.driver?.let { "Assigned driver ${it.name}" } ?: "Pending driver dispatch", "2026-08-08 11:20", request.driver != null)
                    TimelineStep("4. Driver Verification", "Scale measurement & passport check", request.driverVerification?.timestamp ?: "Pending pickup", request.driverVerification != null)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            if (request.driver != null) {
                Button(
                    onClick = { onNavigate(Screen.DriverVerificationPreview(request.awbNumber ?: request.requestId)) },
                    modifier = Modifier.fillMaxWidth().testTag("btn_view_driver_verif"),
                    colors = ButtonDefaults.buttonColors(containerColor = JneNavy)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View Driver Verification Result", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (request.status != RequestStatus.REJECTED && request.status != RequestStatus.PICKUP_COMPLETED) {
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth().testTag("btn_cancel_request"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = JneRed)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cancel Pickup Request")
                }
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            containerColor = JneRed,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Confirm Cancel Request", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Are you sure you want to cancel Pickup Request ${request.requestId}?", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = cancelReason,
                        onValueChange = { cancelReason = it },
                        label = { Text("Reason for cancellation") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        viewModel.cancelPickupRequest(request.requestId, cancelReason)
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Confirm Cancel", color = JneRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Keep Active", color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun TimelineStep(title: String, subtitle: String, timestamp: String, isDone: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isDone) JneGreen else JneTextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isDone) JneNavy else JneTextSecondary)
            Text(subtitle, fontSize = 10.sp, color = JneTextSecondary)
        }
        Text(timestamp, fontSize = 10.sp, color = JneTextSecondary)
    }
}
