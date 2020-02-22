package com.example.hlnhome.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.hlnhome.R
import com.example.hlnhome.database.entity.Service
import com.example.hlnhome.database.entity.ServiceCategories
import kotlinx.android.synthetic.main.parent_recycler.view.*


class ParentAdapter(
    private val parents: List<ServiceCategories>,
    val context: Context,
    val clickListener: (Service) -> Unit
) : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.parent_recycler, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parents[position]
        holder.textView.text = parent.naming
        val snapHelper = LinearSnapHelper()
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
            adapter = ChildAdapter(parent.services, context, clickListener)
            snapHelper.attachToRecyclerView(this)
            setRecycledViewPool(viewPool)
            if (size <= 2)
                this.stopScroll()
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.child_recycler
        val textView: TextView = itemView.parent_headline
    }
}