package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.SubAgentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen {
    object Auth : Screen()
    object Home : Screen()
    object MyShipments : Screen()
    object RequestPickup : Screen()
    object Wallet : Screen()
    object Profile : Screen()
    object QrBooking : Screen()
    data class ShipmentDetail(val shipmentId: String) : Screen()
    data class PickupDetail(val requestId: String) : Screen()
    data class DriverVerificationPreview(val shipmentId: String) : Screen()
    data class InvoiceInsurance(val shipmentId: String) : Screen()
    data class TrackAwb(val initialAwb: String? = null) : Screen()
    object Notifications : Screen()
    data class FeedbackComplaint(val shipmentId: String = "") : Screen()
}

class SubAgentViewModel(
    private val repository: SubAgentRepository = SubAgentRepository()
) : ViewModel() {

    // --- State Exposure from Repository ---
    val profile: StateFlow<SubAgentProfile> = repository.profile
    val pickupRequests: StateFlow<List<PickupRequest>> = repository.pickupRequests
    val shipments: StateFlow<List<Shipment>> = repository.shipments
    val availableBalanceSar: StateFlow<Double> = repository.availableBalanceSar
    val pendingCommissionSar: StateFlow<Double> = repository.pendingCommissionSar
    val creditedThisMonthSar: StateFlow<Double> = repository.creditedThisMonthSar
    val ledgerEntries: StateFlow<List<WalletLedgerEntry>> = repository.ledgerEntries
    val withdrawals: StateFlow<List<WithdrawalRequest>> = repository.withdrawals
    val notifications: StateFlow<List<NotificationRecord>> = repository.notifications

    val qrOpenedCount = repository.qrOpenedCount
    val qrStartedCount = repository.qrStartedCount
    val qrSubmittedCount = repository.qrSubmittedCount
    val qrCompletedCount = repository.qrCompletedCount

    // --- Navigation State ---
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()
    private val navigationBackStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack() {
        if (navigationBackStack.isNotEmpty()) {
            _currentScreen.value = navigationBackStack.removeAt(navigationBackStack.lastIndex)
        } else {
            _currentScreen.value = Screen.Home
        }
    }

    // --- Toast / Snackbar Notification State ---
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // --- 7-Step Pickup Request Form State ---
    var pickupStep = MutableStateFlow(1) // Step 1 to 7

    var formCustomerType = MutableStateFlow(CustomerType.HAJJ_PILGRIM)

    // Step 2: Sender & Hajj Profile
    var senderName = MutableStateFlow("Ahmad Fauzan")
    var senderNationality = MutableStateFlow("Indonesia")
    var senderNikKtp = MutableStateFlow("3273012345670001")
    var senderPassportNumber = MutableStateFlow("X1234567")
    var senderHajjYear = MutableStateFlow("1447H / 2026")
    var senderKloterNumber = MutableStateFlow("JKS-14")
    var senderEmbarkation = MutableStateFlow("Jakarta (JKS)")
    var senderHajjPortion = MutableStateFlow("1300987654")
    var senderTravelDate = MutableStateFlow("2026-06-15")
    var senderWhatsapp = MutableStateFlow("+62 812 3456 7890")
    var senderWhatsappVerifiedState = MutableStateFlow(VerificationState.VERIFIED)

    // Step 3: Receiver Info
    var receiverName = MutableStateFlow("Siti Aleyah Rahmah")
    var receiverPhone = MutableStateFlow("+62 813 9876 5432")
    var receiverCountry = MutableStateFlow("Indonesia")
    var receiverProvince = MutableStateFlow("DKI Jakarta")
    var receiverCity = MutableStateFlow("Jakarta Selatan")
    var receiverDistrict = MutableStateFlow("Kebayoran Baru")
    var receiverSubdistrict = MutableStateFlow("Gunung")
    var receiverPostalCode = MutableStateFlow("12120")
    var receiverAddress = MutableStateFlow("Jl. Pakubuwono VI No. 42A")
    var receiverLandmark = MutableStateFlow("Near Al-Azhar Mosque")

    // Step 4: Package & Item Valuation Table
    var packageQty = MutableStateFlow(1)
    var grossWeightKg = MutableStateFlow(18.5)
    var lengthCm = MutableStateFlow(55)
    var widthCm = MutableStateFlow(48)
    var heightCm = MutableStateFlow(60)

    var formItems = MutableStateFlow(
        listOf(
            ShipmentItem(
                description = "Ajwa Dates Makkah Premium Box (5 Packs x 1kg)",
                category = "Food / Dates",
                quantity = 5,
                unit = "Packs",
                unitValueUsd = 97.00,
                isNew = true,
                isPersonal = true
            )
        )
    )

    // Screening Questions
    var hasBattery = MutableStateFlow(false)
    var hasLiquidAerosol = MutableStateFlow(false)
    var hasMedicine = MutableStateFlow(false)
    var hasFood = MutableStateFlow(true)
    var hasDangerousGoods = MutableStateFlow(false)

    // Step 7: Pickup Schedule
    var pickupAddress = MutableStateFlow("Hotel Ajyad Royale, Room 402, Makkah")
    var pickupDate = MutableStateFlow("2026-08-09")
    var pickupTimeSlot = MutableStateFlow("14:00 - 17:00")

    fun addFormItem() {
        val current = formItems.value.toMutableList()
        current.add(
            ShipmentItem(
                description = "New Item ${current.size + 1}",
                category = "General",
                quantity = 1,
                unit = "Pcs",
                unitValueUsd = 20.0
            )
        )
        formItems.value = current
    }

    fun removeFormItem(index: Int) {
        if (formItems.value.size > 1) {
            val current = formItems.value.toMutableList()
            current.removeAt(index)
            formItems.value = current
        }
    }

    fun updateFormItem(index: Int, updated: ShipmentItem) {
        val current = formItems.value.toMutableList()
        if (index in current.indices) {
            current[index] = updated
            formItems.value = current
        }
    }

    // Calculate total FOB USD
    val totalFobUsd: Double
        get() = formItems.value.sumOf { it.totalValueUsd }

    // Regulatory Check Result
    val calculatedRegulationStatus: RegulationStatus
        get() {
            if (hasLiquidAerosol.value || hasDangerousGoods.value) return RegulationStatus.PROHIBITED
            if (hasBattery.value || hasMedicine.value) return RegulationStatus.RESTRICTED
            if (totalFobUsd > 1500) return RegulationStatus.MANUAL_REVIEW
            return RegulationStatus.ALLOWED
        }

    fun resetPickupForm() {
        pickupStep.value = 1
        formCustomerType.value = CustomerType.HAJJ_PILGRIM
        senderName.value = "Ahmad Fauzan"
        senderWhatsapp.value = "+62 812 3456 7890"
        senderWhatsappVerifiedState.value = VerificationState.VERIFIED
        receiverName.value = "Siti Aleyah Rahmah"
        receiverPhone.value = "+62 813 9876 5432"
        packageQty.value = 1
        grossWeightKg.value = 18.5
        formItems.value = listOf(
            ShipmentItem(
                description = "Ajwa Dates Makkah Premium Box (5 Packs x 1kg)",
                category = "Food / Dates",
                quantity = 5,
                unit = "Packs",
                unitValueUsd = 97.00
            )
        )
        hasLiquidAerosol.value = false
        hasDangerousGoods.value = false
        hasBattery.value = false
        hasMedicine.value = false
        hasFood.value = true
    }

    fun submitPickupForm(): Boolean {
        if (calculatedRegulationStatus == RegulationStatus.PROHIBITED) {
            showMessage("Cannot submit: Package contains prohibited goods (Liquid/Aerosol/Dangerous Goods).")
            return false
        }

        val newRequestId = "PR-2026-${(1000..9999).random()}"
        val estimatedTotal = (grossWeightKg.value * 10.0) + 12.0 + 5.0
        val estimatedComm = estimatedTotal * 0.12

        val newRequest = PickupRequest(
            requestId = newRequestId,
            customerType = formCustomerType.value,
            sender = SenderProfile(
                fullName = senderName.value,
                nationality = senderNationality.value,
                nikKtp = senderNikKtp.value,
                passportNumber = senderPassportNumber.value,
                hajjYear = senderHajjYear.value,
                kloterNumber = senderKloterNumber.value,
                embarkation = senderEmbarkation.value,
                hajjPortionNumber = senderHajjPortion.value,
                travelDate = senderTravelDate.value,
                whatsappNumber = senderWhatsapp.value,
                isWhatsappVerified = (senderWhatsappVerifiedState.value == VerificationState.VERIFIED)
            ),
            receiver = ReceiverProfile(
                fullName = receiverName.value,
                phone = receiverPhone.value,
                country = receiverCountry.value,
                province = receiverProvince.value,
                city = receiverCity.value,
                district = receiverDistrict.value,
                subdistrict = receiverSubdistrict.value,
                postalCode = receiverPostalCode.value,
                streetAddress = receiverAddress.value,
                landmark = receiverLandmark.value
            ),
            items = formItems.value,
            packageQuantity = packageQty.value,
            estimatedWeightKg = grossWeightKg.value,
            dimensionsCm = "${lengthCm.value} x ${widthCm.value} x ${heightCm.value} cm",
            pickupAddress = pickupAddress.value,
            requestedDate = pickupDate.value,
            requestedTimeSlot = pickupTimeSlot.value,
            status = RequestStatus.WAITING_ADMIN_REVIEW,
            submissionTimestamp = "2026-08-08 18:25:00",
            regulationStatus = calculatedRegulationStatus,
            isHajjFacilityEligible = (formCustomerType.value == CustomerType.HAJJ_PILGRIM && grossWeightKg.value <= 30 && totalFobUsd <= 1500),
            HajjShipmentCount = 1,
            estimatedTotalSar = estimatedTotal,
            estimatedCommissionSar = estimatedComm
        )

        repository.submitNewPickupRequest(newRequest)
        showMessage("Pickup Request $newRequestId submitted successfully!")
        navigateTo(Screen.PickupDetail(newRequestId))
        return true
    }

    fun cancelPickupRequest(requestId: String, reason: String) {
        repository.cancelPickupRequest(requestId, reason)
        showMessage("Request $requestId cancelled.")
    }

    fun approvePickupRequest(requestId: String, notes: String? = null) {
        repository.approvePickupRequest(requestId, notes)
        showMessage("Pickup Request $requestId APPROVED! Driver assigned & WhatsApp notification sent.")
    }

    fun rejectPickupRequest(requestId: String, reason: String) {
        repository.cancelPickupRequest(requestId, reason)
        showMessage("Pickup Request $requestId REJECTED.")
    }

    fun requestAdditionalDocs(requestId: String, note: String) {
        repository.requestAdditionalDocs(requestId, note)
        showMessage("Additional document request sent to customer via WhatsApp.")
    }

    fun requestWithdrawal(amountSar: Double, iban: String, bankName: String) {
        val success = repository.requestWithdrawal(amountSar, iban, bankName)
        if (success) {
            showMessage("Withdrawal request of SAR %.2f submitted for review.".format(amountSar))
        } else {
            showMessage("Insufficient wallet balance for withdrawal.")
        }
    }

    fun triggerReversalDemo(shipmentId: String) {
        repository.simulateCommissionReversal(shipmentId, 15.00)
        showMessage("Commission reversal of SAR 15.00 applied for $shipmentId.")
    }

    fun updateProfileInfo(businessName: String, whatsappNumber: String, city: String, addressDetail: String, iban: String) {
        repository.updateProfile(businessName, whatsappNumber, city, addressDetail, iban)
        showMessage("Sub-Agent Profile updated successfully.")
    }

    fun updateBankAccount(bankName: String, accountHolder: String, iban: String) {
        repository.updateBankAccount(bankName, accountHolder, iban)
        showMessage("Akun Bank Penarikan berhasil diperbarui & diverifikasi via Gateway SAMA / SADAD.")
    }

    fun switchAccountStatus(status: AccountStatus) {
        repository.setAccountStatus(status)
        showMessage("Account status switched to: $status")
    }
}
