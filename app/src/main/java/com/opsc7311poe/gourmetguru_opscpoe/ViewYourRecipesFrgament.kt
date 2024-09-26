package com.opsc7311poe.gourmetguru_opscpoe

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ViewYourRecipesFrgament : Fragment() {
    private lateinit var btnBack: ImageView
    private lateinit var svAllRec: ScrollView
    private lateinit var linlayAllRec: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_your_recipes_frgament, container, false)

        //back button functionality
        btnBack = view.findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            replaceFragment(MyRecipesFragment())
        }

        //fetching recipes saved in db and displaying names in textview
        svAllRec = view.findViewById(R.id.svAllRecipes)
        linlayAllRec = view.findViewById(R.id.linlayRecipes)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null)
        {
            val database = Firebase.database

            val recRef = database.getReference("Users").child(userId).child("Recipes")

            recRef.addValueEventListener(object: ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    linlayAllRec.removeAllViews()

                    for(pulledOrder in snapshot.children)
                    {
                        val recName : String? = pulledOrder.child("name").getValue(String::class.java)
                        if (recName != null)
                        {
                            //adding text view with recipe name
                            val textView = TextView(requireContext())

                            textView.text = recName
                            textView.textSize = 20f
                            textView.setTextColor(Color.parseColor("#FFFFFF"))
                            textView.typeface = ResourcesCompat.getFont(requireContext(), R.font.lora)
                            textView.height = 100
                            //when recipe name is tapped the project name is logged and the user is taken to project details page
                            textView.setOnClickListener {
                                val viewSelectedRecipeFrag = ViewSelectedRecipeFragment()
                                //transferring recipe info using a bundle
                                val bundle = Bundle()
                                bundle.putString("recipeID", pulledOrder.key)
                                viewSelectedRecipeFrag.arguments = bundle
                                //changing to recipe info fragment
                                it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                                replaceFragment(viewSelectedRecipeFrag)
                            }

                            linlayAllRec.addView(textView)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error reading from the database: " + error.toString(), Toast.LENGTH_SHORT).show()
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