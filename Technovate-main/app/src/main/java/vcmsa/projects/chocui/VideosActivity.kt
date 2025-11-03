package vcmsa.projects.chocui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class VideosActivity : BaseActivity() {

    private val videoList = listOf(
        Video("Early Warning Signs", "cHtRlUNY_qE", "Learn about the early signs and symptoms of childhood cancer that parents should watch for"),
        Video("Keemo gets a port", "YucfqAxBONc", "Join Keemo as he gets a port implanted to make his treatment journey easier and less painful"),
        Video("Keemo goes home", "jxm4xP5w5Vk", "Follow Keemo's emotional journey as he returns home from the hospital during treatment"),
        Video("Keemo goes to the doctor", "V1tvnxLa-iw", "See what happens during Keemo's doctor visits and how medical teams care for children with cancer"),
        Video("Keemo loses his hair", "PpkVnXqjNps", "A gentle explanation of why hair loss happens during treatment and how Keemo copes with this change"),
        Video("Keemo starts treatment", "Hi4rS0ppJVI", "Follow Keemo as he begins his cancer treatment journey and meets his healthcare team"),
        Video("Keemo feels pain", "nKZcR00PnfE", "Learn how Keemo and his medical team manage pain and discomfort during cancer treatment")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)


        val recyclerView = findViewById<RecyclerView>(R.id.videosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VideoAdapter(videoList)

        setupNavigationDrawer()

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            startActivity(Intent(this, Chatbot::class.java))
        }
    }

    data class Video(val title: String, val videoId: String, val description: String)
}