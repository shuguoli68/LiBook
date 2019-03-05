package com.li.libook.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.li.libook.R
import com.li.libook.model.entity.BookResource
import kotlinx.android.synthetic.main.item_resource.view.*

class ResourceAdapter(private val list: MutableList<BookResource>): RecyclerView.Adapter<ResourceAdapter.ResourceHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceHolder {
        val view: View = View.inflate(parent.context, R.layout.item_resource,null)
        return ResourceHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResourceHolder, position: Int) {
        with(holder.itemView){
            item_resource_name.text = list[position].name
            item_resource_count.text = "共："+list[position].chaptersCount
            item_resource_last.text = list[position].lastChapter
            item_resource_link.text = list[position].link
            item_resource.setOnClickListener {
                mListen(position)
            }
        }
    }
    class ResourceHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int) -> Unit
}