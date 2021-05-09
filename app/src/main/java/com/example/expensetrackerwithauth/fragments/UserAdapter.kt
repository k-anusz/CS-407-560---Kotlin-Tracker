package com.example.expensetrackerwithauth.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.single_transaction.view.*

class UserAdapter(options: FirestoreRecyclerOptions<UserTransactions>) :
    FirestoreRecyclerAdapter<UserTransactions, UserAdapter.UserAdapterVH>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.single_transaction, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserTransactions) {
        // for positive transactions
        if (model.addedBalance == true) {
            holder.singleItemAmount.text = "+ $" + model.userBalance.toString()
            // will change the background color of the card view to green
            holder.mCardView.setCardBackgroundColor(Color.parseColor("#83c985"))
        }
        // for negative transactions
        if (model.subtractedBalance == true) {
            holder.singleItemAmount.text = "- $" + model.userBalance.toString()
            // will change the background color of the card view to red
            holder.mCardView.setCardBackgroundColor(Color.parseColor("#ef5d5d"))
        }
        holder.singleNoteAmount.text = model.userNote
        holder.singleTypeAmount.text = model.userType
        holder.transactionID.text = "ID: " + model.id.toString()
    }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var singleItemAmount = itemView.single_item_Amount
        var singleNoteAmount = itemView.single_note_Amount
        var singleTypeAmount = itemView.single_type_Amount
        var transactionID = itemView.single_ID
        var mCardView = itemView.cardview
    }
}


