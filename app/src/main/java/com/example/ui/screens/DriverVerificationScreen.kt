package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.components.VerificationTable
import com.example.ui.theme.*

@Composable
fun DriverVerificationScreen(
    shipmentId: String,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val shipments by viewModel.shipments.collectAsState()
    val shipment = shipments.firstOrNull { it.shipmentId == shipmentId || it.awbNumber == shipmentId } ?: shipments.first()

    val verification = shipment.driverVerification ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
        // Top Header
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
                        text = "Driver Verification Result",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Read-Only View • AWB: ${shipment.awbNumber}",
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
            // Read-Only Scope Disclaimer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sub-Agent Read-Only Access: Driver actions (scale capture, payment collection, customer signature) belong exclusively to the Driver App. This screen displays verified actuals.",
                        fontSize = 11.sp,
                        color = Color(0xFF92400E),
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Assigned Driver Info Card
            shipment.driver?.let { drv ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_driver_avatar_1786238593853),
                            contentDescription = "Driver Avatar",
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Driver: ${drv.name}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            Text(text = "${drv.vehicleType} • Plate: ${drv.plateNumber}", fontSize = 11.sp, color = JneTextSecondary)
                            Text(text = "Rating: ⭐ ${drv.rating}", fontSize = 11.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reusable Verification Comparison Table
            VerificationTable(driverVerification = verification)

            Spacer(modifier = Modifier.height(16.dp))

            // Verification Photo Evidence Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Digital Photo Verification Evidence", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PhotoBox("Digital Scale 18.8kg", "Scale Photo", JneGreen)
                        PhotoBox("Package Packaging", "Box Photo", JneNavy)
                        PhotoBox("Passport Match", "ID Scan", JnePurple)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onNavigate(Screen.InvoiceInsurance(shipment.shipmentId)) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JneRed)
            ) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Final Invoice & Insurance Policy", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RowScope.PhotoBox(title: String, badge: String, color: Color) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(80.dp)
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = JneTextPrimary)
        }
    }
}
