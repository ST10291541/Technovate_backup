package vcmsa.projects.chocui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AdminEditActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    // About Us fields
    private lateinit var etWhatIsChoc: EditText
    private lateinit var etWhereWeKeepHope: EditText
    private lateinit var etRegionsDescription: EditText
    private lateinit var etFounderNote: EditText

    // Contact fields
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAddressDurban: EditText
    private lateinit var etAddressPmb: EditText
    private lateinit var etFacebookUrl: EditText

    // Donation fields
    private lateinit var etBankName: EditText
    private lateinit var etAccountName: EditText
    private lateinit var etAccountNumber: EditText
    private lateinit var etBranchCode: EditText
    private lateinit var etReference: EditText
    private lateinit var etWalletDocUrl: EditText

    // Volunteer fields
    private lateinit var etStep1: EditText
    private lateinit var etStep2: EditText
    private lateinit var etStep3: EditText
    private lateinit var etVolunteerEmail: EditText
    private lateinit var etPdfUrl: EditText

    private lateinit var btnSaveAll: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit)

        db = FirebaseFirestore.getInstance()
        initializeViews()
        loadAllData()

        btnSaveAll.setOnClickListener {
            saveAllData()
        }
    }

    private fun initializeViews() {
        // About Us
        etWhatIsChoc = findViewById(R.id.etWhatIsChoc)
        etWhereWeKeepHope = findViewById(R.id.etWhereWeKeepHope)
        etRegionsDescription = findViewById(R.id.etRegionsDescription)
        etFounderNote = findViewById(R.id.etFounderNote)

        // Contacts
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etAddressDurban = findViewById(R.id.etAddressDurban)
        etAddressPmb = findViewById(R.id.etAddressPmb)
        etFacebookUrl = findViewById(R.id.etFacebookUrl)

        // Donations
        etBankName = findViewById(R.id.etBankName)
        etAccountName = findViewById(R.id.etAccountName)
        etAccountNumber = findViewById(R.id.etAccountNumber)
        etBranchCode = findViewById(R.id.etBranchCode)
        etReference = findViewById(R.id.etReference)
        etWalletDocUrl = findViewById(R.id.etWalletDocUrl)

        // Volunteer
        etStep1 = findViewById(R.id.etStep1)
        etStep2 = findViewById(R.id.etStep2)
        etStep3 = findViewById(R.id.etStep3)
        etVolunteerEmail = findViewById(R.id.etVolunteerEmail)
        etPdfUrl = findViewById(R.id.etPdfUrl)

        btnSaveAll = findViewById(R.id.btnSaveAll)
    }

    private fun loadAllData() {
        lifecycleScope.launch {
            // Load About Us
            val aboutUsInfo = FirebaseRepository().getAboutUsInfo()
            aboutUsInfo?.let {
                etWhatIsChoc.setText(it.whatIsChoc)
                etWhereWeKeepHope.setText(it.whereWeKeepHope)
                etRegionsDescription.setText(it.regionsDescription)
                etFounderNote.setText(it.founderNote)
            }

            // Load Contacts
            val contactInfo = FirebaseRepository().getContactInfo()
            contactInfo?.let {
                etPhone.setText(it.phone)
                etEmail.setText(it.email)
                etAddressDurban.setText(it.addressDurban)
                etAddressPmb.setText(it.addressPmb)
                etFacebookUrl.setText(it.facebookUrl)
                etBankName.setText(it.bankName)
                etAccountName.setText(it.accountName)
                etAccountNumber.setText(it.accountNumber)
                etBranchCode.setText(it.branchCode)
                etReference.setText(it.reference)
            }

            // Load Donations
            val donationInfo = FirebaseRepository().getDonationInfo()
            donationInfo?.let {
                // Note: Some fields might be duplicated from contacts
                etWalletDocUrl.setText(it.walletDocUrl)
            }

            // Load Volunteer
            val volunteerInfo = FirebaseRepository().getVolunteerInfo()
            volunteerInfo?.let {
                etStep1.setText(it.step1)
                etStep2.setText(it.step2)
                etStep3.setText(it.step3)
                etVolunteerEmail.setText(it.emailAddress)
                etPdfUrl.setText(it.pdfUrl)
            }
        }
    }

    private fun saveAllData() {
        // About Us
        val aboutUsData = hashMapOf(
            "whatIsChoc" to etWhatIsChoc.text.toString(),
            "whereWeKeepHope" to etWhereWeKeepHope.text.toString(),
            "regionsDescription" to etRegionsDescription.text.toString(),
            "founderNote" to etFounderNote.text.toString()
        )

        // Contacts
        val contactData = hashMapOf(
            "phone" to etPhone.text.toString(),
            "email" to etEmail.text.toString(),
            "addressDurban" to etAddressDurban.text.toString(),
            "addressPmb" to etAddressPmb.text.toString(),
            "facebookUrl" to etFacebookUrl.text.toString(),
            "bankName" to etBankName.text.toString(),
            "accountName" to etAccountName.text.toString(),
            "accountNumber" to etAccountNumber.text.toString(),
            "branchCode" to etBranchCode.text.toString(),
            "reference" to etReference.text.toString()
        )

        // Donations
        val donationData = hashMapOf(
            "bankName" to etBankName.text.toString(),
            "accountName" to etAccountName.text.toString(),
            "accountNumber" to etAccountNumber.text.toString(),
            "branchCode" to etBranchCode.text.toString(),
            "reference" to etReference.text.toString(),
            "walletDocUrl" to etWalletDocUrl.text.toString()
        )

        // Volunteer
        val volunteerData = hashMapOf(
            "step1" to etStep1.text.toString(),
            "step2" to etStep2.text.toString(),
            "step3" to etStep3.text.toString(),
            "emailAddress" to etVolunteerEmail.text.toString(),
            "pdfUrl" to etPdfUrl.text.toString()
        )

        // Save all documents
        db.collection("appData").document("aboutUs").set(aboutUsData)
        db.collection("appData").document("contactInfo").set(contactData)
        db.collection("appData").document("donationInfo").set(donationData)
        db.collection("appData").document("volunteerInfo").set(volunteerData)
            .addOnSuccessListener {
                Toast.makeText(this, "All data saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}