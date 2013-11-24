package com.wjd.speechnotepad.home.fragment;

import com.wjd.speechnotepad.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotesFragment extends Fragment
{
	
	public static NotesFragment newInstance()
	{
		return new NotesFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = LayoutInflater.from(getActivity().getBaseContext())
				.inflate(R.layout.speechnotepad_notes_fragment_layout, null);
		return view;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
