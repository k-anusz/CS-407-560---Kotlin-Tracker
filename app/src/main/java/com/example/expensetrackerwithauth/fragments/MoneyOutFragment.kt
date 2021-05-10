package com.example.expensetrackerwithauth.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensetrackerwithauth.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_money_in.*
import kotlinx.android.synthetic.main.fragment_money_out.*


class MoneyOutFragment : Fragment() {

    private val TAG = "MoneyOutFragment"

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

        var outcome_transactions = ArrayList<String>()
        collectionReference
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var outcome = document.toObject<UserTransactions>()
                    Log.d(TAG, "onFailure : ${outcome.userTransactionTitle}")
                    outcome_transactions.add(outcome.userTransactionTitle!!)
                }
            }
        val aaChartView = view.findViewById<AAChartView>(R.id.aa_chart_view2)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("Types of Expenses")
            .backgroundColor("#ffffff")
            .dataLabelsEnabled(false)
            .legendEnabled(false)
            .series(arrayOf(
                AASeriesElement()
                    .name("Utilities")
                    .data(arrayOf(5)),
                AASeriesElement()
                    .name("Groceries")
                    .data(arrayOf(10)),
                ),
            )

        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        aaChartView.aa_drawChartWithChartModel(aaChartModel)

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


