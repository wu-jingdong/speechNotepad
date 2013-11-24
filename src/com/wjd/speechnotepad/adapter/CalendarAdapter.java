package com.wjd.speechnotepad.adapter;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.entity.CalendarEntity;

public class CalendarAdapter extends BaseAdapter
{

	private List<CalendarEntity> cls = null;

	private Context context = null;

	private int curmonth = 0;

	public CalendarAdapter(Context context, List<CalendarEntity> cls,
			long curTime)
	{
		this.context = context;
		this.cls = cls;
		initCurMonth(curTime);
	}

	private void initCurMonth(long time)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(time);
		this.curmonth = cal.get(Calendar.MONTH) + 1;
	}

	@Override
	public int getCount()
	{
		return cls.size();
	}

	@Override
	public CalendarEntity getItem(int position)
	{
		return cls.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void notify(long curTime)
	{
		initCurMonth(curTime);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder vh = null;
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.speechnotepad_calendar_listitem_layout, null);
			vh = new ViewHolder();
			vh.tvDayOfMonth = (TextView) convertView
					.findViewById(R.id.tv_day_of_month);
			convertView.setTag(vh);
		} else
		{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tvDayOfMonth.setText(String.valueOf(getItem(position).getDay()));
		if (getItem(position).getMonth() == curmonth)
		{
			vh.tvDayOfMonth.setTextColor(context.getResources().getColor(
					android.R.color.black));
		} else
		{
			vh.tvDayOfMonth.setTextColor(context.getResources().getColor(
					android.R.color.darker_gray));
		}
		return convertView;
	}

	class ViewHolder
	{
		TextView tvDayOfMonth;
	}
}
