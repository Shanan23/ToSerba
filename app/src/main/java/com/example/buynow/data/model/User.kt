package com.example.buynow.data.model

data class User(
    var userName: String = "",
    var userImage: String = "",
    var userUid: String = "",
    var userEmail: String = "",
    var userAddress: String = "",
    var userPhone: String = "",
    var userRole: String = "",
    var userCountProduct: Int = 0
)