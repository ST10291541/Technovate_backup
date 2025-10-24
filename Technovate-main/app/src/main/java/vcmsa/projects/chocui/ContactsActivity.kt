package vcmsa.projects.chocui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ContactsActivity : BaseActivity() {

    private lateinit var mapDbn: MapView
    private lateinit var mapPmb: MapView
    private val firebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Setup navigation drawer
        setupNavigationDrawer()

        // Initialize maps
        setupMaps(savedInstanceState)

        // Load contact info from Firestore
        loadContactInfo()

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    private fun loadContactInfo() {
        lifecycleScope.launch {
            val contactInfo = firebaseRepository.getContactInfo()
            contactInfo?.let {
                updateUIWithContactInfo(it)
                Toast.makeText(this@ContactsActivity, "✓ Contacts loaded from Firebase", Toast.LENGTH_SHORT).show()
            } ?: run {
                setupFallbackContactInfo()
                Toast.makeText(this@ContactsActivity, "⚠ Contacts: Using local data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUIWithContactInfo(contactInfo: ContactInfo) {
        val phoneButton = findViewById<MaterialButton>(R.id.phoneButton)
        val emailButton = findViewById<MaterialButton>(R.id.emailButton)

        // Update phone button text and click listener
        val phoneNumber = contactInfo.phone.ifEmpty { "0312402917" }
        phoneButton.text = "Call: $phoneNumber"
        phoneButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        // Update email button text and click listener
        val email = contactInfo.email.ifEmpty { "dbn@choc.org.za" }
        emailButton.text = "Email: $email"
        emailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
            }
            startActivity(intent)
        }

        // Update addresses
        findViewById<TextView>(R.id.tvAddressDurban).text =
            contactInfo.addressDurban.ifEmpty { "Durban Office Address" }
        findViewById<TextView>(R.id.tvAddressPmb).text =
            contactInfo.addressPmb.ifEmpty { "Pietermaritzburg Office Address" }

        // Update banking details in contacts
        findViewById<TextView>(R.id.tvBankNameContacts).text =
            "Bank: ${contactInfo.bankName}\nAccount: ${contactInfo.accountName}\nAccount Number: ${contactInfo.accountNumber}"

        // Update social media
        findViewById<MaterialButton>(R.id.facebookButton).setOnClickListener {
            val facebookUrl = contactInfo.facebookUrl.ifEmpty {
                "https://www.facebook.com/CHOC.Childhood.Cancer.Foundation/"
            }
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(facebookUrl)
            }
            startActivity(intent)
        }
    }

    private fun setupFallbackContactInfo() {
        val phoneButton = findViewById<MaterialButton>(R.id.phoneButton)
        val emailButton = findViewById<MaterialButton>(R.id.emailButton)

        // Set fallback button texts
        phoneButton.text = "Call: 0312402917"
        phoneButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:0312402917")
            }
            startActivity(intent)
        }

        emailButton.text = "Email: dbn@choc.org.za"
        emailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:dbn@choc.org.za")
            }
            startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.facebookButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.facebook.com/CHOC.Childhood.Cancer.Foundation/")
            }
            startActivity(intent)
        }

        // Set fallback addresses
        findViewById<TextView>(R.id.tvAddressDurban).text = "Your Durban Address Here"
        findViewById<TextView>(R.id.tvAddressPmb).text = "Your PMB Address Here"
        findViewById<TextView>(R.id.tvBankNameContacts).text = "Bank: Standard Bank Killarney\nAccount: CHOC KZN\nAccount Number: 241 319 978"
    }

    private fun setupMaps(savedInstanceState: Bundle?) {
        mapDbn = findViewById(R.id.mapDbn)
        mapPmb = findViewById(R.id.mapPmb)

        mapDbn.onCreate(savedInstanceState?.getBundle("mapDbnBundle"))
        mapPmb.onCreate(savedInstanceState?.getBundle("mapPmbBundle"))

        mapDbn.getMapAsync { googleMap ->
            val durban = LatLng(-29.8579, 31.0292)
            googleMap.addMarker(MarkerOptions().position(durban).title("Durban Office"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(durban, 14f))
        }

        mapPmb.getMapAsync { googleMap ->
            val pmb = LatLng(-29.6104, 30.3794)
            googleMap.addMarker(MarkerOptions().position(pmb).title("PMB Office"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pmb, 14f))
        }
    }


}