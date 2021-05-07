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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.income_input_dialog.view.*
import kotlinx.android.synthetic.main.single_transaction.*


class HomeFragment : Fragment() {

    // expandable button animations
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }

    // acts as an on / off switch
    private var clicked = false

    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        // Show the data on the screen
        /*db.collection("users")
                .get()*/


        // val currentUser = FirebaseAuth.getInstance().currentUser

        // Show details
        // view.testingTextView.text = currentUser.email
        //view?.testingTextView?.text = name

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateAllBalances()

        /*
            Expandable Action button code
        */
        view.fab.setOnClickListener { view ->
            onAddButtonClicked()
        }
        view.addButton.setOnClickListener { view ->
            Toast.makeText(activity, "Add button Clicked", Toast.LENGTH_SHORT).show()
            showDialogIncome()
        }
        view.subtractButton.setOnClickListener { view ->
            Toast.makeText(activity, "Subtract button Clicked", Toast.LENGTH_SHORT).show()
            showDialogSubtract()
        }
        view.viewAllButton.setOnClickListener { view ->
            viewAllDataButtonWithCustomClass()
            updateAllBalances()
        }
    }


    private fun showDialogIncome() {
        // inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.income_input_dialog, null)

        val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Add Income")
        // show dialog
        val mAlertDialog = mBuilder.show()
        // save button of custom layout
        mDialogView.saveButtonIncome.setOnClickListener {
            // dissmiss dialog
            mAlertDialog.dismiss()
            // get text from EditTexts of custom layout
            val userIncomeInputed = mDialogView.dialogAmountInput.text.toString().toInt()
            val userTypeInputed = mDialogView.dialogTypeInput.text.toString()
            val userNoteInputed = mDialogView.dialogNoteInput.text.toString()

            val currentUser = FirebaseAuth.getInstance().currentUser.displayName

            // Show details
            // view.testingTextView.text = currentUser.email

            // Get an instance of our collection
            val userTransactions = db.collection("$currentUser")

            // Custom class is used to represent your document
            // it is recommended to have a custom class to represent the data
            val transaction = UserTransactions(
                id = 1,
                addedBalance = true,
                subtractedBalance = false,
                userBalance = userIncomeInputed,
                userNote = userNoteInputed,
                userType = userTypeInputed,
            )

            // Get an auto generated id for a document that you want to insert
            val id = userTransactions.document().id

            // Add data
            userTransactions.document(id).set(transaction)
            updateAllBalances()
        }
        // cancel button
        mDialogView.cancelButtonIncome.setOnClickListener {
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
        mDialogView.saveButtonIncome.setOnClickListener {
            // dissmiss dialog
            mAlertDialog.dismiss()
            // get text from EditTexts of custom layout
            val userIncomeForSubtracted = mDialogView.dialogAmountInput.text.toString().toInt()
            val userTypeInputedForSubtracted = mDialogView.dialogTypeInput.text.toString()
            val userNoteInputedForSubtracted = mDialogView.dialogNoteInput.text.toString()

            // do something with the data
            val currentUser = FirebaseAuth.getInstance().currentUser.displayName

            // Get an instance of our collection
            val userTransactions = db.collection("$currentUser")

            // Custom class is used to represent your document
            // it is recommended to have a custom class to represent the data
            val transaction = UserTransactions(
                id = 1,
                addedBalance = false,
                subtractedBalance = true,
                userBalance = userIncomeForSubtracted,
                userNote = userNoteInputedForSubtracted,
                userType = userTypeInputedForSubtracted,
            )

            // Get an auto generated id for a document that you want to insert
            val id = userTransactions.document().id

            // Add data
            userTransactions.document(id).set(transaction)
            updateAllBalances()
        }
        // cancel button
        mDialogView.cancelButtonIncome.setOnClickListener {
            // dismiss dialog
            mAlertDialog.dismiss()
        }
    }


    // Alternative-2 --> Uses custom objects (i.e., Contact data class)
    // Read all the records from the database
    private fun viewAllDataButtonWithCustomClass() {

        val currentUser = FirebaseAuth.getInstance().currentUser.displayName


        // Get data using addOnSuccessListener
        db.collection("$currentUser")
            .orderBy("id")
            .get()
            .addOnSuccessListener { documents ->

                val buffer = StringBuffer()

                // Turn your document(s) to Contact object
                val userTransactionsObject = documents.toObjects<UserTransactions>()

                for (userTransactions in userTransactionsObject) {

                    //Log.d(TAG, "contact: ${contact}")

                    // Create a string buffer (i.e., concatenate all the fields into one string)
                    buffer.append("ID : ${userTransactions.id}" + "\n")
                    buffer.append("added Balance : ${userTransactions.addedBalance}" + "\n")
                    buffer.append("subtracted Balance :  ${userTransactions.subtractedBalance}" + "\n")
                    buffer.append("user Balance : ${userTransactions.userBalance}" + "\n")
                    buffer.append("user Note :  ${userTransactions.userNote}" + "\n")
                    buffer.append("user Type :  ${userTransactions.userType}" + "\n\n")
                }

                // show all the records as a string in a dialog
                showDialog("Data Listing", buffer.toString())
            }
            .addOnFailureListener {
                //Log.d(TAG, "Error getting documents")
                //showDialog("Error", "Error getting documents")
            }
    }

    /**
     * show an alert dialog with data dialog.
     */
    private fun showDialog(title : String,Message : String){
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }

    private fun updateAllBalances() {
        val currentUser = FirebaseAuth.getInstance().currentUser.displayName

        // Get data using addOnSuccessListener
        db.collection("$currentUser")
            .orderBy("id")
            .get()
            .addOnSuccessListener { documents ->

                // Turn your document(s) to UserTransactions object
                val userTransactionsObject = documents.toObjects<UserTransactions>()

                var usersTotalAddedBalance = 0
                var usersTotalSpentBalance = 0
                var usersTotalBalance = 0

                for (userTransactions in userTransactionsObject) {
                    if (userTransactions.addedBalance == true) {
                        usersTotalAddedBalance += userTransactions.userBalance!!
                    }
                    if (userTransactions.subtractedBalance == true) {
                        usersTotalSpentBalance += userTransactions.userBalance!!
                    }

                }
                // Find out the total balance
                usersTotalBalance = usersTotalAddedBalance - usersTotalSpentBalance

                totalEarnedTextView.text = "Earned: $ $usersTotalAddedBalance"
                totalSpentTextView.text = "Spent: $ $usersTotalSpentBalance"
                balanceTextView.text = "Balance: $ $usersTotalBalance"
            }
            .addOnFailureListener {
                //Log.d(TAG, "Error getting documents")
                showDialog("Error", "Error getting documents")
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
        if (!clicked) {
            addButton.visibility = View.VISIBLE
            subtractButton.visibility = View.VISIBLE
        } else {
            addButton.visibility = View.INVISIBLE
            subtractButton.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addButton.startAnimation(fromBottom)
            subtractButton.startAnimation(fromBottom)
            fab.startAnimation(rotateOpen)
        } else {
            addButton.startAnimation(toBottom)
            subtractButton.startAnimation(toBottom)
            fab.startAnimation(rotateClose)
        }
    }

    // Gets rid of error in which u can click the expanded buttons without them being opened
    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            addButton.isClickable = true
            subtractButton.isClickable = true
        } else {
            addButton.isClickable = false
            subtractButton.isClickable = false
        }
    }
}










