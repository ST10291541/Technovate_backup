package vcmsa.projects.chocui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class VolunteerActivity : BaseActivity() {

    private val firebaseRepository = FirebaseRepository()
    private lateinit var btnContact: MaterialButton
    private var currentVolunteerInfo: VolunteerInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Setup navigation drawer
        setupNavigationDrawer()

        // Load volunteer info from Firestore
        loadVolunteerInfo()

        // Set up buttons (they'll be updated after Firestore loads)
        setupButtons()

        btnContact = findViewById(R.id.contactButton)

        // Set up click listeners
        btnContact.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    private fun loadVolunteerInfo() {
        lifecycleScope.launch {
            val volunteerInfo = firebaseRepository.getVolunteerInfo()
            volunteerInfo?.let {
                currentVolunteerInfo = it
                updateUIWithVolunteerInfo(it)
                Toast.makeText(this@VolunteerActivity, "✓ Volunteer info from Firebase", Toast.LENGTH_SHORT).show()
            } ?: run {
                setupFallbackVolunteerInfo()
                Toast.makeText(this@VolunteerActivity, "⚠ Volunteer: Using local data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUIWithVolunteerInfo(volunteerInfo: VolunteerInfo) {
        findViewById<TextView>(R.id.tvStep1).text = volunteerInfo.step1
        findViewById<TextView>(R.id.tvStep2).text = volunteerInfo.step2
        findViewById<TextView>(R.id.tvStep3).text = volunteerInfo.step3

        // Update buttons with dynamic data
        updateButtons(volunteerInfo)
    }

    private fun setupButtons() {
        // Initial setup - will be updated after Firestore loads
        val downloadBtn: MaterialButton = findViewById(R.id.downloadBtn)
        val emailBtn: MaterialButton = findViewById(R.id.emailBtn)

        downloadBtn.setOnClickListener {
            val pdfUrl = currentVolunteerInfo?.pdfUrl ?: "https://drive.google.com/uc?export=download&id=1KSgPfQRS-rzEcaZWNLWgikhW1mu-S7hz"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
            startActivity(browserIntent)
        }

        emailBtn.setOnClickListener {
            val emailAddress = currentVolunteerInfo?.emailAddress ?: "dbn@choc.org.za"
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailAddress")
                putExtra(Intent.EXTRA_SUBJECT, "CHOC Volunteer Registration Form")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hi\n\nPlease find my completed volunteer form attached.\n\nRegards,\n CHOC"
                )
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }

    private fun updateButtons(volunteerInfo: VolunteerInfo) {
        // Update button texts if needed
        val emailBtn: MaterialButton = findViewById(R.id.emailBtn)
        emailBtn.text = "Send Completed Form to ${volunteerInfo.emailAddress}"
    }

    private fun setupFallbackVolunteerInfo() {
        // Fallback to original hardcoded values
        findViewById<TextView>(R.id.tvStep1).text = "Download the PDF form"
        findViewById<TextView>(R.id.tvStep2).text = "Fill in digitally or by hand"
        findViewById<TextView>(R.id.tvStep3).text = "Email the completed form to dbn@choc.org.za"

        // Keep your original hardcoded button functionality
        val downloadBtn: MaterialButton = findViewById(R.id.downloadBtn)
        val emailBtn: MaterialButton = findViewById(R.id.emailBtn)

        downloadBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/uc?export=download&id=1KSgPfQRS-rzEcaZWNLWgikhW1mu-S7hz"))
            startActivity(browserIntent)
        }

        emailBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:dbn@choc.org.za")
                putExtra(Intent.EXTRA_SUBJECT, "CHOC Volunteer Registration Form")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hi\n\nPlease find my completed volunteer form attached.\n\nRegards,\n CHOC"
                )
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }
}