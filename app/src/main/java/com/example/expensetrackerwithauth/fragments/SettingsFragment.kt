package com.example.expensetrackerwithauth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.expensetrackerwithauth.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get instance of the FirebaseAuth and then
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Show details
        activity?.let {
            person_name.text = currentUser.displayName
            person_email.text = currentUser.email
            Glide.with(it)
                .load(currentUser.photoUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(person_image)
        }
    }
}






