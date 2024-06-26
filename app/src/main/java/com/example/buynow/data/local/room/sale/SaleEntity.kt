package com.example.buynow.data.local.room.sale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(
    @ColumnInfo(name = "Sale_ID") var sId: String,
    @ColumnInfo(name = "Sale_User_ID") var sUId: String,
    @ColumnInfo(name = "Sale_Date") var date: String,
    @ColumnInfo(name = "Sale_total") var total: Double,
    @ColumnInfo(name = "Sale_Desc") var desc: String,
    @ColumnInfo(name = "Sale_Payment_Type") var payType: String,
    @ColumnInfo(name = "Sale_Courier") var courier: String,
    @ColumnInfo(name = "Sale_Package_Status") var status: String,
    @ColumnInfo(name = "Sale_Discount") var discount: Double,
    @ColumnInfo(name = "Sale_Send_Amount") var sendAmount: Double,
    @ColumnInfo(name = "Sale_Resi") var resi: String,
    @ColumnInfo(name = "Sale_Detail") var detail: String,
    @ColumnInfo(name = "Sale_Pay_name") var payName: String,
    @ColumnInfo(name = "Sale_Paid_At") var paidAt: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}