package com.example.buynow.data.model

import androidx.room.ColumnInfo

data class Item (
    var productId:Int = 0,
    var productUserId:String = "",
    var productName:String = "",
    var productPrice:Int = 0,
    var productStock:Int = 0,
    var productImage:String = "",
    var productDes:String = "",
    var productRating: Float = 0.0F,
    var productDisCount:String = "",
    var productFav:Boolean = false,
    var productBrand:String = "",
    var productCategory:String = "",
    var productNote:String = "",
)