package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class SubAgentRepository {

    // --- Account & Profile State ---
    private val _profile = MutableStateFlow(
        SubAgentProfile()
    )
    val profile: StateFlow<SubAgentProfile> = _profile.asStateFlow()

    // --- Seeded Pickup Requests ---
    private val _pickupRequests = MutableStateFlow<List<PickupRequest>>(emptyList())
    val pickupRequests: StateFlow<List<PickupRequest>> = _pickupRequests.asStateFlow()

    // --- Seeded Shipments ---
    private val _shipments = MutableStateFlow<List<Shipment>>(emptyList())
    val shipments: StateFlow<List<Shipment>> = _shipments.asStateFlow()

    // --- Wallet Ledger & Financials ---
    private val _availableBalanceSar = MutableStateFlow(1425.00)
    val availableBalanceSar: StateFlow<Double> = _availableBalanceSar.asStateFlow()

    private val _pendingCommissionSar = MutableStateFlow(85.00)
    val pendingCommissionSar: StateFlow<Double> = _pendingCommissionSar.asStateFlow()

    private val _creditedThisMonthSar = MutableStateFlow(650.00)
    val creditedThisMonthSar: StateFlow<Double> = _creditedThisMonthSar.asStateFlow()

    private val _ledgerEntries = MutableStateFlow<List<WalletLedgerEntry>>(emptyList())
    val ledgerEntries: StateFlow<List<WalletLedgerEntry>> = _ledgerEntries.asStateFlow()

    private val _withdrawals = MutableStateFlow<List<WithdrawalRequest>>(emptyList())
    val withdrawals: StateFlow<List<WithdrawalRequest>> = _withdrawals.asStateFlow()

    // --- Notifications ---
    private val _notifications = MutableStateFlow<List<NotificationRecord>>(emptyList())
    val notifications: StateFlow<List<NotificationRecord>> = _notifications.asStateFlow()

    // --- Customer Booking QR Stats ---
    private val _qrOpenedCount = MutableStateFlow(148)
    val qrOpenedCount: StateFlow<Int> = _qrOpenedCount.asStateFlow()

    private val _qrStartedCount = MutableStateFlow(62)
    val qrStartedCount: StateFlow<Int> = _qrStartedCount.asStateFlow()

    private val _qrSubmittedCount = MutableStateFlow(38)
    val qrSubmittedCount: StateFlow<Int> = _qrSubmittedCount.asStateFlow()

    private val _qrCompletedCount = MutableStateFlow(32)
    val qrCompletedCount: StateFlow<Int> = _qrCompletedCount.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        val seededDriver = DriverInfo(
            driverId = "DRV-8821",
            name = "Tariq Al-Mansoor",
            phone = "+966 55 987 6543",
            rating = 4.9,
            vehicleType = "Toyota HiAce Cargo Van",
            plateNumber = "MKA 4821",
            currentEta = "12 mins (0.6 km)"
        )

        val seededSender = SenderProfile(
            fullName = "Ahmad Fauzan",
            nationality = "Indonesia",
            nikKtp = "3273012345670001",
            passportNumber = "X1234567",
            hajjYear = "1447H / 2026",
            kloterNumber = "JKS-14",
            embarkation = "Jakarta (JKS)",
            hajjPortionNumber = "1300987654",
            travelDate = "2026-06-15",
            whatsappNumber = "+62 812 3456 7890",
            isWhatsappVerified = true
        )

        val seededReceiver = ReceiverProfile(
            fullName = "Siti Aleyah Rahmah",
            phone = "+62 813 9876 5432",
            country = "Indonesia",
            province = "DKI Jakarta",
            city = "Jakarta Selatan",
            district = "Kebayoran Baru",
            subdistrict = "Gunung",
            postalCode = "12120",
            streetAddress = "Jl. Pakubuwono VI No. 42A",
            landmark = "Near Al-Azhar Mosque"
        )

        val seededItems = listOf(
            ShipmentItem(
                description = "Ajwa Dates Makkah Premium Box (5 Packs x 1kg)",
                category = "Food / Dates",
                quantity = 5,
                unit = "Packs",
                unitValueUsd = 97.00, // Total 485 USD FOB
                isNew = true,
                isPersonal = true
            )
        )

        val seededVerification = DriverVerification(
            timestamp = "2026-08-08 14:35:10",
            driverName = "Tariq Al-Mansoor",
            isVerified = true,
            hasCorrection = true,
            correctionSummary = "Actual gross weight is 18.8 kg (Submitted 18.5 kg). Dimensions match.",
            comparisons = listOf(
                VerificationComparisonRow("Passport Match", "X1234567", "X1234567", true, "Verified with physical passport"),
                VerificationComparisonRow("Hajj Registration", "SISKOHAT Verified", "SISKOHAT Verified", true, "Kloter JKS-14 confirmed"),
                VerificationComparisonRow("Kloter Number", "JKS-14", "JKS-14", true, "Matches pilgrim wristband"),
                VerificationComparisonRow("Gross Weight", "18.5 kg", "18.8 kg", false, "Actual digital scale measurement: 18.8 kg"),
                VerificationComparisonRow("Package Dimensions", "55 x 48 x 60 cm", "55 x 48 x 60 cm", true, "Within max 60x60x80 cm limit"),
                VerificationComparisonRow("FOB Value", "$485.00 USD", "$485.00 USD", true, "Exempt under $1,500 Hajj allowance"),
                VerificationComparisonRow("Prohibited Goods Check", "Clear", "Clear", true, "No aerosol, liquid, or battery found")
            ),
            driverNotes = "Collected in Makkah Ajyad Hotel lobby. Sender Ahmad Fauzan signed verification terminal. MADA payment collected."
        )

        val seededInvoice = Invoice(
            invoiceNumber = "INV-2026-88019",
            issuedDate = "2026-08-08 14:40:00",
            verifiedWeightKg = 18.8,
            verifiedDimensionsCm = "55 x 48 x 60 cm",
            shippingFeeSar = 188.00, // 10 SAR/kg
            serviceFeeSar = 12.00,
            insuranceFeeSar = 5.00,
            importDutySar = 0.00, // Exempt
            importTaxSar = 0.00, // Exempt
            discountSar = 0.00,
            paymentStatus = PaymentStatus.VERIFIED_COMPLETED
        )

        val seededInsurance = InsurancePolicy(
            isCovered = true,
            policyNumber = "POL-JNE-2026-99120",
            provider = "Saudi Enaya Insurance & JNE Protection",
            declaredValueUsd = 485.00,
            insuredValueSar = 1818.75,
            premiumSar = 5.00,
            coveragePeriod = "Pickup to Final Delivery in Jakarta"
        )

        // Seeded Primary Pickup Request & Shipment
        val primaryRequest = PickupRequest(
            requestId = "PR-2026-0842",
            customerType = CustomerType.HAJJ_PILGRIM,
            sender = seededSender,
            receiver = seededReceiver,
            items = seededItems,
            packageQuantity = 1,
            estimatedWeightKg = 18.5,
            dimensionsCm = "55 x 48 x 60 cm",
            pickupAddress = "Hotel Ajyad Royale, Room 402, Makkah",
            requestedDate = "2026-08-08",
            requestedTimeSlot = "14:00 - 17:00",
            status = RequestStatus.PICKUP_COMPLETED,
            submissionTimestamp = "2026-08-08 10:15:00",
            regulationStatus = RegulationStatus.ALLOWED,
            isHajjFacilityEligible = true,
            HajjShipmentCount = 1,
            estimatedTotalSar = 205.00,
            estimatedCommissionSar = 25.00,
            driver = seededDriver,
            driverVerification = seededVerification,
            invoice = seededInvoice,
            insurance = seededInsurance,
            autoClaimedCommissionSar = 25.00,
            awbNumber = "SA123456785SA"
        )

        val primaryShipment = Shipment(
            shipmentId = "SHP-2026-001",
            awbNumber = "SA123456785SA",
            isOwnedBySubAgent = true,
            customerType = CustomerType.HAJJ_PILGRIM,
            sender = seededSender,
            receiver = seededReceiver,
            items = seededItems,
            status = ShipmentStatus.DELIVERED,
            pickupRequestId = "PR-2026-0842",
            createdTimestamp = "2026-08-08 10:15:00",
            lastUpdatedTimestamp = "2026-08-08 18:10:00",
            verifiedWeightKg = 18.8,
            dimensionsCm = "55 x 48 x 60 cm",
            driver = seededDriver,
            driverVerification = seededVerification,
            invoice = seededInvoice,
            insurance = seededInsurance,
            commissionStatus = CommissionStatus.AUTO_CLAIMED,
            commissionAmountSar = 25.00,
            rating = 5,
            feedbackComment = "Very fast pickup at hotel lobby and dates arrived safely in Jakarta!",
            trackingEvents = listOf(
                TrackingEvent("2026-08-08 18:00", "Jakarta Selatan", "Delivered", "Shipment delivered to receiver Siti Aleyah Rahmah. Signed by receiver.", true),
                TrackingEvent("2026-08-08 15:20", "Jakarta Gateway Hub", "Out for Delivery", "Courier assigned for final mile delivery in Kebayoran Baru.", true),
                TrackingEvent("2026-08-08 11:30", "Soekarno-Hatta Customs", "Customs Processing", "Customs inspection cleared under Hajj facility exemption.", true),
                TrackingEvent("2026-08-08 06:15", "CGK Airport", "Arrived Indonesia", "Flight JNE-801 arrived at CGK airport.", true),
                TrackingEvent("2026-08-07 22:40", "JED Airport Hub", "Exported & Dispatched", "Manifested for air freight departure to Jakarta.", true),
                TrackingEvent("2026-08-07 19:10", "JNE Makkah Central Warehouse", "Warehouse Received & Packed", "Received from driver. Verified & sealed with security barcode.", true),
                TrackingEvent("2026-08-07 14:35", "Makkah Ajyad Hotel", "Pickup Completed", "Collected by driver Tariq Al-Mansoor. Payment verified.", true)
            )
        )

        // Additional Seeded Shipments for full coverage of scenarios
        val prohibitedShipment = Shipment(
            shipmentId = "SHP-2026-002",
            awbNumber = "SA990011223SA",
            isOwnedBySubAgent = true,
            customerType = CustomerType.GENERAL_CUSTOMER,
            sender = SenderProfile("Budi Santoso", whatsappNumber = "+62 811 2233 4455"),
            receiver = ReceiverProfile("Rina Santoso", phone = "+62 812 9988 7766", province = "Jawa Barat", city = "Bandung", district = "Coblong", subdistrict = "Dago", postalCode = "40135", streetAddress = "Jl. Ir. H. Juanda No. 102"),
            items = listOf(ShipmentItem(description = "Perfume Spray Bottle (Liquid/Aerosol)", category = "Cosmetics / Aerosol", quantity = 3, unitValueUsd = 40.0)),
            status = ShipmentStatus.REJECTED,
            pickupRequestId = "PR-2026-0910",
            createdTimestamp = "2026-08-07 11:00:00",
            lastUpdatedTimestamp = "2026-08-07 11:05:00",
            verifiedWeightKg = 2.0,
            dimensionsCm = "20 x 20 x 20 cm",
            driver = null,
            driverVerification = null,
            invoice = null,
            insurance = null,
            commissionStatus = CommissionStatus.NOT_ELIGIBLE,
            commissionAmountSar = 0.0,
            isProhibited = true,
            trackingEvents = listOf(
                TrackingEvent("2026-08-07 11:05", "Sub-Agent System", "Prohibited Goods - Rejected", "Automated regulation check blocked shipment containing aerosol spray.", true)
            )
        )

        val customsHoldShipment = Shipment(
            shipmentId = "SHP-2026-003",
            awbNumber = "SA443322110SA",
            isOwnedBySubAgent = true,
            customerType = CustomerType.GENERAL_CUSTOMER,
            sender = SenderProfile("Hasan Basri", whatsappNumber = "+966 50 998 8776"),
            receiver = ReceiverProfile("Dewi Sartika", phone = "+62 813 1122 3344", province = "Jawa Tengah", city = "Semarang", district = "Semarang Tengah", subdistrict = "Pandanaran", postalCode = "50241", streetAddress = "Jl. Pandanaran No. 88"),
            items = listOf(ShipmentItem(description = "Commercial Electronics Parts", category = "Electronics", quantity = 10, unitValueUsd = 120.0)),
            status = ShipmentStatus.HOLD,
            pickupRequestId = "PR-2026-0911",
            createdTimestamp = "2026-08-06 09:30:00",
            lastUpdatedTimestamp = "2026-08-08 12:00:00",
            verifiedWeightKg = 12.0,
            dimensionsCm = "40 x 40 x 40 cm",
            driver = seededDriver,
            driverVerification = null,
            invoice = null,
            insurance = null,
            commissionStatus = CommissionStatus.PENDING_ADMIN,
            commissionAmountSar = 18.00,
            isCustomsHold = true,
            trackingEvents = listOf(
                TrackingEvent("2026-08-08 12:00", "JNE Jeddah Air Gateway", "Hold - Document Clarification Required", "Admin requested commercial invoice & HS code clarification.", true),
                TrackingEvent("2026-08-06 14:00", "JNE Jeddah Air Gateway", "Warehouse Verification In Progress", "Inspected by customs agent.", true)
            )
        )

        val refundedShipment = Shipment(
            shipmentId = "SHP-2026-004",
            awbNumber = "SA776655443SA",
            isOwnedBySubAgent = true,
            customerType = CustomerType.GENERAL_CUSTOMER,
            sender = SenderProfile("Faisal Amir", whatsappNumber = "+966 54 112 2334"),
            receiver = ReceiverProfile("Lia Indriani", phone = "+62 815 6677 8899", province = "Jawa Timur", city = "Surabaya", district = "Tegalsari", subdistrict = "Kedungdoro", postalCode = "60261", streetAddress = "Jl. Basuki Rahmat No. 12"),
            items = listOf(ShipmentItem(description = "Souvenir Garments", category = "Apparel", quantity = 4, unitValueUsd = 25.0)),
            status = ShipmentStatus.REFUNDED,
            pickupRequestId = "PR-2026-0912",
            createdTimestamp = "2026-08-05 16:20:00",
            lastUpdatedTimestamp = "2026-08-07 10:00:00",
            verifiedWeightKg = 5.0,
            dimensionsCm = "30 x 30 x 30 cm",
            driver = seededDriver,
            driverVerification = null,
            invoice = null,
            insurance = null,
            commissionStatus = CommissionStatus.REVERSED,
            commissionAmountSar = 0.0,
            isRefunded = true,
            trackingEvents = listOf(
                TrackingEvent("2026-08-07 10:00", "Finance System", "Shipment Cancelled & Refunded", "Customer requested cancellation before warehouse transit. Commission SAR 15.00 reversed.", true)
            )
        )

        // Seeded External AWB for public tracking test
        val externalShipment = Shipment(
            shipmentId = "SHP-EXT-999",
            awbNumber = "JNE88990011SA",
            isOwnedBySubAgent = false, // External JNE shipment
            customerType = CustomerType.GENERAL_CUSTOMER,
            sender = SenderProfile("Private Sender", whatsappNumber = "HIDDEN"),
            receiver = ReceiverProfile("Private Receiver", phone = "HIDDEN", province = "Bali", city = "Denpasar", district = "Denpasar Barat", subdistrict = "Pemecutan", postalCode = "80119", streetAddress = "HIDDEN ADDRESS"),
            items = listOf(ShipmentItem(description = "General Logistics Parcel", category = "General", quantity = 1, unitValueUsd = 50.0)),
            status = ShipmentStatus.IN_TRANSIT,
            pickupRequestId = "PR-EXT-001",
            createdTimestamp = "2026-08-07 08:00:00",
            lastUpdatedTimestamp = "2026-08-08 14:00:00",
            verifiedWeightKg = 8.5,
            dimensionsCm = "30 x 30 x 30 cm",
            driver = null,
            driverVerification = null,
            invoice = null,
            insurance = null,
            commissionStatus = CommissionStatus.NOT_ELIGIBLE,
            commissionAmountSar = 0.0,
            trackingEvents = listOf(
                TrackingEvent("2026-08-08 14:00", "Riyadh Hub", "In Transit", "Departed sorting facility to Jakarta flight.", true),
                TrackingEvent("2026-08-07 18:30", "Riyadh Branch", "Shipment Received", "Accepted at branch counter.", true)
            )
        )

        _pickupRequests.value = listOf(
            PickupRequest(
                requestId = "PR-QR-2026-101",
                customerType = CustomerType.HAJJ_PILGRIM,
                bookingSource = BookingSource.CUSTOMER_QR,
                sender = SenderProfile(
                    fullName = "Hj. Ratna Sulastri",
                    nationality = "Indonesia",
                    nikKtp = "3175026203840003",
                    passportNumber = "X9812341",
                    hajjYear = "1447H / 2026",
                    kloterNumber = "JKS-22",
                    embarkation = "Jakarta (JKS)",
                    hajjPortionNumber = "1300882145",
                    travelDate = "2026-06-18",
                    whatsappNumber = "+62 812 8877 6655",
                    isWhatsappVerified = true
                ),
                receiver = ReceiverProfile(
                    fullName = "H. Bambang Soeprapto",
                    phone = "+62 813 5544 3322",
                    province = "DKI Jakarta",
                    city = "Jakarta Timur",
                    district = "Duren Sawit",
                    subdistrict = "Pondok Kelapa",
                    postalCode = "13450",
                    streetAddress = "Jl. Radar AURI No. 88"
                ),
                items = listOf(
                    ShipmentItem(
                        description = "Ajwa Dates & Sajjada Carpets (3 Boxes)",
                        category = "Food & Prayer Items",
                        quantity = 3,
                        unitValueUsd = 65.0
                    )
                ),
                packageQuantity = 2,
                estimatedWeightKg = 14.2,
                pickupAddress = "Hotel Dar Al Eiman Royal, Room 608, Makkah",
                requestedDate = "2026-08-09",
                requestedTimeSlot = "15:00 - 18:00",
                status = RequestStatus.WAITING_ADMIN_REVIEW,
                submissionTimestamp = "2026-08-08 18:30:00",
                regulationStatus = RegulationStatus.ALLOWED,
                estimatedTotalSar = 162.00,
                estimatedCommissionSar = 19.44
            ),
            PickupRequest(
                requestId = "PR-LINK-2026-102",
                customerType = CustomerType.GENERAL_CUSTOMER,
                bookingSource = BookingSource.SHARED_LINK,
                sender = SenderProfile(
                    fullName = "Drs. Mohammad Rasyid",
                    nationality = "Indonesia",
                    passportNumber = "X4455667",
                    whatsappNumber = "+62 815 1122 3344",
                    isWhatsappVerified = true
                ),
                receiver = ReceiverProfile(
                    fullName = "Aisyah Rasyid",
                    phone = "+62 818 9900 1122",
                    province = "Jawa Barat",
                    city = "Bandung",
                    district = "Coblong",
                    subdistrict = "Dago",
                    postalCode = "40135",
                    streetAddress = "Jl. H. Juanda No. 15"
                ),
                items = listOf(
                    ShipmentItem(
                        description = "Middle Eastern Spices & Non-Alcohol Perfume Oils",
                        category = "Food & Fragrance",
                        quantity = 4,
                        unitValueUsd = 80.0
                    )
                ),
                packageQuantity = 1,
                estimatedWeightKg = 8.5,
                pickupAddress = "Swissotel Al Maqam Tower, Room 1204, Makkah",
                requestedDate = "2026-08-09",
                requestedTimeSlot = "10:00 - 13:00",
                status = RequestStatus.WAITING_ADMIN_REVIEW,
                submissionTimestamp = "2026-08-08 19:10:00",
                regulationStatus = RegulationStatus.RESTRICTED,
                estimatedTotalSar = 110.00,
                estimatedCommissionSar = 13.20
            ),
            primaryRequest,
            PickupRequest(
                requestId = "PR-2026-0843",
                customerType = CustomerType.HAJJ_PILGRIM,
                sender = SenderProfile("Nurul Hidayah", whatsappNumber = "+62 821 9988 7711"),
                receiver = ReceiverProfile("Mustofa Bisri", phone = "+62 812 3344 5566", province = "DI Yogyakarta", city = "Sleman", district = "Depok", subdistrict = "Caturtunggal", postalCode = "55281", streetAddress = "Jl. Kaliurang Km 5.5"),
                items = listOf(ShipmentItem(description = "Zamzam Holy Water Accessories & Rugs", category = "Prayer Items", quantity = 2, unitValueUsd = 75.0)),
                packageQuantity = 1,
                estimatedWeightKg = 12.0,
                pickupAddress = "Hotel PullMan Zamzam, Makkah",
                requestedDate = "2026-08-09",
                requestedTimeSlot = "09:00 - 12:00",
                status = RequestStatus.WAITING_ADMIN_REVIEW,
                submissionTimestamp = "2026-08-08 17:00:00",
                estimatedTotalSar = 135.00,
                estimatedCommissionSar = 16.50
            ),
            PickupRequest(
                requestId = "PR-2026-0844",
                customerType = CustomerType.GENERAL_CUSTOMER,
                sender = SenderProfile("Dr. Hendra Wijaya", whatsappNumber = "+966 50 776 5432"),
                receiver = ReceiverProfile("Siska Wijaya", phone = "+62 811 4455 6677", province = "Banten", city = "Tangerang Selatan", district = "Serpong", subdistrict = "BSD City", postalCode = "15310", streetAddress = "Cluster Foresta No. B3"),
                items = listOf(ShipmentItem(description = "Medical Books & Conference Papers", category = "Printed Books", quantity = 1, unitValueUsd = 150.0)),
                packageQuantity = 1,
                estimatedWeightKg = 6.5,
                pickupAddress = "King Abdulaziz Medical City, Jeddah",
                requestedDate = "2026-08-09",
                requestedTimeSlot = "14:00 - 17:00",
                status = RequestStatus.DRIVER_ASSIGNED,
                submissionTimestamp = "2026-08-08 11:20:00",
                estimatedTotalSar = 85.00,
                estimatedCommissionSar = 10.20,
                driver = seededDriver
            )
        )

        _shipments.value = listOf(
            primaryShipment,
            prohibitedShipment,
            customsHoldShipment,
            refundedShipment,
            externalShipment
        )

        // Wallet Initial Ledger
        _ledgerEntries.value = listOf(
            WalletLedgerEntry(
                timestamp = "2026-08-08 14:42:00",
                title = "Commission Auto-Claimed",
                referenceId = "INV-2026-88019 / SHP-2026-001",
                type = LedgerType.COMMISSION_AUTO_CLAIMED,
                amountSar = 25.00,
                balanceAfterSar = 1425.00,
                description = "Auto-claimed direct commission for verified shipment SA123456785SA (Ahmad Fauzan)"
            ),
            WalletLedgerEntry(
                timestamp = "2026-08-07 10:05:00",
                title = "Commission Reversal",
                referenceId = "SHP-2026-004",
                type = LedgerType.COMMISSION_REVERSAL,
                amountSar = -15.00,
                balanceAfterSar = 1400.00,
                description = "Commission reversed for cancelled shipment SA776655443SA"
            ),
            WalletLedgerEntry(
                timestamp = "2026-08-04 16:00:00",
                title = "Withdrawal Completed",
                referenceId = "WD-2026-003",
                type = LedgerType.WITHDRAWAL_COMPLETED,
                amountSar = -500.00,
                balanceAfterSar = 1415.00,
                description = "Bank payout to Al Rajhi Bank SA4480000123456789012345"
            )
        )

        _withdrawals.value = listOf(
            WithdrawalRequest("WD-2026-003", "2026-08-04 16:00", 500.00, "Al Rajhi Bank", "SA4480000123456789012345", "Approved & Completed"),
            WithdrawalRequest("WD-2026-002", "2026-07-28 11:30", 750.00, "Al Rajhi Bank", "SA4480000123456789012345", "Approved & Completed")
        )

        _notifications.value = listOf(
            NotificationRecord(
                title = "Commission Auto-Claimed +SAR 25.00",
                message = "Admin verification complete for SA123456785SA. SAR 25.00 credited to wallet.",
                channel = NotificationChannel.PUSH,
                recipient = "Sub-Agent: Kareem Ghanem",
                relatedId = "SHP-2026-001",
                timestamp = "2026-08-08 14:42"
            ),
            NotificationRecord(
                title = "WhatsApp Sent to Ahmad Fauzan",
                message = "Driver Tariq Al-Mansoor assigned for pickup at Hotel Ajyad Royale.",
                channel = NotificationChannel.WHATSAPP,
                recipient = "+62 812 3456 7890",
                relatedId = "PR-2026-0842",
                timestamp = "2026-08-08 14:00"
            ),
            NotificationRecord(
                title = "Document Clarification Required",
                message = "Shipment SA443322110SA requires commercial HS code review.",
                channel = NotificationChannel.ACTION_REQUIRED,
                recipient = "Sub-Agent: Kareem Ghanem",
                relatedId = "SHP-2026-003",
                timestamp = "2026-08-08 12:00"
            )
        )
    }

    // --- Actions & Mutators ---

    fun submitNewPickupRequest(request: PickupRequest) {
        val current = _pickupRequests.value.toMutableList()
        current.add(0, request)
        _pickupRequests.value = current

        // Also increase booking counters
        _qrSubmittedCount.value += 1

        // Add Push & WhatsApp Notification
        val newNotifs = _notifications.value.toMutableList()
        newNotifs.add(0, NotificationRecord(
            title = "New Pickup Request ${request.requestId}",
            message = "Pickup request submitted for ${request.sender.fullName}. Status: Waiting Review.",
            channel = NotificationChannel.PUSH,
            recipient = "Sub-Agent: Kareem Ghanem",
            relatedId = request.requestId,
            timestamp = getCurrentTimestamp()
        ))
        newNotifs.add(0, NotificationRecord(
            title = "WhatsApp Confirmation Sent",
            message = "Booking confirmation sent to ${request.sender.whatsappNumber}.",
            channel = NotificationChannel.WHATSAPP,
            recipient = request.sender.whatsappNumber,
            relatedId = request.requestId,
            timestamp = getCurrentTimestamp()
        ))
        _notifications.value = newNotifs
    }

    fun cancelPickupRequest(requestId: String, reason: String) {
        val current = _pickupRequests.value.map { req ->
            if (req.requestId == requestId) {
                req.copy(status = RequestStatus.REJECTED, reviewNotes = reason)
            } else req
        }
        _pickupRequests.value = current

        val newNotifs = _notifications.value.toMutableList()
        newNotifs.add(0, NotificationRecord(
            title = "Pickup Request Cancelled",
            message = "Request $requestId was cancelled: $reason",
            channel = NotificationChannel.PUSH,
            recipient = "Sub-Agent: Kareem Ghanem",
            relatedId = requestId,
            timestamp = getCurrentTimestamp()
        ))
        _notifications.value = newNotifs
    }

    fun approvePickupRequest(requestId: String, reviewNotes: String? = null) {
        val defaultDriver = DriverInfo(
            driverId = "DRV-8821",
            name = "Tariq Al-Mansoor",
            phone = "+966 55 987 6543",
            rating = 4.9,
            vehicleType = "Toyota HiAce Cargo Van",
            plateNumber = "MKA 4821",
            currentEta = "15 mins (0.8 km)"
        )

        val targetReq = _pickupRequests.value.firstOrNull { it.requestId == requestId }
        val generatedAwb = targetReq?.awbNumber ?: "JNE-MKA-2026-${(1000..9999).random()}"
        
        val current = _pickupRequests.value.map { req ->
            if (req.requestId == requestId) {
                req.copy(
                    status = RequestStatus.DRIVER_ASSIGNED,
                    driver = defaultDriver,
                    awbNumber = generatedAwb,
                    reviewNotes = reviewNotes ?: "Approved by Sub-Agent. Driver dispatched."
                )
            } else req
        }
        _pickupRequests.value = current
        _qrCompletedCount.value += 1

        val req = _pickupRequests.value.firstOrNull { it.requestId == requestId }
        val senderWa = req?.sender?.whatsappNumber ?: "+966 50 123 4567"
        val senderName = req?.sender?.fullName ?: "Customer"

        // Create & Add Shipment to _shipments if not existing
        if (req != null && _shipments.value.none { it.pickupRequestId == requestId }) {
            val newShipment = Shipment(
                shipmentId = "SHP-${req.requestId}",
                awbNumber = generatedAwb,
                isOwnedBySubAgent = true,
                customerType = req.customerType,
                sender = req.sender,
                receiver = req.receiver,
                items = req.items,
                status = ShipmentStatus.DRIVER_ASSIGNED,
                pickupRequestId = req.requestId,
                createdTimestamp = req.submissionTimestamp,
                lastUpdatedTimestamp = getCurrentTimestamp(),
                verifiedWeightKg = req.estimatedWeightKg,
                dimensionsCm = "40 x 30 x 25 cm",
                driver = defaultDriver,
                driverVerification = null,
                invoice = Invoice(
                    invoiceNumber = "INV-2026-${req.requestId.takeLast(4)}",
                    issuedDate = getCurrentTimestamp(),
                    verifiedWeightKg = req.estimatedWeightKg,
                    verifiedDimensionsCm = "40 x 30 x 25 cm",
                    shippingFeeSar = req.estimatedTotalSar * 0.85,
                    serviceFeeSar = req.estimatedTotalSar * 0.10,
                    insuranceFeeSar = req.estimatedTotalSar * 0.05,
                    paymentStatus = PaymentStatus.VERIFIED_COMPLETED,
                    paymentMethod = "Sub-Agent QR Approval",
                    paymentReference = "TXN-QR-${req.requestId.takeLast(4)}"
                ),
                insurance = InsurancePolicy(
                    declaredValueUsd = 500.0,
                    insuredValueSar = 1875.0,
                    premiumSar = req.estimatedTotalSar * 0.05,
                    policyNumber = "INS-JNE-2026-${req.requestId.takeLast(4)}",
                    provider = "Syariah Cargo Insurance"
                ),
                commissionStatus = CommissionStatus.PENDING_ADMIN,
                commissionAmountSar = req.estimatedCommissionSar,
                trackingEvents = listOf(
                    TrackingEvent(
                        timestamp = getCurrentTimestamp(),
                        location = "Al Buraq Sub-Agent Office, Makkah",
                        statusTitle = "Request Approved & AWB Issued",
                        description = "Approved by Sub-Agent. AWB $generatedAwb issued, driver assigned.",
                        isCompleted = true
                    ),
                    TrackingEvent(
                        timestamp = getCurrentTimestamp(),
                        location = "Makkah Hub",
                        statusTitle = "Driver Dispatched",
                        description = "Driver Tariq Al-Mansoor (+966 55 987 6543) dispatched to ${req.pickupAddress}.",
                        isCompleted = true
                    )
                )
            )
            _shipments.value = listOf(newShipment) + _shipments.value
        }

        val newNotifs = _notifications.value.toMutableList()
        newNotifs.add(0, NotificationRecord(
            title = "Request $requestId Approved (AWB: $generatedAwb)",
            message = "Pickup request for $senderName approved and added to My Shipments. AWB $generatedAwb assigned.",
            channel = NotificationChannel.PUSH,
            recipient = "Sub-Agent: Kareem Ghanem",
            relatedId = requestId,
            timestamp = getCurrentTimestamp()
        ))
        newNotifs.add(0, NotificationRecord(
            title = "WhatsApp Notification Sent to $senderName",
            message = "Hello $senderName, your pickup request $requestId is APPROVED! AWB $generatedAwb generated. Driver Tariq Al-Mansoor (+966 55 987 6543) is on the way.",
            channel = NotificationChannel.WHATSAPP,
            recipient = senderWa,
            relatedId = requestId,
            timestamp = getCurrentTimestamp()
        ))
        _notifications.value = newNotifs
    }

    fun requestAdditionalDocs(requestId: String, note: String) {
        val current = _pickupRequests.value.map { req ->
            if (req.requestId == requestId) {
                req.copy(
                    status = RequestStatus.ADDITIONAL_DOCS_REQUIRED,
                    reviewNotes = note
                )
            } else req
        }
        _pickupRequests.value = current

        val req = _pickupRequests.value.firstOrNull { it.requestId == requestId }
        val senderWa = req?.sender?.whatsappNumber ?: "+966 50 123 4567"

        val newNotifs = _notifications.value.toMutableList()
        newNotifs.add(0, NotificationRecord(
            title = "Docs Requested for $requestId",
            message = "Customer requested to upload missing documents: $note",
            channel = NotificationChannel.WHATSAPP,
            recipient = senderWa,
            relatedId = requestId,
            timestamp = getCurrentTimestamp()
        ))
        _notifications.value = newNotifs
    }

    fun requestWithdrawal(amountSar: Double, iban: String, bankName: String): Boolean {
        if (amountSar > _availableBalanceSar.value || amountSar <= 0) return false

        val newBalance = _availableBalanceSar.value - amountSar
        _availableBalanceSar.value = newBalance

        val newWithdrawal = WithdrawalRequest(
            id = "WD-2026-${System.currentTimeMillis().toString().takeLast(3)}",
            timestamp = getCurrentTimestamp(),
            amountSar = amountSar,
            bankName = bankName,
            iban = iban,
            status = "Under Admin Review"
        )
        _withdrawals.value = listOf(newWithdrawal) + _withdrawals.value

        val newLedger = WalletLedgerEntry(
            timestamp = getCurrentTimestamp(),
            title = "Withdrawal Requested",
            referenceId = newWithdrawal.id,
            type = LedgerType.WITHDRAWAL_REQUESTED,
            amountSar = -amountSar,
            balanceAfterSar = newBalance,
            description = "Requested transfer to $bankName ($iban)"
        )
        _ledgerEntries.value = listOf(newLedger) + _ledgerEntries.value

        return true
    }

    fun simulateCommissionReversal(shipmentId: String, amountSar: Double) {
        var currentBal = _availableBalanceSar.value
        val newBal = (currentBal - amountSar).coerceAtLeast(0.0)
        _availableBalanceSar.value = newBal

        val ledgerType = if (currentBal >= amountSar) LedgerType.COMMISSION_REVERSAL else LedgerType.PENDING_DEDUCTION

        val ledgerEntry = WalletLedgerEntry(
            timestamp = getCurrentTimestamp(),
            title = if (ledgerType == LedgerType.COMMISSION_REVERSAL) "Commission Reversal" else "Pending Deduction",
            referenceId = shipmentId,
            type = ledgerType,
            amountSar = -amountSar,
            balanceAfterSar = newBal,
            description = "Commission reversed for refunded shipment $shipmentId."
        )
        _ledgerEntries.value = listOf(ledgerEntry) + _ledgerEntries.value
    }

    fun updateProfile(businessName: String, whatsappNumber: String, city: String, addressDetail: String, iban: String) {
        _profile.value = _profile.value.copy(
            businessName = businessName,
            whatsappNumber = whatsappNumber,
            city = city,
            addressDetail = addressDetail,
            iban = iban
        )
    }

    fun updateBankAccount(bankName: String, accountHolder: String, iban: String) {
        _profile.value = _profile.value.copy(
            bankName = bankName,
            accountHolder = accountHolder,
            iban = iban
        )
    }

    fun setAccountStatus(status: AccountStatus) {
        _profile.value = _profile.value.copy(status = status)
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
