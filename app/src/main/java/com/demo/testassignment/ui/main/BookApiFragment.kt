package com.demo.testassignment.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demo.testassignment.R
import com.demo.testassignment.activity.NewsDetailActivity
import com.demo.testassignment.adapter.BookAdapter
import com.demo.testassignment.api.ApiClient
import com.demo.testassignment.api.ApiInterface
import com.demo.testassignment.api.GetRetrofit.instance
import com.demo.testassignment.api.GetRetrofit.updateBaseUrl
import com.demo.testassignment.model.book.BookItem
import com.demo.testassignment.model.book.Item
import kotlinx.android.synthetic.main.error.view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class BookApiFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: BookAdapter
    private var item: List<Item> = ArrayList<Item>()
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var root:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_book, container, false)
        init()
        return root
    }
    fun init(){
        setAdapter()
        onLoadingSwipeRefresh()

    }
    fun setAdapter(){
        viewManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        ) as RecyclerView.LayoutManager
        root.recyclerView.layoutManager = viewManager

        root.recyclerView.setItemAnimator(DefaultItemAnimator())
        root.recyclerView.setNestedScrollingEnabled(false)
    }

    private fun onLoadingSwipeRefresh() {
        root.swipe_refresh_layout.post(
            Runnable { LoadJson() }
        )
    }

    fun LoadJson() {
        root.errorLayout.setVisibility(View.GONE)
        root.swipe_refresh_layout.setRefreshing(true)
        val BASE_URL = "https://www.googleapis.com/"

        updateBaseUrl(BASE_URL)
        val retrofit = instance // this provides new retrofit instance with given url


        val apiInterface: ApiInterface? =
            retrofit?.create(ApiInterface::class.java)
        val bookName = "harry potter"
        val call: Call<BookItem?>? = apiInterface?.getBook(bookName)

        call?.enqueue(object : Callback<BookItem?> {
            override fun onResponse(call: Call<BookItem?>, response: Response<BookItem?>) {
                if (response.isSuccessful() && response.body()?.items != null) {
                    if (!item.isEmpty()) {
                        item = ArrayList<Item>()
                    }
                    item = response.body()!!.items as List<Item>
                    adapter = BookAdapter(item, activity!!)
                    root.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    initListener()
                    root.swipe_refresh_layout.setRefreshing(false)
                } else {
                    root.swipe_refresh_layout.setRefreshing(false)
                    val errorCode: String
                    errorCode = when (response.code()) {
                        404 -> "404 not found"
                        500 -> "500 server broken"
                        else -> "unknown error"
                    }
                    showErrorMessage(
                        R.drawable.no_result,
                        "No Result",
                        """
                        Please Try Again!
                        $errorCode
                        """.trimIndent()
                    )
                }
            }

            override fun onFailure(call: Call<BookItem?>, t: Throwable) {
                swipe_refresh_layout.setRefreshing(false)
                showErrorMessage(
                    R.drawable.oops,
                    "Oops..",
                    """
                    Network failure, Please Try Again
                    $t
                    """.trimIndent()
                )
            }
        })
    }


    private fun initListener() {
        adapter.setOnItemClickListener(object : BookAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {

                val intent = Intent(activity, NewsDetailActivity::class.java)
                val model: Item = item.get(position)

                intent.putExtra("url", model.volumeInfo?.infoLink)
                intent.putExtra("title", model.volumeInfo?.title)
                intent.putExtra("img", model.volumeInfo?.imageLinks?.thumbnail)
                intent.putExtra("date", model.volumeInfo?.publishedDate)
                intent.putExtra("source", model.volumeInfo?.publisher?.get(0))
                intent.putExtra("author", model.volumeInfo?.authors?.get(0))
                intent.putExtra("tag", "BookApiFragment")
                startActivity(intent)

            }
        })
    }

    override fun onRefresh() {
        LoadJson()
    }



    private fun showErrorMessage(imageView: Int, title: String, message: String) {
        if (root.errorLayout.visibility == View.GONE) {
            root.errorLayout.visibility = View.VISIBLE
        }
        root.errorImage.setImageResource(imageView)
        root.errorTitle.text = title
        root.errorMessage.text = message
        root.btnRetry.setOnClickListener { onLoadingSwipeRefresh() }
    }

}