package com.example.expensetrackerwithauth

import com.google.gson.annotations.SerializedName

data class StockData(
    @SerializedName("Global Quote") val Stock: Stock
)

data class Stock(
    @SerializedName("01. symbol")           val symbol: String,
    @SerializedName("02. open")             val open: Double,
    @SerializedName("03. high")             val high: Double,
    @SerializedName("04. low")              val low: Double,
    @SerializedName("05. price")            val price: Double,
    @SerializedName("10. change percent")   val change: String
)