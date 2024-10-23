package com.opsc7311poe.gourmetguru_opscpoe

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.graphics.Color
import android.view.HapticFeedbackConstants
import androidx.core.content.res.ResourcesCompat

class ViewCollectionsFragment : Fragment() {

    private lateinit var colLinLay: LinearLayout
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_collections, container, false)

        // Initialize views
        colLinLay = view.findViewById(R.id.colLinLay)
        btnBack = view.findViewById(R.id.btnBack)

        btnBack.setOnClickListener(){
        replaceFragment(MyRecipesFragment())
        }

        // Initialize Firebase references
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().reference

        // Load collections
        loadCollections()

        return view
    }

    private fun replaceFragment(fragment: Fragment) {

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun loadCollections() {
        if (userId.isEmpty()) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Reference to the user's collections in Firebase
        database.child("Users").child(userId).child("Collections")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    colLinLay.removeAllViews() // Clear previous views

                    for (collectionSnapshot in dataSnapshot.children) {
                        val collectionName = collectionSnapshot.child("name").value as String

                        // Create a TextView for each collection
                        val collectionTextView = TextView(requireContext()).apply {
                            text = collectionName
                            textSize = dpToPx(24f) / requireContext().resources.displayMetrics.density
                            typeface = ResourcesCompat.getFont(requireContext(), R.font.lora)
                            setPadding(25, 18, 16, 16)
                            setTextColor(Color.WHITE)
                        }

                        // Set click listener for saving a recipe or viewing the collection
                        collectionTextView.setOnClickListener {
                            val selectedCollectionID = collectionSnapshot.key ?: ""
                            val recipeID = arguments?.getString("recipeID") // Get the passed recipeID

                            if (selectedCollectionID.isNotEmpty()) {
                                if (recipeID != null) {
                                    // Save the recipe to the collection
                                    saveRecipeToCollection(recipeID, selectedCollectionID)
                                } else {
                                    // No recipe to save, just view the collection
                                    openCollection(selectedCollectionID)
                                }
                            } else {
                                Toast.makeText(requireContext(), "Collection ID is missing", Toast.LENGTH_SHORT).show()
                            }
                        }

                        // Add the TextView to the LinearLayout
                        colLinLay.addView(collectionTextView)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to load collections: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveRecipeToCollection(recipeID: String, collectionID: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Reference to the user's selected collection in Firebase
        val collectionRef = database.child("Users").child(userId).child("Collections").child(collectionID).child("Recipes")

        // Add the recipe to the collection
        collectionRef.child(recipeID).setValue(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Recipe saved to collection", Toast.LENGTH_SHORT).show()
                // Navigate to the selected collection to view it
                openCollection(collectionID)
            } else {
                Toast.makeText(requireContext(), "Failed to save recipe: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCollection(collectionID: String) {
        val viewSelectedCollFrag = ViewCollectionFragment()
        val bundle = Bundle()
        bundle.putString("collectionID", collectionID)
        viewSelectedCollFrag.arguments = bundle
        replaceFragment(viewSelectedCollFrag)
    }




    ///TESTING































    fun dpToPx(dp: Float): Float {
        val density = requireContext().resources.displayMetrics.density
        return dp * density
    }
}
