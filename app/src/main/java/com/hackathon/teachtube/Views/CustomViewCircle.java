package com.hackathon.teachtube.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hackathon.teachtube.R;


public class CustomViewCircle extends View {

    private int mCircleSize;
    private int mCircleColor;

    private Paint mPaint;


    public CustomViewCircle(Context context) {
        super(context);
        init(null);
    }

    public CustomViewCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomViewCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomViewCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set)
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        if (set == null)
            return;

        TypedArray typedArray = getContext().obtainStyledAttributes(set , R.styleable.CustomViewCircle);
        mCircleSize = typedArray.getDimensionPixelSize(R.styleable.CustomViewCircle_circle_size , 0);
        mCircleColor = typedArray.getColor(R.styleable.CustomViewCircle_circle_color , getResources().getColor(R.color.HolidayColor));

        mPaint.setColor(mCircleColor);

        typedArray.recycle();
    }

    public void setCircleColor(int color)
    {
        mPaint.setColor(color);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2, cy = getHeight() / 2;

        canvas.drawCircle(cx , cy , mCircleSize , mPaint);

    }
}
