package com.li.libook

import android.app.Activity
import android.app.DownloadManager
import android.app.FragmentManager
import android.content.Intent
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.widget.ImageView
import android.widget.TextView
import com.li.libook.adapter.MainPagerAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.LocalBean
import com.li.libook.util.LocalFileTool
import com.li.libook.view.activity.BaseAcivity
import com.li.libook.view.activity.LocalActivity
import com.li.libook.view.fragment.BookShelfFragment
import com.li.libook.view.fragment.FindFragment
import com.li.libook.view.fragment.RankFragment
import com.li.libook.view.fragment.SortFragment
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import android.opengl.ETC1.getWidth
import android.view.*
import android.widget.Toast
import cn.xinlian.kotlinweb.util.ToastUtil
import com.li.libook.view.activity.DownLoadActivity


class MainActivity : BaseAcivity() {

    private val mMessage: Message = Message.obtain()


    override fun getResourceId() : Int{
        return R.layout.activity_main
    }

    override fun initData() {
        val tabs = arrayOf(getString(R.string.book_shelf),getString(R.string.book_rank),getString(R.string.book_sort),getString(R.string.book_find))
        val tabImg = intArrayOf(
            R.drawable.tab_book_shelf,
            R.drawable.tab_book_store,
            R.drawable.tab_classify,
            R.drawable.tab_discovery
        )
        val fragments = listOf<Fragment>(BookShelfFragment(),RankFragment(),SortFragment(),FindFragment())
        val tabsAdapter = MainPagerAdapter(supportFragmentManager, fragments, tabs)
        main_pager.adapter = tabsAdapter
        main_tab.setupWithViewPager(main_pager)//因为TabLayout设置关联viewpager后，会清空所有tab栏
        main_tab.removeAllTabs()
        for (i in 0 until tabs.size){//设置关联后，再用代码显示tab标题
            val view = this.layoutInflater.inflate(R.layout.item_main_tab, null)
            val tab = main_tab.newTab()
            tab?.customView = view
            val itemTxt = view.findViewById(R.id.main_tab_txt) as TextView
            itemTxt.text = tabs[i]
            val itemImg = view.findViewById(R.id.main_tab_img) as ImageView
            itemImg.setImageResource(tabImg[i])
            main_tab.addTab(tab!!)
        }
        BookShelfFragment.mainAct = this
    }

    override fun initView() {

        setSupportActionBar(main_toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(true)
        main_toolbar.setNavigationOnClickListener(View.OnClickListener {
            if (!main_drawer.isDrawerOpen(main_left))
                main_drawer.openDrawer(Gravity.LEFT)
            else
                main_drawer.closeDrawer(main_left,true)
        })

        //侧滑菜单
        main_drawer.setDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                //菜单展开，主页面收缩；关闭，舒张
                // 得到contentView
//                val content = main_drawer.getChildAt(0)
//                val offset = (drawerView.width * slideOffset) as Int
//                content.setTranslationX(offset.toFloat())
//                content.setScaleX(1 - slideOffset * 0.2f)
//                content.setScaleY(1 - slideOffset * 0.2f)
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerOpened(drawerView: View) {
                drawerView.isClickable = true//解决：当侧滑菜单被滑出来时,下层被遮盖住的主布局依然可以响应点击事件
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.main_add -> startActivityForResult(Intent(this@MainActivity,LocalActivity::class.java),MyConfig.CODE_ONE)
            R.id.main_scan -> {
                Logger.i("下载")
                startTo(DownLoadActivity::class.java)
            }
            R.id.main_logout -> {
                Logger.i("退出")
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyConfig.CODE_ONE && resultCode == Activity.RESULT_OK){
            val localList: ArrayList<LocalBean> = data!!.getParcelableArrayListExtra(MyConfig.LOCAL_LIST)
            if (localList!=null && localList.size>0){
                val shelf:BookShelfFragment = BookShelfFragment()
                shelf.addLocal(localList)
            }
        }
    }

    var exitTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Logger.i("退出")
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtil.showShort(this,getString(R.string.exit))
                exitTime = System.currentTimeMillis()
            }else{
                exitApp()
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
