package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.SubAgentViewModel
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: SubAgentViewModel,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()

    var showEditModal by remember { mutableStateOf(false) }
    var showBankEditModal by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(profile.businessName) }
    var editWa by remember { mutableStateOf(profile.whatsappNumber) }
    var editCity by remember { mutableStateOf(profile.city) }
    var editAddress by remember { mutableStateOf(profile.addressDetail) }
    var editIban by remember { mutableStateOf(profile.iban) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneGrayBackground)
            .padding(bottom = 80.dp)
    ) {
        // Header
        Surface(color = JneNavy, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val initials = profile.businessName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(JneRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (initials.isNotEmpty()) initials else "KG", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(profile.businessName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Sub-Agent ID: ${profile.subAgentId} • ${profile.ownerName}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Verified JNE Sub-Agent Partner", color = Color(0xFF4ADE80), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Profile Info Card
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
                        Text("Business & Contact Information", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        TextButton(
                            onClick = { showEditModal = true },
                            modifier = Modifier.testTag("btn_edit_profile")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 12.sp)
                        }
                    }

                    ProfileRow("Owner / Responsible Person", profile.ownerName)
                    ProfileRow("CR Number (Saudi Arabia)", profile.crNumber)
                    ProfileRow("CR Document Expiry", profile.documentExpiry)
                    ProfileRow("Sub-Agent Verified WhatsApp", profile.whatsappNumber)
                    ProfileRow("Business Address", "${profile.addressDetail}, ${profile.city}")
                    ProfileRow("Parent Master Agent", profile.parentAgentName)
                    ProfileRow("Direct Commission Scheme", "12.5% on verified shipments")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bank Account Card
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = JneNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bank Penarikan Komisi", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                        }

                        Button(
                            onClick = { showBankEditModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = JneNavy),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp).testTag("btn_profile_edit_bank")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit & Verifikasi", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = JneGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAMA / SADAD System Verified", fontSize = 11.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Bank: ${profile.bankName}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text("An: ${profile.accountHolder}", fontSize = 12.sp)
                    Text("IBAN: ${profile.iban}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Settings & Actions Menu
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    MenuItemRow(Icons.Default.QrCode2, "QR & Customer Booking", "Manage booking code & printable poster") {
                        onNavigate(Screen.QrBooking)
                    }
                    MenuItemRow(Icons.Default.AccountBalanceWallet, "Wallet & Withdrawal Account", "Bank IBAN: ${profile.iban}") {
                        onNavigate(Screen.Wallet)
                    }
                    MenuItemRow(Icons.Default.SupportAgent, "Support & Complaint Tickets", "View 24/7 JNE Saudi Help Center tickets") {
                        onNavigate(Screen.FeedbackComplaint("SHP-2026-001"))
                    }
                    MenuItemRow(Icons.Default.Security, "Security & 2FA PIN", "Password, Biometric login & Wallet PIN") {
                        viewModel.showMessage("Security settings configured.")
                    }
                    MenuItemRow(Icons.Default.Notifications, "Notification Preferences", "Push & WhatsApp event alerts") {
                        onNavigate(Screen.Notifications)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_logout"),
                colors = ButtonDefaults.buttonColors(containerColor = JneRed)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out of Sub-Agent App", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Edit Profile Dialog
    if (showEditModal) {
        AlertDialog(
            onDismissRequest = { showEditModal = false },
            containerColor = JneNavy,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Edit Business Information", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                val fieldColors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                )
                Column {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Business Name") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = editWa, onValueChange = { editWa = it }, label = { Text("Verified WhatsApp") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = editCity, onValueChange = { editCity = it }, label = { Text("City") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = editAddress, onValueChange = { editAddress = it }, label = { Text("Address") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = editIban, onValueChange = { editIban = it }, label = { Text("Payout IBAN") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfileInfo(editName, editWa, editCity, editAddress, editIban)
                        showEditModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JneOrange)
                ) {
                    Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditModal = false }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    // Edit Bank Account Dialog
    if (showBankEditModal) {
        EditBankAccountDialog(
            currentBankName = profile.bankName,
            currentAccountHolder = profile.accountHolder,
            currentIban = profile.iban,
            onDismiss = { showBankEditModal = false },
            onSave = { newBank, newHolder, newIban ->
                viewModel.updateBankAccount(newBank, newHolder, newIban)
                showBankEditModal = false
            }
        )
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontSize = 10.sp, color = JneTextSecondary)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneTextPrimary)
    }
}

@Composable
private fun MenuItemRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(JneGrayBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = JneNavy, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = JneNavy)
            Text(subtitle, fontSize = 10.sp, color = JneTextSecondary)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = JneTextSecondary)
    }
}
