package vcmsa.projects.chocui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MealPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val mealCategories = listOf(
        MealCategory(
            "Breakfast Options",
            listOf(
                "Oats with mashed banana: A warm, gentle start to the day. Add a little peanut butter for extra energy.",
                "Soft mielie pap with milk and butter: Classic, filling, and easy to digest.",
                "Scrambled eggs on soft bread: High in protein and soft for little mouths.",
                "Smoothie bowl: Blend banana, yogurt, and soft fruit puree for a quick, nutritious meal.",
                "French toast (mild): Made with full-cream milk and egg; serve with soft fruit puree instead of syrup.",
                "Mashed avocado on soft brown bread: Healthy fats for energy and creamy texture for easy eating.",
                "Mini pumpkin muffins (soft and moist): Great for variety and mild in flavor."
            ),
            "#FF9800"
        ),
        MealCategory(
            "Easy to Digest & Mild",
            listOf(
                "Soft chicken stew: Cook chicken breast until very tender and serve with mashed potatoes or soft rice.",
                "Maize meal porridge (pap): Serve with milk and a little butter or cheese for extra energy.",
                "Fish cakes: Made with pilchards or tuna, mashed potato, and soft breadcrumbs.",
                "Mild soups: Pumpkin, butternut, or chicken broth-based soups.",
                "Macaroni and cheese: A classic comfort food. Use full-cream milk and a good cheese for extra calories.",
                "Creamy butternut and sweet potato soup: Smooth and naturally sweet, easy to swallow.",
                "Steamed pumpkin with soft pap: Light, soft, and nutritious."
            ),
            "#2196F3"
        ),
        MealCategory(
            "High-Energy & Protein-Rich",
            listOf(
                "Beef or chicken mince dishes: Cottage pie or savoury mince served with soft rice or pasta.",
                "Lentil or bean stew: A good, budget-friendly source of protein.",
                "Boiled eggs and toast: A simple, quick meal high in protein.",
                "Oats with added goodness: Cooked oats with nuts, seeds, peanut butter, or honey (for children over 1 year).",
                "Mini omelets or egg muffins: Soft and full of protein, easy to hold and eat.",
                "Avocado on soft bread: Healthy fats for energy and smooth texture for easy eating.",
                "Chicken and vegetable pasta: Use a light cream sauce for added calories and to make it easier to swallow."
            ),
            "#4CAF50"
        ),
        MealCategory(
            "Budget-Friendly & Local",
            listOf(
                "Pumpkin and maize meal porridge: Mild, filling, and very cost-effective.",
                "Pilchard stew with pap: Rich in omega-3 and protein at a low cost.",
                "Vegetable and lentil curry (mild): Serve with soft rice; a hearty, nutritious option.",
                "Samp and beans (soft cooked): A traditional favorite, high in energy and fiber when cooked until tender."
            ),
            "#9C27B0"
        ),
        MealCategory(
            "Dessert Options",
            listOf(
                "Jelly with custard: A soft, soothing treat for sore mouths.",
                "Rice pudding with soft fruits: Warm, creamy, and easy to swallow.",
                "Stewed apples or pears: Cook until soft; add a sprinkle of cinnamon for flavor.",
                "Pumpkin fritters (mild): Sweet, soft, and comforting.",
                "Fruit yogurt cups: Use peeled soft fruits like pawpaw or banana for extra nutrients.",
                "Baked apple slices with a touch of honey (for children over 1 year): A naturally sweet, gentle dessert.",
                "Banana custard: Mash banana into custard for a smooth, energy-rich treat."
            ),
            "#795548"
        )
    )

    override fun getItemCount(): Int = mealCategories.size

    override fun createFragment(position: Int): Fragment {
        return MealFragment.newInstance(mealCategories[position])
    }
}

data class MealCategory(val title: String, val meals: List<String>, val color: String) : java.io.Serializable