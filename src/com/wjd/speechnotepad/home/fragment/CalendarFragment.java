package com.wjd.speechnotepad.home.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.adapter.CalendarAdapter;
import com.wjd.speechnotepad.entity.CalendarEntity;

public class CalendarFragment extends BaseFragment implements OnClickListener
{

	public static CalendarFragment newInstance()
	{
		return new CalendarFragment();
	}

	private GridView gdCalendar = null;

	private CalendarAdapter adapter = null;

	private List<CalendarEntity> cls = new ArrayList<CalendarEntity>();

	private Button btnLastMonth;

	private TextView tvCurMoth;

	private Button btnNextMonth;

	private long curTime = 0;

	private long dayTime = 24 * 3600 * 1000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		curTime = System.currentTimeMillis();
		initCalendar();
		View view = LayoutInflater.from(getActivity().getBaseContext())
				.inflate(R.layout.speechnotepad_calendar_fragment_layout, null);
		btnLastMonth = (Button) view.findViewById(R.id.btn_last_month);
		btnLastMonth.setOnClickListener(this);
		tvCurMoth = (TextView) view.findViewById(R.id.tv_cur_month);
		btnNextMonth = (Button) view.findViewById(R.id.btn_next_month);
		btnNextMonth.setOnClickListener(this);
		setTitleBar();

		gdCalendar = (GridView) view.findViewById(R.id.gd_calendar);
		adapter = new CalendarAdapter(getActivity().getBaseContext(), cls,
				curTime);
		gdCalendar.setAdapter(adapter);
		return view;
	}

	private void setTitleBar()
	{
		btnLastMonth.setText(getLastMonthStr());
		tvCurMoth.setText(getCurMonthStr());
		btnNextMonth.setText(getNextMonthStr());
	}

	private void initCalendar()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(curTime);
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		int weeks = cal.get(Calendar.WEEK_OF_MONTH);
		// 第一天的time
		long firstDayTime = curTime - dayTime * (curDay - 1);
		cal.setTimeInMillis(firstDayTime);
		// 第一天的星期
		int firstDayWeek = cal.get(Calendar.DAY_OF_WEEK);
		// 第一天和周日的间隔
		int range = firstDayWeek - 1;

		cls.clear();
		for (int i = 0, n = weeks * 7; i < n; ++i)
		{
			CalendarEntity calentity = new CalendarEntity();
			calentity
					.setTimestamp(curTime + dayTime * (1 + i - range - curDay));
			cls.add(calentity);
		}
	}

	private String getCurMonthStr()
	{
		return String.format(Locale.getDefault(), "%d年%d月", getYear(curTime),
				getMonth(curTime));
	}

	private String getLastMonthStr()
	{
		long lastMonthTime = getLastMonthTime();
		return String.format(Locale.getDefault(), "%d月",
				getMonth(lastMonthTime));
	}

	private String getNextMonthStr()
	{
		long nextMonthTime = getNextMonthTime();
		return String.format(Locale.getDefault(), "%d月",
				getMonth(nextMonthTime));
	}

	private long getLastMonthTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(curTime);
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		return curTime - dayTime * (curDay);
	}

	private long getNextMonthTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(curTime);
		int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		return curTime + dayTime * (count - curDay + 1);
	}

	private int getMonth(long time)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(time);
		return cal.get(Calendar.MONTH) + 1;
	}

	private int getYear(long time)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(time);
		return cal.get(Calendar.YEAR);
	}

	private void updateView()
	{
		setTitleBar();
		initCalendar();
		adapter.notify(curTime);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_last_month:
				curTime = getLastMonthTime();
				updateView();
				break;
			case R.id.btn_next_month:
				curTime = getNextMonthTime();
				updateView();
				break;
			default:
				break;
		}
	}
}
