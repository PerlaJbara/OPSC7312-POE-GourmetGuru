package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.opsc7311poe.gourmetguru_opscpoe.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var btnBack: ImageView
    private var mealType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Get the mealType argument if passed from the previous fragment
        mealType = arguments?.getString("mealType")

        // Set up the back button with haptic feedback and navigation
        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            findNavController().popBackStack() // Go back to the previous fragment
        }

        // Search button action
        binding.btnSearch.setOnClickListener {
            val searchQuery = binding.txtName.text.toString()
            performSearch(searchQuery)
        }

        // Filter button action
        binding.btnFilter.setOnClickListener {
            showFilterDialog()
        }

        return binding.root
    }

    private fun performSearch(query: String) {
        // Replace this with your actual search logic (API calls, Firebase, etc.)
        // Update the results dynamically based on the search query and meal type
       // binding.textViewResults.text = "Searching for: $query in $mealType"
    }

    private fun showFilterDialog() {
        // Create a simple filter dialog (you can customize this more)
        val filterOptions = arrayOf("Breakfast", "Lunch", "Dinner", "Vegan", "Pescatarian", "Vegetarian")
        // Show the options in a dialog and update the query after selection
    }
}
