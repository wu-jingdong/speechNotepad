package com.wjd.speechnotepad;

import android.app.Activity;
import android.os.Bundle;

import com.wjd.speechnotepad.handler.DeliveredEntity;
import com.wjd.speechnotepad.handler.MainHandler;
import com.wjd.speechnotepad.handler.PostListener;
import com.wjd.speechnotepad.home.HomeActivity;
import com.wjd.speechnotepad.util.FileUtil;

public class MainActivity extends Activity implements PostListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainHandler.instance().registListener(this.hashCode(), this,
				MainActivity.class.getName());
		MainHandler.instance().sendEmptyMessageDelayed(
				MainHandler.getIntKey(MainActivity.class.getName()), 3000);
		FileUtil.newInstance().initDir(getBaseContext());
	}

	@Override
	public void onPostListener(DeliveredEntity postEntity)
	{
		HomeActivity.actionLuanch(this);
		finish();
	}

	@Override
	protected void onDestroy()
	{
		MainHandler.instance().unRegistListener(this.hashCode());
		super.onDestroy();
	}
}
