package com.example.gson

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream
import java.net.URL

class Adapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val cellClickListener: MainActivity): RecyclerView.Adapter<Adapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.imageView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.count()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rViewList = list[position]
        val inptStream: InputStream = URL(rViewList).openStream()
        val bmp = BitmapFactory.decodeStream(inptStream)
        holder.image.setImageBitmap(bmp)
        holder.itemView.setOnClickListener{
            cellClickListener.onCellClickListener(rViewList)
        }
    }
}