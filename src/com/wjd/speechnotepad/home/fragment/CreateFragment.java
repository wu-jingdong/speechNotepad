package com.wjd.speechnotepad.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjd.speechnotepad.R;

public class CreateFragment extends BaseFragment
{

	public static CreateFragment newInstance()
	{
		return new CreateFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = LayoutInflater.from(getActivity().getBaseContext())
				.inflate(R.layout.speechnotepad_create_fragment_layout, null);
		return view;
	}
}