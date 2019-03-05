package com.li.libook.model.entity


data class ZhuiStatis(
    val female: List<Female>,
    val male: List<Male>,
    val ok: Boolean,
    val picture: List<Picture>,
    val press: List<Pres>
)

data class Picture(
    val bookCount: Int,
    val bookCover: List<String>,
    val icon: String,
    val monthlyCount: Int,
    val name: String
)

data class Pres(
    val bookCount: Int,
    val bookCover: List<String>,
    val icon: String,
    val monthlyCount: Int,
    val name: String
)

data class Female(
    val bookCount: Int,
    val bookCover: List<String>,
    val icon: String,
    val monthlyCount: Int,
    val name: String
)

data class Male(
    val bookCount: Int,
    val bookCover: List<String>,
    val icon: String,
    val monthlyCount: Int,
    val name: String
)