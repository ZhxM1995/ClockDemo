package com.zhx.clockdemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import kotlin.math.cos
import kotlin.math.sin


/**
 * author: zhx
 * date: 2020/10/13
 * description:
 */
class ClockView : View {

    var mPaint = Paint()
    var paintNum = Paint()
    var paintHours = Paint()
    var paintMinute = Paint()
    var paintSecond = Paint()

    var xx: Float = 0f
    var yy: Float = 0f
    var r = 0f

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas?.save()
//        canvas?.restore()
//        canvas?.translate(0f, 0f)
//        canvas?.rotate(0f)
        canvas?.drawCircle(xx, yy, r, mPaint)
        canvas?.drawCircle(xx, yy, 15f, paintMinute)
        if (canvas != null) {
            drawDial(canvas)
            drawNum(canvas)
            initCurrentTime(canvas)
        }
        canvas?.drawCircle(xx, yy, 15f, paintMinute)
        postInvalidateDelayed(1000)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val width = measuredWidth
        xx = (width / 2).toFloat()
        yy = (height / 2).toFloat()
        r = xx - 5
    }

    private fun initPaint() {
        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 3f
        mPaint.style = Paint.Style.STROKE

        paintMinute.color = Color.BLACK
        paintMinute.isAntiAlias = true
        paintMinute.strokeWidth = 8f
        paintMinute.style = Paint.Style.FILL

        paintNum.color = Color.BLACK
        paintNum.isAntiAlias = true
        paintNum.strokeWidth = 3f
        paintNum.textSize = 35f
        paintNum.style = Paint.Style.STROKE
        paintNum.textAlign = Paint.Align.CENTER

        paintSecond.color = Color.RED
        paintSecond.isAntiAlias = true
        paintSecond.strokeWidth = 5f
        paintSecond.style = Paint.Style.FILL

        paintMinute.color = Color.BLACK
        paintMinute.isAntiAlias = true
        paintMinute.strokeWidth = 8f
        paintMinute.style = Paint.Style.FILL

        paintHours.apply {
            color = Color.BLACK
            isAntiAlias = true
            strokeWidth = 13f
            style = Paint.Style.FILL
        }
    }

    private fun drawDial(canvas: Canvas) {
        for (i in 0..59) {
            if (i % 5 == 0) {
                //绘制整点刻度
                mPaint.strokeWidth = 8f
                canvas.drawLine(xx, yy - r, xx, yy - r + 40, mPaint)
            } else {
                //绘制分钟刻度
                mPaint.strokeWidth = 3f
                canvas.drawLine(xx, yy - r, xx, yy - r + 40, mPaint)
            }
            //绕着(x,y)旋转6°
            canvas.rotate(6f, xx, yy)
        }
    }

    private fun drawNum(canvas: Canvas) {
        // 获取文字高度用于设置文本垂直居中
        val textSize =
            paintNum.fontMetrics.bottom - paintNum.fontMetrics.top
        // 数字离圆心的距离,40为刻度的长度,20文字大小
        val distance = (r - 40 - 20).toInt()
        // 数字的坐标(a,b)
        var a: Float
        var b: Float
        // 每30°写一个数字
        for (i in 0..11) {
            a = (distance * sin(i * 30 * Math.PI / 180) + xx).toFloat()
            b = (yy - distance * cos(i * 30 * Math.PI / 180)).toFloat()
            if (i == 0) {
                canvas.drawText(i.toString(), a, b + textSize / 3, paintNum)
            } else {
                canvas.drawText(i.toString(), a, b + textSize / 3, paintNum)
            }
        }
    }

    private fun initCurrentTime(canvas: Canvas) {
        val format = SimpleDateFormat("HH-mm-ss")
        var time = format.format(System.currentTimeMillis())
        var timeSplitList = time.split("-")
        var hour = timeSplitList[0].toInt()
        var minute = timeSplitList[1].toInt()
        var second = timeSplitList[2].toInt()
        var hourAngle = hour * 30 + minute / 2
        var minuteAngle = minute * 6 + second / 10
        var secondAngle = second * 6

        canvas.rotate(hourAngle.toFloat(), xx, yy)
        canvas.drawLine(xx, yy, xx, yy - r + 150, paintHours)
        canvas.save()
        canvas.restore()
        canvas.rotate((-hourAngle).toFloat(), xx, yy)

        canvas.rotate(minuteAngle.toFloat(), xx, yy)
        canvas.drawLine(xx, yy, xx, yy - r + 60, paintMinute)
        canvas.save()
        canvas.restore()
        canvas.rotate((-minuteAngle).toFloat(), xx, yy)

        canvas.rotate(secondAngle.toFloat(), xx, yy)
        canvas.drawLine(xx, yy, xx, yy - r + 20, paintSecond)

    }


}