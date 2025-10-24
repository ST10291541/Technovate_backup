package vcmsa.projects.chocui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Check if user is authenticated
        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AdminLoginActivity::class.java))
            finish()
            return
        }

        // Single edit button for all content
        findViewById<Button>(R.id.btnEditAll).setOnClickListener {
            startActivity(Intent(this, AdminEditActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AdminLoginActivity::class.java))
            finish()
        }
    }
}