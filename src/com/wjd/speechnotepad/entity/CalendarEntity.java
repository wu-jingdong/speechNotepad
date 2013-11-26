package com.wjd.speechnotepad.entity;

import com.wjd.speechnotepad.util.DateUtil;

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

	public int getMonth()
	{
		return DateUtil.getDay(timestamp);
	}

	public int getDay()
	{
		return DateUtil.getDay(timestamp);
	}

	public boolean isToday()
	{
		return DateUtil.isToday(timestamp);
	}
}
