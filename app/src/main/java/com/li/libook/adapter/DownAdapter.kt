package com.li.libook.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cn.xinlian.kotlinweb.util.ToastUtil
import com.li.libook.R
import com.li.libook.model.bean.DownLoad
import kotlinx.android.synthetic.main.item_down.view.*

class DownAdapter(private val mContext: Context,private val list: MutableList<DownLoad>): RecyclerView.Adapter<DownAdapter.DownHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownHolder {
        val view: View = View.inflate(parent.context, R.layout.item_down,null)
        return DownHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DownHolder, position: Int) {
        with(holder.itemView){
            var type = mContext.getString(R.string.not_down)
            when(list[position].type){
                1 -> type = mContext.getString(R.string.pause)
                2 -> type = mContext.getString(R.string.downloading)
                3 -> type = mContext.getString(R.string.has_down)
            }
            item_down_type.text = type
            item_down_name.text = list[position].name+"("+list[position].total+"章)"
            item_down_progress.text = list[position].progress+"/"+list[position].total
            item_down_seek.max = list[position].total.toInt()
            item_down_seek.progress = list[position].progress.toInt()
            item_down.setOnClickListener {
                when(list[position].type) {
                    3 -> ToastUtil.showShort(mContext,mContext.getString(R.string.has_down))
                    2 -> mListen(position,2)
                    else -> mListen(position,1)
                }
            }
            item_down.setOnLongClickListener {
                mLongListen(position)
                true
            }
        }
    }

    class DownHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int,Int) -> Unit){
        this.mListen = listener
    }
    fun longClick(listener: (Int) -> Unit){
        this.mLongListen = listener
    }

    private lateinit var mListen: (Int,Int) -> Unit
    private lateinit var mLongListen: (Int) -> Unit
}