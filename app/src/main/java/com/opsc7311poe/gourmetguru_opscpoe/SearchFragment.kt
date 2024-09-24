package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.app.AlertDialog
import com.opsc7311poe.gourmetguru_opscpoe.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    class SearchFragment : Fragment() {

        private lateinit var binding: FragmentSearchBinding
        private lateinit var btnBack: ImageView
        private var mealType: String? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentSearchBinding.inflate(inflater, container, false)

            mealType = arguments?.getString("mealType")

            btnBack = binding.btnBack
            btnBack.setOnClickListener {
                it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                findNavController().popBackStack()
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
            val database = FirebaseDatabase.getInstance().reference

            // Clear previous results
            binding.layoutResults.removeAllViews()

            database.child("Users").child("cuisines")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val resultList = mutableListOf<String>()

                        // Loop through all cuisines
                        for (cuisineSnapshot in dataSnapshot.children) {
                            val cuisine = cuisineSnapshot.key

                            // Loop through the recipes within each cuisine
                            val recipeSnapshot = cuisineSnapshot.child("recipes")
                            for (recipe in recipeSnapshot.children) {
                                val recipeName = recipe.key
                                if (recipeName?.contains(query, ignoreCase = true) == true) {
                                    resultList.add("$cuisine: $recipeName")

                                    // Dynamically add results to ScrollView
                                    val textView = TextView(requireContext()).apply {
                                        text = "$recipeName ($cuisine)"
                                        textSize = 18f
                                        setTextColor(resources.getColor(android.R.color.white))
                                        setPadding(16, 16, 16, 16)
                                    }
                                    binding.layoutResults.addView(textView)
                                }
                            }
                        }

                        // No results found
                        if (resultList.isEmpty()) {
                            val textView = TextView(requireContext()).apply {
                                text = "No results found for: $query"
                                textSize = 18f
                                setTextColor(resources.getColor(android.R.color.white))
                                setPadding(16, 16, 16, 16)
                            }
                            binding.layoutResults.addView(textView)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        val textView = TextView(requireContext()).apply {
                            text = "Error fetching data: ${databaseError.message}"
                            textSize = 18f
                            setTextColor(resources.getColor(android.R.color.white))
                            setPadding(16, 16, 16, 16)
                        }
                        binding.layoutResults.addView(textView)
                    }
                })
        }

        private fun showFilterDialog() {
            val filterOptions = arrayOf("Italian", "Lebanese", "Mexican","Portuguese")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose a cuisine")
            builder.setItems(filterOptions) { dialog, which ->
                val selectedCuisine = filterOptions[which]
                filterRecipesByCuisine(selectedCuisine)
            }
            builder.show()
        }

        private fun filterRecipesByCuisine(cuisine: String) {
            val database = FirebaseDatabase.getInstance().reference

            // Clear previous results
            binding.layoutResults.removeAllViews()

            database.child("Users").child("cuisines").child(cuisine).child("recipes")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val recipeList = mutableListOf<String>()

                        // Loop through the recipes within the selected cuisine
                        for (recipeSnapshot in dataSnapshot.children) {
                            val recipeName = recipeSnapshot.key
                            recipeList.add(recipeName ?: "")

                            // Dynamically add results to ScrollView
                            val textView = TextView(requireContext()).apply {
                                text = recipeName
                                textSize = 18f
                                setTextColor(resources.getColor(android.R.color.white))
                                setPadding(16, 16, 16, 16)
                            }
                            binding.layoutResults.addView(textView)
                        }

                        // No recipes found for the selected cuisine
                        if (recipeList.isEmpty()) {
                            val textView = TextView(requireContext()).apply {
                                text = "No recipes found for: $cuisine"
                                textSize = 18f
                                setTextColor(resources.getColor(android.R.color.white))
                                setPadding(16, 16, 16, 16)
                            }
                            binding.layoutResults.addView(textView)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        val textView = TextView(requireContext()).apply {
                            text = "Error fetching data: ${databaseError.message}"
                            textSize = 18f
                            setTextColor(resources.getColor(android.R.color.white))
                            setPadding(16, 16, 16, 16)
                        }
                        binding.layoutResults.addView(textView)
                    }
                })
        }
    }
}
