# SimpleSpanTextView
### 只封装了常用的span,XML中完成所有操作，加粗，改色，改大小，用kotlin和java格式的都有
![image](https://github.com/q1987320aaa/SimpleSpanTextView/blob/master/image/效果图.png)
### 下面是实现的效果图，逻辑比较简单，只是将文字间的大小颜色设置利用字符串分隔实现
![image](https://github.com/q1987320aaa/SimpleSpanTextView/blob/master/image/GIF.gif)
# 使用方式
# xml实现
#### 下面两种只是实现方式不一样，逻辑都一样
```Java
<!--kotlin代码实现-->
    <com.huizhuang.zxsq.widget.textview.SpanTextView

        android:id="@+id/tv_spantext"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="常用的span格式%简单封装%
        ,XML中完成所有操作，%加粗%，%改色%，
        %改大小%，用kotlin和java格式的都有"
        android:textColor="#000000"
        android:textSize="14dp"
        app:spanColor="#22c2cc,#ffc2cc,#ff6c38,#ff5e5e"
        app:spanSize="14,18"
        app:spanBold="0,1,1,0"
        app:spanSeparator="%"
        />
    <!--java代码实现-->
    <com.example.spantextviewjava.SpanText
        android:id="@+id/tv_spantext_java"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"

        android:text="常用的span格式%简单封装%
        ,XML中完成所有操作，%加粗%，%改色%，
        %改大小%，用kotlin和java格式的都有"
        android:textColor="#000000"
        android:textSize="14dp"
        app:spanColor="#22c2cc,#ffc2cc,#ff6c38,#ff5e5e"
        app:spanSize="14,18"
        app:spanBold="0,1,1,0"
        app:spanSeparator="%"/>
```
## 部分核心代码

```Java
 private fun getSpannableString(text: CharSequence): SpannableString {
        try {
            baseText=text.toString()
            var content = text?.split(separator) ?: listOf("")
            val s1 = text.replace(Regex(separator), "")
            val span = SpannableString(s1)
            if (content.size > 1) {
                for ((count, s) in (1 until content.size step 2).withIndex()) {
                    val start = getStringIndex(content, s)
                    val end = start + content[s].length
                    val sizeindex = if (count > spanSize!!.size - 1) spanSize!!.size - 1 else count
                    val colorindex = if (count > spanColor!!.size - 1) spanColor!!.size - 1 else count
                    val boldindex = if (count > isBold!!.size - 1) isBold!!.size - 1 else count
                    setSpanString(span, spanSize!![sizeindex].toInt(), spanColor!![colorindex]
                            , start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE, isBold!![boldindex])
                }
            }
            return span
        } catch (e: Exception) {
            return SpannableString(text)
        }
    }
```
# 有问题请联系
QQ:1354982095
邮箱:1354982095@qq.com
