package com.example.com_us

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.com_us.databinding.ActivityMainBinding
import com.example.com_us.ui.block.basic.BlockViewModel
import com.example.com_us.ui.qa.AnswerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel : AnswerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_questions, R.id.navigation_profile
            )
        )

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Add listener to hide/show BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                 R.id.loadingFragment , R.id.answerFragment , R.id.conversationQuestionFragment, R.id.selectAnswerFragment, R.id.blockModifyFragment, R.id.previousAnswerFragment, R.id.collectBlockFragment, R.id.blockCompleteFragment-> {
                    binding.navView.visibility = View.GONE  // Hide Bottom Bar
                }
                else ->
                    binding.navView.visibility = View.VISIBLE
            }

        }

        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.hide()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.itemIconTintList = null


//
//        navController.addOnDestinationChangedListener{_, destination, _ ->
//           binding.navView.selectedItemId = destination.id
//            binding.navView.visibility =
//             if (destination.id == R.id.selectAnswerFragment) View.GONE
//             else if (destination.id==R.id.conversationQuestionFragment) View.GONE
//            else {
//                View.VISIBLE
//             }
//       }


        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_questions -> {
                    navController.navigate(R.id.navigation_questions)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.checkFragment)
                    true
                }
                R.id.navigation_block -> {
                    navController.navigate(R.id.blockFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun changeNavItemColor(colorResId: Int) {
        binding.navView.itemIconTintList = ContextCompat.getColorStateList(this, colorResId)
    }

}