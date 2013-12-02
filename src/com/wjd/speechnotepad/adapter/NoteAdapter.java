package com.wjd.speechnotepad.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.util.DateUtil;

public class NoteAdapter extends BaseAdapter
{

	private List<NotepadEntity> notes = null;

	private Context context = null;

	private OnClickListener click;

	public NoteAdapter(Context context, List<NotepadEntity> notes,
			OnClickListener click)
	{
		this.context = context;
		this.notes = notes;
		this.click = click;
	}

	@Override
	public int getCount()
	{
		return notes.size();
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
		ViewHolder holder = null;
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.speechnotepad_notes_listitem_layout, null);
			holder = new ViewHolder();
			holder.btnAudio = (Button) convertView.findViewById(R.id.btn_audio);
			holder.btnClock = (Button) convertView
					.findViewById(R.id.ibtn_clock);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btnAudio.setText(getItem(position).getDuration() + "\'");
		holder.btnClock.setVisibility(TextUtils.isEmpty(getItem(position)
				.getNoticeTime()) ? View.GONE : View.VISIBLE);
		holder.btnClock.setTag(position);
		holder.btnClock.setOnClickListener(click);
		long time = Long.parseLong(getItem(position).getId());
		holder.tvTime
				.setText(DateUtil.format(getItem(position).getId(), DateUtil
						.isToday(time) ? DateUtil.FMT_HMS : DateUtil.FMT_YMDHM));
		return convertView;
	}

	class ViewHolder
	{
		Button btnAudio;

		Button btnClock;

		TextView tvTime;
	}

}
