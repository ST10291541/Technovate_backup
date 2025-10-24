package vcmsa.projects.chocui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class AboutUsActivity : BaseActivity() {

    private val firebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        // Initialize toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Find drawer layout before setting up navigation
        drawerLayout = findViewById(R.id.drawer_layout)

        // Setup navigation drawer after drawer layout is initialized
        setupNavigationDrawer()

        // Load about us info from Firestore
        loadAboutUsInfo()

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    private fun loadAboutUsInfo() {
        lifecycleScope.launch {
            val aboutUsInfo = firebaseRepository.getAboutUsInfo()
            aboutUsInfo?.let {
                updateUIWithAboutUsInfo(it)
                Toast.makeText(this@AboutUsActivity, "✓ About Us loaded", Toast.LENGTH_SHORT).show()
            } ?: run {
                setupFallbackAboutUsInfo()
                Toast.makeText(this@AboutUsActivity, "⚠ About Us: Using local data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUIWithAboutUsInfo(aboutUsInfo: AboutUsInfo) {
        findViewById<TextView>(R.id.tvWhatIsChoc).text = aboutUsInfo.whatIsChoc
        findViewById<TextView>(R.id.tvWhereWeKeepHope).text = aboutUsInfo.whereWeKeepHope
        findViewById<TextView>(R.id.tvRegionsDescription).text = aboutUsInfo.regionsDescription
        findViewById<TextView>(R.id.tvFounderNote).text = aboutUsInfo.founderNote
    }

    private fun setupFallbackAboutUsInfo() {
        // Fallback to original hardcoded values
        findViewById<TextView>(R.id.tvWhatIsChoc).text = "Childhood Cancer Foundation SA\nA non-profit organisation that advocates for the health and well-being of children and teenagers diagnosed with cancer or life-threatening blood disorders and their families.\n\nThe passionate and dedicated staff and volunteers of CHOC aim to save lives through early detection and comprehensive support programmes for the families affected by cancer.\n\nWe offer psychosocial, emotional and practical support as we augment the different medical fraternities."

        findViewById<TextView>(R.id.tvWhereWeKeepHope).text = "Where We Keep Hope Alive"

        findViewById<TextView>(R.id.tvRegionsDescription).text = "CHOC has a national office in Johannesburg, and six regional offices offering services and support to the nine provinces of South Africa.\n\nThe regions are demographically positioned to ensure that we reach every child and family battling cancer in South Africa."

        findViewById<TextView>(R.id.tvFounderNote).text = "Sister Sadie Cutland founded CHOC in 1979 after her daughter Hillary was diagnosed with aplastic anaemia. Her personal experience inspired the creation of a support group that has grown into South Africa's leading childhood cancer organization."
    }
}