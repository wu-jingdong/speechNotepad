package com.wjd.speechnotepad.home.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NoteFragmentAdapter extends FragmentPagerAdapter
{

	private Fragment[] fragments;

	private int count = 0;

	private String[] titles = null;

	public NoteFragmentAdapter(FragmentManager fm, int count, String[] titles)
	{
		super(fm);
		fragments = new Fragment[count];
		this.count = count;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int position)
	{
		if (null != fragments[position])
		{
			return fragments[position];
		} else
		{
			if (position == 0)
			{
				fragments[0] = NotesFragment.newInstance();
				return fragments[0];
			} else if (position == 1)
			{
				fragments[1] = CalendarFragment.newInstance();
				return fragments[1];
			} else if (position == 2)
			{
				fragments[2] = CreateFragment.newInstance();
				return fragments[2];
			}
			return null;
		}
	}

	@Override
	public int getCount()
	{
		return count;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return titles[position];
	}
}
