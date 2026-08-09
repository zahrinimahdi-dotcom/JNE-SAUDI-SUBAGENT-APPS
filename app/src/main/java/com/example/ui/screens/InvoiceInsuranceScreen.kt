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
import com.example.ui.components.PaymentStatusChip
import com.example.ui.theme.*

@Composable
fun InvoiceInsuranceScreen(
    shipmentId: String,
    viewModel: SubAgentViewModel,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val shipments by viewModel.shipments.collectAsState()
    val shipment = shipments.firstOrNull { it.shipmentId == shipmentId || it.awbNumber == shipmentId } ?: shipments.first()

    val invoice = shipment.invoice ?: return
    val insurance = shipment.insurance ?: return

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
                        text = "Verified Invoice & Insurance",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Invoice: ${invoice.invoiceNumber} • AWB: ${shipment.awbNumber}",
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
            // Invoice Card Breakdown
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
                        Column {
                            Text("JNE SAUDI OFFICIAL INVOICE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = JneRed)
                            Text(invoice.invoiceNumber, fontSize = 16.sp, fontWeight = FontWeight.Black, color = JneNavy)
                        }
                        PaymentStatusChip(status = invoice.paymentStatus)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Verified Weight: ${invoice.verifiedWeightKg} kg • Dimensions: ${invoice.verifiedDimensionsCm}", fontSize = 12.sp, color = JneTextSecondary)
                    Text("Payment Method: ${invoice.paymentMethod} (Ref: ${invoice.paymentReference})", fontSize = 11.sp, color = JneTextSecondary)

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = JneBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    InvoiceRow("Freight Charge (${invoice.verifiedWeightKg} kg x SAR 10.00)", invoice.shippingFeeSar)
                    InvoiceRow("Handling & Operations Fee", invoice.serviceFeeSar)
                    InvoiceRow("JNE Marine Insurance Premium", invoice.insuranceFeeSar)
                    InvoiceRow("Indonesian Import Duty (Hajj Facility Exempt)", invoice.importDutySar, isExempt = true)
                    InvoiceRow("Import VAT / Taxes (Hajj Facility Exempt)", invoice.importTaxSar, isExempt = true)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = JneBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL FINAL BILL PAID:", fontSize = 14.sp, fontWeight = FontWeight.Black, color = JneNavy)
                        Text("SAR %.2f".format(invoice.totalBillSar), fontSize = 18.sp, fontWeight = FontWeight.Black, color = JneNavy)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Insurance Coverage Policy Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = JneGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cargo Insurance Coverage Policy", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Policy Number: ${insurance.policyNumber}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Underwriter: ${insurance.provider}", fontSize = 12.sp, color = JneTextSecondary)
                    Text("Insured Declared Value: $%.2f USD (SAR %.2f)".format(insurance.declaredValueUsd, insurance.insuredValueSar), fontSize = 12.sp, color = JneNavy, fontWeight = FontWeight.Bold)
                    Text("Coverage Period: ${insurance.coveragePeriod}", fontSize = 11.sp, color = JneTextSecondary)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.showMessage("Downloading Insurance Policy Certificate PDF...") },
                        modifier = Modifier.fillMaxWidth().testTag("btn_download_insurance_cert")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download Insurance Certificate (PDF)", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // View Auto-Claimed Commission Result Button
            Button(
                onClick = { viewModel.showMessage("Admin verification complete! SAR %.2f credited to wallet.".format(shipment.commissionAmountSar)) },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_view_commission"),
                colors = ButtonDefaults.buttonColors(containerColor = JneGreen)
            ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Commission Auto-Claimed (+SAR %.2f)".format(shipment.commissionAmountSar), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun InvoiceRow(label: String, amountSar: Double, isExempt: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = JneTextSecondary)
        if (isExempt) {
            Text("SAR 0.00 (Exempt)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneGreen)
        } else {
            Text("SAR %.2f".format(amountSar), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = JneTextPrimary)
        }
    }
}
