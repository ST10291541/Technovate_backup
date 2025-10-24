package vcmsa.projects.chocui

data class ContactInfo(
    val phone: String = "",
    val email: String = "",
    val addressDurban: String = "",
    val addressPmb: String = "",
    val facebookUrl: String = "",
    val bankName: String = "",
    val accountName: String = "",
    val accountNumber: String = "",
    val branchCode: String = "",
    val reference: String = ""
)

data class AboutUsInfo(
    val whatIsChoc: String = "",
    val whereWeKeepHope: String = "",
    val regionsDescription: String = "",
    val founderNote: String = ""
)

data class DonationInfo(
    val bankName: String = "",
    val accountName: String = "",
    val accountNumber: String = "",
    val branchCode: String = "",
    val reference: String = "",
    val walletDocUrl: String = ""
)

data class VolunteerInfo(
    val step1: String = "",
    val step2: String = "",
    val step3: String = "",
    val emailAddress: String = "",
    val pdfUrl: String = ""
)