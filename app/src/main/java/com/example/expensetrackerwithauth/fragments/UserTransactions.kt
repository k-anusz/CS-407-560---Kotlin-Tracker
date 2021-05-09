package com.example.expensetrackerwithauth.fragments


data class UserTransactions(
        // id of transaction
        var id: Int? = null,
        // if the transaction is a positive income
        var addedBalance: Boolean? = null,
        // if the transaction is a negative income
        var subtractedBalance: Boolean? = null,
        // transaction amount
        var userTransactionAmount: Int? = null,
        // transaction description
        var userTransactionDescription: String? = null,
        // transaction title
        var userTransactionTitle: String? = null,
)

