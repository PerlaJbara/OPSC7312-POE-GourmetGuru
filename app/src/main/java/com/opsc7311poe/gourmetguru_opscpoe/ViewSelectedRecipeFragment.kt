package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Locale

class ViewSelectedRecipeFragment : Fragment() {
    private lateinit var btnBack: ImageView
    private lateinit var txtDuration: TextView
    private lateinit var txtIngredients: TextView
    private lateinit var txtMethods: TextView
    private lateinit var txtRecipeName: TextView

    private lateinit var fetchedRecipe: RecipeData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_selected_recipe, container, false)

        //back button functionality
        btnBack = view.findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            replaceFragment(MyRecipesFragment())
        }

        //fetching and populating information
        txtRecipeName = view.findViewById(R.id.txtRecipeName)
        txtDuration = view.findViewById(R.id.txtDuration)
        txtIngredients = view.findViewById(R.id.txtIngredients)
        txtMethods = view.findViewById(R.id.txtMethods)

        val database = Firebase.database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val recRef = database.getReference("Users").child(userId!!).child("Recipes")
        val recipeID = arguments?.getString("recipeID")
        //fetching service data from DB
        if (userId != null && recipeID != null) {

            // Query the database to find the service with the matching ID
            val query = recRef.child(recipeID)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Directly fetch the service object without looping
                    fetchedRecipe = dataSnapshot.getValue(RecipeData::class.java)!!

                    if (fetchedRecipe != null) {
                        // Assign fetched service info to text views
                        txtRecipeName.text = fetchedRecipe.name
                        txtDuration.text = "${fetchedRecipe.durationHrs} Hours ${fetchedRecipe.durationMins} Minutes"
                        //displaying all ingredients
                        var allIngreString: String = ""
                        for(Ingredient in fetchedRecipe.ingredients!!)
                        {
                            allIngreString += "${Ingredient.amount}             ${Ingredient.name}"
                            allIngreString += "\n"
                        }
                        txtIngredients.text = allIngreString
                        //displaying all steps in method
                        var allStepsString: String = ""
                        for(step in fetchedRecipe.method!!)
                        {
                            allStepsString += step
                            allStepsString += "\n"
                        }

                        txtMethods.text = allStepsString

                    } else {
                        Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Error reading from the database: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("ServicesFragment", "Replacing fragment: ${fragment::class.java.simpleName}")
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}