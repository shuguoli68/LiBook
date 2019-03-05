package com.li.libook.util.book

import android.text.TextUtils
import android.util.Log

import com.li.libook.App
import com.li.libook.model.MyConfig
import com.li.libook.model.bean.Book
import com.li.libook.model.bean.BookCatalogue
import com.li.libook.model.bean.BookMarkBean
import com.li.libook.model.gen.BookCatalogueDao
import com.li.libook.model.gen.BookDao
import com.li.libook.model.gen.BookMarkBeanDao

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.ref.WeakReference
import java.util.ArrayList

/**
 * Created by Administrator on 2016/8/11 0011.
 */
class BookUtil {

    private val bookListDao: BookDao = App.instance.session().bookDao
    private val bookCatalogueDao: BookCatalogueDao = App.instance.session().bookCatalogueDao
    private val bookMarkBeanDao: BookMarkBeanDao = App.instance.session().bookMarkBeanDao
    //    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    protected val myArray = ArrayList<Cache>()
    //目录
    private val directoryList = ArrayList<BookCatalogue>()

    private var m_strCharsetName: String? = null
    private var bookName: String? = null
    private var bookPath: String? = null
    var bookLen: Long = 0
        private set
    var position: Long = 0
        private set
    private var bookList: Book? = null

    @Synchronized
    @Throws(IOException::class)
    fun openBook(bookList: Book) {
        this.bookList = bookList
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (bookPath == null || bookPath != bookList.getBookpath()) {
            cleanCacheFile()
            this.bookPath = bookList.getBookpath()
            bookName = CommonUtil.instance.getFileName(bookPath!!)
            cacheBook()
        }
    }

    private fun cleanCacheFile() {
        val file = File(cachedPath)
        if (!file.exists()) {
            file.mkdir()
        } else {
            val files = file.listFiles()
            for (i in files.indices) {
                files[i].delete()
            }
        }
    }

    fun next(back: Boolean): Int {
        position += 1
        if (position > bookLen) {
            position = bookLen
            return -1
        }
        val result = current()
        if (back) {
            position -= 1
        }
        return result.toInt()
    }

    fun nextLine(): CharArray? {
        if (position >= bookLen) {
            return null
        }
        var line = ""
        while (position < bookLen) {
            val word = next(false)
            if (word == -1) {
                break
            }
            val wordChar = word.toChar()
            if (wordChar + "" == "\r" && next(true).toChar() + "" == "\n") {
                next(false)
                break
            }
            line += wordChar
        }
        return line.toCharArray()
    }

    fun preLine(): CharArray? {
        if (position <= 0) {
            return null
        }
        var line = ""
        while (position >= 0) {
            val word = pre(false)
            if (word == -1) {
                break
            }
            val wordChar = word.toChar()
            if (wordChar + "" == "\n" && pre(true).toChar() + "" == "\r") {
                pre(false)
                //                line = "\r\n" + line;
                break
            }
            line = wordChar + line
        }
        return line.toCharArray()
    }

    fun current(): Char {
        //        int pos = (int) (position % cachedSize);
        //        int cachePos = (int) (position / cachedSize);
        var cachePos = 0
        var pos = 0
        var len = 0
        for (i in myArray.indices) {
            val size = myArray[i].size
            if (size + len - 1 >= position) {
                cachePos = i
                pos = (position - len).toInt()
                break
            }
            len += size.toInt()
        }

        val charArray = block(cachePos)
        return charArray[pos]
    }

    fun pre(back: Boolean): Int {
        position -= 1
        if (position < 0) {
            position = 0
            return -1
        }
        val result = current()
        if (back) {
            position += 1
        }
        return result.toInt()
    }

    fun setPostition(position: Long) {
        this.position = position
    }

    //缓存书本
    @Throws(IOException::class)
    private fun cacheBook() {
        if (TextUtils.isEmpty(bookList!!.getCharset())) {
            m_strCharsetName = CommonUtil.instance.getCharset(bookPath!!)//RxFileTool.getFileCharsetSimple(bookPath);
            if (m_strCharsetName == null) {
                m_strCharsetName = "UTF-8"
            }
            bookList!!.charset = m_strCharsetName
            updateBook(bookList!!)
        } else {
            m_strCharsetName = bookList!!.charset
        }
        Log.i("charset", m_strCharsetName!! + "")
        val file = File(bookPath!!)
        val reader = InputStreamReader(FileInputStream(file), m_strCharsetName!!)
        var index = 0
        bookLen = 0
        directoryList.clear()
        myArray.clear()
        while (true) {
            var buf = CharArray(cachedSize)
            val result = reader.read(buf)
            if (result == -1) {
                reader.close()
                break
            }

            var bufStr = String(buf)
            //            bufStr = bufStr.replaceAll("\r\n","\r\n\u3000\u3000");
            //            bufStr = bufStr.replaceAll("\u3000\u3000+[ ]*","\u3000\u3000");
            bufStr = bufStr.replace("\r\n+\\s*".toRegex(), "\r\n\u3000\u3000")
            //            bufStr = bufStr.replaceAll("\r\n[ {0,}]","\r\n\u3000\u3000");
            //            bufStr = bufStr.replaceAll(" ","");
            bufStr = bufStr.replace("\u0000".toRegex(), "")
            buf = bufStr.toCharArray()
            bookLen += buf.size.toLong()

            val cache = Cache()
            cache.size = buf.size.toLong()
            cache.data = WeakReference(buf)

            //            bookLen += result;
            myArray.add(cache)
            //            myArray.add(new WeakReference<char[]>(buf));
            //            myArray.set(index,);
            try {
                val cacheBook = File(fileName(index))
                if (!cacheBook.exists()) {
                    cacheBook.createNewFile()
                }
                val writer = OutputStreamWriter(FileOutputStream(fileName(index)), "UTF-16LE")
                writer.write(buf)
                writer.close()
            } catch (e: IOException) {
                throw RuntimeException("Error during writing " + fileName(index))
            }

            index++
        }

        object : Thread() {
            override fun run() {
                getChapter()
            }
        }.start()
    }

    //获取章节
    @Synchronized
    fun getChapter() {
        try {
            var size: Long = 0
            for (i in myArray.indices) {
                val buf = block(i)
                val bufStr = String(buf)
                val paragraphs = bufStr.split("\r\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (str in paragraphs) {
                    if (str.length <= 30 && (str.matches(".*第.{1,8}章.*".toRegex()) || str.matches(".*第.{1,8}节.*".toRegex()))) {
                        val bookCatalogue = BookCatalogue(bookName, bookPath, str, size)
                        directoryList.add(bookCatalogue)
                    }
                    if (str.contains("\u3000\u3000")) {
                        size += (str.length + 2).toLong()
                    } else if (str.contains("\u3000")) {
                        size += (str.length + 1).toLong()
                    } else {
                        size += str.length.toLong()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        addAllBookCatalog(directoryList)
    }

    fun getDirectoryList(): List<BookCatalogue> {
        return directoryList
    }

    protected fun fileName(index: Int): String {
        return cachedPath + File.separator + bookName
    }

    //获取书本缓存
    fun block(index: Int): CharArray {
        if (myArray.size == 0) {
            return CharArray(1)
        }
        var block: CharArray? = myArray[index].data!!.get()
        if (block == null) {
            try {
                val file = File(fileName(index))
                val size = file.length().toInt()
                if (size < 0) {
                    throw RuntimeException("Error during reading " + fileName(index))
                }
                block = CharArray(size / 2)
                val reader = InputStreamReader(
                    FileInputStream(file),
                    "UTF-16LE"
                )
                if (reader.read(block) != block.size) {
                    throw RuntimeException("Error during reading " + fileName(index))
                }
                reader.close()
            } catch (e: IOException) {
                throw RuntimeException("Error during reading " + fileName(index))
            }

            val cache = myArray[index]
            cache.data = WeakReference(block)
            //            myArray.set(index, new WeakReference<char[]>(block));
        }
        return block
    }

    //书本信息操作
    fun addBook(bookList: Book) {
        bookListDao.insertOrReplace(bookList)
    }

    fun addAllBook(bookList: List<Book>) {
        bookListDao.insertOrReplaceInTx(bookList)
    }

    fun loadAllBook(): List<Book> {
        return bookListDao.loadAll()
    }

    fun loadBook(key: String): Book {
        return bookListDao.queryBuilder().where(BookDao.Properties.Bookpath.eq(key)).unique()
    }

    fun delBook(bookList: Book) {
        bookListDao.delete(bookList)
    }

    fun updateBook(bookList: Book) {
        bookListDao.update(bookList)
    }

    fun delAllBook() {
        bookListDao.deleteAll()
    }

    //目录信息操作
    fun addBookCatalog(bookList: BookCatalogue) {
        bookCatalogueDao.insertOrReplace(bookList)
    }

    fun addAllBookCatalog(bookList: List<BookCatalogue>) {
        bookCatalogueDao.insertOrReplaceInTx(bookList)
    }

    fun loadAllBookCatalog(): List<BookCatalogue> {
        return bookCatalogueDao.loadAll()
    }

    fun delBookCatalog(bookList: BookCatalogue) {
        bookCatalogueDao.delete(bookList)
    }

    fun updateBookCatalog(bookList: BookCatalogue) {
        bookCatalogueDao.update(bookList)
    }

    //书签信息操作
    fun addBookMark(bookMarkBean: BookMarkBean) {
        bookMarkBeanDao.insertOrReplace(bookMarkBean)
    }

    fun loadAllBookMark(key: String): List<BookMarkBean> {
        return bookMarkBeanDao.queryBuilder().where(BookMarkBeanDao.Properties.Bookpath.eq(key)).list()
    }

    fun loadBookMark(key: String, begin: Long): BookMarkBean {
        return bookMarkBeanDao.queryBuilder()
            .where(BookMarkBeanDao.Properties.Bookpath.eq(key), BookMarkBeanDao.Properties.Begin.eq(begin)).unique()
    }

    fun delBookMark(bookMarkBean: BookMarkBean) {
        bookMarkBeanDao.delete(bookMarkBean)
    }

    companion object {
        private val cachedPath = MyConfig.ROOT_CACHE_PATH
        //存储的字符数
        val cachedSize = 30000

        val instance: BookUtil
            get() = BookUtil()
    }
}
