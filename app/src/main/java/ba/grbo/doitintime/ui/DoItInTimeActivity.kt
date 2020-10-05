package ba.grbo.doitintime.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import ba.grbo.doitintime.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoItInTimeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    var onReleaseFocusListener: ((ev: MotionEvent) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_it_in_time)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        onReleaseFocusListener?.invoke(ev)
        return super.dispatchTouchEvent(ev)
    }
}