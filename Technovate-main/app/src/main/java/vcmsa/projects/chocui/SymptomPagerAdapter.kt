package vcmsa.projects.chocui



import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class SymptomPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val symptoms = listOf(
        Symptom(
            "Nausea / Upset Stomach",
            listOf(
                "Dry crackers or plain toast (easy on the stomach)",
                "Ginger rooibos tea (gentle, helps with nausea)",
                "Cold fruit smoothies (banana + pawpaw + yogurt, avoid citrus)",
                "Plain mashed potatoes with a drizzle of olive oil",
                "Rice porridge with soft vegetables or chicken broth"
            ),
            "#FF9800"
        ),
        Symptom(
            "Mouth Sores / Sore Throat",
            listOf(
                "Soft scrambled eggs with a little cream or milk",
                "Mashed butternut or pumpkin with melted butter",
                "Smooth yogurt or custard (avoid acidic fruits)",
                "Soft mielie pap with milk and honey (for kids over 1 year)",
                "Chilled jelly or pudding (soothing for the mouth)"
            ),
            "#2196F3"
        ),
        Symptom(
            "Low Appetite / Fatigue",
            listOf(
                "High-energy smoothies (banana + peanut butter + yogurt)",
                "Mashed avocado on soft bread",
                "Mini cheesy omelet muffins (easy to chew)",
                "Maize meal porridge with peanut butter swirl",
                "Small cheese and chicken mayo sandwich triangles"
            ),
            "#4CAF50"
        ),
        Symptom(
            "Diarrhea",
            listOf(
                "White rice with soft chicken or pumpkin",
                "Soft pap without added sugar (plain, with a little salt)",
                "Banana mashed with a little yogurt",
                "Rooibos tea (caffeine-free and hydrating)",
                "Toasted brown bread with a thin layer of margarine"
            ),
            "#9C27B0"
        ),
        Symptom(
            "Constipation",
            listOf(
                "Mashed pawpaw or ripe pear (peeled)",
                "Oatmeal with soft fruit puree",
                "Stewed prunes or applesauce",
                "Smoothies with yogurt and soft fruit",
                "Whole-wheat crackers with peanut butter"
            ),
            "#795548"
        )
    )

    override fun getItemCount(): Int = symptoms.size

    override fun createFragment(position: Int): Fragment {
        return SymptomFragment.newInstance(symptoms[position])
    }
}

