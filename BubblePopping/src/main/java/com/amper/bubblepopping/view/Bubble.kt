package com.amper.bubblepopping.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.amper.bubblepopping.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.abs
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.M)
class Bubble(context: Context): CircleImageView(context) {
    companion object {
        const val SCALE = 3
        const val MAX_SIZE = 75
        const val MIN_SIZE = 15
    }

    private var bubbleSize: Int = 0

    private var name: String = ""

    private var picture: String? = ""

    private var animator: ValueAnimator? = null

    private var isImageLoaded = false

    init {
        foreground = ContextCompat.getDrawable(context, R.drawable.ic_bubble)

    }

    fun bubbleItem(picture: Uri): Bubble{
        this.picture = picture.toString()
        return this
    }

    override fun performClick(): Boolean {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
        resetView()
        return super.performClick()
    }

    fun bubbleItem(picture: Uri, name: String): Bubble{
        this.name = name
        this.picture = picture.toString()
        return this
    }

    override fun onAttachedToWindow() {
        inflateImage(picture!!)
        super.onAttachedToWindow()
    }

    fun update() {
        generateBubbleSize()
        println("update: BubbleSize = $bubbleSize")
        val screen = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        screen.getSize(size)
        x = abs(Random.nextFloat() * size.x - (bubbleSize/2))
        y = size.y * 1F
        animator = moveAnimation(size.y)
        animator?.start()
        println("onAttachedToWindow ${size.x} $x , $y")
    }

    private fun generateBubbleSize() {
        bubbleSize = (Random.nextInt((MAX_SIZE - MIN_SIZE) + 1) + MIN_SIZE) * SCALE
        layoutParams = RelativeLayout.LayoutParams(
            bubbleSize,
            bubbleSize
        )
        borderWidth = (bubbleSize/15 * SCALE)
        borderColor = Color.TRANSPARENT
    }

    private fun inflateImage(picture: String) {
        if(picture.isEmpty() || isImageLoaded) {
            update()
            return
        }
        println("inflateImage $picture")
        Glide.with(this)
            .load(picture)
            .listener(object: RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    update()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    update()
                    isImageLoaded = true
                    return false
                }
            })
            .into(this)
    }



    private fun moveAnimation(screenHeight: Int): ValueAnimator {
        val radius = (bubbleSize/2)
        val animator: ValueAnimator = ValueAnimator.ofFloat(screenHeight.toFloat(), screenHeight / 2F - radius * 10)
        with(animator) {
            addUpdateListener { animation ->
                y = animation.animatedValue as Float
            }
            doOnEnd {
                val layout = (parent as ViewGroup)
                layout.removeView(this@Bubble)
                println("onEnd $name")
//                layout.addView(this@Bubble)
            }
            duration = 3000L + 100L * radius.toLong()
            interpolator = LinearInterpolator()
        }
        return animator
    }

    fun resetView(){
        animator?.end()
    }

}