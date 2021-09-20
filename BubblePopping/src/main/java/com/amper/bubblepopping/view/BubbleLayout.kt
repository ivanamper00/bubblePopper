package com.amper.bubblepopping.view

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.amper.bubblepopping.`interface`.OnListEndListener
import com.amper.bubblepopping.model.Products

class BubbleLayout:
    RelativeLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context,attributeSet,defStyle)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int, defStyleRef: Int): super(context,attributeSet,defStyle,defStyleRef)

    private var products = mutableListOf<Products>()
    var onListEndListener: OnListEndListener? = null
    var repeatList = true
    private var reservedList = mutableListOf<Products>()
    private var reservedItem: Products? = null
    private var loaded = false


    fun addItem(uri: Uri, name: String){
        addItem(uri.toString(), name)
    }

    fun addItem(uri: String, name: String){
        if(products.isEmpty()){
            products.add(Products(uri, name))
        }else {
            reservedItem = Products(uri, name)
        }
    }


    private fun addBubbleInstance(uri: String, name: String, onClickListener: OnClickListener){
        val bubble = Bubble(context)
        bubble.bubbleItem(Uri.parse(uri), name)
        bubble.setOnClickListener(onClickListener)
        addBubble(bubble)
    }

    fun addList(list: MutableList<Products>){
        if(products.isNotEmpty()) reservedList.addAll(list)
        else products.addAll(list)
    }

    override fun onAttachedToWindow() {
        onEnd()
        super.onAttachedToWindow()
    }

    private fun onEnd() {
        if(reservedList.isNotEmpty()){
            println("emptyList")
            products.clear()
            products.addAll(reservedList)
            reservedList.clear()
        }
        if(reservedItem != null){
            products.add(reservedItem!!)
            reservedItem = null
        }

        repeatList(repeatList)
    }

    private fun repeatList(repeat: Boolean){
        if(repeat) {
            for(i in products.indices){
                handler.postDelayed({
                    val it = products[i]
                    addBubbleInstance(it.picture!!,it.name!!,it.onClick!!)
                    if(i == products.lastIndex) onEnd()
                }, 200L * i)
            }
        }
    }

    private fun addBubble(bubbleView: View){
        addView(bubbleView)
    }


}