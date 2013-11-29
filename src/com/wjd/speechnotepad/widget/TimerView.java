package com.wjd.speechnotepad.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.util.Loger;

public class TimerView extends View
{

	private Drawable clockRect;

	private Bitmap clockSecond;

	private Matrix matrix;

	private int mWidth;

	private int mHeight;

	private int time = 0;

	private int sWidth = 0;

	private int sHeight = 0;

	public TimerView(Context context)
	{
		super(context);
		initLayout();
	}

	public TimerView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initLayout();
	}

	public TimerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initLayout();
	}

	private void initLayout()
	{
		clockRect = getResources().getDrawable(R.drawable.ic_clock_rect);
		clockSecond = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_clock_second);
		mWidth = clockRect.getIntrinsicWidth();
		mHeight = clockRect.getIntrinsicHeight();
		sWidth = clockSecond.getWidth();
		sHeight = clockSecond.getHeight();
		matrix = new Matrix();
		matrix.setTranslate((mWidth - sWidth) / 2, mHeight / 2 - sHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(mWidth, mHeight);
	}

	public void invalidate(int time)
	{
		this.time = time;
		if (0 == time)
		{
			matrix = new Matrix();
			matrix.setTranslate((mWidth - sWidth) / 2, mHeight / 2 - sHeight);
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		clockRect.setBounds(0, 0, mWidth, mHeight);
		clockRect.draw(canvas);
		Loger.print(this.getClass().getSimpleName(), "rot", Loger.INFO);
		// matrix.reset();
		// matrix.postTranslate((mWidth - sWidth) / 2, mHeight / 2 - sHeight);
		matrix.preRotate(0 == time ? 0 : 6, sWidth / 2, sHeight);
		canvas.drawBitmap(clockSecond, matrix, null);
	}
}
