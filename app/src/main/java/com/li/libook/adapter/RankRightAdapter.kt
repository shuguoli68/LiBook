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

class RankRightAdapter(private val mContext: Context, private val leftIndex:Int, private val rightData: Rank):RecyclerView.Adapter<RankRightAdapter.RankRightHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankRightHolder {
        val view:View = View.inflate(parent.context, R.layout.item_sort_right,null)
        return RankRightHolder(view)
    }

    override fun getItemCount(): Int {
        return when(leftIndex){
            0 -> rightData.male.size
            1 -> rightData.female.size
            2 -> rightData.picture.size
            3 -> rightData.epub.size
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: RankRightHolder, position: Int) {
        val list1:List<RMale> = rightData.male
        val list2:List<RFemale> = rightData.female
        val list3:List<RPicture> = rightData.picture
        val list4:List<Epub> = rightData.epub
        with(holder.itemView){
            when(leftIndex){
                0 -> {
                    if (list1[position].cover!=null) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list1[position].cover).apply(MyConfig.cicleOptions).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list1[position].shortTitle
                    item_sort_right_type.text = "全名："+list1[position].title
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
                1 -> {
                    if (list2[position].cover!=null ){
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list2[position].cover).apply(MyConfig.cicleOptions).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list2[position].shortTitle
                    item_sort_right_type.text = "全名："+list2[position].title
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
                2 -> {
                    if (list3[position].cover!=null) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list3[position].cover).apply(MyConfig.cicleOptions).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list3[position].shortTitle
                    item_sort_right_type.text = "全名："+list3[position].title
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
                3 -> {
                    if (list4[position].cover!=null) {
                        Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list4[position].cover).apply(MyConfig.cicleOptions).into(item_sort_right_img)
                    }
                    item_sort_right_name.text = list4[position].shortTitle
                    item_sort_right_type.text = "全名："+list4[position].title
                    item_sort_right.setOnClickListener {
                        mListen(leftIndex,position)
                    }
                }
            }
        }
    }

    class RankRightHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (leftIndex:Int,position:Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int,Int) -> Unit
}