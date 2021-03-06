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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.adapter.CalendarAdapter;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.CalendarEntity;
import com.wjd.speechnotepad.handler.DeliveredEntity;
import com.wjd.speechnotepad.handler.MainHandler;
import com.wjd.speechnotepad.handler.PostListener;
import com.wjd.speechnotepad.home.day.DayNoteActivity;
import com.wjd.speechnotepad.util.DateUtil;

public class CalendarFragment extends BaseFragment implements OnClickListener,
		PostListener, OnItemClickListener
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

	private LinearLayout lineWeekTitle = null;

	private List<Integer> days = new ArrayList<Integer>();

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
		// view.findViewById(R.id.btn_today).setOnClickListener(this);
		setTitleBar();
		lineWeekTitle = (LinearLayout) view.findViewById(R.id.line_week_title);

		gdCalendar = (GridView) view.findViewById(R.id.gd_calendar);
		gdCalendar.setOnItemClickListener(this);
		adapter = new CalendarAdapter(getActivity().getBaseContext(), cls,
				curTime, days);
		gdCalendar.setAdapter(adapter);

		MainHandler.instance().registListener(hashCode(), this,
				getClass().getName());
		sendGdHeightMessage();
		return view;
	}

	private void sendGdHeightMessage()
	{
		MainHandler.instance().sendEmptyMessageDelayed(
				MainHandler.getIntKey(getClass().getName()), 200);
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
		int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
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
		days.clear();
		NotepadDbWrapper.getDays(days, getApp().db(),
				String.valueOf(getMonthBeginTime()),
				String.valueOf(getMonthEndTime()));
	}

	private String getCurMonthStr()
	{
		return String.format(Locale.getDefault(), "%d年%d月",
				DateUtil.getYear(curTime), DateUtil.getMonth(curTime));
	}

	private String getLastMonthStr()
	{
		long lastMonthTime = getLastMonthTime();
		return String.format(Locale.getDefault(), "%d月",
				DateUtil.getMonth(lastMonthTime));
	}

	private String getNextMonthStr()
	{
		long nextMonthTime = getNextMonthTime();
		return String.format(Locale.getDefault(), "%d月",
				DateUtil.getMonth(nextMonthTime));
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

	private long getMonthBeginTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(curTime);
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		return curTime - dayTime * (curDay - 1) - hour * 3600 * 1000 - minute
				* 60 * 1000 - second * 1000;
	}

	private long getMonthEndTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(curTime);
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int monthDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return curTime - hour * 3600 * 1000 - minute * 60 * 1000 - second
				* 1000 + (monthDay - curDay + 1) * dayTime;
	}

	@Override
	public void updateView()
	{
		setTitleBar();
		initCalendar();
		adapter.notify(curTime);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		CalendarEntity ce = adapter.getItem(position);
		if (ce.getMonth() != DateUtil.getMonth(System.currentTimeMillis()))
		{
			return;
		}
		Integer day = ce.getDay();
		if (days.contains(day))
		{
			DayNoteActivity.actionLuanch(getActivity(),
					String.valueOf(ce.getTimestamp()));
		}
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
			// case R.id.btn_today:
			// curTime = System.currentTimeMillis();
			// updateView();
			// break;
			default:
				break;
		}
	}

	@Override
	public void onPostListener(DeliveredEntity postEntity)
	{
		int top = gdCalendar.getTop();
		int bottom = gdCalendar.getBottom();
		if (0 == top)
		{
			sendGdHeightMessage();
		} else
		{
			adapter.notify(bottom - top);
			lineWeekTitle.setPadding(0, 0, getScreenWidth() % 7, 0);
			lineWeekTitle.requestLayout();
		}
	}
}
