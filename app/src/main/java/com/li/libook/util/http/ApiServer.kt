package com.li.libook.util.http

import com.bumptech.glide.load.engine.Resource
import com.li.libook.model.entity.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiServer {
    companion object {
        const val BASE_URL = "http://api.zhuishushenqi.com"
        const val BASE_URL_IMG = "http://statics.zhuishushenqi.com"
        const val BASE_URL_SOURCE = "http://novel.juhe.im"
        const val BASE_URL_CHAPTER = "http://chapter2.zhuishushenqi.com"
    }
    //获取带子分类的分类
    @GET("/cats/lv2/statistics")
    fun category(): Observable<ZhuiStatis>

    //按分类获取书籍列表
    @GET("/book/by-categories")
    fun booksCat(@QueryMap map: MutableMap<String,Any>): Observable<BooksByCat>

    //书籍详情
//    @GET("/book/{bookId}")
//    fun getBookDetail(@Path("bookId") bookId: String): Observable<BookDetailBean>

    //目录
    @GET("/mix-atoc/{bookId}")
    fun bookChapter(@Path("bookId") bookId: String): Observable<BookChapter>

    //获取所有排行榜
    @GET("/ranking/gender")
    fun ranking(): Observable<Rank>

    //单个排行榜的所有书籍
    @GET("/ranking/{rankingId}")
    fun oneRanking(@Path("rankingId") rankingId: String): Observable<OneRank>

    //获取书籍源
    @GET("/book-sources")//例如：http://novel.juhe.im/book-sources?view=summary&book=567d2cb9ee0e56bc713cb2c0
    fun bookResource(@QueryMap map: MutableMap<String,String>): Observable<MutableList<BookResource>>

    //由书籍源获取书籍章节
    @GET("/book-chapters/{resourceId}")//例如：http://novel.juhe.im/book-chapters/56f8da09176d03ac1983f6cd
    fun allChapter(@Path("resourceId") resourceId: String): Observable<ResourceChapter>

    //单章节的内容
    @GET("/chapter/{url}")//例如：http://chapter2.zhuishushenqi.com/chapter/http://vip.zhuishushenqi.com/chapter/56f8da09176d03ac1983f6e1?cv=1500450749135
    fun chapContent(@Path("url") url: String): Observable<ChapterContent>
}