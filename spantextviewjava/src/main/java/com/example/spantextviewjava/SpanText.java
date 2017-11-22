package com.example.spantextviewjava;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @className:SpanText
 * @description:
 * @author: JIAMING.LI
 * @date:2017年11月22日 14:52
 */
public class SpanText extends android.support.v7.widget.AppCompatTextView{
    private List<Integer> spanColor;
    private List<Float> spanSize;
    private List<Boolean> isBold;
    private String separator = "%";//字符串分隔符
    private String baseText;
    public SpanText(Context context) {
        super(context);
        initAttr(null);
    }

    public SpanText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public SpanText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }




    /**
     * 将颜色转换成#开头的16进制颜色，为了得到当前颜色
     */
    private String toHexEncoding(int color){
        String R = Integer.toHexString(Color.red(color));
        String G = Integer.toHexString(Color.green(color));
        String B = Integer.toHexString(Color.blue(color));
        StringBuffer sb =new StringBuffer("");
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
         if (R.length() == 1) R ="0" + R;
         if (G.length() == 1) G ="0" + G;
         if (B.length() == 1) B ="0" + B;
        sb.append("#");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }

    private void initAttr(AttributeSet attrs) {
        spanColor =new ArrayList<>();
        spanSize = new ArrayList<>();
        isBold = new ArrayList<>();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpanText);
            String tempColor=a.getString(R.styleable.SpanText_spanColor);
            String tempSize=a.getString(R.styleable.SpanText_spanSize);
            String tempBold=a.getString(R.styleable.SpanText_spanBold);
            String[] colors = TextUtils.isEmpty(tempColor)?new String[]{toHexEncoding(getCurrentTextColor())}:tempColor.split(",");
            String[] sizes = TextUtils.isEmpty(tempSize)?new String[]{String.valueOf(getTextSize() / getResources().getDisplayMetrics().density)}:
            tempSize.split(",");
            String[] bolds = TextUtils.isEmpty(tempSize)?new String[]{"0"}:tempBold.split(",");
            String s = a.getString(R.styleable.SpanText_spanSeparator);
            initColors(colors);
            initSize(sizes);
            initBold(bolds);
             if (TextUtils.isEmpty(s)) separator ="%"; else separator =s;
            a.recycle();
        }
        init();
    }

    private void initBold(String[] bolds) {
        for (String s : bolds) {
            switch(s){
                case "0":
                    isBold.add(false);
                    break;

                default:
                    isBold.add(true);
                    break;
            }
        }
    }

    private void initSize(String[] sizes) {
        for (String size : sizes) {
            try{
                spanSize.add(Float.parseFloat(size));
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    private void initColors(String[] colors) {
        for (String color : colors) {
            spanColor.add(Color.parseColor(color));
        }
    }

    private void init() {
        SpannableString span = getSpannableString(getText());
        setText(span);
    }

    private SpannableString getSpannableString( CharSequence text1) {
        try {
            String text=text1.toString();
            baseText=text;
            String[] content = text.split(separator);
            String s1 = text.replaceAll(separator, "");
            SpannableString span =new SpannableString(s1);
            int lenght=content.length;
            if (lenght > 1) {
                int count=0;
                for (int i = 1; i <lenght; i+=2) {
                    int start = getStringIndex(content, i);
                    int end = start + content[i].length();
                    int sizeindex=count>spanSize.size()-1? spanSize.size()-1:count;
                    int colorindex=count>spanColor.size()-1? spanColor.size()-1:count;
                    int boldindex=count>isBold.size()-1? isBold.size()-1:count;
                    setSpanString(span, spanSize.get(sizeindex), spanColor.get(colorindex)
                            , start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE, isBold.get(boldindex));
                    count++;
                }

            }
            return span;
        } catch (Exception e) {
            return new SpannableString(getText().toString());
        }
    }

    private int getStringIndex(String[] strings ,int index) {
        int sum=0;
        for (int i = 0; i <index ; i++) {
            sum+=strings[i].length();
        }
        return sum;
    }

    /**
     * 获取span格式字符串
     */
    void setSpanString(SpannableString span , float size, int colorid, int start, int end, int spanStyle, boolean bold) {
        span.setSpan(new AbsoluteSizeSpan((int) size, true), start, end, spanStyle);
        span.setSpan(new ForegroundColorSpan(colorid), start, end, spanStyle);
        if (bold) {
            span.setSpan(new StyleSpan(Typeface.BOLD), start, end, spanStyle);
        }
    }

    /**
     * 设置span格式文字
     */
    public void setSpanText(String text ) {
        setText( getSpannableString(text));
    }

    /**
     * 设置span颜色
     */
    public void setSpanColor(String...colors) {
        initColors(colors);
        setSpanText(baseText);
    }

    /**
     * 设置span文字大小
     */
    public void setSpanSize(Float...sizes) {
        spanSize =Arrays.asList(sizes);
        setSpanText(baseText);
    }

    /**
     * 设置span加粗
     */
    public void setSpanBold(Boolean...bolds ) {
        isBold =Arrays.asList(bolds);
        setSpanText(baseText);
    }

    /**
     * 设置分隔符
     */
    public void setSeparator(String separator ) {
        baseText=baseText.replaceAll(this.separator,separator);
        this.separator = separator;
        setSpanText(baseText);
    }

    public String getSeparator() {
        return separator;
    }
}
