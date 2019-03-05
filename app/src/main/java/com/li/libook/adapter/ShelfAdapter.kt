package com.li.libook.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.model.bean.Book
import kotlinx.android.synthetic.main.item_shelf.view.*

class ShelfAdapter(val mContext:Context,val list: MutableList<Book>): RecyclerView.Adapter<ShelfAdapter.ShelfHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfHolder {
        val view:View = View.inflate(parent.context,R.layout.item_shelf,null)
        return ShelfHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ShelfHolder, position: Int) {
        with(holder.itemView){
            Glide.with(mContext).load(list[position].resource).apply(MyConfig.options).into(item_shelf_img)
            item_shelf_txt.text = list[position].bookname
            item_shelf_section.text = list[position].section
            item_shelf_update.text = list[position].update
            item_shelf_progress.text = list[position].readsection.toString()+"/"+list[position].state
            item_shelf_end.text = list[position].end
            item_shelf.setOnClickListener {
                mListen(position)
            }
            item_shelf.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(p0: View?): Boolean {
                    mLongListen(position)
                    return true
                }
            })
        }
    }

    class ShelfHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    fun longClick(listener: (Int) -> Unit){
        this.mLongListen = listener
    }

    lateinit var mListen: (Int) -> Unit
    lateinit var mLongListen: (Int) -> Unit
}