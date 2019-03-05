package com.li.libook.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.li.libook.R
import com.li.libook.R.color.txt
import com.li.libook.util.book.CommonUtil
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.item_sort_left.view.*

class SortLeftAdapter(private val mContext: Context, private val list: MutableList<String>):RecyclerView.Adapter<SortLeftAdapter.SortLeftHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortLeftHolder {
        val view:View = View.inflate(parent.context, R.layout.item_sort_left,null)
        return SortLeftHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SortLeftHolder, position: Int) {
        with(holder.itemView){
            val item = list[position].split("_")
            Logger.i("标题："+item.toString())
            val b:Boolean = item[1].toBoolean()
//            var img:Int = R.drawable.head_blue
            var s:Int = android.R.color.transparent
//            when(position % 4){
//                1 -> img = R.drawable.head_red
//                2 -> img = R.drawable.head_green
//                3 -> img = R.drawable.head_gray
//            }
            item_sort_left_txt.text = item[0]
            if (b)
                s = R.color.white_press
            item_sort_left.setBackgroundColor(mContext.resources.getColor(s))
            item_sort_left.setOnClickListener {
                mListen(position)
            }
        }
    }
    class SortLeftHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int) -> Unit
}