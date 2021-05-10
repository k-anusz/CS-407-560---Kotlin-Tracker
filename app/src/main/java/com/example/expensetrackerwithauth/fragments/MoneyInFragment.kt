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
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_money_in.*


class MoneyInFragment : Fragment() {

    val TAG = "MoneyInFragment"
    private val db = Firebase.firestore
    private var userAdapter: UserAdapterPositiveTransactionsOnly? = null
    private val currentUID = FirebaseAuth.getInstance().currentUser.uid
    private val collectionReference: CollectionReference = db.collection("$currentUID")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_money_in, container, false)

//        var income_transactions = ArrayList<Int>()
//        collectionReference
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    var income = document.toObject<UserTransactions>()
//                    Log.d(TAG, "onFailure : ${income.userTransactionAmount}")
//                    income_transactions.add(income.userTransactionAmount!!)
//                }
//            }
        val aaChartView = view.findViewById<AAChartView>(R.id.aa_chart_view)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("Income Vs Outcome")
            .backgroundColor("#ffffff")
            .dataLabelsEnabled(false)
            .legendEnabled(false)
            .series(arrayOf(
                AASeriesElement()
                    .name("Income")
                    .data(arrayOf(10000, 5000, 1000 )),

            )
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

        userAdapter = UserAdapterPositiveTransactionsOnly(firestoreRecyclerOptions)

        recycler_view_money_in.layoutManager = LinearLayoutManager(activity)
        recycler_view_money_in.adapter = userAdapter
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




