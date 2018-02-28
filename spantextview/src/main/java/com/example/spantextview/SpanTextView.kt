package com.huizhuang.zxsq.widget.textview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.widget.TextView
import com.huizhuang.hz.R

/**
 * @className:SpanTextView
 * @description:
 * @author: JIAMING.LI
 * @date:2017年10月12日 14:53
 */
class SpanTextView : TextView {
    private var spanColor: MutableList<Int>? = null
    private var spanSize: MutableList<Float>? = null
    private var isBold: MutableList<Boolean>? = null
    private var separator = "%"//字符串分隔符
    private var baseText: String? = null

    constructor(context: Context) : super(context) {
        initAttr(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttr(attrs)
    }

    /**
     * 将颜色转换成#开头的16进制颜色，为了得到当前颜色
     */
    private fun toHexEncoding(color: Int): String {
        var R: String = Integer.toHexString(Color.red(color))
        var G: String = Integer.toHexString(Color.green(color))
        var B: String = Integer.toHexString(Color.blue(color))
        val sb = StringBuffer()
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = if (R.length == 1) "0" + R else R
        G = if (G.length == 1) "0" + G else G
        B = if (B.length == 1) "0" + B else B
        sb.append("#")
        sb.append(R)
        sb.append(G)
        sb.append(B)
        return sb.toString()
    }

    private fun initAttr(attrs: AttributeSet?) {
        spanColor = mutableListOf()
        spanSize = mutableListOf()
        isBold = mutableListOf()
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SpanTextView)
            val colors = a.getString(R.styleable.SpanTextView_spanColor)?.split(",") ?: arrayListOf(toHexEncoding(currentTextColor))
            val sizes = a.getString(R.styleable.SpanTextView_spanSize)?.split(",") ?: arrayListOf((textSize / resources.displayMetrics.density).toString())
            val bolds = a.getString(R.styleable.SpanTextView_spanBold)?.split(",") ?: arrayListOf("0")
            val s = a.getString(R.styleable.SpanTextView_spanSeparator)
            initColors(colors)
            initSize(sizes)
            initBold(bolds)
            separator = if (s.isNullOrEmpty()) "%" else s
            a.recycle()
        } else {
            initEmpty()
        }
        init()
    }

   private fun initEmpty(){
       val colors = arrayListOf(toHexEncoding(currentTextColor))
       val sizes = arrayListOf((textSize / resources.displayMetrics.density).toString())
       val bolds = arrayListOf("0")
       initColors(colors)
       initSize(sizes)
       initBold(bolds)
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        spanSize?.clear()
        val sizes = arrayListOf((textSize / resources.displayMetrics.density).toString())
        initSize(sizes)
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        spanSize?.clear()
        val sizes = arrayListOf((textSize / resources.displayMetrics.density).toString())
        initSize(sizes)
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        spanColor?.clear()
        val colors = arrayListOf(toHexEncoding(currentTextColor))
        initColors(colors)
    }

    private fun initBold(bolds: List<String>) {
        bolds?.forEach {
            isBold!!.add(when (it) {
                "0" -> false
                else -> true
            })
        }
    }

    private fun initSize(sizes: List<String>) {
        sizes?.forEach { spanSize!!.add(it.toFloat()) }
    }

    private fun initColors(colors: List<String>) {
        colors?.forEach { spanColor!!.add(Color.parseColor(it)) }
    }

    private fun init() {
        val span = getSpannableString(text)
        text = span
    }

    private fun getSpannableString(text: CharSequence): SpannableString {
        try {
            baseText = text.toString()
            var content = text?.split(Regex(separator),0) ?: listOf("")
           /* content=content.filter { !it.isNullOrBlank() }
            var content1=content.filter { it.isNullOrBlank() }*/
            val s1 = text.replace(Regex(separator), "")
            val span = SpannableString(s1)
            if (content.size > 1) {
                for ((count, s) in (1 until content.size step 2).withIndex()) {
                    val start = getStringIndex(content, s)
                    val end = start + content[s].length
                    val sizeindex = if (count > spanSize!!.size - 1 && spanSize!!.size > 0) spanSize!!.size - 1 else count
                    val colorindex = if (count > spanColor!!.size - 1 && spanColor!!.size > 0) spanColor!!.size - 1 else count
                    val boldindex = if (count > isBold!!.size - 1 && isBold!!.size > 0) isBold!!.size - 1 else count
                    setSpanString(span, spanSize!![sizeindex].toInt(), spanColor!![colorindex]
                            , start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE, isBold!![boldindex])
                }
            }
            return span
        } catch (e: Exception) {
            return SpannableString(text)
        }
    }

    private fun getStringIndex(strings: List<String>, index: Int): Int {
        return (0 until index).sumBy { strings[it].length }
    }

    /**
     * 获取span格式字符串
     */
    private fun setSpanString(span: SpannableString, size: Int, colorid: Int, start: Int, end: Int, spanStyle: Int, bold: Boolean) {
        span.setSpan(AbsoluteSizeSpan(size, true), start, end, spanStyle)
        span.setSpan(ForegroundColorSpan(colorid), start, end, spanStyle)
        if (bold) {
            span.setSpan(StyleSpan(Typeface.BOLD), start, end, spanStyle)
        }
    }

    /**
     * 设置span格式文字
     */
    fun setSpanText(text: String) {
        setText(getSpannableString(text))
    }

    fun getSpanText(): String? {
        return text.toString()
    }

    /**
     * 设置span颜色
     */
    fun setSpanColor(vararg colors: String) {
        spanColor?.clear()
        initColors(colors.asList())
        setSpanText(baseText!!)
    }

    /**
     * 设置span文字大小
     */
    fun setSpanSize(vararg sizes: Float) {
        spanSize?.clear()
        spanSize = sizes.toMutableList()
        setSpanText(baseText!!)
    }

    /**
     * 设置span加粗
     */
    fun setSpanBold(vararg bolds: Boolean) {
        isBold?.clear()
        isBold = bolds.toMutableList()
        setSpanText(baseText!!)
    }

    /**
     * 设置分隔符
     */
    fun setSeparator(separator: String) {
        baseText = baseText?.replace(Regex(this.separator), separator)
        this.separator = separator
        setSpanText(baseText!!)
    }

    fun getSeparator() = separator
}
