package vcmsa.projects.chocui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : BaseActivity() {

    private lateinit var cardGetInvolved: MaterialCardView
    private lateinit var cardGetToKnowUs: MaterialCardView
    private lateinit var cardCancerInfo: MaterialCardView

    private lateinit var goldenRibbon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Find drawer layout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Setup navigation drawer - use admin menu if the admin is logged in
        setupNavigationDrawer()

        // Initialize navigation cards
        cardGetInvolved = findViewById(R.id.cardGetInvolved)
        cardGetToKnowUs = findViewById(R.id.cardGetToKnowUs)
        cardCancerInfo = findViewById(R.id.cardCancerInfo)

        // Initialize golden ribbon
        val goldenRibbon: ImageView = findViewById(R.id.goldenRibbon)

        // Set click listeners for cards
        cardGetInvolved.setOnClickListener {
            startActivity(Intent(this, VolunteerActivity::class.java))
        }

        cardGetToKnowUs.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

        cardCancerInfo.setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }

// Set click listener for golden ribbon (admin login)
        // In your MainActivity.kt, keep the golden ribbon click listener:
        goldenRibbon.setOnClickListener {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                // Already logged in, go to admin dashboard
                startActivity(Intent(this, AdminDashboardActivity::class.java))
            } else {
                // Not logged in, go to login screen
                startActivity(Intent(this, AdminLoginActivity::class.java))
            }
        }
    }
}