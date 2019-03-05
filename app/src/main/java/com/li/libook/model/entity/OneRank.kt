package com.li.libook.model.entity


data class OneRank(
    val ok: Boolean,
    val ranking: Ranking
)

data class Ranking(
    val __v: Int,
    val _id: String,
    val biTag: String,
    val books: List<Book>,
    val collapse: Boolean,
    val cover: String,
    val created: String,
    val gender: String,
    val icon: String,
    val id: String,
    val isSub: Boolean,
    val monthRank: String,
    val new: Boolean,
    val priority: Int,
    val shortTitle: String,
    val tag: String,
    val title: String,
    val total: Int,
    val totalRank: String,
    val updated: String
)

data class OneBook(
    val _id: String,
    val allowMonthly: Boolean,
    val author: String,
    val banned: Int,
    val cover: String,
    val latelyFollower: Int,
    val majorCate: String,
    val minorCate: String,
    val retentionRatio: String,
    val shortIntro: String,
    val site: String,
    val title: String
)