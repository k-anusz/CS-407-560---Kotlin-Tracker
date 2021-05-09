package com.example.expensetrackerwithauth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensetrackerwithauth.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_money_in.*
import kotlinx.android.synthetic.main.fragment_money_in.recycler_view_money_in
import kotlinx.android.synthetic.main.fragment_money_out.*


class MoneyOutFragment : Fragment() {

    private val db = Firebase.firestore
    private var userAdapter: UserAdapterNegativeTransactionsOnly? = null
    private val currentUID = FirebaseAuth.getInstance().currentUser.uid
    private val collectionReference: CollectionReference = db.collection("$currentUID")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_money_out, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {

        val query: Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<UserTransactions> =
            FirestoreRecyclerOptions.Builder<UserTransactions>()
                .setQuery(query, UserTransactions::class.java)
                .build()

        userAdapter = UserAdapterNegativeTransactionsOnly(firestoreRecyclerOptions)

        recycler_view_money_out.layoutManager = LinearLayoutManager(activity)
        recycler_view_money_out.adapter = userAdapter
    }

    override fun onStart() {
        super.onStart()
        userAdapter!!.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userAdapter!!.stopListening()
    }
}


