package com.wjd.speechnotepad.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wjd.speechnotepad.R;

public class BaseActivity extends Activity
{

	protected void setSecondTitleBar(String title)
	{
		findViewById(R.id.btn_bar_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						finish();
					}
				});
		((TextView) findViewById(R.id.tv_bar_title)).setText(title);
	}
}
