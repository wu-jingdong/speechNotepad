package com.wjd.speechnotepad.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{

	public static final String FMT_YMDHMS = "yyyy-MM-dd hh:mm:ss";

	public static final String FMT_HMS = "hh:mm:ss";

	public static final String FMT_YMDHM = "yyyy-MM-dd hh:mm";

	public static String format(String longTime, String format)
	{
		return new SimpleDateFormat(format, Locale.getDefault())
				.format(new Date(Long.valueOf(longTime)));
	}

	public static String format(long longTime, String format)
	{
		return new SimpleDateFormat(format, Locale.getDefault())
				.format(new Date(longTime));
	}

	public static long parse(String format, String strTime)
	{
		try
		{
			return new SimpleDateFormat(format, Locale.getDefault()).parse(
					strTime).getTime();
		} catch (ParseException e)
		{
			return -1;
		}
	}

	public static boolean isToday(long timestamp)
	{
		long curTime = System.currentTimeMillis();
		long dayTime = 24 * 36000 * 1000;
		if (Math.abs(curTime - timestamp) <= dayTime)
		{
			if (Calendar.getInstance(Locale.getDefault()).get(
					Calendar.DAY_OF_MONTH) == getDay(timestamp))
			{
				return true;
			}
		}
		return false;
	}

	public static int getDay(long timestamp)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(long timestamp)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMonth(long timestamp)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getYear(long time)
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(time);
		return cal.get(Calendar.YEAR);
	}
}
