package com.wjd.speechnotepad.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjd.speechnotepad.R;

public class CalendarFragment extends BaseFragment
{

	public static CalendarFragment newInstance()
	{
		return new CalendarFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = LayoutInflater.from(getActivity().getBaseContext())
				.inflate(R.layout.speechnotepad_calendar_fragment_layout, null);
		return view;
	}
}
