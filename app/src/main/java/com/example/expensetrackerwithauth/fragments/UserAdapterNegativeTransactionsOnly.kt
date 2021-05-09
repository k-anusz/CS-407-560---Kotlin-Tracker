package com.example.expensetrackerwithauth.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.single_transaction.view.*

// User adapter which displays only negative transactions on the "Money Out" Page
class UserAdapterNegativeTransactionsOnly(options: FirestoreRecyclerOptions<UserTransactions>) :
    FirestoreRecyclerAdapter<UserTransactions, UserAdapterNegativeTransactionsOnly.UserAdapterVH>(
        options
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.single_transaction, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserTransactions) {
        // makes Positive transactions invisible
        if (model.addedBalance == true) {
            holder.mCardView.isVisible = false
        }
        // Display only negative transactions
        if (model.subtractedBalance == true) {
            holder.singleItemAmount.text = "- $" + model.userTransactionAmount.toString()
            // will change the background color of the card view to red
            holder.mCardView.setCardBackgroundColor(Color.parseColor("#ef5d5d"))
            holder.singleDescription.text = model.userTransactionDescription
            holder.singleTitle.text = model.userTransactionTitle
            holder.transactionID.text = "ID: " + model.id.toString()
        }
    }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var singleItemAmount = itemView.single_item_Amount
        var singleDescription = itemView.single_description
        var singleTitle = itemView.single_title
        var transactionID = itemView.single_ID
        var mCardView = itemView.cardview
    }
}


