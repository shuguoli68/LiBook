package com.li.libook.util.book

class TRPage(){
    private var begin: Long = 0
    private var end: Long = 0
    private var lines: List<String>? = null

    fun getBegin(): Long {
        return begin
    }

    fun setBegin(begin: Long) {
        this.begin = begin
    }

    fun getEnd(): Long {
        return end
    }

    fun setEnd(end: Long) {
        this.end = end
    }

    fun getLines(): List<String>? {
        return lines
    }

    fun getLineToString(): String {
        var text = ""
        if (lines != null) {
            for (line in lines!!) {
                text += line
            }
        }
        return text
    }

    fun setLines(lines: List<String>) {
        this.lines = lines
    }
}