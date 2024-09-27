package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.opsc7311poe.gourmetguru_opscpoe.databinding.FragmentViewSelectedBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class ViewSelectedFragment : Fragment() {

    private lateinit var binding: FragmentViewSelectedBinding
    private var recipeName: String? = null
    private var cuisine: String? = null
    private lateinit var btnBack: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentViewSelectedBinding.inflate(inflater, container, false)

        // Get the passed recipe data
        cuisine = arguments?.getString("cuisine")
        recipeName = arguments?.getString("recipeName")

        // Fetch and display recipe details only if cuisine and recipeName are not null
        if (cuisine != null && recipeName != null) {
            loadRecipe(cuisine!!, recipeName!!)
        } else {
            Log.e("ViewSelectedFragment", "Cuisine or Recipe Name is null.")
        }


        btnBack = binding.btnBack // Use binding to get btnBack directly
        btnBack.setOnClickListener {
            // Go back to the previous fragment instead of replacing
            parentFragmentManager.popBackStack()
        }

        return binding.root

    }

    private fun loadRecipe(cuisine: String, recipeName: String) {
        val database = FirebaseDatabase.getInstance().reference

        database.child("cuisines").child(cuisine).child("recipes").child(recipeName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val duration = dataSnapshot.child("Duration").getValue(String::class.java)
                        val imageUrl = dataSnapshot.child("image").getValue(String::class.java)
                        val ingredients = dataSnapshot.child("ingredients").getValue(object : GenericTypeIndicator<List<String>>() {})
                        val steps = dataSnapshot.child("steps").getValue(object : GenericTypeIndicator<List<String>>() {})

                        // Use the retrieved values to display in your UI
                        binding.txtSelRecpName.text = recipeName
                        binding.txtDuration.text = duration

                        // Load image using Glide if needed
                        if (imageUrl != null) {
                            Glide.with(this@ViewSelectedFragment)
                                .load(imageUrl)
                                .into(binding.imgSelRecipeback)
                        }

                        // Display ingredients
                        if (ingredients != null) {
                            binding.tvIngredientsList.text = ingredients.joinToString("\n")
                        }

                        // Display steps
                        if (steps != null) {
                            binding.tvSteps.text = steps.joinToString("\n")
                        }
                    } else {
                        // Handle the case where the recipe doesn't exist
                        Log.e("ViewSelectedFragment", "Recipe not found.")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("ViewSelectedFragment", "Database error: ${databaseError.message}")
                }
            })
    }


}
