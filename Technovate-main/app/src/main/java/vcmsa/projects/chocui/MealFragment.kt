package vcmsa.projects.chocui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MealFragment : Fragment() {

    companion object {
        private const val ARG_MEAL_CATEGORY = "meal_category"

        fun newInstance(mealCategory: MealCategory): MealFragment {
            val fragment = MealFragment()
            val args = Bundle()
            args.putSerializable(ARG_MEAL_CATEGORY, mealCategory)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealCategory = arguments?.getSerializable(ARG_MEAL_CATEGORY) as? MealCategory ?: return

        val titleTextView = view.findViewById<TextView>(R.id.mealTitle)
        val mealsTextView = view.findViewById<TextView>(R.id.mealList)

        titleTextView.text = mealCategory.title
        titleTextView.setTextColor(Color.parseColor(mealCategory.color))

        val mealsText = mealCategory.meals.joinToString("\n") { "• $it" }
        mealsTextView.text = mealsText
    }
}