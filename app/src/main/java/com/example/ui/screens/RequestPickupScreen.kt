package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.*
import com.example.ui.SubAgentViewModel
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun RequestPickupScreen(
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val step by viewModel.pickupStep.collectAsState()
    val customerType by viewModel.formCustomerType.collectAsState()

    val senderName by viewModel.senderName.collectAsState()
    val senderNationality by viewModel.senderNationality.collectAsState()
    val senderNikKtp by viewModel.senderNikKtp.collectAsState()
    val senderPassportNumber by viewModel.senderPassportNumber.collectAsState()
    val senderHajjYear by viewModel.senderHajjYear.collectAsState()
    val senderKloterNumber by viewModel.senderKloterNumber.collectAsState()
    val senderEmbarkation by viewModel.senderEmbarkation.collectAsState()
    val senderHajjPortion by viewModel.senderHajjPortion.collectAsState()
    val senderTravelDate by viewModel.senderTravelDate.collectAsState()
    val senderWhatsapp by viewModel.senderWhatsapp.collectAsState()
    val senderWhatsappState by viewModel.senderWhatsappVerifiedState.collectAsState()

    val receiverName by viewModel.receiverName.collectAsState()
    val receiverPhone by viewModel.receiverPhone.collectAsState()
    val receiverCountry by viewModel.receiverCountry.collectAsState()
    val receiverProvince by viewModel.receiverProvince.collectAsState()
    val receiverCity by viewModel.receiverCity.collectAsState()
    val receiverDistrict by viewModel.receiverDistrict.collectAsState()
    val receiverSubdistrict by viewModel.receiverSubdistrict.collectAsState()
    val receiverPostalCode by viewModel.receiverPostalCode.collectAsState()
    val receiverAddress by viewModel.receiverAddress.collectAsState()
    val receiverLandmark by viewModel.receiverLandmark.collectAsState()

    val packageQty by viewModel.packageQty.collectAsState()
    val grossWeightKg by viewModel.grossWeightKg.collectAsState()
    val lengthCm by viewModel.lengthCm.collectAsState()
    val widthCm by viewModel.widthCm.collectAsState()
    val heightCm by viewModel.heightCm.collectAsState()
    val formItems by viewModel.formItems.collectAsState()

    val hasBattery by viewModel.hasBattery.collectAsState()
    val hasLiquidAerosol by viewModel.hasLiquidAerosol.collectAsState()
    val hasMedicine by viewModel.hasMedicine.collectAsState()
    val hasFood by viewModel.hasFood.collectAsState()
    val hasDangerousGoods by viewModel.hasDangerousGoods.collectAsState()

    val pickupAddress by viewModel.pickupAddress.collectAsState()
    val pickupDate by viewModel.pickupDate.collectAsState()
    val pickupTimeSlot by viewModel.pickupTimeSlot.collectAsState()

    val totalFobUsd = viewModel.totalFobUsd
    val regStatus = viewModel.calculatedRegulationStatus

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
    ) {
        // Top Header with Progress Bar
        Surface(color = JneNavy, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (step > 1) viewModel.pickupStep.value = step - 1 else onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "New Pickup Request",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Step $step of 7 • ${getStepTitle(step)}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { step / 7f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = JneRed,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }
        }

        // Scrollable Form Content
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (step) {
                // Step 1: Customer Type
                1 -> {
                    Column {
                        Text("Select Customer Type", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Text("Different customs regulations and tax facilities apply", fontSize = 12.sp, color = JneTextSecondary)
                        Spacer(modifier = Modifier.height(16.dp))

                        TypeSelectionCard(
                            title = "Hajj Pilgrim Customer",
                            subtitle = "Registered pilgrim with SISKOHAT / Passport. Eligible for Indonesian Customs duty/tax facility (Exempt up to $1,500 FOB).",
                            isSelected = customerType == CustomerType.HAJJ_PILGRIM,
                            badgeText = "Tax Facility Eligible",
                            badgeColor = JneGreen,
                            onClick = { viewModel.formCustomerType.value = CustomerType.HAJJ_PILGRIM }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TypeSelectionCard(
                            title = "General Customer / Resident",
                            subtitle = "Standard commercial or personal cargo shipment subject to standard Indonesian import duty and VAT regulations.",
                            isSelected = customerType == CustomerType.GENERAL_CUSTOMER,
                            badgeText = "Standard Customs",
                            badgeColor = JneNavy,
                            onClick = { viewModel.formCustomerType.value = CustomerType.GENERAL_CUSTOMER }
                        )
                    }
                }

                // Step 2: Sender & Hajj Profile
                2 -> {
                    Column {
                        Text("Sender Identity & Verification", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Text("Sender WhatsApp is mandatory and verified for transactional updates", fontSize = 12.sp, color = JneTextSecondary)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = senderName,
                            onValueChange = { viewModel.senderName.value = it },
                            label = { Text("Sender Full Name (Passport Exact)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Mandatory Verified Sender WhatsApp
                        OutlinedTextField(
                            value = senderWhatsapp,
                            onValueChange = { viewModel.senderWhatsapp.value = it },
                            label = { Text("Sender WhatsApp (Mandatory)") },
                            trailingIcon = {
                                if (senderWhatsappState == VerificationState.VERIFIED) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = JneGreen)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Verified", fontSize = 10.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        if (customerType == CustomerType.HAJJ_PILGRIM) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Hajj Pilgrimage Identity", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = senderPassportNumber,
                                    onValueChange = { viewModel.senderPassportNumber.value = it },
                                    label = { Text("Passport #") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = senderNikKtp,
                                    onValueChange = { viewModel.senderNikKtp.value = it },
                                    label = { Text("NIK / KTP #") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = senderKloterNumber,
                                    onValueChange = { viewModel.senderKloterNumber.value = it },
                                    label = { Text("Kloter #") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = senderEmbarkation,
                                    onValueChange = { viewModel.senderEmbarkation.value = it },
                                    label = { Text("Embarkation") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = senderHajjPortion,
                                    onValueChange = { viewModel.senderHajjPortion.value = it },
                                    label = { Text("Portion #") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = senderHajjYear,
                                    onValueChange = { viewModel.senderHajjYear.value = it },
                                    label = { Text("Hajj Season") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Passport & SISKOHAT Upload Buttons
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { viewModel.showMessage("Passport document scanned successfully.") },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Passport Scan", fontSize = 11.sp)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.showMessage("SISKOHAT proof attached.") },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("SISKOHAT Proof", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Step 3: Receiver Information
                3 -> {
                    Column {
                        Text("Receiver Address in Indonesia", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Text("Receiver phone is mandatory for last-mile delivery contact", fontSize = 12.sp, color = JneTextSecondary)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = receiverName,
                            onValueChange = { viewModel.receiverName.value = it },
                            label = { Text("Receiver Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = receiverPhone,
                            onValueChange = { viewModel.receiverPhone.value = it },
                            label = { Text("Receiver Phone Number (Required)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = receiverProvince,
                                onValueChange = { viewModel.receiverProvince.value = it },
                                label = { Text("Province") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = receiverCity,
                                onValueChange = { viewModel.receiverCity.value = it },
                                label = { Text("City / Regency") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = receiverDistrict,
                                onValueChange = { viewModel.receiverDistrict.value = it },
                                label = { Text("District") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = receiverPostalCode,
                                onValueChange = { viewModel.receiverPostalCode.value = it },
                                label = { Text("Postal Code") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = receiverAddress,
                            onValueChange = { viewModel.receiverAddress.value = it },
                            label = { Text("Street Address & Building #") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = receiverLandmark,
                            onValueChange = { viewModel.receiverLandmark.value = it },
                            label = { Text("Landmark (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }

                // Step 4: Package Details & Item Valuation Table
                4 -> {
                    Column {
                        Text("Package & Item Valuation Table", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Text("Enter item descriptions and declared FOB value", fontSize = 12.sp, color = JneTextSecondary)
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = grossWeightKg.toString(),
                                onValueChange = { viewModel.grossWeightKg.value = it.toDoubleOrNull() ?: 1.0 },
                                label = { Text("Estimated Weight (kg)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = packageQty.toString(),
                                onValueChange = { viewModel.packageQty.value = it.toIntOrNull() ?: 1 },
                                label = { Text("Package Quantity") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = lengthCm.toString(),
                                onValueChange = { viewModel.lengthCm.value = it.toIntOrNull() ?: 10 },
                                label = { Text("Length (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            OutlinedTextField(
                                value = widthCm.toString(),
                                onValueChange = { viewModel.widthCm.value = it.toIntOrNull() ?: 10 },
                                label = { Text("Width (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            OutlinedTextField(
                                value = heightCm.toString(),
                                onValueChange = { viewModel.heightCm.value = it.toIntOrNull() ?: 10 },
                                label = { Text("Height (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Declared Items List", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            TextButton(onClick = { viewModel.addFormItem() }) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Item", fontSize = 12.sp)
                            }
                        }

                        // Item Valuation Table Rows
                        formItems.forEachIndexed { index, item ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = ButtonDefaults.outlinedButtonBorder
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Item #${index + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                                        if (formItems.size > 1) {
                                            IconButton(onClick = { viewModel.removeFormItem(index) }, modifier = Modifier.size(24.dp)) {
                                                Icon(Icons.Default.Delete, contentDescription = null, tint = JneRed, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }

                                    OutlinedTextField(
                                        value = item.description,
                                        onValueChange = { viewModel.updateFormItem(index, item.copy(description = it)) },
                                        label = { Text("Description") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true
                                    )

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        OutlinedTextField(
                                            value = item.category,
                                            onValueChange = { viewModel.updateFormItem(index, item.copy(category = it)) },
                                            label = { Text("Category") },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedTextField(
                                            value = item.quantity.toString(),
                                            onValueChange = { viewModel.updateFormItem(index, item.copy(quantity = it.toIntOrNull() ?: 1)) },
                                            label = { Text("Qty") },
                                            modifier = Modifier.weight(0.6f),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedTextField(
                                            value = item.unitValueUsd.toString(),
                                            onValueChange = { viewModel.updateFormItem(index, item.copy(unitValueUsd = it.toDoubleOrNull() ?: 0.0)) },
                                            label = { Text("Unit ($)") },
                                            modifier = Modifier.weight(0.8f),
                                            singleLine = true
                                        )
                                    }

                                    Text(
                                        text = "Subtotal: $%.2f USD".format(item.totalValueUsd),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = JneGreen,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Total Item Value (FOB): $%.2f USD".format(totalFobUsd),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = JneNavy,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Product Safety Screening Questions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Spacer(modifier = Modifier.height(6.dp))

                        SwitchRow("Contains Liquid or Aerosol Spray?", hasLiquidAerosol) { viewModel.hasLiquidAerosol.value = it }
                        SwitchRow("Contains Dangerous / Prohibited Goods?", hasDangerousGoods) { viewModel.hasDangerousGoods.value = it }
                        SwitchRow("Contains Lithium Batteries or Electronics?", hasBattery) { viewModel.hasBattery.value = it }
                        SwitchRow("Contains Medicine / Supplements?", hasMedicine) { viewModel.hasMedicine.value = it }
                    }
                }

                // Step 5: Regulation Check
                5 -> {
                    Column {
                        Text("Automated Regulation Check", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Spacer(modifier = Modifier.height(16.dp))

                        when (regStatus) {
                            RegulationStatus.PROHIBITED -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(JneRedLight, RoundedCornerShape(12.dp))
                                        .border(2.dp, JneRed, RoundedCornerShape(12.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Cancel, contentDescription = null, tint = JneRed)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("PROHIBITED GOODS - REQUEST BLOCKED", fontWeight = FontWeight.Black, color = JneRed, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Your package contains liquid, aerosol, or dangerous goods prohibited under international aviation cargo laws. Submission is automatically rejected.",
                                            fontSize = 12.sp,
                                            color = JneTextPrimary
                                        )
                                    }
                                }
                            }

                            RegulationStatus.ALLOWED -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(JneGreenLight, RoundedCornerShape(12.dp))
                                        .border(2.dp, JneGreen, RoundedCornerShape(12.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JneGreen)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("REGULATION CHECK PASSED", fontWeight = FontWeight.Black, color = JneGreen, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "All items are cleared for international air freight. Dimensions (${lengthCm}x${widthCm}x${heightCm} cm) are within maximum 60x60x80 cm limits.",
                                            fontSize = 12.sp,
                                            color = JneTextPrimary
                                        )
                                    }
                                }
                            }

                            else -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(JneOrangeLight, RoundedCornerShape(12.dp))
                                        .border(2.dp, JneOrange, RoundedCornerShape(12.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Warning, contentDescription = null, tint = JneOrange)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("REQUIRES MANUAL ADMIN REVIEW", fontWeight = FontWeight.Black, color = JneOrange, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Items require additional document inspection by JNE Saudi customs operations team before driver dispatch.",
                                            fontSize = 12.sp,
                                            color = JneTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Step 6: Customs Facility / Tax Estimate
                6 -> {
                    Column {
                        Text("Customs Facility & Duty Estimator", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (customerType == CustomerType.HAJJ_PILGRIM && regStatus == RegulationStatus.ALLOWED) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = JneGreenLight),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = JneGreen)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("ELIGIBLE - HAJJ CUSTOMS FACILITY", fontWeight = FontWeight.Bold, color = JneGreen, fontSize = 15.sp)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("FOB Declared Value: $%.2f USD".format(totalFobUsd), fontSize = 12.sp)
                                    Text("Import Duty Rate: 0.0% (Exempt under Hajj Facility)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneGreen)
                                    Text("Import Taxes / VAT: SAR 0.00 (Exempt)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneGreen)
                                    Text("Hajj Allowance Usage: Shipment 1 of 2 max per pilgrim", fontSize = 12.sp, color = JneTextSecondary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Disclaimer: Final customs release is determined by Indonesian Customs at arrival airport.", fontSize = 10.sp, color = JneTextSecondary)
                                }
                            }
                        } else {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("General Customs Duty Estimate", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = JneNavy)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("FOB Value: $%.2f USD".format(totalFobUsd), fontSize = 12.sp)
                                    Text("Estimated Import Duty (7.5%): $%.2f USD".format(totalFobUsd * 0.075), fontSize = 12.sp)
                                    Text("Estimated Import VAT (11%): $%.2f USD".format(totalFobUsd * 0.11), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                // Step 7: Pickup Review & Schedule
                7 -> {
                    Column {
                        Text("Pickup Schedule & Final Review", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        Text("Verify all details before submitting to JNE Saudi Operations", fontSize = 12.sp, color = JneTextSecondary)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = pickupAddress,
                            onValueChange = { viewModel.pickupAddress.value = it },
                            label = { Text("Hotel / Pickup Address in Makkah") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = pickupDate,
                                onValueChange = { viewModel.pickupDate.value = it },
                                label = { Text("Pickup Date") },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = pickupTimeSlot,
                                onValueChange = { viewModel.pickupTimeSlot.value = it },
                                label = { Text("Time Window") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Estimate Cost Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = JneNavy),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Estimated Charges Summary", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text("Freight Fee (${grossWeightKg} kg):", color = Color.White.copy(0.8f), fontSize = 12.sp)
                                    Text("SAR %.2f".format(grossWeightKg * 10.0), color = Color.White, fontSize = 12.sp)
                                }
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text("Service Fee:", color = Color.White.copy(0.8f), fontSize = 12.sp)
                                    Text("SAR 12.00", color = Color.White, fontSize = 12.sp)
                                }
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text("Insurance Premium:", color = Color.White.copy(0.8f), fontSize = 12.sp)
                                    Text("SAR 5.00", color = Color.White, fontSize = 12.sp)
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color.White.copy(0.2f))
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text("Estimated Total Bill:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("SAR %.2f".format((grossWeightKg * 10.0) + 17.0), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text("Sub-Agent Estimated Commission:", color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("SAR %.2f".format(((grossWeightKg * 10.0) + 17.0) * 0.12), color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Navigation Buttons (Back / Next / Submit)
        Surface(color = Color.White, shadowElevation = 8.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { viewModel.pickupStep.value = step - 1 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Button(
                    onClick = {
                        if (step < 7) {
                            viewModel.pickupStep.value = step + 1
                        } else {
                            viewModel.submitPickupForm()
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("btn_step_next"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (step == 7) JneGreen else JneRed
                    ),
                    enabled = (step != 5 || regStatus != RegulationStatus.PROHIBITED)
                ) {
                    Text(if (step == 7) "Submit Pickup Request" else "Next Step", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TypeSelectionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    badgeText: String,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) JneRed else JneBorder,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) JneRedLight.copy(alpha = 0.3f) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                StatusChip(text = badgeText, backgroundColor = badgeColor.copy(0.15f), textColor = badgeColor)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = subtitle, fontSize = 12.sp, color = JneTextSecondary, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun SwitchRow(label: String, checkedState: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = JneTextPrimary, modifier = Modifier.weight(1f))
        Switch(checked = checkedState, onCheckedChange = onCheckedChange)
    }
}

private fun getStepTitle(step: Int): String {
    return when (step) {
        1 -> "Customer Type"
        2 -> "Sender & Hajj Profile"
        3 -> "Receiver Address"
        4 -> "Package & Item Valuation"
        5 -> "Regulation Check"
        6 -> "Customs Facility"
        7 -> "Pickup Schedule & Review"
        else -> ""
    }
}
