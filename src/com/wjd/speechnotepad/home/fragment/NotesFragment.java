package com.wjd.speechnotepad.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.adapter.NoteAdapter;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.util.Loger;

public class NotesFragment extends BaseFragment
{

	public static NotesFragment newInstance()
	{
		return new NotesFragment();
	}

	private List<NotepadEntity> notes = new ArrayList<NotepadEntity>();

	private ListView lstNotes = null;

	private NoteAdapter adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		NotepadDbWrapper.getNotes(notes, getApp().db());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = LayoutInflater.from(getActivity().getBaseContext())
				.inflate(R.layout.speechnotepad_notes_fragment_layout, null);
		lstNotes = (ListView) view.findViewById(R.id.lst_note);
		lstNotes.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

			}
		});
		adapter = new NoteAdapter(getActivity().getBaseContext(), notes);
		lstNotes.setAdapter(adapter);
		return view;
	}

	@Override
	public void onDestroyView()
	{
		Loger.print(this.getClass().getName(),
				"onDestroyView ================ ", Loger.INFO);
		super.onDestroyView();
	}

	@Override
	public void onDestroy()
	{
		notes.clear();
		super.onDestroy();
	}

}
