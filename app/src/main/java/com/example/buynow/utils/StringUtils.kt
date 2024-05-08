package com.example.buynow.utils

import android.content.Context
import com.example.buynow.data.model.Category
import com.example.buynow.data.model.Courier
import java.io.IOException

class StringUtils {

    companion object {

        val orderStatusList = listOf(
            "Pesanan Sedang Diproses",
            "Pesanan Sedang Dikirim",
            "Pesanan Terkirim",
            "Pesanan Ditolak",
        )

        val voucherMap = mapOf(
            "Z8Y2W1" to 10000,
            "T3X6P9" to 20000,
            "B7R5A2" to 30000,
            "M9V4C1" to 40000,
            "H1K3F7" to 50000,
            "D5N6Q8" to 15000,
            "L2S9E4" to 25000,
            "F4G7J1" to 35000,
            "P6U9I2" to 45000,
            "W8O3T5" to 55000,
        )


        val cateList = listOf(
            Category(
                "Clothing",
                "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=649&q=80"
            ),
            Category(
                "Acoustic Guitar",
                "https://s3-ap-southeast-1.amazonaws.com/media.evaly.com.bd/media/images/eab94ee10b4b-yamaha-f310-acoustic-guitar-wooden-china-1.jpg"
            ),
        )

        val courierList = listOf(
            Courier("Gojek", 14000, "10 - 15 Menit"),
            Courier("Grab", 15000, "7 - 10 Menit"),
            Courier("Maxim", 12000, "9 - 13 Menit")
        )
    }
}