package com.example.expensetrackerwithauth

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView


class StockAdapter(private val stocks: ArrayList<Stock>) : RecyclerView.Adapter<StockAdapter.MyViewHolder>() {

    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {

        var symbol = itemView.findViewById<TextView>(R.id.symbol)
        val open = itemView.findViewById<TextView>(R.id.open)
        val high = itemView.findViewById<TextView>(R.id.high)
        val low = itemView.findViewById<TextView>(R.id.low)
        val price = itemView.findViewById<TextView>(R.id.price)
        val change = itemView.findViewById<TextView>(R.id.change)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val stock = stocks[position]

        holder.symbol.text = "${stock.symbol}"
        holder.open.text = "${number2digits(stock.open!!)}"
        holder.high.text =  "${number2digits(stock.high!!)}"
        holder.low.text =  "${number2digits(stock.low!!)}"
        holder.price.text =  "${number2digits(stock.price!!)}"
        holder.change.text = "${stock.change}"
        if (stock.change!!.contains('-'))
        {
            holder.change.setTextColor(Color.parseColor(("#FF0000")))
        }
        else {
            holder.change.setTextColor(Color.parseColor(("#00ff00")))
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        stocks.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }


    override fun getItemCount(): Int {
        return stocks.size
    }

    fun number2digits(number: Double): Double {
        val number2digits: Double = String.format("%.2f", number).toDouble()

        return number2digits
    }
}