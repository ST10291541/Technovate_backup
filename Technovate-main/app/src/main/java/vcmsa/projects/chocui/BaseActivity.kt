package vcmsa.projects.chocui

import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var toggle: ActionBarDrawerToggle
    protected lateinit var navigationView: NavigationView

    protected fun setupNavigationDrawer() {
        try {
            drawerLayout = findViewById(R.id.drawer_layout)
            navigationView = findViewById<NavigationView>(R.id.nav_view)
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)

            // Update admin menu items visibility
            updateAdminMenuItems()

            navigationView.setNavigationItemSelectedListener(this)

            toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun updateAdminMenuItems() {
        val currentUser = Firebase.auth.currentUser
        val isAdminLoggedIn = currentUser != null

        // Show/hide admin menu items based on login status
        try {
            navigationView.menu.findItem(R.id.nav_admin_dashboard)?.isVisible = isAdminLoggedIn
            navigationView.menu.findItem(R.id.nav_admin_logout)?.isVisible = isAdminLoggedIn
        } catch (e: Exception) {
            // Menu items might not exist in all layouts, that's okay
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val context = this

        // Helper function to start activity only if we're not already on it
        fun <T : BaseActivity> startActivityIfNotCurrent(activityClass: Class<T>) {
            if (context::class.java != activityClass) {
                startActivity(Intent(context, activityClass))
                finish()
            }
        }

        val handled = when (item.itemId) {
            R.id.nav_home -> {
                startActivityIfNotCurrent(MainActivity::class.java)
                true
            }
            R.id.nav_about -> {
                startActivityIfNotCurrent(AboutUsActivity::class.java)
                true
            }
            R.id.nav_contacts -> {
                startActivityIfNotCurrent(ContactsActivity::class.java)
                true
            }
            R.id.nav_donate -> {
                startActivityIfNotCurrent(DonationsActivity::class.java)
                true
            }
            R.id.nav_events -> {
                startActivityIfNotCurrent(EventsActivity::class.java)
                true
            }
            R.id.nav_volunteer -> {
                startActivityIfNotCurrent(VolunteerActivity::class.java)
                true
            }
            R.id.nav_nutrition -> {
                startActivityIfNotCurrent(NutritionActivity::class.java)
                true
            }
            R.id.nav_videos -> {
                startActivityIfNotCurrent(VideosActivity::class.java)
                true
            }
            R.id.nav_remembrance_wall -> {
                startActivityIfNotCurrent(RemembranceWallActivity::class.java)
                true
            }
            R.id.nav_admin_dashboard -> {
                startActivity(Intent(context, AdminDashboardActivity::class.java))
                true
            }
            R.id.nav_admin_logout -> {
                Firebase.auth.signOut()
                updateAdminMenuItems() // Hide admin items immediately
                Toast.makeText(context, "Admin logged out", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }

        if (handled) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return handled
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        // Update admin menu items whenever the activity resumes
        updateAdminMenuItems()
    }
}