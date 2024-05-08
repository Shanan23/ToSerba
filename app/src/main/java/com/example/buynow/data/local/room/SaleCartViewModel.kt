package com.example.buynow.data.local.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.buynow.data.local.room.cart.ProductDao
import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.sale.SaleDao
import com.example.buynow.data.local.room.sale.SaleEntity
import com.example.buynow.data.model.SaleCart
import com.example.buynow.data.model.SummaryAdmin

class SaleCartViewModel(
    private val productDao: ProductDao,
    private val saleDao: SaleDao
) : ViewModel() {

//    val combinedData: LiveData<Pair<List<ProductEntity>, List<SaleEntity>>> =
//        MediatorLiveData<Pair<List<ProductEntity>, List<SaleEntity>>>().apply {
//            val product = productDao.getProductBySeller(sellerId)
//            val sale = saleDao.getAll()
//            var currentSaleDetails: List<ProductEntity>? = null
//            var currentSale: List<SaleEntity>? = null
//
//            fun update() {
//                val localSale = currentSale
//                val localSaleDetails = currentSaleDetails
//                if (localSale != null && localSaleDetails != null) {
//                    value = Pair(localSaleDetails, localSale)
//                }
//            }
//
//            addSource(sale) { sale ->
//                currentSale = sale
//                update()
//            }
//
//            addSource(product) { details ->
//                currentSaleDetails = details
//                update()
//            }
//        }

    fun getCombinedData(sellerId: String): LiveData<Pair<List<ProductEntity>, List<SaleEntity>>> {
        val product = productDao.getProductBySeller(sellerId)
        val sale = saleDao.getAll()

        return MediatorLiveData<Pair<List<ProductEntity>, List<SaleEntity>>>().apply {
            var currentSaleDetails: List<ProductEntity>? = null
            var currentSale: List<SaleEntity>? = null

            fun update() {
                val localSale = currentSale
                val localSaleDetails = currentSaleDetails
                if (localSale != null && localSaleDetails != null) {
                    value = Pair(localSaleDetails, localSale)
                }
            }

            addSource(sale) { sale ->
                currentSale = sale
                update()
            }

            addSource(product) { details ->
                currentSaleDetails = details
                update()
            }
        }
    }
}