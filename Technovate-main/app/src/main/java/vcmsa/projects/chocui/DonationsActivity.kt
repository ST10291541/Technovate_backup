package vcmsa.projects.chocui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class DonationsActivity : BaseActivity() {

    private val firebaseRepository = FirebaseRepository()
    private var currentDonationInfo: DonationInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_donations)

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Set up the navigation drawer
        setupNavigationDrawer()

        // Setup copy button first
        setupCopyButton()

        // Load donation info from Firestore
        loadDonationInfo()

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    private fun loadDonationInfo() {
        lifecycleScope.launch {
            val donationInfo = firebaseRepository.getDonationInfo()
            donationInfo?.let {
                currentDonationInfo = it
                updateUIWithDonationInfo(it)
                Toast.makeText(this@DonationsActivity, "✓ Loaded from Firebase", Toast.LENGTH_SHORT).show()
            } ?: run {
                // Fallback to hardcoded values if Firestore fails
                setupFallbackDonationInfo()
                // Show fallback message
                Toast.makeText(this@DonationsActivity, "⚠ Using local data", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun updateUIWithDonationInfo(donationInfo: DonationInfo) {
        // Update banking details TextViews
        findViewById<TextView>(R.id.tvBankName).text = donationInfo.bankName.ifEmpty { "Standard Bank Killarney" }
        findViewById<TextView>(R.id.tvAccountName).text = donationInfo.accountName.ifEmpty { "CHOC KZN" }
        findViewById<TextView>(R.id.tvAccountNumber).text = donationInfo.accountNumber.ifEmpty { "241 319 978" }
        findViewById<TextView>(R.id.tvBranchCode).text = donationInfo.branchCode.ifEmpty { "007205" }
        findViewById<TextView>(R.id.tvReference).text = donationInfo.reference.ifEmpty { "Your Name" }

        // Setup donation button
        val makeDonation = findViewById<Button>(R.id.btnMakeDonation)
        makeDonation.setOnClickListener { _ ->
            val url = donationInfo.walletDocUrl.ifEmpty { "https://www.walletdoc.com/pay/ChocDBN" }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun setupCopyButton() {
        val btnCopyAllDetails = findViewById<Button>(R.id.btnCopyAllDetails)
        btnCopyAllDetails.setOnClickListener {
            copyBankingDetailsToClipboard()
        }
    }

    private fun copyBankingDetailsToClipboard() {
        val bankingDetails = if (currentDonationInfo != null) {
            // Use Firestore data
            val info = currentDonationInfo!!
            """
                Bank Name: ${info.bankName}
                Account Name: ${info.accountName}
                Account Number: ${info.accountNumber}
                Branch Code: ${info.branchCode}
                Reference: ${info.reference}
            """.trimIndent()
        } else {
            // Use current TextView values (fallback)
            val bankName = findViewById<TextView>(R.id.tvBankName).text.toString()
            val accountName = findViewById<TextView>(R.id.tvAccountName).text.toString()
            val accountNumber = findViewById<TextView>(R.id.tvAccountNumber).text.toString()
            val branchCode = findViewById<TextView>(R.id.tvBranchCode).text.toString()
            val reference = findViewById<TextView>(R.id.tvReference).text.toString()

            """
                Bank Name: $bankName
                Account Name: $accountName
                Account Number: $accountNumber
                Branch Code: $branchCode
                Reference: $reference
            """.trimIndent()
        }

        // Copy to clipboard
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Banking Details", bankingDetails)
        clipboard.setPrimaryClip(clip)

        // Show confirmation message
        Toast.makeText(this, "Banking details copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun setupFallbackDonationInfo() {
        // Setup donation button with fallback URL
        val makeDonation = findViewById<Button>(R.id.btnMakeDonation)
        makeDonation.setOnClickListener { _ ->
            val url = "https://www.walletdoc.com/pay/ChocDBN"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }

        // Set fallback values in TextViews
        findViewById<TextView>(R.id.tvBankName).text = "Standard Bank Killarney"
        findViewById<TextView>(R.id.tvAccountName).text = "CHOC KZN"
        findViewById<TextView>(R.id.tvAccountNumber).text = "241 319 978"
        findViewById<TextView>(R.id.tvBranchCode).text = "007205"
        findViewById<TextView>(R.id.tvReference).text = "Your Name"
    }
}