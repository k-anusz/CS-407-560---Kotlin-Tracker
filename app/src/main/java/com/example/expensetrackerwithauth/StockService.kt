package com.example.expensetrackerwithauth

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StockService {

    @GET("/query")
    fun getStock(
                      @Query("function") function: String,
                      @Query("symbol") symbol: String,
                      @Query("apikey") apikey: String) : Call<StockData>
    }
