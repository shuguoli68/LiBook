package com.li.libook.model.entity


data class ResourceChapter(
    val _id: String,
    val book: String,
    val chapters: List<RChapter>,
    val host: String,
    val link: String,
    val name: String,
    val source: String,
    val updated: String
)

data class RChapter(
    val chapterCover: String,
    val currency: Int,
    val id: String,
    val isVip: Boolean,
    val link: String,
    val order: Int,
    val partsize: Int,
    val time: Int,
    val title: String,
    val totalpage: Int,
    val unreadble: Boolean
)