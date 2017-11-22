package com.example.administrator.simplespantextview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_spantext.setSpanText("《%遮天%》是一部古典仙侠类型的网络小说，%小说签约%授权首发连载于起点中文网，作者是辰东。" +
                "本书以九龙拉棺为引子，%带出一个%庞大的洪荒仙侠世界。")
        tv_spantext_java.setSpanText("《%遮天%》是一部古典仙侠类型的网络小说，%小说签约%授权首发连载于起点中文网，作者是辰东。" +
                "本书以九龙拉棺为引子，%带出一个%庞大的洪荒仙侠世界。")
    }
}
