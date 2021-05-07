package com.example.expensetrackerwithauth.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import kotlinx.android.synthetic.main.single_transaction.view.*






/*class ExampleAdapter(private val exampleList: List<UserTransactions>) :
        RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_transaction,
                parent, false)
        return ExampleViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.singleItemAmount.text = currentItem.userBalance.toString()
        holder.singleNoteAmount.text = currentItem.userNote.toString()
        holder.singleTypeAmount.text = currentItem.userType.toString()
    }
    override fun getItemCount() = exampleList.size
    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val singleItemAmount: TextView = itemView.single_item_Amount
        val singleNoteAmount: TextView = itemView.single_note_Amount
        val singleTypeAmount: TextView = itemView.single_type_Amount
    }
}*/






