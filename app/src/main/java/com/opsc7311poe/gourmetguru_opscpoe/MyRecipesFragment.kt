package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class MyRecipesFragment : Fragment() {

    // Declare ImageView variables
    private lateinit var imgTimer: ImageView
    private lateinit var imgAddRecipes: ImageView
    private lateinit var imgViewRecipes: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_recipes, container, false)

        // Initialize ImageView variables
        imgTimer = view.findViewById(R.id.imgTimer)

        // Set onClickListener for imgTimer
        imgTimer.setOnClickListener {
            replaceFragment(TimerFragment())
        }

        //handling add recipe navigation
        imgAddRecipes = view.findViewById(R.id.imgAddRecipes)
        imgAddRecipes.setOnClickListener {
            replaceFragment(AddYourRecipeFregment())
        }

        //handling view recipe navigation
        imgViewRecipes = view.findViewById(R.id.imgViewRecipes)
        imgViewRecipes.setOnClickListener {
            replaceFragment(ViewYourRecipesFrgament())
        }


        return view
    }

    // Function to replace the current fragment
    private fun replaceFragment(fragment: Fragment) {
        Log.d("CustomerFragment", "Replacing fragment: ${fragment::class.java.simpleName}")
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
