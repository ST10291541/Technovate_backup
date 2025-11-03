package vcmsa.projects.chocui

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NutritionActivity : BaseActivity() {
    private val symptomTitles = listOf(
        "Nausea", "Mouth Sores", "Low Appetite", "Diarrhea", "Constipation"
    )

    private val mealTitles = listOf(
        "Breakfast", "Easy Digest", "High Energy", "Budget", "Desserts"
    )

    // Declare view references
    private lateinit var symptomViewPager: ViewPager2
    private lateinit var symptomTabLayout: TabLayout
    private lateinit var mealViewPager: ViewPager2
    private lateinit var mealTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        // Initialize views
        symptomViewPager = findViewById(R.id.viewPager)
        symptomTabLayout = findViewById(R.id.tabLayout)
        mealViewPager = findViewById(R.id.mealViewPager)
        mealTabLayout = findViewById(R.id.mealTabLayout)

        setupSymptomViewPager()
        setupMealViewPager()

        // Setup navigation drawer
        setupNavigationDrawer()

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    private fun setupSymptomViewPager() {
        val adapter = SymptomPagerAdapter(this)
        symptomViewPager.adapter = adapter

        TabLayoutMediator(symptomTabLayout, symptomViewPager) { tab, position ->
            tab.text = symptomTitles[position]
        }.attach()
    }

    private fun setupMealViewPager() {
        val adapter = MealPagerAdapter(this)
        mealViewPager.adapter = adapter

        TabLayoutMediator(mealTabLayout, mealViewPager) { tab, position ->
            tab.text = mealTitles[position]
        }.attach()
    }
}