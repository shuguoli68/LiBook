package com.li.libook.util.book

import java.lang.ref.WeakReference

/**
 * Created by Administrator on 2016/8/15 0015.
 */
class Cache {
    var size: Long = 0
    var data: WeakReference<CharArray>? = null
}
