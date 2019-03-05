package com.li.libook.model.entity


data class BooksByCat(
    val books: List<Book>,
    val ok: Boolean,
    val total: Int
)

data class Book(
    val _id: String,
    val allowMonthly: Boolean,
    val author: String,
    val banned: Int,
    val contentType: String,
    val cover: String,
    val lastChapter: String,
    val latelyFollower: Int,
    val majorCate: String,
    val minorCate: String,
    val retentionRatio: Double,
    val shortIntro: String,
    val site: String,
    val sizetype: Int,
    val superscript: String,
    val tags: List<String>,
    val title: String
)