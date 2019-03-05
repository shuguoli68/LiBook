package com.li.libook.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.OneRank
import com.li.libook.util.http.ApiServer
import kotlinx.android.synthetic.main.item_catbooks.view.*

class RankAdapter(private val mContext: Context, private val oneRank: OneRank): RecyclerView.Adapter<RankAdapter.RankHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val view: View = View.inflate(parent.context, R.layout.item_catbooks,null)
        return RankHolder(view)
    }

    override fun getItemCount(): Int {
        return oneRank.ranking.books.size
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        with(holder.itemView){
            val  list = oneRank.ranking.books
            Glide.with(mContext).load(ApiServer.BASE_URL_IMG+list[position].cover).apply(MyConfig.options).into(item_catbooks_img)
            item_catbooks.setOnClickListener {
                mListen(position)
            }
            item_catbooks_title.text = list[position].title
            item_catbooks_author.text = list[position].author
            item_catbooks_retentionRatio.text = "留存率："+list[position].retentionRatio+"%"
            var last = if (TextUtils.isEmpty(list[position].lastChapter)) list[position].shortIntro else list[position].lastChapter
            item_catbooks_lastChapter.text = last

        }
    }

    class RankHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int) -> Unit
}