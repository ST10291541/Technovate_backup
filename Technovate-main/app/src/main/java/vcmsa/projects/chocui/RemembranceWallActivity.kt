package vcmsa.projects.chocui

import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import com.google.android.material.appbar.MaterialToolbar

class RemembranceWallActivity : BaseActivity() {

    private lateinit var nameDisplay: TextView

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


        val gridLayout = findViewById<GridLayout>(R.id.remembranceGrid)

        // Create the TextView to display the name in the center
        nameDisplay = findViewById(R.id.nameDisplay)

        // Load names from JSON
        val names = loadNamesFromJson()

        // Create a heart for each name
        for (name in names) {
            val heartView = AppCompatTextView(this).apply {
                text = "💙"
                textSize = 32f
                setTextColor(resources.getColor(android.R.color.holo_blue_light, theme))
                setPadding(24, 24, 24, 24)

                setOnClickListener {
                    Toast.makeText(this@RemembranceWallActivity, name, Toast.LENGTH_SHORT).show()
                }
            }
            gridLayout.addView(heartView)
        }
    }

    fun loadNamesFromJson(): List<String> {
        val namesList = mutableListOf<String>()
        try {
            // Open the asset file as InputStream
            val inputStream: InputStream = assets.open("remembrance.json")

            // Read the InputStream into a string
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Parse the string into a JSONObject
            val jsonObject = JSONObject(jsonString)

            // Extract the "names" JSONArray from the JSONObject
            val namesArray: JSONArray = jsonObject.getJSONArray("names")

            // Convert the JSONArray to a list of names
            for (i in 0 until namesArray.length()) {
                namesList.add(namesArray.getString(i))
            }

        } catch (e: Exception) {
            e.printStackTrace()  // Handle exceptions (e.g., JSON parsing errors)
        }

        return namesList
    }
}


