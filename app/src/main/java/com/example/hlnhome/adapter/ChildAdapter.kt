package com.example.hlnhome.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hlnhome.R
import com.example.hlnhome.database.entity.Service
import com.example.hlnhome.util.toImageUrl
import kotlinx.android.synthetic.main.child_recycler.view.*


class ChildAdapter(
    private val children: List<Service>,
    val context: Context,
    val clickListener: (Service) -> Unit
) :
    RecyclerView.Adapter<ChildAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.child_recycler, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val child = children[position]

        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(context).load(toImageUrl(child.pic, child.prefix))
            .apply(requestOptions).into(holder.imageView)
        holder.textView.text = child.name
        holder.serviceItem.setOnClickListener { clickListener(child) }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.service_title
        val imageView: ImageView = itemView.service_image
        val serviceItem: ConstraintLayout = itemView.service_item

    }
}