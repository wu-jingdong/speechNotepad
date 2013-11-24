package com.wjd.speechnotepad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.entity.NotepadEntity;

public class NoteAdapter extends BaseAdapter
{

	private List<NotepadEntity> notes = null;

	private Context context = null;

	public NoteAdapter(Context context, List<NotepadEntity> notes)
	{
		this.context = context;
		this.notes = notes;
	}

	@Override
	public int getCount()
	{
		return 50;
	}

	@Override
	public NotepadEntity getItem(int position)
	{
		return notes.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.speechnotepad_notes_listitem_layout, null);
		}
		return convertView;
	}

}
