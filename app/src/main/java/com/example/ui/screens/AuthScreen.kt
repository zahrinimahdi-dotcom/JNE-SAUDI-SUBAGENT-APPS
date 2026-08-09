package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AccountStatus
import com.example.ui.SubAgentViewModel
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    viewModel: SubAgentViewModel,
    onLoginSuccess: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()

    var phoneInput by remember { mutableStateOf("+966 50 123 4567") }
    var passwordInput by remember { mutableStateOf("••••••••") }
    var otpInput by remember { mutableStateOf("") }
    var showOtpDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JneNavy)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Badge Logo
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(JneRed, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JNE",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "JNE Saudi Express",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sub-Agent Operational Portal",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Sub-Agent Sign In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneNavy
                )
                Text(
                    text = "Enter your registered phone/email to continue",
                    fontSize = 12.sp,
                    color = JneTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("Phone or Email") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_phone"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("input_password"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showOtpDialog = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_signin"),
                    colors = ButtonDefaults.buttonColors(containerColor = JneRed)
                ) {
                    Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Account Status Switcher for Prototype Evaluation
                Text(
                    text = "Prototype Account State:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = JneTextSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterChip(
                        selected = profile.status == AccountStatus.VERIFIED,
                        onClick = { viewModel.switchAccountStatus(AccountStatus.VERIFIED) },
                        label = { Text("Verified", fontSize = 10.sp) }
                    )
                    FilterChip(
                        selected = profile.status == AccountStatus.PENDING_VERIFICATION,
                        onClick = { viewModel.switchAccountStatus(AccountStatus.PENDING_VERIFICATION) },
                        label = { Text("Pending", fontSize = 10.sp) }
                    )
                    FilterChip(
                        selected = profile.status == AccountStatus.REJECTED,
                        onClick = { viewModel.switchAccountStatus(AccountStatus.REJECTED) },
                        label = { Text("Rejected", fontSize = 10.sp) }
                    )
                    FilterChip(
                        selected = profile.status == AccountStatus.SUSPENDED,
                        onClick = { viewModel.switchAccountStatus(AccountStatus.SUSPENDED) },
                        label = { Text("Suspended", fontSize = 10.sp) }
                    )
                }

                // Show State Warning Card if not verified
                if (profile.status != AccountStatus.VERIFIED) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(JneOrangeLight, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = JneOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (profile.status) {
                                    AccountStatus.PENDING_VERIFICATION -> "Account documents are under admin review. Pickup requests are temporarily disabled."
                                    AccountStatus.REJECTED -> "Account verification rejected. Reason: Commercial registration document expired."
                                    AccountStatus.SUSPENDED -> "Account suspended due to compliance audit. Contact JNE Support."
                                    else -> ""
                                },
                                fontSize = 11.sp,
                                color = JneOrange,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    // OTP Simulation Dialog
    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = { Text("Enter WhatsApp 2FA OTP") },
            text = {
                Column {
                    Text("We sent a 6-digit verification code to $phoneInput via WhatsApp.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { if (it.length <= 6) otpInput = it },
                        label = { Text("Verification Code") },
                        placeholder = { Text("123456") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_otp")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showOtpDialog = false
                        onLoginSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JneNavy)
                ) {
                    Text("Verify & Enter App")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
