package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.opsc7311poe.gourmetguru_opscpoe.R

class MealPlan : Fragment() {

    private lateinit var txtMon: TextView
    private lateinit var txtTue: TextView
    private lateinit var txtWed: TextView
    private lateinit var txtThur: TextView
    private lateinit var txtFri: TextView
    private lateinit var txtSat: TextView
    private lateinit var txtSun: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_plan, container, false)


        txtMon = view.findViewById(R.id.txtMon)
        txtTue = view.findViewById(R.id.txtTues)
        txtWed = view.findViewById(R.id.txtWed)
        txtThur = view.findViewById(R.id.txtThur)
        txtFri = view.findViewById(R.id.txtFri)
        txtSat = view.findViewById(R.id.txtSat)
        txtSun = view.findViewById(R.id.txtSun)

        txtMon.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Monday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
        }


        txtTue.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Tuesday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
        }


        txtWed.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Wednesday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
        }


        txtThur.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Thursday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
        }


        txtFri.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Friday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
        }


        txtSat.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Saturday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
        }


        txtSun.setOnClickListener {
            val mealPlanDays = MealPlanDaysFragment()
            val bundle = Bundle()
            bundle.putString("day", "Sunday")
            mealPlanDays.arguments = bundle
            it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            replaceFragment(mealPlanDays)
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
