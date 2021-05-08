package com.demo.testassignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
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
import com.demo.testassignment.model.book.Item
import com.demo.testassignment.utils.Utils
import kotlinx.android.synthetic.main.item.view.*


class BookAdapter(val items: List<Item?>, val context: Context) :
    RecyclerView.Adapter<BookAdapter.MyViewHolder>() {

    var mOnItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(view, mOnItemClickListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val model: Item = items[position]!!
        val requestOptions = RequestOptions()
        requestOptions.placeholder(Utils.getRandomDrawbleColor())
        requestOptions.error(Utils.getRandomDrawbleColor())
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()
        model.volumeInfo?.imageLinks?.thumbnail?.let { img ->
            //Log.d("Test: ",""+img)
            Glide.with(context)
                .load(img)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Test:exceptiom ",""+e)

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
        model.volumeInfo?.title?.let {
            holder.title.setText(it)
        }
        model.volumeInfo?.description?.let {
            holder.desc.setText(it)
        }
        model.volumeInfo?.authors?.let {list->
            for (item in list) {
                holder.source.append(item)
                holder.source.append("\n")
            }

        }
        model.volumeInfo?.publisher?.let {
            holder.time.setText(it)
        }
        model.volumeInfo?.publishedDate?.let {
            holder.published_ad.setText(""+it)
        }
        model.volumeInfo?.averageRating?.let {
            model.volumeInfo?.ratingsCount?.let { count->
                holder.author.setText("Rating: $it ($count)")
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
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
