package vcmsa.projects.chocui

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var toggle: ActionBarDrawerToggle


    protected fun setupNavigationDrawer() {
        try {
            drawerLayout = findViewById(R.id.drawer_layout)
            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
            
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
}
