package com.example.a26api

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
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

    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    private var loadingPB: ProgressBar? = null

    private val limit = 10
    private var skip = 0
    private var isLoading = false
    private var isLastPage = false
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        loadingPB = findViewById(R.id.progressBar)

        myAdapter = MyAdapter(this, productList)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adding scroll listener to RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems()
                    }
                } else if (isLastPage && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    Toast.makeText(this@MainActivity, "You have reached the end", Toast.LENGTH_SHORT).show()
                }
            }
        })

        loadMoreItems()
    }

    private fun loadMoreItems() {
        isLoading = true
        loadingPB?.visibility = View.VISIBLE

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getProductData(limit, skip)

        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    productList.addAll(responseBody.products)
                    myAdapter.notifyDataSetChanged()
                    skip += limit
                    isLoading = false
                    loadingPB?.visibility = View.GONE
                    if (responseBody.products.size < limit) {
                        isLastPage = true
                        Toast.makeText(this@MainActivity, "You have reached the end", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    isLoading = false
                    loadingPB?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: " + t.message)
                isLoading = false
                loadingPB?.visibility = View.GONE
            }
        })
    }
}
