package vcmsa.projects.chocui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

class MainActivity : BaseActivity() {

    private lateinit var cardGetInvolved: MaterialCardView
    private lateinit var cardGetToKnowUs: MaterialCardView
    private lateinit var cardCancerInfo: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout) // make sure the filename matches!

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Find drawer layout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Setup navigation drawer
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
        goldenRibbon.setOnClickListener {
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }
}