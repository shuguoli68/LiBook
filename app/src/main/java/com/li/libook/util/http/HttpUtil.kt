package cn.sharelink.kotlinweb.util.http

import com.li.libook.model.entity.*
import com.li.libook.util.http.ApiServer
import com.li.libook.util.http.RetrofitManager
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
//追书神器api 参考：https://github.com/xiadd/zhuishushenqi
class HttpUtil() {

    /**
     * TODO 1、获取分类：
     * http://api.zhuishushenqi.com/cats/lv2/statistics
     */
    fun category(): Observable<ZhuiStatis> = RetrofitManager
        .builder(ApiServer.BASE_URL)
        .Service()!!
        .category()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }

    /**
     * TODO 2、获取分类的书籍列表：
     * http://api.zhuishushenqi.com/book/by-categories?gender=male&type=hot&major=玄幻&minor=东方玄幻&start=0&limit=20
     */
    fun bookByCat(map: MutableMap<String,Any>):Observable<BooksByCat> = RetrofitManager
        .builder(ApiServer.BASE_URL)
        .Service()!!
        .booksCat(map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }
    /**
     * TODO 3、获取书籍目录：
     * http://api.zhuishushenqi.com/mix-atoc/508de73e55e53b9a1a000031
     * 注：54d42d92321052167dfb75e3为bookId
     */
    fun bookChapter(bookId: String):Observable<BookChapter> = RetrofitManager
        .builder(ApiServer.BASE_URL)
        .Service()!!
        .bookChapter(bookId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }

    /**
     * TODO 4、获取书籍所有排行：
     * http://api.zhuishushenqi.com/ranking/gender
     */
    fun ranking():Observable<Rank> = RetrofitManager
        .builder(ApiServer.BASE_URL)
        .Service()!!
        .ranking()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }

    /**
     *
     * TODO 5、获取单个排行的所有书籍：
     * http://api.zhuishushenqi.com/ranking/54d42d92321052167dfb75e3
     * 注：54d42d92321052167dfb75e3为rankingId
     */
    fun oneRanking(rankingId: String):Observable<OneRank> = RetrofitManager
        .builder(ApiServer.BASE_URL)
        .Service()!!
        .oneRanking(rankingId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }

    /**
     *
     * TODO 6、获取书籍源：
     * http://novel.juhe.im/book-sources?view=summary&book=567d2cb9ee0e56bc713cb2c0
     * 注：567d2cb9ee0e56bc713cb2c0为bookId
     */
    fun bookResource(map: MutableMap<String,String>):Observable<MutableList<BookResource>> = RetrofitManager
        .builder(ApiServer.BASE_URL_SOURCE)
        .Service()!!
        .bookResource(map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }

    /**
     *
     * TODO 7、由书籍源获取书籍章节：
     * http://novel.juhe.im/book-chapters/56f8da09176d03ac1983f6cd
     * 注：56f8da09176d03ac1983f6cd为resourceId
     */
    fun allChapter(resourceId: String):Observable<ResourceChapter> = RetrofitManager
        .builder(ApiServer.BASE_URL_SOURCE)
        .Service()!!
        .allChapter(resourceId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }

    /**
     *
     * TODO 8、获取单个章节的内容：
     * http://chapter2.zhuishushenqi.com/chapter/http://vip.zhuishushenqi.com/chapter/56f8da09176d03ac1983f6e1?cv=1500450749135
     * 注：54d42d92321052167dfb75e3为rankingId
     */
    fun chapContent(url: String):Observable<ChapterContent> = RetrofitManager
        .builder(ApiServer.BASE_URL_CHAPTER)
        .Service()!!
        .chapContent(url)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { Logger.i("call…………………………") }
}