package com.example.expensetrackerwithauth.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.R
import com.firebase.ui.auth.ui.InvisibleActivityBase
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.single_transaction.view.*

class UserAdapterPositiveTransactionsOnly(options: FirestoreRecyclerOptions<UserTransactions>) :
    FirestoreRecyclerAdapter<UserTransactions, UserAdapterPositiveTransactionsOnly.UserAdapterVH>(options) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.single_transaction, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserTransactions) {
        // Display only positive transactions
        if (model.addedBalance == true) {
            holder.singleItemAmount.text = "+ $" + model.userBalance.toString()
            // will change the background color of the card view to green
            holder.mCardView.setCardBackgroundColor(Color.parseColor("#83c985"))

            holder.singleNoteAmount.text = model.userNote
            holder.singleTypeAmount.text = model.userType

        }
        // makes negative transactions invisible
        if (model.subtractedBalance == true) {
            holder.mCardView.isVisible = false
        }
    }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var singleItemAmount = itemView.single_item_Amount
        var singleNoteAmount = itemView.single_note_Amount
        var singleTypeAmount = itemView.single_type_Amount
        var mCardView = itemView.cardview

    }
}


