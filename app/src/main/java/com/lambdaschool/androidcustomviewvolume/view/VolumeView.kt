package com.lambdaschool.androidcustomviewvolume.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class VolumeView (context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paintBackgroundCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintKnobCircle = Paint(Paint.ANTI_ALIAS_FLAG)

    private var startX: Float = 0f
    private var endX: Float = 0f
    private var diffX: Float = 0f

    init {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // triggered when view is touched
                // get and store start point with event.getX()
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                // triggered after ACTION_DOWN but when touch is moved
                // get end point and calculate total distance traveled
                // use the total distance traveled to calculate the desired change in rotation
                // apply that change to your rotation variable
                // you may want to use a minimum and maximum rotation value to limit the rotation
                // use the new rotation to convert to the desired volume setting
                endX = event.x - startX + diffX
                invalidate() // this will cause the onDraw method to be called again with your new values
            }
            MotionEvent.ACTION_UP -> {
                // triggered when touch ends
                limitRange()
                Toast.makeText(context, "Volume Level: ${(endX/280f) * 100}%", Toast.LENGTH_LONG).show()
                diffX = endX
            }
        }
        return true // this indicates that the event has been processed
    }

    override fun onDraw(canvas: Canvas?) {

        val centerX = width/2f
        val centerY = width/2f
        val mainRadius = width/2.2f
        val borderRadius = mainRadius * .9f

        limitRange()
        canvas?.rotate(endX, centerX, centerY)

        paintBackgroundCircle.color = Color.BLACK
        canvas?.drawCircle(centerX, centerY, mainRadius, paintBackgroundCircle)

        paintKnobCircle.color = Color.WHITE
        canvas?.drawCircle(centerX - borderRadius *.5f, centerY + borderRadius*.6f, borderRadius *.15f, paintKnobCircle)

        super.onDraw(canvas)
    }

    private fun limitRange() {
        when {
            endX <= 0 -> endX = 0f
            endX >= 280f -> endX = 280f
        }
    }

    fun getVolume(): Float {
        return diffX
    }

    fun setVolume(volume: Float) {
        diffX = volume
    }
}