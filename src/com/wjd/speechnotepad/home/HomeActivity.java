package com.wjd.speechnotepad.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.wjd.speechnotepad.MainApp;
import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.home.fragment.NoteFragmentAdapter;
import com.wjd.speechnotepad.pageIndicator.TitlePageIndicator;
import com.wjd.speechnotepad.pageIndicator.TitlePageIndicator.IndicatorStyle;

public class HomeActivity extends FragmentActivity
{

	public static void actionLuanch(Activity ac)
	{
		ac.startActivity(new Intent(ac.getBaseContext(), HomeActivity.class));
	}

	private TitlePageIndicator pageIndicator;

	private NoteFragmentAdapter adapter;

	private ViewPager page;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speechnotepad_home_activity_layout);
		page = (ViewPager) findViewById(R.id.pager);
		adapter = new NoteFragmentAdapter(getSupportFragmentManager(), 3,
				getResources().getStringArray(R.array.tab_titles));
		page.setAdapter(adapter);
		pageIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pageIndicator.setViewPager(page);
		pageIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			((MainApp) getApplication()).recycle();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setCurPage(int idx)
	{
		page.setCurrentItem(idx);
		adapter.getItem(idx).updateView();
	}
}
