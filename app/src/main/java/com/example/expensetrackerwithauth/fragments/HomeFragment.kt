package com.example.expensetrackerwithauth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.expensetrackerwithauth.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    // expandable button animations
    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim ) }
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim ) }
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim ) }
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim ) }

    // acts as an on / off switch
    private var clicked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        /*
            Expandable Action button code
        */
        view.fab.setOnClickListener { view ->
            onAddButtonClicked()
        }
        view.addButton.setOnClickListener { view ->
            Toast.makeText(activity,"Add button Clicked", Toast.LENGTH_SHORT).show()
        }
        view.subtractButton.setOnClickListener { view ->
            Toast.makeText(activity,"Subtract button Clicked", Toast.LENGTH_SHORT).show()
        }









        return view
    }


    /*
        Expandable Action button code
    */

    // Expandable action button animation functions called
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            addButton.visibility = View.VISIBLE
            subtractButton.visibility = View.VISIBLE
        }
        else{
            addButton.visibility = View.INVISIBLE
            subtractButton.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            addButton.startAnimation(fromBottom)
            subtractButton.startAnimation(fromBottom)
            fab.startAnimation(rotateOpen)
        }
        else{
            addButton.startAnimation(toBottom)
            subtractButton.startAnimation(toBottom)
            fab.startAnimation(rotateClose)
        }
    }

    // Gets rid of error in which u can click the expanded buttons without them being opened
    private fun setClickable(clicked:Boolean){
        if(!clicked){
            addButton.isClickable = true
            subtractButton.isClickable = true
        }
        else{
            addButton.isClickable = false
            subtractButton.isClickable = false
        }
    }
}










