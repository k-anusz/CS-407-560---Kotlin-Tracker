package com.example.expensetrackerwithauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.expensetrackerwithauth.fragments.*
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // #### Authentication using FirebaseAuth #####

        // Get instance of the FirebaseAuth and then
        val currentUser = FirebaseAuth.getInstance().currentUser

        // If currentUser is null, open the RegisterActivity
        if (currentUser == null) {
            startRegisterActivity()
        }
        else {
            val homeFragment = HomeFragment()
            val moneyInFragment = MoneyInFragment()
            val moneyOutFragment = MoneyOutFragment()
            val settingsFragment = SettingsFragment()
            val stockFragment = StockFragment()

            makeCurrentFragment(homeFragment)

            bottom_navigation.setOnNavigationItemSelectedListener {
                // when you click on the fragment in navigation it changes to the fragment you clicked on
                when (it.itemId){
                    R.id.ic_home -> makeCurrentFragment(homeFragment)
                    R.id.ic_money_in -> makeCurrentFragment(moneyInFragment)
                    R.id.ic_money_out -> makeCurrentFragment(moneyOutFragment)
                    R.id.ic_baseline_settings_24 -> makeCurrentFragment(settingsFragment)
                    R.id.ic_stock -> makeCurrentFragment(stockFragment)
                }
                true
            }
        }
    }

    // for navigation bar
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    // An helper function to start our RegisterActivity
    private fun startRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        // Make sure to call finish(), otherwise the user would be able to go back to the MainActivity
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                // User chose the "logout" item, logout the user then
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()

                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // After logout, start the RegisterActivity again
                            startRegisterActivity()
                        }
                        else {
                            Log.e(TAG, "Task is not successful:${task.exception} ")
                        }
                    }
                true
            }
            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
    }
}


