package com.example.expensetrackerwithauth.fragments

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.income_input_dialog.view.*


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
            showDialogIncome()
        }
        view.subtractButton.setOnClickListener { view ->
            Toast.makeText(activity,"Subtract button Clicked", Toast.LENGTH_SHORT).show()
            showDialogSubtract()
        }


        return view
    }


    private fun showDialogIncome(){
        // inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.income_input_dialog, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
            .setTitle("Add Income")
        // show dialog
        val mAlertDialog = mBuilder.show()
        // save button of custom layout
        mDialogView.saveButtonIncome.setOnClickListener{
            // dissmiss dialog
            mAlertDialog.dismiss()
            // get text from EditTexts of custom layout
            val userIncomeInputed = mDialogView.dialogAmountInput.text.toString()
            val userTypeInputed = mDialogView.dialogTypeInput.text.toString()
            val userNoteInputed = mDialogView.dialogNoteInput.text.toString()


            // do something with the data
        }
        // cancel button
        mDialogView.cancelButtonIncome.setOnClickListener{
            // dismiss dialog
            mAlertDialog.dismiss()
        }
    }


    private fun showDialogSubtract() {
        // inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.income_input_dialog, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
            .setTitle("Subtract Income")
        // show dialog
        val mAlertDialog = mBuilder.show()
        // save button of custom layout
        mDialogView.saveButtonIncome.setOnClickListener{
            // dissmiss dialog
            mAlertDialog.dismiss()
            // get text from EditTexts of custom layout
            val userIncomeForSubtracted = mDialogView.dialogAmountInput.text.toString()
            val userTypeInputedForSubtracted = mDialogView.dialogTypeInput.text.toString()
            val userNoteInputedForSubtracted = mDialogView.dialogNoteInput.text.toString()

            // do something with the data
        }
        // cancel button
        mDialogView.cancelButtonIncome.setOnClickListener{
            // dismiss dialog
            mAlertDialog.dismiss()
        }
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










