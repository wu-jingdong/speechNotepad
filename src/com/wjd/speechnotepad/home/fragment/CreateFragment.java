package com.wjd.speechnotepad.home.fragment;

import com.wjd.speechnotepad.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateFragment extends Fragment
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
