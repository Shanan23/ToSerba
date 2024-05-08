package com.example.buynow.data.model

import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.sale.SaleEntity

data class SaleCart(
    val productEntity: ProductEntity,
    val saleEntity: SaleEntity
)