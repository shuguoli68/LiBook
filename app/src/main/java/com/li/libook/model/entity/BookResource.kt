package com.li.libook.model.entity


data class BookResource(
    val _id: String,
    val source: String,
    val name: String,
    val link: String,
    val lastChapter: String,
    val isCharge: Boolean,
    val chaptersCount: Int,
    val updated: String,
    val starting: Boolean,
    val host: String
)