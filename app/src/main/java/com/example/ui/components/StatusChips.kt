package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun StatusChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = textColor.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun RequestStatusChip(status: RequestStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        RequestStatus.DRAFT -> Pair(Color(0xFFF1F5F9), JneTextSecondary)
        RequestStatus.WAITING_ADMIN_REVIEW -> Pair(JneOrangeLight, JneOrange)
        RequestStatus.ADDITIONAL_DOCS_REQUIRED -> Pair(JneOrangeLight, JneOrange)
        RequestStatus.APPROVED -> Pair(JneGreenLight, JneGreen)
        RequestStatus.REJECTED, RequestStatus.EXPIRED, RequestStatus.PICKUP_FAILED -> Pair(JneRedLight, JneRed)
        RequestStatus.DRIVER_ASSIGNED, RequestStatus.DRIVER_ON_THE_WAY, RequestStatus.DRIVER_ARRIVED -> Pair(Color(0xFFE0F2FE), Color(0xFF0284C7))
        RequestStatus.PICKUP_COMPLETED -> Pair(JneGreenLight, JneGreen)
    }
    StatusChip(text = status.displayName, backgroundColor = bg, textColor = fg, modifier = modifier)
}

@Composable
fun ShipmentStatusChip(status: ShipmentStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        ShipmentStatus.DELIVERED, ShipmentStatus.COMPLETED -> Pair(JneGreenLight, JneGreen)
        ShipmentStatus.HOLD, ShipmentStatus.RE_DELIVERY, ShipmentStatus.WAITING_CONFIRMATION -> Pair(JneOrangeLight, JneOrange)
        ShipmentStatus.REJECTED, ShipmentStatus.REFUNDED, ShipmentStatus.CLOSED -> Pair(JneRedLight, JneRed)
        ShipmentStatus.CUSTOMS_PROCESSING, ShipmentStatus.VERIFICATION_IN_PROGRESS -> Pair(JnePurpleLight, JnePurple)
        else -> Pair(Color(0xFFE0F2FE), Color(0xFF0369A1))
    }
    StatusChip(text = status.displayName, backgroundColor = bg, textColor = fg, modifier = modifier)
}

@Composable
fun CommissionStatusChip(status: CommissionStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        CommissionStatus.AUTO_CLAIMED, CommissionStatus.CREDITED -> Pair(JneGreenLight, JneGreen)
        CommissionStatus.PENDING_PAYMENT, CommissionStatus.PENDING_ADMIN, CommissionStatus.CALCULATED -> Pair(JneOrangeLight, JneOrange)
        CommissionStatus.REVERSED, CommissionStatus.NOT_ELIGIBLE -> Pair(JneRedLight, JneRed)
        CommissionStatus.ADJUSTED -> Pair(JnePurpleLight, JnePurple)
    }
    StatusChip(text = status.displayName, backgroundColor = bg, textColor = fg, modifier = modifier)
}

@Composable
fun PaymentStatusChip(status: PaymentStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        PaymentStatus.VERIFIED_COMPLETED -> Pair(JneGreenLight, JneGreen)
        PaymentStatus.COLLECTED_BY_DRIVER, PaymentStatus.PENDING_VERIFICATION, PaymentStatus.VERIFICATION_IN_PROGRESS -> Pair(JneOrangeLight, JneOrange)
        PaymentStatus.FAILED -> Pair(JneRedLight, JneRed)
        PaymentStatus.PENDING_COLLECTION -> Pair(Color(0xFFF1F5F9), JneTextSecondary)
    }
    StatusChip(text = status.displayName, backgroundColor = bg, textColor = fg, modifier = modifier)
}
