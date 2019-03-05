package com.li.libook.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.CatBook
import com.li.libook.util.http.ApiServer
import kotlinx.android.synthetic.main.item_catbooks.view.*

class CatBookAdapter(private val mContext: Context, private val list: MutableList<CatBook>):RecyclerView.Adapter<CatBookAdapter.CatBookHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatBookHolder {
        val view:View = View.inflate(parent.context, R.layout.item_catbooks,null)
        return CatBookHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CatBookHolder, position: Int) {
        with(holder.itemView){
            Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list[position].res).apply(MyConfig.options).into(item_catbooks_img)
            item_catbooks.setOnClickListener {
                mListen(position)
            }
            item_catbooks_title.text = list[position].title
            item_catbooks_author.text = list[position].author
            item_catbooks_retentionRatio.text = "留存率："+list[position].retentionRatio+"%"
            item_catbooks_lastChapter.text = list[position].lastChapter

        }
    }

    class CatBookHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int) -> Unit
}