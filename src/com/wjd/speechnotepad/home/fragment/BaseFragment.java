package com.wjd.speechnotepad.home.fragment;

import com.wjd.speechnotepad.MainApp;
import com.wjd.speechnotepad.home.HomeActivity;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment
{

	protected MainApp getApp()
	{
		return (MainApp) getActivity().getApplication();
	}

	@SuppressWarnings("deprecation")
	protected int getScreenWidth()
	{
		return getActivity().getWindowManager().getDefaultDisplay().getWidth();
	}

	protected int dp2px(int dip)
	{
		float scale = getActivity().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	protected HomeActivity getParent()
	{
		return (HomeActivity) getActivity();
	}

	public abstract void updateView();
}
