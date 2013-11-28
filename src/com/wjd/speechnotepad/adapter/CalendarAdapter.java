package com.wjd.speechnotepad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.entity.CalendarEntity;
import com.wjd.speechnotepad.util.DateUtil;

public class CalendarAdapter extends BaseAdapter
{

	private List<CalendarEntity> cls = null;

	private Context context = null;

	private int curmonth = 0;

	private int itemHeight = 0;

	private List<Integer> days = null;

	public CalendarAdapter(Context context, List<CalendarEntity> cls,
			long curTime, List<Integer> days)
	{
		this.context = context;
		this.cls = cls;
		this.curmonth = DateUtil.getMonth(curTime);
		this.days = days;
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
		this.curmonth = DateUtil.getMonth(curTime);
		notifyDataSetChanged();
	}

	public void notify(int totalHeight)
	{
		itemHeight = totalHeight * 7 / cls.size();
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
			vh.tvLeft = (TextView) convertView.findViewById(R.id.tv_left);
			vh.imgIndicator = (ImageView) convertView
					.findViewById(R.id.img_indicator);
			convertView.setTag(vh);
		} else
		{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.imgIndicator.setVisibility(View.GONE);
		vh.tvDayOfMonth.setText(String.valueOf(getItem(position).getDay()));
		if (getItem(position).getMonth() == curmonth)
		{
			vh.tvDayOfMonth.setTextColor(context.getResources().getColor(
					android.R.color.black));
			if (days.contains(DateUtil.getDay(getItem(position).getTimestamp())))
			{
				vh.imgIndicator.setVisibility(View.VISIBLE);
			}
		} else
		{
			vh.tvDayOfMonth.setTextColor(context.getResources().getColor(
					android.R.color.darker_gray));
		}

		if (getItem(position).isToday())
		{
			convertView.setBackgroundResource(R.color.bg_calendar_today);
		} else
		{
			convertView.setBackgroundResource(android.R.color.transparent);
		}

		LayoutParams params = (LayoutParams) convertView.getLayoutParams();
		if (null == params)
		{
			params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
		if (itemHeight > 0)
		{
			params.height = itemHeight;
		}
		convertView.setLayoutParams(params);
		vh.tvLeft.setVisibility(View.GONE);
		if (position % 7 == 0)
		{
			vh.tvLeft.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	class ViewHolder
	{
		TextView tvDayOfMonth;

		TextView tvLeft;

		ImageView imgIndicator;
	}
}
