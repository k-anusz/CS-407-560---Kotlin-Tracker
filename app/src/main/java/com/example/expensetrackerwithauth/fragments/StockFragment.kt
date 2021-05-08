package com.example.expensetrackerwithauth.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.*
import kotlinx.android.synthetic.main.fragment_stock.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.functions.FunctionN


class StockFragment : Fragment() {

    private val TAG = "StockFragment"
    private val API_KEY = "QOE1V1F0XSRUXNZN"
    private val BASE_URL = "https://www.alphavantage.co/"
    private val FUNCTION = "GLOBAL_QUOTE"

    private var stockList = ArrayList<Stock>()
    private var adapter = StockAdapter(stockList)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stock_portfolio.adapter = adapter
        stock_portfolio.layoutManager = LinearLayoutManager(this.context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stock, container, false)

        //stock_portfolio.adapter = adapter
        //stock_portfolio.layoutManager = LinearLayoutManager(this.context)



        val symbol_input = view.findViewById<EditText>(R.id.symbol_input)
        val add_stock_button= view.findViewById(R.id.add_stock_button) as Button


        add_stock_button.setOnClickListener() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val StockAPI = retrofit.create(StockService::class.java)

            val symbol = symbol_input.text.toString()

            // Using enqueue method allows to make asynchronous call without blocking/freezing main thread
            StockAPI.getStock("${FUNCTION}","$symbol","$API_KEY").enqueue(object :
                Callback<StockData> {

                override fun onFailure(call: Call<StockData>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                }

                override fun onResponse(call: Call<StockData>, response: Response<StockData>) {
                    Log.d(TAG, "onResponse: $response")

                    val body = response.body()

                    if (body == null) {
                        Log.w(TAG, "Valid response was not received")
                        return
                    }

                    // Update the adapter with the new data
                    stockList.add(body.Stock)
                    adapter.notifyDataSetChanged()
                }
            })
        }
        return view
    }
}