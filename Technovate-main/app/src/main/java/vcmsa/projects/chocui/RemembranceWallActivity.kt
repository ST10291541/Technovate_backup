package vcmsa.projects.chocui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class RemembranceWallActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private var currentPopup: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remembrance_wall)

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Set up the navigation drawer
        setupNavigationDrawer()

        recyclerView = findViewById(R.id.remembranceGrid)

        // Load names from JSON
        val names = loadNamesFromJson()

        // Calculate optimal column count
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val columnCount = (screenWidthDp / 80).toInt()

        // Setup RecyclerView with GridLayoutManager
        recyclerView.layoutManager = GridLayoutManager(this, columnCount)
        recyclerView.adapter = HeartAdapter(names) { name ->
            showMemorialPopup(name)
        }

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    private fun showMemorialPopup(name: String) {
        // Dismiss any existing popup
        currentPopup?.dismiss()

        // Create a custom popup view
        val popupView = LayoutInflater.from(this).inflate(R.layout.memorial_popup, null)

        val nameText = popupView.findViewById<TextView>(R.id.popupName)
        val messageText = popupView.findViewById<TextView>(R.id.popupMessage)

        nameText.text = name
        messageText.text = getRandomMemorialMessage()

        // Create the popup window
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            // Set a semi-transparent background
            setBackgroundDrawable(null)
            elevation = 20f
            animationStyle = android.R.style.Animation_Dialog
            isOutsideTouchable = true
        }

        // Show the popup in the center of the screen
        popupWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0)
        currentPopup = popupWindow

        // Add entrance animation
        animatePopupEntrance(popupView)

        // Auto-dismiss after 5 seconds, or let user tap to dismiss
        popupView.setOnClickListener {
            animatePopupExit(popupView) {
                popupWindow.dismiss()
            }
        }

        // Also allow tapping outside the popup to dismiss
        popupWindow.setTouchInterceptor { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                animatePopupExit(popupView) {
                    popupWindow.dismiss()
                }
                true
            } else {
                false
            }
        }

        // Auto-dismiss after 8 seconds
        popupView.postDelayed({
            if (popupWindow.isShowing) {
                animatePopupExit(popupView) {
                    popupWindow.dismiss()
                }
            }
        }, 8000L)
    }

    private fun animatePopupEntrance(view: View) {
        view.alpha = 0f
        view.scaleX = 0.5f
        view.scaleY = 0.5f

        val animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.1f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.1f, 1f)
            )
            duration = 600
            interpolator = DecelerateInterpolator()
        }
        animatorSet.start()
    }

    private fun animatePopupExit(view: View, onEnd: () -> Unit) {
        val animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f)
            )
            duration = 400
            interpolator = AccelerateInterpolator()
            doOnEnd { onEnd() }
        }
        animatorSet.start()
    }

    private fun createPopupBackground(): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(resources.getColor(android.R.color.transparent, theme))
            cornerRadius = 24f
            setStroke(4, 0x6687CEFA) // Light blue border
        }
    }

    private fun getRandomMemorialMessage(): String {
        val messages = listOf(
            "Forever in our hearts 💙",
            "Your light shines on ✨",
            "Always remembered, always loved 💙",
            "A beautiful soul lives on ✨",
            "Your courage inspires us ✨",
            "Forever young, forever loved 💙",
            "In our hearts forever 💙",
            "Your star shines bright ✨"
        )
        return messages.random()
    }

    // HeartAdapter class
    private class HeartAdapter(
        private val names: List<String>,
        private val onHeartClick: (String) -> Unit
    ) : RecyclerView.Adapter<HeartAdapter.HeartViewHolder>() {

        class HeartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val heartText: TextView = itemView.findViewById(R.id.heartText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_heart, parent, false)
            return HeartViewHolder(view)
        }

        override fun onBindViewHolder(holder: HeartViewHolder, position: Int) {
            val name = names[position]
            holder.heartText.text = "💙"

            holder.itemView.setOnClickListener {
                onHeartClick(name)
            }
        }

        override fun getItemCount() = names.size
    }

    fun loadNamesFromJson(): List<String> {
        val namesList = mutableListOf<String>()
        try {
            val inputStream: InputStream = assets.open("remembrance.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val namesArray: JSONArray = jsonObject.getJSONArray("names")

            for (i in 0 until namesArray.length()) {
                namesList.add(namesArray.getString(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return namesList
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPopup?.dismiss()
    }
}