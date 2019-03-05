package com.li.libook.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.li.libook.R
import com.li.libook.model.entity.ResourceChapter
import kotlinx.android.synthetic.main.item_chapter.view.*
import kotlinx.android.synthetic.main.item_sort_left.view.*

class ChapterAdapter(private val mContext:Context, private val list: ResourceChapter): RecyclerView.Adapter<ChapterAdapter.ChapterHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterHolder {
        val view: View = View.inflate(parent.context, R.layout.item_chapter,null)
        return ChapterHolder(view)
    }

    override fun getItemCount(): Int {
        return list.chapters.size
    }

    override fun onBindViewHolder(holder: ChapterHolder, position: Int) {
        with(holder.itemView){
            item_chapter_txt.text = list.chapters[position].title
            var b:Boolean = list.chapters[position].isVip
            item_chapter_type.text = if (b) mContext.getString(R.string.vip) else mContext.getString(R.string.free)
            item_chapter.setOnClickListener {
                mListen(position)
            }
        }
    }
    class ChapterHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int) -> Unit
}