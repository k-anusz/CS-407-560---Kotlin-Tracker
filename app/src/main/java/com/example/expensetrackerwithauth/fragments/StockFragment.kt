package com.example.expensetrackerwithauth.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithauth.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_stock.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class StockFragment : Fragment() {

    private val TAG = "StockFragment"
    private val API_KEY = "QOE1V1F0XSRUXNZN"
    private val BASE_URL = "https://www.alphavantage.co/"
    private val FUNCTION = "GLOBAL_QUOTE"

    private var stockList = ArrayList<Stock>()
    private var adapter = StockAdapter(stockList)

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    private val db = Firebase.firestore
    private val currentUID = FirebaseAuth.getInstance().currentUser.uid
    private val collectionReference: CollectionReference = db.collection("$currentUID stocks")


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stock_portfolio.adapter = adapter
        stock_portfolio.layoutManager = LinearLayoutManager(this.context)
        val dividerItemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        stock_portfolio.addItemDecoration(dividerItemDecoration)


        //Update stockList with stock from Firebase, but first get the most recent values
        if(stockList.isEmpty()) {
            collectionReference
                .get()
                .addOnSuccessListener { documents ->
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val StockAPI = retrofit.create(StockService::class.java)
                    for (document in documents) {
                        val stock = document.toObject<Stock>()

                        // Using enqueue method allows to make asynchronous call without blocking/freezing main thread
                        StockAPI.getStock("${FUNCTION}", "${stock.symbol}", "$API_KEY")
                            .enqueue(object :
                                Callback<StockData> {

                                override fun onFailure(call: Call<StockData>, t: Throwable) {
                                    Log.d(TAG, "onFailure : $t")

                                    // In case the request fails
                                    collectionReference.document("${stock.symbol}").set(stock)
                                    // Update the adapter with the old data that firebase had saved
                                    stockList.add(stock)
                                    adapter.notifyDataSetChanged()
                                }
                                override fun onResponse(
                                    call: Call<StockData>,
                                    response: Response<StockData>
                                ) {
                                    Log.d(TAG, "onResponse: $response")
                                    val body = response.body()

                                    val stock = Stock(
                                        symbol = body!!.Stock.symbol,
                                        open = body!!.Stock.open,
                                        high = body!!.Stock.high,
                                        low = body!!.Stock.low,
                                        price = body!!.Stock.price,
                                        change = body!!.Stock.change
                                    )
                                    // Add data
                                    collectionReference.document("${body!!.Stock.symbol}").set(stock)
                                    // Update the adapter with the new data
                                    stockList.add(body!!.Stock)
                                    adapter.notifyDataSetChanged()
                                }
                            })
                    }

                }
        }

        deleteIcon = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_baseline_delete_24)!!

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {

                var stock2BeDeleted = stockList.get(viewHolder.adapterPosition)
                collectionReference.document("${stock2BeDeleted.symbol}")
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting document", e)
                    }
                (adapter as StockAdapter).removeItem(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0 ) {
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(
                        itemView.left + iconMargin,
                        itemView.top + iconMargin,
                        itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMargin
                    )
                }
                else {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(
                        itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin)
                }
                swipeBackground.draw(c)
                deleteIcon.draw(c)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(stock_portfolio)
    }

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stock, container, false)


        val symbol_input = view.findViewById<EditText>(R.id.symbol_input)
        val add_stock_button= view.findViewById(R.id.add_stock_button) as Button

        add_stock_button.setOnClickListener() {
            if (symbol_input.text.isEmpty()) {
                val builder = AlertDialog.Builder(this.requireContext())
                builder.setTitle("Stock Ticker Symbol Missing")
                builder.setMessage("Please enter a symbol first and retry.")
                // Set an icon, optional
                builder.setIcon(android.R.drawable.ic_delete)
                // Set the button actions (i.e. listeners), optional
                builder.setPositiveButton("OKAY") { dialog, which ->
                    dialog.dismiss()
                }
                // create the dialog and show it
                val dialog = builder.create()
                dialog.show()

            } else {

                symbol_input.hideKeyboard()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val StockAPI = retrofit.create(StockService::class.java)

                val symbol = symbol_input.text.toString()
                symbol_input.text.clear()

                // Using enqueue method allows to make asynchronous call without blocking/freezing main thread
                StockAPI.getStock("${FUNCTION}", "$symbol", "$API_KEY").enqueue(object :
                    Callback<StockData> {

                    override fun onFailure(call: Call<StockData>, t: Throwable) {
                        Log.d(TAG, "onFailure : $t")
                    }

                    override fun onResponse(call: Call<StockData>, response: Response<StockData>) {
                        Log.d(TAG, "onResponse: $response")

                        val body = response.body()

                        if (body != null) {
                            if (body.Stock.symbol == null) {
                                Log.w(TAG, "Valid response was not received")
                                val builder = AlertDialog.Builder(view.rootView.context)
                                builder.setTitle("Stock Ticker Symbol Does NOT Exist")
                                builder.setMessage("Please enter a valid symbol and retry.")
                                // Set an icon, optional
                                builder.setIcon(android.R.drawable.ic_delete)
                                // Set the button actions (i.e. listeners), optional
                                builder.setPositiveButton("OKAY") { dialog, which ->
                                    dialog.dismiss()
                                }
                                // create the dialog and show it
                                val dialog = builder.create()
                                dialog.show()
                                return
                            }
                        }

                        val stock = Stock(
                            symbol = body!!.Stock.symbol,
                            open = body!!.Stock.open,
                            high = body!!.Stock.high,
                            low = body!!.Stock.low,
                            price = body!!.Stock.price,
                            change = body!!.Stock.change
                        )

                        // Add data
                        collectionReference.document("${body!!.Stock.symbol}").set(stock)

                        // Update the adapter with the new data
                        stockList.add(body!!.Stock)
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }
        return view
    }
}