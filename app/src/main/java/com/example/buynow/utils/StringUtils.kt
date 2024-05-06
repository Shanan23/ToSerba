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
            Category(
                "Jewelry",
                "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1050&q=80"
            ),
            Category(
                "Hair Accessories",
                "https://images.unsplash.com/photo-1626954079979-ec4f7b05e032?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
            ),
            Category(
                "Costume Accessories",
                "https://images.unsplash.com/photo-1606760227091-3dd870d97f1d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
            ),
            Category(
                "Handbag & Wallet Accessories",
                "https://images.unsplash.com/photo-1601924928357-22d3b3abfcfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"
            ),
            Category(
                "One-Pieces",
                "https://images.unsplash.com/photo-1529171374461-2ea966dee0f5?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
            ),
            Category(
                "Masks",
                "https://images.unsplash.com/photo-1586942593568-29361efcd571?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"
            ),
            Category(
                "Glasses",
                "https://images.unsplash.com/photo-1546180245-c59500ad14d0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=675&q=80"
            ),
        )

        val courierList = listOf(
            Courier("Gojek", 14000, "10 - 15 Menit"),
            Courier("Grab", 15000, "7 - 10 Menit"),
            Courier("Maxim", 12000, "9 - 13 Menit")
        )

        fun getJsonData(context: Context, fileName: String): String? {

            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }
}