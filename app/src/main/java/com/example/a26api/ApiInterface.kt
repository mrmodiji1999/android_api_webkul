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
    fun getProductData(   ) : Call<MyData>

}