package com.wjd.speechnotepad.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

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
		adapter = new NoteFragmentAdapter(getSupportFragmentManager(), 2);
		page.setAdapter(adapter);
		pageIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pageIndicator.setViewPager(page);
		pageIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	}
}
