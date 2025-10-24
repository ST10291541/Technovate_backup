package vcmsa.projects.chocui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SymptomFragment : Fragment() {

    companion object {
        private const val ARG_SYMPTOM = "symptom"

        fun newInstance(symptom: Symptom): SymptomFragment {
            val fragment = SymptomFragment()
            val args = Bundle()
            args.putSerializable(ARG_SYMPTOM, symptom)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_symptom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val symptom = arguments?.getSerializable(ARG_SYMPTOM) as? Symptom ?: return

        val titleTextView = view.findViewById<TextView>(R.id.symptomTitle)
        val snacksTextView = view.findViewById<TextView>(R.id.snackList)

        titleTextView.text = "For ${symptom.title}"
        titleTextView.setTextColor(Color.parseColor(symptom.color))

        val snacksText = symptom.snacks.joinToString("\n") { "• $it" }
        snacksTextView.text = snacksText
    }
}