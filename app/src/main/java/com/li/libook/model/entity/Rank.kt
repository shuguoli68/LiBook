package com.li.libook.model.entity


data class Rank(
    val epub: List<Epub>,
    val female: List<RFemale>,
    val male: List<RMale>,
    val ok: Boolean,
    val picture: List<RPicture>
)

data class Epub(
    val _id: String,
    val collapse: Boolean,
    val cover: String,
    val shortTitle: String,
    val title: String
)

data class RFemale(
    val _id: String,
    val collapse: Boolean,
    val cover: String,
    val monthRank: String,
    val shortTitle: String,
    val title: String,
    val totalRank: String
)

data class RMale(
    val _id: String,
    val collapse: Boolean,
    val cover: String,
    val monthRank: String,
    val shortTitle: String,
    val title: String,
    val totalRank: String
)

data class RPicture(
    val _id: String,
    val collapse: Boolean,
    val cover: String,
    val shortTitle: String,
    val title: String
)