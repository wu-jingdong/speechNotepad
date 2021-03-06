package com.wjd.speechnotepad.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.adapter.NoteAdapter;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.util.AudioMsgUtil;
import com.wjd.speechnotepad.util.DateUtil;
import com.wjd.speechnotepad.util.FileUtil;

public class NotesFragment extends BaseFragment implements OnItemClickListener,
		OnClickListener
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
		lstNotes.setOnItemClickListener(this);
		adapter = new NoteAdapter(getActivity().getBaseContext(), notes, this);
		lstNotes.setAdapter(adapter);
		return view;
	}

	@Override
	public void updateView()
	{
		notes.clear();
		NotepadDbWrapper.getNotes(notes, getApp().db());
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy()
	{
		notes.clear();
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		AudioMsgUtil.getInstance().startPlay(
				FileUtil.newInstance().getFile(
						adapter.getItem(position).getAudioRoute(), false));
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.ibtn_clock)
		{
			String clock = DateUtil.format(notes.get((Integer) v.getTag())
					.getNoticeTime(), DateUtil.FMT_YMDHM);
			Toast.makeText(getActivity().getBaseContext(),
					getActivity().getString(R.string.cur_clock, clock),
					Toast.LENGTH_LONG).show();
		}
	}
}
