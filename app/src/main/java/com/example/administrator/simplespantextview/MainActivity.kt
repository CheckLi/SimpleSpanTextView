package com.example.administrator.simplespantextview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() ,View.OnClickListener{
    var buff:StringBuffer?=null
    override fun onClick(v: View) {
        when(v.id){
           R.id.btn_color ->{
               setColor()
           }
           R.id.btn_size ->{
               setSize()
           }
           R.id.btn_bold ->{
               setBold()
           }
           R.id.btn_insert ->{
               insert()
           }
           R.id.btn_chang_separator ->{
              chang()
           }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buff= StringBuffer("常用的span格式%简单封装%,XML中完成所有操作，%加粗%，%改色%，%改大小%，用kotlin和java格式的都有")
        setText(buff.toString())
    }

    private fun setText(content:String){
        tv_spantext.setSpanText(content)
        tv_spantext_java.setSpanText(content)
        tv_text.text = content
    }

    private fun setColor(){
        tv_spantext.setSpanColor("#22c2cc","#ffc2cc","#ff6c38","#ff5e5e")
        tv_spantext_java.setSpanColor("#22c2cc","#ffc2cc","#ff6c38","#ff5e5e")
    }

    private fun setSize(){
        tv_spantext.setSpanSize(14f,14f,14f,20f)
        tv_spantext_java.setSpanSize(14f,14f,14f,20f)
    }

    private fun setBold(){
        tv_spantext.setSpanBold(false,true,false,false)
        tv_spantext_java.setSpanBold(false,true,false,false)
    }

    private fun insert(){
        val length=(buff?.length?:1)-1
        val random=Random()
        var index=random.nextInt(length)
        buff?.insert(index,tv_spantext.getSeparator())
        index=if(index+2>length) index-2 else index+2
        buff?.insert(index,tv_spantext.getSeparator())
        setText(buff.toString())
    }

    val seps= arrayOf("@","-","bb","cc","!")
    private fun chang(){
        val se=seps[Random().nextInt(seps.size)]
        Log.e("chang",se)
        buff= StringBuffer(buff?.replace(Regex(tv_spantext.getSeparator()),se))
        tv_spantext.setSeparator(se)
        tv_spantext_java.separator = se
        setText(buff.toString())
    }
}
