package com.abbisea.pwexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.abbisea.pwexercise.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // val navController = findNavController(R.id.navHostFragmentContainer)
        // val graph = (navHostFragmentContainer as NavHostFragment).navController.navInflater.inflate(R.navigation.nav_graph)
        // check sign in status here
    }
}