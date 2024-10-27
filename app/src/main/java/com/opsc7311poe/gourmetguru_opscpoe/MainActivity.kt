package com.opsc7311poe.gourmetguru_opscpoe

import android.media.RingtoneManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottom_navigation)

        // Set "Home" as the selected item
        bottomNavView.selectedItemId = R.id.navHome

        // Load the HomeFragment initially
        replaceFragment(HomeFragment())

        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> {

                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navMealPlan -> {

                    replaceFragment(MealPlanFragment())
                    true
                }
                R.id.navSearch -> {

                    replaceFragment(SearchFragment())
                    true
                }
                R.id.navMyProfile -> {

                    replaceFragment(MyProfileFragment())
                    true
                }
                R.id.navMyRecipe -> {

                    replaceFragment(MyRecipesFragment())
                    true
                }
                else -> false
            }
        }

        // Set default fragment if there's no saved instance state
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // Stop the alarm sound if the app is opened from notification
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(this, notificationSound)
        if (ringtone.isPlaying) {
            ringtone.stop()
        }

    }


    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}
