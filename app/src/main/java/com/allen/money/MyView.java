package com.allen.money;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View {
    private int mExampleBc = Color.RED;
    private int mExampleBp = Color.GREEN;
    private int mExampleBl = Color.BLACK;
    private float mExampleDimension = 0;
    private int time = 60;
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private Paint paint;
    private float mTextWidth;
    private float mTextHeight;

    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyView, defStyle, 0);

        mExampleBc = a.getColor(
                R.styleable.MyView_exampleBc, Color.RED);
        mExampleBp = a.getColor(
                R.styleable.MyView_examplePc,
                Color.GREEN);
        mExampleBl = a.getColor(
                R.styleable.MyView_exampleLc,
                Color.BLACK);
        time = a.getInteger(
                R.styleable.MyView_exampleTime,
                60);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.

        if (a.hasValue(R.styleable.MyView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.MyView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        paint = new Paint();
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private int count = 60;
    private Handler handler;

    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);

//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        mTextHeight = fontMetrics.bottom;
        count = time;

        handler = new Handler(Looper.getMainLooper());


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.reset();

        int contentWidth = getWidth();
        int contentHeight = getHeight();

        int r = Math.min(contentHeight, contentWidth);

        paint.setColor(mExampleBl);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(contentWidth / 2, contentHeight / 2, r / 2 - 5, paint);

        paint.setColor(mExampleBp);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);

        RectF rectF = new RectF(30, 30, contentWidth - 30, contentWidth - 30);

        float st = 360f * count / time;
        canvas.drawArc(rectF, 630 - st, st, false, paint);


        SweepGradient sweepGradient = new SweepGradient(contentWidth / 2, contentHeight / 2, Color.RED, Color.CYAN);
        //LinearGradient linearGradient =new LinearGradient()
        paint.setColor(mExampleBc);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(sweepGradient);
        canvas.drawCircle(contentWidth / 2, contentHeight / 2, r / 2 - 60, paint);

        // Draw the text.

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(80, 80,
                    contentWidth - 80, contentHeight - 80);
            mExampleDrawable.draw(canvas);
        }
//
        if (count > 0) {
            count--;
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacks(runnable);
        super.onDetachedFromWindow();
    }
}
