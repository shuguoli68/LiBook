package com.li.libook.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.*
import com.li.libook.util.http.ApiServer
import kotlinx.android.synthetic.main.item_sort_right.view.*

class SortRightAdapter(private val mContext:Context, private val leftIndex:Int, private val rightData: ZhuiStatis):RecyclerView.Adapter<SortRightAdapter.SortRightHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortRightHolder {
        val view:View = View.inflate(parent.context, R.layout.item_sort_right,null)
        return SortRightHolder(view)
    }

    override fun getItemCount(): Int {
        return when(leftIndex){
            0 -> rightData.male.size
            1 -> rightData.female.size
            2 -> rightData.picture.size
            3 -> rightData.press.size
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: SortRightHolder, position: Int) {
        val list1:List<Male> = rightData.male
        val list2:List<Female> = rightData.female
        val list3:List<Picture> = rightData.picture
        val list4:List<Pres> = rightData.press
        with(holder.itemView){
            when(leftIndex){
                0 -> {
                    if (list1[position].bookCover!=null && list1[position].bookCover.isNotEmpty()) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list1[position].bookCover[0]).apply(MyConfig.options).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list1[position].name
                    item_sort_right_count.text = "共"+list1[position].bookCount+"本"
                    item_sort_right_type.text = "月更新："+list1[position].monthlyCount
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
                1 -> {
                    if (list2[position].bookCover!=null && list2[position].bookCover.size>0) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list2[position].bookCover[0]).apply(MyConfig.options).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list2[position].name
                    item_sort_right_count.text = "共"+list2[position].bookCount+"本"
                    item_sort_right_type.text = "月更新："+list2[position].monthlyCount
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
                2 -> {
                    if (list3[position].bookCover!=null && list3[position].bookCover.size>0) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list3[position].bookCover[0]).apply(MyConfig.options).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list3[position].name
                    item_sort_right_count.text = "共"+list3[position].bookCount+"本"
                    item_sort_right_type.text = "月更新："+list3[position].monthlyCount
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
                3 -> {
                    if (list4[position].bookCover!=null && list4[position].bookCover.size>0) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list4[position].bookCover[0]).apply(MyConfig.options).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list4[position].name
                    item_sort_right_count.text = "共"+list4[position].bookCount+"本"
                    item_sort_right_type.text = "月更新："+list4[position].monthlyCount
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
            }
        }
    }

    class SortRightHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (leftIndex:Int,position:Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int,Int) -> Unit
}