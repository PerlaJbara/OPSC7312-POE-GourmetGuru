package com.opsc7311poe.gourmetguru_opscpoe

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewCollectionFragment : Fragment() {
    private lateinit var txtName: TextView
    private lateinit var btnAdd: Button
    private lateinit var collectionID: String
    private lateinit var linlayCollectionRecipes: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_collection, container, false)

        // Fetching collection info and displaying
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val database = FirebaseDatabase.getInstance().reference
        collectionID = arguments?.getString("collectionID")!!

        txtName = view.findViewById(R.id.txtCollName)
        linlayCollectionRecipes = view.findViewById(R.id.linlayCollectionRecipes)

        if (userId.isEmpty()) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        } else {
            // Reference to the user's collections in Firebase
            database.child("Users").child(userId).child("Collections").child(collectionID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        txtName.text = dataSnapshot.child("name").value as? String ?: "Unnamed Collection"

                        // Retrieve the recipes as a map and iterate through them
                        val recipeMap = dataSnapshot.child("Recipes").children

                        // Displaying all recipes in collection
                        linlayCollectionRecipes.removeAllViews()

                        recipeMap.forEach { recipeSnapshot ->
                            // Extracting recipe details
                            val recipeID = recipeSnapshot.key ?: ""
                            val recipeName = recipeSnapshot.child("name").value as? String ?: recipeID

                            // Create a TextView for each recipe
                            val recipeNameTextView = TextView(requireContext()).apply {
                                text = recipeName
                                textSize = dpToPx(24f) / requireContext().resources.displayMetrics.density
                                typeface = ResourcesCompat.getFont(requireContext(), R.font.lora)
                                setPadding(25, 18, 16, 16)
                                setTextColor(Color.WHITE)
                                setOnClickListener {
                                    navigateToRecipeDetails(recipeID)
                                }
                            }
                            linlayCollectionRecipes.addView(recipeNameTextView)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load collections: ${databaseError.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        // Handling add recipe button functionality
        btnAdd = view.findViewById(R.id.btnAddRecipe)
        btnAdd.setOnClickListener {
            val addRecToCollFrag = AddRecipeToCollectionFragment()
            val bundle = Bundle()
            bundle.putString("collectionID", collectionID)
            Log.d("AddRecipeToCollection", "Collection ID sent: $collectionID")
            addRecToCollFrag.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(addRecToCollFrag)
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToRecipeDetails(recipeID: String) {
        val viewSelectedFragment = ViewSelectedFragment()
        val bundle = Bundle()
        bundle.putString("recipeID", recipeID)
        viewSelectedFragment.arguments = bundle
        replaceFragment(viewSelectedFragment)
    }

    fun dpToPx(dp: Float): Float {
        val density = requireContext().resources.displayMetrics.density
        return dp * density
    }
}
