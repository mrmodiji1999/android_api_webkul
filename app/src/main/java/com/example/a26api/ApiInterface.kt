package com.example.a26api

//import com.example.a26api.sampledata.Allproducts
import com.example.a26api.sampledata.MyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("products")
    fun getProductData(@Query("limit") limit: Int, @Query("skip") skip: Int): Call<MyData>


}