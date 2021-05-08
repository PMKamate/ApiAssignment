package com.demo.testassignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.demo.testassignment.R
import com.demo.testassignment.model.news.Article
import com.demo.testassignment.utils.Utils
import kotlinx.android.synthetic.main.item.view.*

class NewsAdapter(val articles: List<Article?>, val context: Context) :
    RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    var mOnItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(view, mOnItemClickListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val model: Article = articles[position]!!
        val requestOptions = RequestOptions()
        requestOptions.placeholder(Utils.getRandomDrawbleColor())
        requestOptions.error(Utils.getRandomDrawbleColor())
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()
        model.getUrlToImage()?.let { img ->
            Glide.with(context)
                .load(img)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.setVisibility(View.GONE)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.setVisibility(View.GONE)
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView)
        }
        model.getTitle()?.let {
            holder.title.setText(model.getTitle())
        }
        model.getDescription()?.let {
            holder.desc.setText(model.getDescription())
        }
        model.getSource()?.getName()?.let {
            holder.source.setText(model.getSource()?.getName())
        }
        model.getPublishedAt()?.let {
            holder.time.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()))
        }
        model.getPublishedAt()?.let {
            holder.published_ad.setText(Utils.DateFormat(model.getPublishedAt()))
        }
        model.getAuthor()?.let {
            holder.author.setText(model.getAuthor())
        }
    }

    override fun getItemCount(): Int {
        return articles!!.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    class MyViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title = itemView.title
        var desc = itemView.desc
        var author = itemView.author
        var published_ad = itemView.publishedAt
        var source = itemView.source
        var time = itemView.time
        var imageView = itemView.img
        var progressBar = itemView.prograss_load_photo
        lateinit var onItemClickListener: OnItemClickListener
        override fun onClick(v: View) {
            onItemClickListener.onItemClick(v, getAdapterPosition())
        }

        init {
            itemView.setOnClickListener(this)

            if (onItemClickListener != null) {
                this.onItemClickListener = onItemClickListener
            }
        }
    }


}
