package com.example.data.model

import java.util.UUID

enum class AccountStatus {
    VERIFIED,
    PENDING_VERIFICATION,
    REJECTED,
    SUSPENDED
}

enum class CustomerType {
    HAJJ_PILGRIM,
    GENERAL_CUSTOMER
}

enum class BookingSource(val displayName: String) {
    SUB_AGENT_APP("Sub-Agent App"),
    CUSTOMER_QR("Customer QR Code"),
    SHARED_LINK("Customer Share Link")
}

enum class VerificationState {
    UNVERIFIED,
    OTP_SENT,
    VERIFIED,
    FAILED
}

enum class RegulationStatus {
    ALLOWED,
    RESTRICTED,
    MANUAL_REVIEW,
    PROHIBITED
}

enum class RequestStatus(val displayName: String) {
    DRAFT("Draft"),
    WAITING_ADMIN_REVIEW("Waiting Review"),
    ADDITIONAL_DOCS_REQUIRED("Docs Required"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    EXPIRED("Expired"),
    DRIVER_ASSIGNED("Driver Assigned"),
    DRIVER_ON_THE_WAY("Driver On The Way"),
    DRIVER_ARRIVED("Driver Arrived"),
    PICKUP_COMPLETED("Pickup Completed"),
    PICKUP_FAILED("Pickup Failed")
}

enum class ShipmentStatus(val displayName: String) {
    DRAFT("Draft"),
    PICKUP_REQUESTED("Pickup Requested"),
    ADMIN_REVIEW("Admin Review"),
    DRIVER_ASSIGNED("Driver Assigned"),
    PICKUP_COMPLETED("Pickup Completed"),
    DRIVER_VERIFIED("Driver Verified"),
    AWAITING_ADMIN_VERIFICATION("Awaiting Admin"),
    INVOICE_ISSUED("Invoice Issued"),
    WAREHOUSE_RECEIVED("Warehouse Received"),
    VERIFICATION_IN_PROGRESS("Warehouse Verification"),
    PACKING("Packing"),
    AWB_GENERATED("AWB Generated"),
    DISPATCHED("Dispatched"),
    EXPORTED("Exported"),
    IN_TRANSIT("In Transit"),
    ARRIVED_INDONESIA("Arrived Indonesia"),
    CUSTOMS_PROCESSING("Customs Processing"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    WAITING_CONFIRMATION("Waiting Confirmation"),
    COMPLETED("Completed"),
    HOLD("On Hold"),
    REJECTED("Rejected"),
    CUSTOMER_PICKUP("Customer Pickup"),
    RE_DELIVERY("Re-Delivery"),
    REFUNDED("Refunded"),
    CLOSED("Closed")
}

enum class PaymentStatus(val displayName: String) {
    PENDING_COLLECTION("Pending Collection"),
    COLLECTED_BY_DRIVER("Collected by Driver"),
    PENDING_VERIFICATION("Pending Verification"),
    VERIFICATION_IN_PROGRESS("Verifying Payment"),
    VERIFIED_COMPLETED("Payment Verified"),
    FAILED("Payment Failed")
}

enum class CommissionStatus(val displayName: String) {
    NOT_ELIGIBLE("Not Eligible"),
    PENDING_PAYMENT("Pending Payment"),
    PENDING_ADMIN("Pending Admin"),
    CALCULATED("Calculated"),
    AUTO_CLAIMED("Auto-Claimed"),
    CREDITED("CredITED"),
    ADJUSTED("Adjusted"),
    REVERSED("Reversed")
}

data class SubAgentProfile(
    val subAgentId: String = "SA-26-08-042",
    val businessName: String = "Kareem Ghanem",
    val ownerName: String = "Kareem Ghanem",
    val email: String = "kareem.ghanem@jne-sa.com",
    val phone: String = "+966 50 123 4567",
    val isPhoneVerified: Boolean = true,
    val whatsappNumber: String = "+966 50 123 4567",
    val status: AccountStatus = AccountStatus.VERIFIED,
    val crNumber: String = "1010892341",
    val documentExpiry: String = "2027-12-31",
    val city: String = "Makkah Al Mukarramah",
    val district: String = "Ajyad",
    val addressDetail: String = "Building 42, Ibrahim Al Khalil St, Makkah",
    val bankName: String = "Al Rajhi Bank",
    val iban: String = "SA4480000123456789012345",
    val accountHolder: String = "Kareem Ghanem",
    val commissionRatePercent: Double = 12.5,
    val bookingCode: String = "SA-26-08-042",
    val bookingUrl: String = "https://jne.sa/book?agent=SA-26-08-042",
    val parentAgentName: String = "JNE Western Region Master Branch"
)

data class ShipmentItem(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val category: String,
    val quantity: Int,
    val unit: String = "Pcs",
    val unitValueUsd: Double,
    val isNew: Boolean = true,
    val isPersonal: Boolean = true,
    val photoUrl: String? = null,
    val hsCode: String? = "0804.10"
) {
    val totalValueUsd: Double get() = quantity * unitValueUsd
}

data class SenderProfile(
    val fullName: String,
    val nationality: String = "Indonesia",
    val nikKtp: String = "3273012345670001",
    val passportNumber: String = "X1234567",
    val hajjYear: String = "1447H / 2026",
    val kloterNumber: String = "JKS-14",
    val embarkation: String = "Jakarta (JKS)",
    val hajjPortionNumber: String = "1300987654",
    val travelDate: String = "2026-06-15",
    val whatsappNumber: String,
    val isWhatsappVerified: Boolean = true,
    val passportScanUrl: String? = "passport_scan_ahmad.png",
    val siskohatProofUrl: String? = "siskohat_proof.png"
)

data class ReceiverProfile(
    val fullName: String,
    val phone: String,
    val country: String = "Indonesia",
    val province: String,
    val city: String,
    val district: String,
    val subdistrict: String,
    val postalCode: String,
    val streetAddress: String,
    val landmark: String? = null
)

data class VerificationComparisonRow(
    val fieldName: String,
    val submittedValue: String,
    val actualValue: String,
    val isMatch: Boolean,
    val note: String? = null
)

data class DriverInfo(
    val driverId: String = "DRV-8821",
    val name: String = "Tariq Al-Mansoor",
    val phone: String = "+966 55 987 6543",
    val photoUrl: String? = null,
    val rating: Double = 4.9,
    val vehicleType: String = "Toyota HiAce Cargo Van",
    val plateNumber: String = "MKA 4821",
    val currentEta: String = "15 mins (0.8 km away)"
)

data class DriverVerification(
    val timestamp: String,
    val driverName: String,
    val isVerified: Boolean,
    val hasCorrection: Boolean = false,
    val correctionSummary: String? = null,
    val comparisons: List<VerificationComparisonRow>,
    val scalePhotoUrl: String? = "scale_photo_18.8kg.jpg",
    val packagePhotoUrl: String? = "package_photo_dates.jpg",
    val passportPhotoUrl: String? = "driver_passport_check.jpg",
    val driverNotes: String? = "Verified with customer at Makkah hotel lobby. Actual weight 18.8 kg (submitted 18.5 kg). All items match date packs."
)

data class InsurancePolicy(
    val isCovered: Boolean = true,
    val policyNumber: String = "POL-JNE-2026-99120",
    val provider: String = "Saudi Enaya Insurance & JNE Protection",
    val declaredValueUsd: Double,
    val insuredValueSar: Double,
    val premiumSar: Double,
    val coveragePeriod: String = "From Pickup to Receiver Delivery in Indonesia"
)

data class Invoice(
    val invoiceNumber: String,
    val issuedDate: String,
    val verifiedWeightKg: Double,
    val verifiedDimensionsCm: String,
    val shippingFeeSar: Double,
    val serviceFeeSar: Double,
    val insuranceFeeSar: Double,
    val importDutySar: Double = 0.0,
    val importTaxSar: Double = 0.0,
    val discountSar: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.VERIFIED_COMPLETED,
    val paymentMethod: String = "MADA POS (Collected by Driver)",
    val paymentReference: String = "TXN-MADA-992381"
) {
    val totalBillSar: Double
        get() = shippingFeeSar + serviceFeeSar + insuranceFeeSar + importDutySar + importTaxSar - discountSar
}

data class PickupRequest(
    val requestId: String,
    val customerType: CustomerType,
    val bookingSource: BookingSource = BookingSource.SUB_AGENT_APP,
    val sender: SenderProfile,
    val receiver: ReceiverProfile,
    val items: List<ShipmentItem>,
    val packageQuantity: Int = 1,
    val estimatedWeightKg: Double,
    val dimensionsCm: String = "55 x 48 x 60",
    val pickupAddress: String,
    val requestedDate: String,
    val requestedTimeSlot: String = "14:00 - 17:00",
    val status: RequestStatus,
    val submissionTimestamp: String,
    val regulationStatus: RegulationStatus = RegulationStatus.ALLOWED,
    val isHajjFacilityEligible: Boolean = true,
    val HajjShipmentCount: Int = 1,
    val estimatedTotalSar: Double,
    val estimatedCommissionSar: Double,
    val driver: DriverInfo? = null,
    val driverVerification: DriverVerification? = null,
    val invoice: Invoice? = null,
    val insurance: InsurancePolicy? = null,
    val autoClaimedCommissionSar: Double? = null,
    val awbNumber: String? = null,
    val reviewNotes: String? = null
)

data class Shipment(
    val shipmentId: String,
    val awbNumber: String,
    val isOwnedBySubAgent: Boolean = true,
    val customerType: CustomerType,
    val sender: SenderProfile,
    val receiver: ReceiverProfile,
    val items: List<ShipmentItem>,
    val status: ShipmentStatus,
    val pickupRequestId: String,
    val createdTimestamp: String,
    val lastUpdatedTimestamp: String,
    val verifiedWeightKg: Double,
    val dimensionsCm: String,
    val driver: DriverInfo?,
    val driverVerification: DriverVerification?,
    val invoice: Invoice?,
    val insurance: InsurancePolicy?,
    val commissionStatus: CommissionStatus,
    val commissionAmountSar: Double,
    val trackingEvents: List<TrackingEvent>,
    val isProhibited: Boolean = false,
    val isCustomsHold: Boolean = false,
    val isRefunded: Boolean = false,
    val rating: Int? = null,
    val feedbackComment: String? = null
)

data class TrackingEvent(
    val timestamp: String,
    val location: String,
    val statusTitle: String,
    val description: String,
    val isCompleted: Boolean = true
)

data class WalletLedgerEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: String,
    val title: String,
    val referenceId: String,
    val type: LedgerType,
    val amountSar: Double,
    val balanceAfterSar: Double,
    val description: String
)

enum class LedgerType {
    COMMISSION_AUTO_CLAIMED,
    WITHDRAWAL_REQUESTED,
    WITHDRAWAL_COMPLETED,
    COMMISSION_ADJUSTMENT,
    COMMISSION_REVERSAL,
    PENDING_DEDUCTION
}

data class WithdrawalRequest(
    val id: String = "WD-2026-004",
    val timestamp: String,
    val amountSar: Double,
    val bankName: String = "Al Rajhi Bank",
    val iban: String = "SA4480000123456789012345",
    val status: String = "Approved & Completed"
)

data class NotificationRecord(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: String,
    val title: String,
    val message: String,
    val channel: NotificationChannel,
    val recipient: String,
    val relatedId: String,
    val isRead: Boolean = false
)

enum class NotificationChannel {
    PUSH,
    WHATSAPP,
    ACTION_REQUIRED
}

data class ComplaintTicket(
    val ticketId: String = "TKT-2026-042",
    val awbNumber: String,
    val customerName: String,
    val subject: String,
    val status: String = "Resolved by Admin",
    val createdDate: String,
    val resolutionSummary: String? = "Package located at Jakarta warehouse hub and delivered safely to recipient on Aug 7."
)
