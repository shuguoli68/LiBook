package com.li.libook.model.entity


data class BookChapter(
    val mixToc: MixToc,
    val ok: Boolean
)

data class MixToc(
    val _id: String,
    val book: String,
    val chaptersCount1: Int,
    val chaptersUpdated: String,
    val chapters: List<Chapter>,
    val updated: String
)

data class Chapter(
    val title: String,
    val link: String,
    val unreadble: Boolean
)