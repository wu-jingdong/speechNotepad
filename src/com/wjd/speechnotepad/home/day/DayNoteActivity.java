package com.wjd.speechnotepad.home.day;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.wjd.speechnotepad.MainApp;
import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.adapter.NoteAdapter;
import com.wjd.speechnotepad.base.BaseActivity;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.util.AudioMsgUtil;
import com.wjd.speechnotepad.util.DateUtil;
import com.wjd.speechnotepad.util.FileUtil;

public class DayNoteActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener
{

	private static final String TIMESTAMP = "TIMESTAMP";

	public static void actionLuanch(Activity activity, String timestamp)
	{
		activity.startActivity(new Intent(activity.getBaseContext(),
				DayNoteActivity.class).putExtra(TIMESTAMP, timestamp));
	}

	private List<NotepadEntity> notes = new ArrayList<NotepadEntity>();

	private ListView lstNotes = null;

	private NoteAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.speechnotepad_day_notes_layout);
		setSecondTitleBar(getDayTitle());
		lstNotes = (ListView) findViewById(R.id.lst_note);
		lstNotes.setOnItemClickListener(this);
		adapter = new NoteAdapter(getBaseContext(), notes, this);
		lstNotes.setAdapter(adapter);
	}

	private void initData()
	{
		long timestamp = Long.valueOf(getIntent().getStringExtra(TIMESTAMP));
		NotepadDbWrapper.getDayNotes(notes, DateUtil.getYear(timestamp),
				DateUtil.getMonth(timestamp), DateUtil.getDay(timestamp),
				((MainApp) getApplication()).db());
	}

	private String getDayTitle()
	{
		long timestamp = Long.valueOf(getIntent().getStringExtra(TIMESTAMP));
		return String.format(Locale.getDefault(), "%d年%d月%d日",
				DateUtil.getYear(timestamp), DateUtil.getMonth(timestamp),
				DateUtil.getDay(timestamp));
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
	protected void onDestroy()
	{
		AudioMsgUtil.getInstance().stopPlay();
		notes.clear();
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		if (R.id.ibtn_clock == v.getId())
		{
			String clock = DateUtil.format(notes.get((Integer) v.getTag())
					.getNoticeTime(), DateUtil.FMT_YMDHM);
			Toast.makeText(getBaseContext(),
					getString(R.string.cur_clock, clock), Toast.LENGTH_LONG)
					.show();
		}
	}
}
