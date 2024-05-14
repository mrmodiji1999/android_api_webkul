package com.example.a26api

//import com.example.a26api.sampledata.Allproducts
import com.example.a26api.sampledata.Allproducts
import com.example.a26api.sampledata.MyData
import com.example.a26api.sampledata.Products
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("mobikul/getcategorypage")
    fun getProductData(   @Query("p") page: Int,
                          @Query("id_lang") langId: Int,
                          @Query("id_customer") customerId: String?,
                          @Query("id_category") categoryId: Int,
                          @Query("width") width: Int,
                          @Query("id_currency") currencyId: Int,
                          @Query("ws_key") apiKey: String,
                          @Query("outputformat") format: String   ) : Call<MyData>

}