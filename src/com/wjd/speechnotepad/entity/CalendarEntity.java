package com.wjd.speechnotepad.entity;

import java.util.Calendar;
import java.util.Locale;

public class CalendarEntity
{

	private long timestamp;

	private int noteCount;

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public int getNoteCount()
	{
		return noteCount;
	}

	public void setNoteCount(int noteCount)
	{
		this.noteCount = noteCount;
	}

	public int getDay()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getMonth()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.MONTH) + 1;
	}

	public int getDayOfWeek()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
}
