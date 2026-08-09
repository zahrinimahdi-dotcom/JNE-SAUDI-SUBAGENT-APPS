package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.components.RequestStatusChip
import com.example.ui.components.ShipmentStatusChip
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: SubAgentViewModel,
    onNavigate: (Screen) -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val availableBalance by viewModel.availableBalanceSar.collectAsState()
    val creditedThisMonth by viewModel.creditedThisMonthSar.collectAsState()
    val pendingCommission by viewModel.pendingCommissionSar.collectAsState()
    val pickupRequests by viewModel.pickupRequests.collectAsState()
    val shipments by viewModel.shipments.collectAsState()

    val activePickup = pickupRequests.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
        // Hero Hajj Banner Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hajj_banner_1786238578637),
                    contentDescription = "Hajj Cargo Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(JneNavy.copy(alpha = 0.65f))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "JNE SAUDI ARABIA • HAJJ & UMRAH CARGO",
                        color = JneRedLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sub-Agent: ${profile.businessName}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sub-Agent ID: ${profile.subAgentId} • Verified Partner",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Wallet Balance Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { onNavigate(Screen.Wallet) }
                    .testTag("home_wallet_card"),
                colors = CardDefaults.cardColors(containerColor = JneNavy),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Wallet Available Balance",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { onNavigate(Screen.Wallet) },
                            colors = ButtonDefaults.buttonColors(containerColor = JneRed),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Withdraw", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "SAR %.2f".format(availableBalance),
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Credited This Month",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "+SAR %.2f".format(creditedThisMonth),
                                color = Color(0xFF4ADE80),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Pending Auto-Claim",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "SAR %.2f".format(pendingCommission),
                                color = Color(0xFFFBBF24),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Quick Actions Section (Request Pickup, Share Booking QR, Track AWB, Notifications)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Sub-Agent Quick Actions",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "Request\nPickup",
                            icon = Icons.Default.AddBox,
                            iconBg = JneRedLight,
                            iconColor = JneRed,
                            testTag = "qa_request_pickup",
                            onClick = { onNavigate(Screen.RequestPickup) }
                        )

                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "Review QR\n& Link",
                            icon = Icons.Default.QrCode2,
                            iconBg = JneGreenLight,
                            iconColor = JneGreen,
                            testTag = "qa_share_qr",
                            onClick = { onNavigate(Screen.QrBooking) }
                        )

                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "Track\nAWB",
                            icon = Icons.Default.Search,
                            iconBg = Color(0xFFE0F2FE),
                            iconColor = Color(0xFF0284C7),
                            testTag = "qa_track_awb",
                            onClick = { onNavigate(Screen.TrackAwb()) }
                        )

                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "Help &\nSupport",
                            icon = Icons.Default.SupportAgent,
                            iconBg = JnePurpleLight,
                            iconColor = JnePurple,
                            testTag = "qa_help_support",
                            onClick = { onNavigate(Screen.FeedbackComplaint()) }
                        )
                    }
                }
            }
        }

        // Pending QR / Share Link Review Notification Banner
        val pendingQrCount = pickupRequests.count {
            (it.bookingSource == com.example.data.model.BookingSource.CUSTOMER_QR ||
             it.bookingSource == com.example.data.model.BookingSource.SHARED_LINK ||
             it.requestId.startsWith("PR-QR") ||
             it.requestId.startsWith("PR-LINK")) &&
            it.status == com.example.data.model.RequestStatus.WAITING_ADMIN_REVIEW
        }

        if (pendingQrCount > 0) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onNavigate(Screen.QrBooking) }
                        .testTag("banner_pending_qr_review"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, JneOrange.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(JneOrange, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "$pendingQrCount Request Pickup Perlu Review!",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9A3412)
                                )
                                Text(
                                    text = "Dari Share Link & QR Code Customer",
                                    fontSize = 11.sp,
                                    color = Color(0xFFC2410C)
                                )
                            }
                        }

                        Button(
                            onClick = { onNavigate(Screen.QrBooking) },
                            colors = ButtonDefaults.buttonColors(containerColor = JneOrange),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Review", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Next Pickup Active Card
        item {
            activePickup?.let { pickup ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { onNavigate(Screen.PickupDetail(pickup.requestId)) }
                        .testTag("home_next_pickup_card"),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    tint = JneRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active Pickup Request",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = JneNavy
                                )
                            }
                            RequestStatusChip(status = pickup.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Request ID: ${pickup.requestId} • ${pickup.sender.fullName}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = JneTextPrimary
                        )

                        Text(
                            text = "Pickup: ${pickup.pickupAddress} (${pickup.requestedTimeSlot})",
                            fontSize = 11.sp,
                            color = JneTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Driver info if assigned
                        pickup.driver?.let { drv ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(JneGrayBackground, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_driver_avatar_1786238593853),
                                        contentDescription = "Driver Avatar",
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Driver: ${drv.name} (${drv.plateNumber})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = JneTextPrimary
                                        )
                                        Text(
                                            text = "ETA: ${drv.currentEta}",
                                            fontSize = 10.sp,
                                            color = JneGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = JneTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recent Shipments Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Owned Shipments",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneNavy
                )
                TextTextButton(
                    text = "View All (${shipments.size})",
                    onClick = { onNavigate(Screen.MyShipments) }
                )
            }
        }

        // Recent Shipments List
        items(shipments.take(3)) { shipment ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onNavigate(Screen.ShipmentDetail(shipment.shipmentId)) }
                    .testTag("shipment_card_${shipment.shipmentId}"),
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
                        Text(
                            text = "AWB: ${shipment.awbNumber}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = JneNavy
                        )
                        ShipmentStatusChip(status = shipment.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${shipment.sender.fullName} ➔ ${shipment.receiver.fullName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = JneTextPrimary
                    )

                    Text(
                        text = "Route: Makkah to ${shipment.receiver.city} • Verified ${shipment.verifiedWeightKg} kg",
                        fontSize = 11.sp,
                        color = JneTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Auto-Claimed Comm: SAR %.2f".format(shipment.commissionAmountSar),
                            fontSize = 11.sp,
                            color = JneGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = JneTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = JneTextPrimary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 13.sp
        )
    }
}

@Composable
private fun TextTextButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = JneRed,
        modifier = Modifier.clickable(onClick = onClick)
    )
}
