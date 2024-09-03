package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class HomeFragment : Fragment() {

    private lateinit var btnLeb: ImageView
    private lateinit var btnItalian: ImageView
    private lateinit var btnMexican: ImageView
    private lateinit var btnPortu: ImageView
    private lateinit var btnFrench: ImageView
    private lateinit var btnJapan: ImageView
    private lateinit var btnSA: ImageView
    private lateinit var btnIndian: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLeb = view.findViewById(R.id.btnLebaneseCuisine)
        btnItalian = view.findViewById(R.id.btnItalianCuisine)
        btnMexican = view.findViewById(R.id.btnMexicanCuisine)
        btnPortu = view.findViewById(R.id.btnPortCuisine)
        btnFrench = view.findViewById(R.id.btnFrenchCuisine)
        btnJapan = view.findViewById(R.id.btnJapaneseCuisine)
        btnSA = view.findViewById(R.id.btnSACuisine)
        btnIndian = view.findViewById(R.id.btnIndianCuisine)

        btnLeb.setOnClickListener() {
            replaceFragment(LebaneseFragment())
        }

        btnItalian.setOnClickListener(){
            replaceFragment(ItalianFragment())
        }

        btnMexican.setOnClickListener(){
            replaceFragment(MexicanFragment())
        }

        btnPortu.setOnClickListener(){
            replaceFragment(PortugueseFragment())
        }

        btnFrench.setOnClickListener(){
            replaceFragment(FrenchFragment())
        }

        btnJapan.setOnClickListener(){
            replaceFragment(JapaneseFragment())
        }

        btnSA.setOnClickListener(){
            replaceFragment(SouthafricanFragment())
        }

        btnIndian.setOnClickListener(){
            replaceFragment(IndianFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
