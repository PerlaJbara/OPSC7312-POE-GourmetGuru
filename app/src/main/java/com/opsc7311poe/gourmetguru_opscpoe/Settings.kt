package com.opsc7311poe.gourmetguru_opscpoe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class Settings : Fragment() {

    private lateinit var btnLogout: TextView
    private lateinit var btnChangePassword: TextView
    private lateinit var btnDeleteAccount: TextView
    private lateinit var btnChangeLang: TextView
    private lateinit var btnBack: TextView
    private lateinit var auth: FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        auth = FirebaseAuth.getInstance()
        btnLogout = view.findViewById(R.id.txtlogout)

        btnLogout.setOnClickListener(){
                auth.signOut()
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
                activity?.finish()

        }

        //handling change password button
        btnChangePassword = view.findViewById(R.id.txtchangepass)

        btnChangePassword.setOnClickListener(){
            replaceFragment(changePassword())

        }

        //handling delete account button
        btnDeleteAccount = view.findViewById(R.id.txtdelaccount)

        btnDeleteAccount.setOnClickListener(){
            replaceFragment(DeleteAccount())
        }

        //handling change language button



        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("Settings Fragment", "Replacing fragment: ${fragment::class.java.simpleName}")
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}