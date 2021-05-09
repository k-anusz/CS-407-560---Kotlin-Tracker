package com.example.expensetrackerwithauth.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetrackerwithauth.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.delete_input_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.income_input_dialog.view.*
import kotlinx.android.synthetic.main.income_input_dialog.view.cancelButtonIncome
import kotlinx.android.synthetic.main.income_input_dialog.view.dialogAmountInput
import kotlinx.android.synthetic.main.income_input_dialog.view.saveButtonIncome


class HomeFragment : Fragment() {

    // expandable button animations
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.to_bottom_anim
        )
    }

    // acts as an on / off switch
    private var clicked = false

    private val db = Firebase.firestore

    var userAdapter: UserAdapter? = null

    val currentUser = FirebaseAuth.getInstance().currentUser.displayName

    private val collectionReference: CollectionReference = db.collection("$currentUser")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        view.deleteTransactionButton.setOnClickListener { view ->
            Toast.makeText(activity, "Delete button Clicked", Toast.LENGTH_SHORT).show()
            showDialogDeleteTransaction()
        }

        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {

        val query: Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<UserTransactions> =
            FirestoreRecyclerOptions.Builder<UserTransactions>()
                .setQuery(query, UserTransactions::class.java)
                .build()

        userAdapter = UserAdapter(firestoreRecyclerOptions)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = userAdapter
    }



    // Delete a contact based on its id
    private fun deleteButton(userEnteredTransactionForDeletion: Int) {

        // get the id from the user
        val usEnteredId = userEnteredTransactionForDeletion.toString()

        if (usEnteredId.isNotEmpty()) {

            // To delete the contact based on id, we first execute a query to get a reference to
            // document to be deleted, then loop over matching documents and finally delete each
            // document based on its reference
            collectionReference
                .whereEqualTo("id", usEnteredId.toInt())
                .get()
                .addOnSuccessListener {documents->

                    for (document in documents) {
                        if (document != null) {
                            //Log.d(TAG, "${document.id} => ${document.data}")
                            // delete the document
                            document.reference.delete()

                            //clearEditTexts()
                            //showToast("Contact has been deleted.")
                            // Assuming there is only one document we want to delete so break the loop
                            break
                        } else {
                            //Log.d(TAG, "No such document")
                        }
                    }
                }
        } else {
            //showToast("Enter an id")
        }
    }


    override fun onStart() {
        super.onStart()
        userAdapter!!.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userAdapter!!.stopListening()
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
            val userTypeInputed = mDialogView.dialogTitleInput.text.toString()
            val userNoteInputed = mDialogView.dialogDescriptionInput.text.toString()
            val userIDInputed = mDialogView.dialogIDInput.text.toString().toInt()

            val currentUser = FirebaseAuth.getInstance().currentUser.displayName

            // Show details
            // view.testingTextView.text = currentUser.email

            // Get an instance of our collection
            val userTransactions = db.collection("$currentUser")

            // Custom class is used to represent your document
            // it is recommended to have a custom class to represent the data
            val transaction = UserTransactions(
                id = userIDInputed,
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
            val userTypeInputedForSubtracted = mDialogView.dialogTitleInput.text.toString()
            val userNoteInputedForSubtracted = mDialogView.dialogDescriptionInput.text.toString()
            val userIDInputed = mDialogView.dialogIDInput.text.toString().toInt()

            // do something with the data
            val currentUser = FirebaseAuth.getInstance().currentUser.displayName

            // Get an instance of our collection
            val userTransactions = db.collection("$currentUser")

            // Custom class is used to represent your document
            // it is recommended to have a custom class to represent the data
            val transaction = UserTransactions(
                id = userIDInputed,
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

    private fun showDialogDeleteTransaction() {
        // inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.delete_input_dialog, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
            .setTitle("Delete a Transaction")
        // show dialog
        val mAlertDialog = mBuilder.show()
        // save button of custom layout
        mDialogView.saveButtonIncome.setOnClickListener {
            // dissmiss dialog
            mAlertDialog.dismiss()
            // get text from EditTexts of custom layout
            val userEnteredTransactionForDeletion = mDialogView.dialogDeleteInput.text.toString().toInt()

            // calls the delete method`
            deleteButton(userEnteredTransactionForDeletion)
        }
        // cancel button
        mDialogView.cancelButtonIncome.setOnClickListener {
            // dismiss dialog
            mAlertDialog.dismiss()
        }
    }


    /**
     * show an alert dialog with data dialog.
     */
    private fun showDialog(title: String, Message: String) {
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
            deleteTransactionButton.visibility = View.VISIBLE
        } else {
            addButton.visibility = View.INVISIBLE
            subtractButton.visibility = View.INVISIBLE
            deleteTransactionButton.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addButton.startAnimation(fromBottom)
            subtractButton.startAnimation(fromBottom)
            deleteTransactionButton.startAnimation(fromBottom)
            fab.startAnimation(rotateOpen)
        } else {
            addButton.startAnimation(toBottom)
            subtractButton.startAnimation(toBottom)
            deleteTransactionButton.startAnimation(toBottom)
            fab.startAnimation(rotateClose)
        }
    }

    // Gets rid of error in which u can click the expanded buttons without them being opened
    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            addButton.isClickable = true
            subtractButton.isClickable = true
            deleteTransactionButton.isClickable = true
        } else {
            addButton.isClickable = false
            subtractButton.isClickable = false
            deleteTransactionButton.isClickable = false
        }
    }
}










