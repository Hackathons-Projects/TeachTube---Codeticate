package com.hackathon.teachtube.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hackathon.teachtube.R;

public class CustomViewTrapezium extends View {

    private int mTrapeziumHeightLeft;
    private int mTrapeziumHeightRight;

    private Paint mPaint;
    private Path mPath;

    public CustomViewTrapezium(Context context) {
        super(context);
        init(null);
    }

    public CustomViewTrapezium(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomViewTrapezium(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomViewTrapezium(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set)
    {
        mPaint = new Paint();
        mPath = new Path();

        if (set == null)
            return;

        TypedArray typedArray = getContext().obtainStyledAttributes(set , R.styleable.CustomViewTrapezium);
        mTrapeziumHeightLeft = typedArray.getDimensionPixelSize(R.styleable.CustomViewTrapezium_height_left , 0);
        mTrapeziumHeightRight = typedArray.getDimensionPixelSize(R.styleable.CustomViewTrapezium_height_right , 0);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.moveTo(0 , 0);
        mPath.lineTo(0 , mTrapeziumHeightLeft);
        mPath.lineTo(getWidth() , mTrapeziumHeightRight);
        mPath.lineTo(getWidth(), 0);
        mPath.lineTo(0 , 0);

        mPaint.setColor(getResources().getColor(R.color.TrapeziumColor));

        canvas.drawPath(mPath , mPaint);

    }
}
