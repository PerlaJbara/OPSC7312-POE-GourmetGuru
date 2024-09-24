package com.opsc7311poe.gourmetguru_opscpoe

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
                        val collectionName = collectionSnapshot.value as String

                        // Create a TextView for each collection
                        val collectionTextView = TextView(requireContext()).apply {
                            text = collectionName
                            textSize = 20f // Adjust size as needed
                            setPadding(16, 16, 16, 16) // Adjust padding as needed
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
}
