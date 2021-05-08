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
import com.demo.testassignment.adapter.NewsAdapter
import com.demo.testassignment.adapter.NewsAdapter.OnItemClickListener
import com.demo.testassignment.api.ApiClient
import com.demo.testassignment.api.ApiInterface
import com.demo.testassignment.model.news.Article
import com.demo.testassignment.model.news.News
import com.demo.testassignment.utils.Utils
import kotlinx.android.synthetic.main.error.view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class NewsApiFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    val API_KEY = "a6c1c11ccd4a4d22ab54cbdb9edf371e"
    private lateinit var adapter: NewsAdapter
    private var articles: List<Article> = ArrayList<Article>()
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var root:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_news, container, false)
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
        val BASE_URL = "https://newsapi.org/v2/"

        val apiInterface: ApiInterface? =
            ApiClient.getApiClient(BASE_URL)?.create(ApiInterface::class.java)
        val country: String = Utils.getCountry().toString()
        val call: Call<News?>?
        call = apiInterface?.getNews(country, API_KEY)

        call?.enqueue(object : Callback<News?> {
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
                if (response.isSuccessful() && response.body()?.getArticle() != null) {
                    if (!articles.isEmpty()) {
                        articles = ArrayList<Article>()
                    }
                    articles = response.body()!!.getArticle() as List<Article>
                    adapter = NewsAdapter(articles, activity!!)
                    root.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    initListener()
                    root.topheadelines.setVisibility(View.VISIBLE)
                    root.swipe_refresh_layout.setRefreshing(false)
                } else {
                    root.topheadelines.setVisibility(View.INVISIBLE)
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

            override fun onFailure(call: Call<News?>, t: Throwable) {
                topheadelines.setVisibility(View.INVISIBLE)
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
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(activity, NewsDetailActivity::class.java)
                val article: Article = articles.get(position)
                intent.putExtra("url", article.getUrl())
                intent.putExtra("title", article.getTitle())
                intent.putExtra("img", article.getUrlToImage())
                intent.putExtra("date", article.getPublishedAt())
                intent.putExtra("source", article.getSource()?.getName())
                intent.putExtra("author", article.getAuthor())
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