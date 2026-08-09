package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.ui.SubAgentViewModel
import com.example.ui.theme.*

@Composable
fun DriverAssignedScreen(
    requestId: String,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val requests by viewModel.pickupRequests.collectAsState()
    val request = requests.firstOrNull { it.requestId == requestId } ?: requests.firstOrNull()
    val driver = request?.driver

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
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
                    Text("Driver Assigned Details", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Pickup Request: $requestId", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (driver != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.img_driver_avatar_1786238593853),
                                contentDescription = "Driver Photo",
                                modifier = Modifier.size(56.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(driver.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                                Text("${driver.vehicleType} • ${driver.plateNumber}", fontSize = 12.sp, color = JneTextSecondary)
                                Text("ETA: ${driver.currentEta} • ⭐ ${driver.rating}", fontSize = 12.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.showMessage("Calling driver ${driver.phone}...") },
                                colors = ButtonDefaults.buttonColors(containerColor = JneNavy),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Driver", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.showMessage("Opening WhatsApp with driver ${driver.phone}...") },
                                colors = ButtonDefaults.buttonColors(containerColor = JneGreen),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("WhatsApp", fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else {
                Text("No driver assigned yet.", color = JneTextSecondary)
            }
        }
    }
}
