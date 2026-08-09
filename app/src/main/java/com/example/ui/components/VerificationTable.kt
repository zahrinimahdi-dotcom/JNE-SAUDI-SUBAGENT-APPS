package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverVerification
import com.example.ui.theme.*

@Composable
fun VerificationTable(
    driverVerification: DriverVerification,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Driver Verification Result",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = JneNavy
                    )
                    Text(
                        text = "Read-Only • Verified by ${driverVerification.driverName} on ${driverVerification.timestamp}",
                        fontSize = 11.sp,
                        color = JneTextSecondary
                    )
                }

                if (driverVerification.hasCorrection) {
                    StatusChip(
                        text = "Minor Correction",
                        backgroundColor = JneOrangeLight,
                        textColor = JneOrange
                    )
                } else {
                    StatusChip(
                        text = "100% Match",
                        backgroundColor = JneGreenLight,
                        textColor = JneGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JneSurfaceContainer, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Parameter",
                    modifier = Modifier.weight(1.2f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneTextSecondary
                )
                Text(
                    text = "Submitted",
                    modifier = Modifier.weight(1f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneTextSecondary
                )
                Text(
                    text = "Actual (Scale)",
                    modifier = Modifier.weight(1f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneTextSecondary
                )
                Text(
                    text = "Status",
                    modifier = Modifier.weight(0.8f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneTextSecondary
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = JneBorder)

            // Table Rows
            driverVerification.comparisons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = row.fieldName,
                        modifier = Modifier.weight(1.2f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = JneTextPrimary
                    )
                    Text(
                        text = row.submittedValue,
                        modifier = Modifier.weight(1f),
                        fontSize = 11.sp,
                        color = JneTextSecondary
                    )
                    Text(
                        text = row.actualValue,
                        modifier = Modifier.weight(1f),
                        fontSize = 11.sp,
                        fontWeight = if (!row.isMatch) FontWeight.Bold else FontWeight.Normal,
                        color = if (!row.isMatch) JneOrange else JneTextPrimary
                    )
                    Row(
                        modifier = Modifier.weight(0.8f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (row.isMatch) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Match",
                                tint = JneGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "OK", fontSize = 10.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Correction",
                                tint = JneOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "Diff", fontSize = 10.sp, color = JneOrange, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                HorizontalDivider(color = JneBorder.copy(alpha = 0.5f))
            }

            // Driver Notes
            driverVerification.driverNotes?.let { notes ->
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(JneGrayBackground, RoundedCornerShape(8.dp))
                        .border(1.dp, JneBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Driver Field Notes:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = JneNavy
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = notes,
                            fontSize = 11.sp,
                            color = JneTextSecondary
                        )
                    }
                }
            }
        }
    }
}
