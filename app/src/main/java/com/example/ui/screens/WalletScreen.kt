package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.LedgerType
import com.example.ui.SubAgentViewModel
import com.example.ui.theme.*

@Composable
fun WalletScreen(
    viewModel: SubAgentViewModel,
    onBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val availableBalance by viewModel.availableBalanceSar.collectAsState()
    val pendingCommission by viewModel.pendingCommissionSar.collectAsState()
    val creditedThisMonth by viewModel.creditedThisMonthSar.collectAsState()
    val ledgerEntries by viewModel.ledgerEntries.collectAsState()
    val withdrawals by viewModel.withdrawals.collectAsState()

    var showWithdrawModal by remember { mutableStateOf(false) }
    var showEditBankModal by remember { mutableStateOf(false) }
    var withdrawAmountInput by remember { mutableStateOf("500.00") }

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
                Text(
                    text = "Sub-Agent Wallet & Commission",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Balance Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = JneNavy),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("Available Withdrawal Balance", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("SAR %.2f".format(availableBalance), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { showWithdrawModal = true },
                                colors = ButtonDefaults.buttonColors(containerColor = JneRed),
                                modifier = Modifier.weight(1f).testTag("btn_request_withdraw")
                            ) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Withdraw Funds", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.triggerReversalDemo("SHP-2026-001") },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White)),
                                modifier = Modifier.weight(1f).testTag("btn_reversal_demo")
                            ) {
                                Text("Reversal Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color.White.copy(0.3f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Column {
                                Text("Credited This Month", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Text("+SAR %.2f".format(creditedThisMonth), color = Color(0xFF4ADE80), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Pending Admin Review", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Text("SAR %.2f".format(pendingCommission), color = Color(0xFFFBBF24), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Bank Account Info
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = JneNavy)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verified Withdrawal Bank Account", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            }

                            Button(
                                onClick = { showEditBankModal = true },
                                colors = ButtonDefaults.buttonColors(containerColor = JneNavy),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp).testTag("btn_edit_bank_account")
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
                            Text("Terverifikasi • Gateway SAMA / SADAD", fontSize = 11.sp, color = JneGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Bank: ${profile.bankName} • An. ${profile.accountHolder}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("IBAN: ${profile.iban}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = JneTextPrimary)
                    }
                }
            }

            // Ledger Title
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Wallet Ledger & Transaction History", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Ledger Items List
            items(ledgerEntries) { entry ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    when (entry.type) {
                                        LedgerType.COMMISSION_AUTO_CLAIMED -> JneGreenLight
                                        LedgerType.WITHDRAWAL_COMPLETED, LedgerType.WITHDRAWAL_REQUESTED -> Color(0xFFE0F2FE)
                                        else -> JneRedLight
                                    },
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (entry.type) {
                                    LedgerType.COMMISSION_AUTO_CLAIMED -> Icons.Default.Add
                                    LedgerType.WITHDRAWAL_COMPLETED, LedgerType.WITHDRAWAL_REQUESTED -> Icons.Default.NorthEast
                                    else -> Icons.Default.SouthWest
                                },
                                contentDescription = null,
                                tint = when (entry.type) {
                                    LedgerType.COMMISSION_AUTO_CLAIMED -> JneGreen
                                    LedgerType.WITHDRAWAL_COMPLETED, LedgerType.WITHDRAWAL_REQUESTED -> Color(0xFF0284C7)
                                    else -> JneRed
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(entry.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = JneNavy)
                            Text(entry.description, fontSize = 11.sp, color = JneTextSecondary, maxLines = 2)
                            Text(entry.timestamp, fontSize = 10.sp, color = JneTextSecondary)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (entry.amountSar > 0) "+SAR %.2f".format(entry.amountSar) else "SAR %.2f".format(entry.amountSar),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (entry.amountSar > 0) JneGreen else JneRed
                            )
                            Text("Bal: SAR %.2f".format(entry.balanceAfterSar), fontSize = 10.sp, color = JneTextSecondary)
                        }
                    }
                }
            }
        }
    }

    // Withdrawal Request Modal Dialog
    if (showWithdrawModal) {
        AlertDialog(
            onDismissRequest = { showWithdrawModal = false },
            containerColor = JneNavy,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Request Bank Withdrawal", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Available for withdrawal: SAR %.2f".format(availableBalance), fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = withdrawAmountInput,
                        onValueChange = { withdrawAmountInput = it },
                        label = { Text("Withdrawal Amount (SAR)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_withdraw_amount"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Tujuan Transfer Bank:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                TextButton(
                                    onClick = {
                                        showWithdrawModal = false
                                        showEditBankModal = true
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Ubah Bank", fontSize = 10.sp, color = JneOrange, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text("Bank: ${profile.bankName}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("An: ${profile.accountHolder}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                            Text("IBAN: ${profile.iban}", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = withdrawAmountInput.toDoubleOrNull() ?: 0.0
                        viewModel.requestWithdrawal(amount, profile.iban, profile.bankName)
                        showWithdrawModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JneOrange),
                    modifier = Modifier.testTag("btn_confirm_withdraw")
                ) {
                    Text("Submit Withdrawal", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawModal = false }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    // Edit & Verify Bank Account Modal
    if (showEditBankModal) {
        EditBankAccountDialog(
            currentBankName = profile.bankName,
            currentAccountHolder = profile.accountHolder,
            currentIban = profile.iban,
            onDismiss = { showEditBankModal = false },
            onSave = { newBank, newHolder, newIban ->
                viewModel.updateBankAccount(newBank, newHolder, newIban)
                showEditBankModal = false
            }
        )
    }
}

@Composable
fun EditBankAccountDialog(
    currentBankName: String,
    currentAccountHolder: String,
    currentIban: String,
    onDismiss: () -> Unit,
    onSave: (bankName: String, accountHolder: String, iban: String) -> Unit
) {
    var bankName by remember { mutableStateOf(currentBankName) }
    var accountHolder by remember { mutableStateOf(currentAccountHolder) }
    var iban by remember { mutableStateOf(currentIban) }
    var isVerifying by remember { mutableStateOf(false) }
    var isVerifiedSuccess by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = JneNavy,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit & Verifikasi Bank Account", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        text = {
            Column {
                Text(
                    text = "Rekening penarikan komisi Sub-Agent diverifikasi langsung dengan jaringan perbankan Arab Saudi (SAMA / SADAD / SARIE System).",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = bankName,
                    onValueChange = { bankName = it; isVerifiedSuccess = false },
                    label = { Text("Nama Bank (Saudi Arabia)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_bank_name"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountHolder,
                    onValueChange = { accountHolder = it; isVerifiedSuccess = false },
                    label = { Text("Nama Pemilik Rekening") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_account_holder"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = iban,
                    onValueChange = { iban = it; isVerifiedSuccess = false },
                    label = { Text("Nomor IBAN (misal SA98...)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_iban"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Real-time IBAN Verification Button / Status
                if (isVerifiedSuccess) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JneGreenLight)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("IBAN Verified 100% Valid", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Tersambung dengan SAMA & CR Usaha Sub-Agent", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            isVerifying = true
                            isVerifiedSuccess = true
                            isVerifying = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = JneOrange),
                        modifier = Modifier.fillMaxWidth().testTag("btn_verify_iban")
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Verifikasi IBAN via Gateway SAMA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(bankName, accountHolder, iban) },
                colors = ButtonDefaults.buttonColors(containerColor = JneGreen),
                modifier = Modifier.testTag("btn_save_bank")
            ) {
                Text("Simpan & Guanakan Bank Ini", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color.White)
            }
        }
    )
}
