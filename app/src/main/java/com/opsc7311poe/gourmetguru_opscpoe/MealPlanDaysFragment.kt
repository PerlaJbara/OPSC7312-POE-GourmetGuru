package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.opsc7311poe.gourmetguru_opscpoe.databinding.FragmentMealPlanDaysBinding
import androidx.navigation.NavDirections

class MealPlanDaysFragment : Fragment() {

private lateinit var txtDayOfWeek: TextView
private lateinit var txtBreakfast: TextView
private lateinit var txtLunch: TextView
private lateinit var txtDinner: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meal_plan_days, container, false)

        txtDayOfWeek = view.findViewById(R.id.txtMainDay)
        //getting the day from the argument that was passed from prev frag
        val day = arguments?.getString("day") ?: ""
        // Set the text of the TextView
        txtDayOfWeek.text = day


        txtBreakfast = view.findViewById(R.id.txtBreak)
        txtLunch = view.findViewById(R.id.txtLunch)
        txtDinner = view.findViewById(R.id.txtDinner)

        txtBreakfast.setOnClickListener(){
            replaceFragment(SearchFragment())
            Toast.makeText(requireContext(), "Please select your breakfast!", Toast.LENGTH_SHORT).show()
        }

        txtLunch.setOnClickListener(){
            replaceFragment(SearchFragment())
            Toast.makeText(requireContext(), "Please select your lunch!", Toast.LENGTH_SHORT).show()
        }

        txtDinner.setOnClickListener(){
            replaceFragment(SearchFragment())
            Toast.makeText(requireContext(), "Please select your dinner!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
