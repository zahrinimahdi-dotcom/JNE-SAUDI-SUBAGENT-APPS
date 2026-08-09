package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.*
import com.example.ui.SubAgentViewModel
import com.example.ui.components.RequestStatusChip
import com.example.ui.theme.*

@Composable
fun QrBookingScreen(
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val openedCount by viewModel.qrOpenedCount.collectAsState()
    val startedCount by viewModel.qrStartedCount.collectAsState()
    val submittedCount by viewModel.qrSubmittedCount.collectAsState()
    val completedCount by viewModel.qrCompletedCount.collectAsState()
    val pickupRequests by viewModel.pickupRequests.collectAsState()

    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, PENDING, APPROVED, DOCS_REQUIRED
    var selectedRequestForReview by remember { mutableStateOf<PickupRequest?>(null) }
    var selectedRequestForReject by remember { mutableStateOf<PickupRequest?>(null) }
    var selectedRequestForDocs by remember { mutableStateOf<PickupRequest?>(null) }

    // Filter requests originating from QR or Share Link or all waiting review
    val qrAndLinkRequests = pickupRequests.filter {
        it.bookingSource == BookingSource.CUSTOMER_QR ||
        it.bookingSource == BookingSource.SHARED_LINK ||
        it.requestId.startsWith("PR-QR") ||
        it.requestId.startsWith("PR-LINK")
    }.ifEmpty {
        // Fallback to show active requests for demo
        pickupRequests
    }

    val filteredRequests = when (selectedFilter) {
        "PENDING" -> qrAndLinkRequests.filter { it.status == RequestStatus.WAITING_ADMIN_REVIEW }
        "APPROVED" -> qrAndLinkRequests.filter { it.status == RequestStatus.APPROVED || it.status == RequestStatus.DRIVER_ASSIGNED || it.status == RequestStatus.PICKUP_COMPLETED }
        "DOCS_REQUIRED" -> qrAndLinkRequests.filter { it.status == RequestStatus.ADDITIONAL_DOCS_REQUIRED }
        else -> qrAndLinkRequests
    }

    val pendingReviewCount = qrAndLinkRequests.count { it.status == RequestStatus.WAITING_ADMIN_REVIEW }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top Header
        Surface(
            color = JneNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
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
                        text = "QR & Customer Share Link Booking",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Promosi & Modul Review Request Customer",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // Explicit Mandatory Scope Disclaimer Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF93C5FD), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1D4ED8),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Customer Booking & Review Area Only",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This QR code & share link is exclusively for customer pickup bookings. Incoming requests from QR/Link can be reviewed, verified, and approved directly by the Sub-Agent below.",
                            fontSize = 11.sp,
                            color = Color(0xFF1E40AF),
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // QR Code Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sub-Agent Customer Booking QR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )
                    Text(
                        text = "Share with Hajj pilgrims or customers to start pickup request",
                        fontSize = 12.sp,
                        color = JneTextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated Clean QR Code Graphic
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(Color.White)
                            .border(3.dp, JneNavy, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = "Booking QR Code",
                                tint = JneNavy,
                                modifier = Modifier.size(120.dp)
                            )
                            Text(
                                text = profile.bookingCode,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = JneRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sub-Agent Booking Link:",
                        fontSize = 11.sp,
                        color = JneTextSecondary
                    )
                    Text(
                        text = profile.bookingUrl,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.showMessage("Booking link copied to clipboard!") },
                            colors = ButtonDefaults.buttonColors(containerColor = JneNavy),
                            modifier = Modifier.weight(1f).testTag("btn_copy_link")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Link", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { viewModel.showMessage("Opening WhatsApp to share customer booking link...") },
                            colors = ButtonDefaults.buttonColors(containerColor = JneGreen),
                            modifier = Modifier.weight(1f).testTag("btn_share_wa")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.showMessage("Promotional QR poster PDF downloaded!") },
                        modifier = Modifier.fillMaxWidth().testTag("btn_download_poster")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download Printable QR Poster (PDF)", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Booking Funnel Statistics
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Customer Booking Analytics",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )
                    Text(
                        text = "Real-time funnel for bookings originated from your QR/link",
                        fontSize = 11.sp,
                        color = JneTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatBox("Link Opened", "$openedCount", Color(0xFF0284C7))
                        StatBox("Form Started", "$startedCount", JneOrange)
                        StatBox("Submitted", "$submittedCount", JnePurple)
                        StatBox("Completed", "$completedCount", JneGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =========================================================================
            // MODUL AREA REVIEW REQUEST PICKUP (QR & SHARE LINK)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(JneOrangeLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = JneOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Area Review Request Pickup",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = JneNavy
                                )
                                Text(
                                    text = "Dari Share Link & QR Customer",
                                    fontSize = 11.sp,
                                    color = JneTextSecondary
                                )
                            }
                        }

                        if (pendingReviewCount > 0) {
                            Surface(
                                color = JneRed,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$pendingReviewCount Perlu Review",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedFilter == "ALL",
                            onClick = { selectedFilter = "ALL" },
                            label = { Text("Semua (${qrAndLinkRequests.size})", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = selectedFilter == "PENDING",
                            onClick = { selectedFilter = "PENDING" },
                            label = { Text("Perlu Review ($pendingReviewCount)", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = selectedFilter == "APPROVED",
                            onClick = { selectedFilter = "APPROVED" },
                            label = { Text("Disetujui", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = selectedFilter == "DOCS_REQUIRED",
                            onClick = { selectedFilter = "DOCS_REQUIRED" },
                            label = { Text("Dokumen", fontSize = 11.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (filteredRequests.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircleOutline,
                                    contentDescription = null,
                                    tint = JneGreen,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Tidak ada request yang sesuai filter", fontSize = 12.sp, color = JneTextSecondary)
                            }
                        }
                    } else {
                        filteredRequests.forEach { req ->
                            ReviewRequestItemCard(
                                request = req,
                                onReviewApprove = { selectedRequestForReview = req },
                                onRequestDocs = { selectedRequestForDocs = req },
                                onReject = { selectedRequestForReject = req }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }

    // --- Dialog Review & Approve ---
    selectedRequestForReview?.let { req ->
        ReviewApproveDialog(
            request = req,
            onDismiss = { selectedRequestForReview = null },
            onConfirmApprove = { notes ->
                viewModel.approvePickupRequest(req.requestId, notes)
                selectedRequestForReview = null
            }
        )
    }

    // --- Dialog Reject ---
    selectedRequestForReject?.let { req ->
        RejectRequestDialog(
            request = req,
            onDismiss = { selectedRequestForReject = null },
            onConfirmReject = { reason ->
                viewModel.rejectPickupRequest(req.requestId, reason)
                selectedRequestForReject = null
            }
        )
    }

    // --- Dialog Request Docs ---
    selectedRequestForDocs?.let { req ->
        RequestDocsDialog(
            request = req,
            onDismiss = { selectedRequestForDocs = null },
            onConfirmRequestDocs = { note ->
                viewModel.requestAdditionalDocs(req.requestId, note)
                selectedRequestForDocs = null
            }
        )
    }
}

@Composable
private fun ReviewRequestItemCard(
    request: PickupRequest,
    onReviewApprove: () -> Unit,
    onRequestDocs: () -> Unit,
    onReject: () -> Unit
) {
    val isQr = request.bookingSource == BookingSource.CUSTOMER_QR || request.requestId.startsWith("PR-QR")
    val sourceLabel = if (isQr) "📱 Customer QR" else "🔗 Share Link"
    val sourceBg = if (isQr) Color(0xFFEFF6FF) else Color(0xFFF0FDF4)
    val sourceText = if (isQr) Color(0xFF1D4ED8) else Color(0xFF15803D)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JneGrayBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = sourceBg, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = sourceLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = sourceText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = request.requestId,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )
                }

                RequestStatusChip(status = request.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Customer Info
            Text(
                text = "${request.sender.fullName} (${if (request.customerType == CustomerType.HAJJ_PILGRIM) "Jamaah Haji • Kloter ${request.sender.kloterNumber}" else "Customer Umum"})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = JneNavy
            )
            Text(
                text = "WA: ${request.sender.whatsappNumber} • Lokasi: ${request.pickupAddress}",
                fontSize = 11.sp,
                color = JneTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Items & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Barang: ${request.items.firstOrNull()?.description ?: "Paket"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Berat Est: ${request.estimatedWeightKg} kg • Tanggal: ${request.requestedDate}",
                        fontSize = 10.sp,
                        color = JneTextSecondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Est. Total: SAR %.2f".format(request.estimatedTotalSar),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )
                    Text(
                        text = "Komisi Sub-Agent: +SAR %.2f".format(request.estimatedCommissionSar),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneGreen
                    )
                }
            }

            // Regulation Status Badge
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (request.regulationStatus) {
                        RegulationStatus.ALLOWED -> Icons.Default.CheckCircle
                        RegulationStatus.RESTRICTED -> Icons.Default.Warning
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    tint = when (request.regulationStatus) {
                        RegulationStatus.ALLOWED -> JneGreen
                        RegulationStatus.RESTRICTED -> JneOrange
                        else -> JneRed
                    },
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Regulasi: ${request.regulationStatus.name} (Screening Otomatis Clear)",
                    fontSize = 10.sp,
                    color = JneTextSecondary
                )
            }

            request.reviewNotes?.let { notes ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Catatan Review: $notes",
                    fontSize = 10.sp,
                    color = JneOrange,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            if (request.status == RequestStatus.WAITING_ADMIN_REVIEW || request.status == RequestStatus.ADDITIONAL_DOCS_REQUIRED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = onReviewApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = JneGreen),
                        modifier = Modifier.weight(1.2f).testTag("btn_approve_${request.requestId}")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Review & Setujui", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onRequestDocs,
                        modifier = Modifier.weight(1f).testTag("btn_docs_${request.requestId}")
                    ) {
                        Text("Minta Doc", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = JneRed),
                        modifier = Modifier.weight(0.8f).testTag("btn_reject_${request.requestId}")
                    ) {
                        Text("Tolak", fontSize = 11.sp)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kurir Terpasang: ${request.driver?.name ?: "Tariq Al-Mansoor"}",
                        fontSize = 11.sp,
                        color = JneGreen,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedButton(
                        onClick = onReviewApprove,
                        modifier = Modifier.testTag("btn_view_detail_${request.requestId}")
                    ) {
                        Text("Lihat Detail", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewApproveDialog(
    request: PickupRequest,
    onDismiss: () -> Unit,
    onConfirmApprove: (notes: String) -> Unit
) {
    var reviewNotes by remember { mutableStateOf("Verifikasi data jamaah & lokasi hotel valid. Disetujui untuk penjemputan.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = JneNavy,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = JneGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Review & Setujui Request", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = "Request ID: ${request.requestId} • Source: ${request.bookingSource.displayName}",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Customer Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text("Pengirim: ${request.sender.fullName}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Paspor / NIK: ${request.sender.passportNumber} / ${request.sender.nikKtp}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                        Text("Kloter: ${request.sender.kloterNumber} • WA: ${request.sender.whatsappNumber}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                        Text("Lokasi Penjemputan: ${request.pickupAddress}", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Items summary
                Text("Daftar Barang & Estimasi Biaya:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                request.items.forEach { item ->
                    Text("• ${item.description} (${item.quantity} ${item.unit}) - Val: $${item.totalValueUsd} USD", fontSize = 11.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Est Weight: ${request.estimatedWeightKg} kg", fontSize = 11.sp, color = Color.White)
                    Text("Est. Tagihan: SAR %.2f".format(request.estimatedTotalSar), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Text("Estimasi Komisi Sub-Agent: SAR %.2f (12%%)".format(request.estimatedCommissionSar), fontSize = 12.sp, color = JneGreenLight, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = reviewNotes,
                    onValueChange = { reviewNotes = it },
                    label = { Text("Catatan Approval / Instruksi Kurir") },
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
                onClick = { onConfirmApprove(reviewNotes) },
                colors = ButtonDefaults.buttonColors(containerColor = JneGreen),
                modifier = Modifier.testTag("btn_confirm_approve")
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Setujui & Tugaskan Kurir", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color.White)
            }
        }
    )
}

@Composable
private fun RejectRequestDialog(
    request: PickupRequest,
    onDismiss: () -> Unit,
    onConfirmReject: (reason: String) -> Unit
) {
    var reason by remember { mutableStateOf("Alamat penjemputan di luar jangkauan / Barang tidak sesuai regulasi") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = JneRed,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = { Text("Tolak Request ${request.requestId}", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Apakah Anda yakin ingin menolak request pickup dari ${request.sender.fullName}?", fontSize = 13.sp, color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Alasan Penolakan") },
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
                onClick = { onConfirmReject(reason) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.testTag("btn_confirm_reject")
            ) {
                Text("Tolak Request", color = JneRed, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color.White)
            }
        }
    )
}

@Composable
private fun RequestDocsDialog(
    request: PickupRequest,
    onDismiss: () -> Unit,
    onConfirmRequestDocs: (note: String) -> Unit
) {
    var note by remember { mutableStateOf("Mohon unggah foto/scan paspor pengirim yang lebih jelas dan bukti pendaftaran Kloter Haji.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = JneNavy,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = { Text("Minta Dokumen Tambahan", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Kirim permintaan dokumen tambahan kepada ${request.sender.fullName} via WhatsApp:", fontSize = 13.sp, color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Dokumen yang Diperlukan") },
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
                onClick = { onConfirmRequestDocs(note) },
                colors = ButtonDefaults.buttonColors(containerColor = JneOrange),
                modifier = Modifier.testTag("btn_confirm_request_docs")
            ) {
                Text("Kirim via WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color.White)
            }
        }
    )
}

@Composable
private fun StatBox(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = color)
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = JneTextSecondary)
    }
}
