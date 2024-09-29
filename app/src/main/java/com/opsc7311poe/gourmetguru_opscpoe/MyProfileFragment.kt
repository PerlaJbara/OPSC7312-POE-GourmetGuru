package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class MyProfileFragment : Fragment() {

private lateinit var btnOpenSettings: ImageView
private lateinit var txtName: TextView
private lateinit var txtEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        //fetching and displaying user information
        txtName = view.findViewById(R.id.txtUsername)
        txtEmail = view.findViewById(R.id.txtEmail)

        val database = Firebase.database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = database.getReference("Users").child(userId!!)
        if (userId != null) {

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    txtName.text = dataSnapshot.child("name").getValue().toString() + " " + dataSnapshot.child("surname").getValue().toString()
                    txtEmail.text = dataSnapshot.child("email").getValue().toString()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Error reading from the database: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //handling setting button navigation
        btnOpenSettings = view.findViewById(R.id.btnsettings)

        btnOpenSettings.setOnClickListener(){
            replaceFragment(Settings())
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

}