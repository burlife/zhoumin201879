package com.bawi.zhoumin201879;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by 1 on 2018/7/9.
 */

public class ScratchTextView extends android.support.v7.widget.AppCompatTextView{
    private static final String TAG = "ScratchTextView";
    //最外层的图片
    private Bitmap mbitmap;
    //画布
    private Canvas mCanvas;
    //画笔
    private Paint mPaint;
    //线
    private Path mPath;
    private float mX, mY;
    private float TOUCH_TOLERANCE;
    //判断时覆盖
    private boolean isInited = false;
    public ScratchTextView(Context context) {
        super(context);
    }

    public ScratchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScratchTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInited) {
           //把线画到mCanvas上,mCanva会把线画到mBitmap
            mCanvas.drawPath(mPath, mPaint);
            canvas.drawBitmap(mbitmap, 0, 0, null);
        }

    }
    public void initScratchCard(final int bgColor, final int paintStrokeWidth, float touchTolerance) {
        TOUCH_TOLERANCE = touchTolerance;
        //创建画笔
        mPaint = new Paint();
        mPaint.setAlpha(240);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        //画笔类型
        mPaint.setStyle(Paint.Style.STROKE);
        //画笔接洽点儿的类型
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //笔刷类型
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //笔刷宽度
        mPaint.setStrokeWidth(paintStrokeWidth);

        mPath = new Path();
        //建立一个空的Bit
        mbitmap = Bitmap.createBitmap(getLayoutParams().width, getLayoutParams().height, Bitmap.Config.ARGB_8888);
        //生成画布
        mCanvas = new Canvas(mbitmap);
        //背景图片字体
        Paint paint = new Paint();
        //字体大小
        paint.setTextSize(50);///字体大小
        paint.setColor(Color.parseColor("#ff0717"));
        //背景色
        mCanvas.drawColor(bgColor);

        //从资源文件中获取图片
        Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.guaguale);
        mCanvas.drawBitmap(photo, 0 , 1 , paint);

        //设置外层的文字。
       // mCanvas.drawText("呱呱看", getLayoutParams().width / 4, getLayoutParams().height / 2 + 15, paint);
        isInited = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isInited) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //隐藏之前的痕迹
                mPath.reset();
                //用mPath绘制起点
                mPath.moveTo(event.getX(), event.getY());
                mX = event.getX();
                mY = event.getY();
                //更新界面
                invalidate();
                Log.d(TAG, mX+"|"+mY);
                break;
            case MotionEvent.ACTION_MOVE:
                //x和y移动的距离
                float dx = Math.abs(event.getX() - mX);
                float dy = Math.abs(event.getY() - mY);
                //x,y移动的距离大于画线容差
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    // 二次贝塞尔，实现平滑曲线；mX, mY为操作点，(x + mX) / 2, (y + mY) / 2为终点
                    mPath.quadTo(mX, mY, (event.getX() + mX) / 2, (event.getY() + mY) / 2);
                    // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
                    mX = event.getX();
                    mY = event.getY();
                    Log.d(TAG, mX+"|"+mY);
                    invalidate();
                    break;
                }
        }
        return true;
    }
}
