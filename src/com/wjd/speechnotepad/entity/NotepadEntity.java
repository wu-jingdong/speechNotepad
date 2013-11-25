package com.wjd.speechnotepad.entity;

public class NotepadEntity
{

	private String id;

	private String audioRoute;

	private String photoRoute;

	private String noticeTime;

	private int duration;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getAudioRoute()
	{
		return audioRoute;
	}

	public void setAudioRoute(String audioRoute)
	{
		this.audioRoute = audioRoute;
	}

	public String getPhotoRoute()
	{
		return photoRoute;
	}

	public void setPhotoRoute(String photoRoute)
	{
		this.photoRoute = photoRoute;
	}

	public String getNoticeTime()
	{
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime)
	{
		this.noticeTime = noticeTime;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

}
