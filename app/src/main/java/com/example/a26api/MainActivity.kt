package com.example.a26api

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a26api.sampledata.MyData
import com.example.a26api.sampledata.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var progressLayout: FrameLayout
    private var currentPage = 1
    private var itemCountLoaded = 0
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressLayout = findViewById(R.id.progressLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this, mutableListOf())
        recyclerView.adapter = myAdapter

        // Set up pagination listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (!isLoading && totalItemCount <= lastVisibleItemPosition + 5) {
                    // Load more data if the user is close to the end of the list
                    if (itemCountLoaded >= 10) {
                        loadData(++currentPage)
                    }
                }
            }
        })

        // Load initial data
        loadData(currentPage)
    }

    private fun loadData(page: Int) {
        isLoading = true
        progressLayout.visibility = View.VISIBLE // Show progress bar

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getProductData(

        )

        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val productList = responseBody?.products?.allproducts?.product ?: emptyList()
                    itemCountLoaded += productList.size
                    myAdapter.addData(productList)
                } else {
                    Log.e("MainActivity", "Failed to fetch data: ${response.code()}")
                }
                isLoading = false
                progressLayout.visibility = View.GONE // Hide progress bar after loading
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch data: ${t.message}")
                isLoading = false
                progressLayout.visibility = View.GONE // Hide progress bar after loading
            }
        })
    }
}
