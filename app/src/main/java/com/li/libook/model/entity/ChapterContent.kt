package com.li.libook.model.entity


data class ChapterContent(
    val chapter: ChapContent,
    val ok: Boolean
)

data class ChapContent(
    val body: String,
    val cpContent: String,
    val currency: Int,
    val id: String,
    val isVip: Boolean,
    val title: String
)