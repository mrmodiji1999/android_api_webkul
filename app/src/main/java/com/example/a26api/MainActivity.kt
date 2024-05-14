package com.example.a26api

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a26api.sampledata.Allproducts
//import com.example.a26api.sampledata.Allproducts
import com.example.a26api.sampledata.MyData
import com.example.a26api.sampledata.Products
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://test.hijab-deutschland.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getProductData(

            1,
            1,
            "", // Pass customer ID if available
            32,
            392,
            1,
            "BKBGZ6IEC4FEME5ZTBEZS5SSQC4VSX9X",
            "json"
        )



        retrofitData.enqueue(object : Callback<MyData?> {

            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                // if api call is a success, then use the data of API and show in your app
                var responseBody = response.body()
                val productList = responseBody?.products?.allproducts?.product!!

                myAdapter = MyAdapter(this@MainActivity, productList)
                recyclerView.adapter = myAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                // if api call fails
                Log.d("Main Activity ", "onFailure: " + t.message)
            }
        })



    }
}