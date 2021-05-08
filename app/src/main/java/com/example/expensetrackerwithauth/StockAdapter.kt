package com.example.expensetrackerwithauth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class StockAdapter(private val stocks: ArrayList<Stock>) : RecyclerView.Adapter<StockAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder (itemView){
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file


        var symbol = itemView.findViewById<TextView>(R.id.symbol)
        val open = itemView.findViewById<TextView>(R.id.open)
        val high = itemView.findViewById<TextView>(R.id.high)
        val low = itemView.findViewById<TextView>(R.id.low)
        val price = itemView.findViewById<TextView>(R.id.price)
        val change = itemView.findViewById<TextView>(R.id.change)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val stock = stocks[position]

        holder.symbol.text = "${stock.symbol}"
        holder.open.text = "${stock.open}"
        holder.high.text = "${stock.high}"
        holder.low.text = "${stock.low}"
        holder.price.text = "${stock.price}"
        holder.change.text = "${stock.change}"

    }

    override fun getItemCount(): Int {
        // Return the size of your dataset (invoked by the layout manager)
        return stocks.size
    }
}