package vcmsa.projects.chocui

import vcmsa.projects.chocui.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EventsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        val viewEventsButton = findViewById<Button>(R.id.btnViewEvents)

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Set up the navigation drawer
        setupNavigationDrawer()

        viewEventsButton.setOnClickListener { v: View? ->
            val url = "https://choc.org.za/events/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }

        //CHANGE IMAGES
        //CHECK THEIR FACEBOOK
        val images = listOf(
            R.drawable.chocevents1,
            R.drawable.chocevents2,
            R.drawable.chocevents3
        )

        val adapter = ImageSliderAdapter(images)
        val viewPager: ViewPager2 = findViewById(R.id.imageSlider)
        viewPager.adapter = adapter

// Optional: auto-scroll
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            var currentPosition = 0
            override fun run() {
                if(currentPosition == images.size) currentPosition = 0
                viewPager.currentItem = currentPosition++
                handler.postDelayed(this, 5000) // change every 5 seconds
            }
        }
        handler.post(runnable)

    }
}
