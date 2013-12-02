package com.wjd.speechnotepad.home.clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wjd.speechnotepad.MainApp;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.handler.MainHandler;

public class AlarmReceiver extends BroadcastReceiver
{

	public static final String ACTION_ALARM = "com.wjd.speechnotepad.home.clock.AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (ACTION_ALARM.equals(intent.getAction()))
		{
			Integer key = MainHandler.getIntKey(AlarmActivity.class.getName());
			if (null != key)
			{
				MainHandler.instance().sendEmptyMessage(key);
			}
			AlarmActivity.actionLuanch(context, intent.getStringExtra("id"));
		}
	}

	public static void addAlarm(MainApp app, Activity ac)
	{
		NotepadEntity note = NotepadDbWrapper.getNearestNoticeTime(
				String.valueOf(System.currentTimeMillis()), app.db());
		if (null == note)
		{
			return;
		}
		AlarmManager am = (AlarmManager) ac
				.getSystemService(Context.ALARM_SERVICE);
		Intent it = new Intent(AlarmReceiver.ACTION_ALARM);
		Long time = Long.parseLong(note.getNoticeTime());
		it.putExtra("id", note.getId());
		PendingIntent pi = PendingIntent.getBroadcast(ac.getBaseContext(), 0,
				it, PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
	}
}
