package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.opsc7311poe.gourmetguru_opscpoe.databinding.FragmentMealPlanDaysBinding
import androidx.navigation.NavDirections

class MealPlanDaysFragment : Fragment() {

private lateinit var txtDayOfWeek: TextView

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

        return view
    }

}
