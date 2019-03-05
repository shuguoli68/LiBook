package com.li.libook.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.li.libook.R
import com.li.libook.model.entity.LocalBean
import kotlinx.android.synthetic.main.item_local.view.*

class LocalAdapter(private val list: MutableList<LocalBean>):RecyclerView.Adapter<LocalAdapter.LocalHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalHolder {
        val view:View = View.inflate(parent.context, R.layout.item_local,null)
        return LocalHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LocalHolder, position: Int) {
        with(holder.itemView){
            item_local_name.text = list[position].name
            item_local_path.text = list[position].path
            val b:Boolean = list[position].isSelect
            var res:Int = R.drawable.check_false
            if (b) res = R.drawable.check_true
            item_local_img.setImageResource(res)
            item_local.setOnClickListener {
                mListen(position)
            }
        }
    }

    class LocalHolder(view: View) : RecyclerView.ViewHolder(view)

    fun click(listener: (Int) -> Unit){
        this.mListen = listener
    }
    private lateinit var mListen: (Int) -> Unit
}