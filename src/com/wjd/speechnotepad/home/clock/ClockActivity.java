package com.wjd.speechnotepad.home.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.base.BaseActivity;
import com.wjd.speechnotepad.handler.MainHandler;
import com.wjd.speechnotepad.home.fragment.CreateFragment;
import com.wjd.speechnotepad.util.DateUtil;
import com.wjd.speechnotepad.util.Loger;

public class ClockActivity extends BaseActivity implements OnClickListener
{

	public static void actionLuanch(Activity activity)
	{
		activity.startActivity(new Intent(activity.getBaseContext(),
				ClockActivity.class));
	}

	private DatePicker mDatePicker;

	private TimePicker mTimePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speechnotepad_home_clock_layout);
		setSecondTitleBar(getString(R.string.select_clock));
		mDatePicker = (DatePicker) findViewById(R.id.datePicker1);
		mTimePicker = (TimePicker) findViewById(R.id.timePicker1);
		mTimePicker.setIs24HourView(true);
		mTimePicker
				.setCurrentHour(DateUtil.getHour(System.currentTimeMillis()));
		findViewById(R.id.btn_confirm).setOnClickListener(this);
	}

	private void doConfirm()
	{
		int year = mDatePicker.getYear();
		int month = mDatePicker.getMonth() + 1;
		int day = mDatePicker.getDayOfMonth();

		int hour = mTimePicker.getCurrentHour();
		int minute = mTimePicker.getCurrentMinute();
		String strTime = new StringBuffer().append(year).append("-")
				.append(month).append("-").append(day).append(" ").append(hour)
				.append(":").append(minute).append(":00").toString();
		Loger.print(this.getClass().getSimpleName(), "strTime ============= "
				+ strTime, Loger.INFO);
		long time = DateUtil.parse(DateUtil.FMT_YMDHMS, strTime);
		if (time <= System.currentTimeMillis())
		{
			Toast.makeText(getBaseContext(), R.string.select_time_lager,
					Toast.LENGTH_LONG).show();
			return;
		}
		MainHandler
				.instance()
				.obtainMessage(
						MainHandler.getIntKey(CreateFragment.class.getName()),
						CreateFragment.INNER_PICK_TIME, 0, time).sendToTarget();
		finish();
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btn_confirm)
		{
			doConfirm();
		}
	}
}
