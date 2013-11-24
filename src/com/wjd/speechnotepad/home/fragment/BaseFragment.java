package com.wjd.speechnotepad.home.fragment;

import com.wjd.speechnotepad.MainApp;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment
{

	protected MainApp getApp()
	{
		return (MainApp) getActivity().getApplication();
	}
}
