package com.ads.web.one.quantumdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterNews() :
    RecyclerView.Adapter<AdapterNews.MyViewHolder>() {
    private val newsList: ArrayList<ModalNews> = ArrayList<ModalNews>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val publishedAt: TextView = itemView.findViewById(R.id.published_at)
        val newsSource: TextView = itemView.findViewById(R.id.source)
        val newsTitle: TextView = itemView.findViewById(R.id.title)
        val newsDescription: TextView = itemView.findViewById(R.id.description)
        val imageNews: ImageView = itemView.findViewById(R.id.image_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.single_news_layout, parent, false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.publishedAt.text = newsList[position].publishedAt
        holder.newsSource.text = newsList[position].author
        holder.newsTitle.text = newsList[position].title
        holder.newsDescription.text = newsList[position].description
        Picasso.get().load(newsList[position].urlToImage).into(holder.imageNews)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateData(newData: ArrayList<ModalNews>) {
        newsList.clear()
        newsList.addAll(newData)

        notifyDataSetChanged()
    }
}