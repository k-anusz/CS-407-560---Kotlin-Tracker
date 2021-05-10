package com.example.expensetrackerwithauth

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

data class StockData(
    @SerializedName("Global Quote") val Stock: Stock
)
@Keep
@IgnoreExtraProperties
data class Stock(
    @SerializedName("01. symbol")           val symbol: String? = null,
    @SerializedName("02. open")             val open: Double? = null,
    @SerializedName("03. high")             val high: Double? = null,
    @SerializedName("04. low")              val low: Double? = null,
    @SerializedName("05. price")            val price: Double? = null,
    @SerializedName("10. change percent")   val change: String? = null
)