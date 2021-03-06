package com.wjd.speechnotepad;

import com.wjd.speechnotepad.database.DBHelper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Process;

public class MainApp extends Application
{

	private static MainApp app;

	private SQLiteDatabase db;

	@Override
	public void onCreate()
	{
		super.onCreate();
		app = this;
		db = new DBHelper(getBaseContext(), DBHelper.DB_NAME, null,
				DBHelper.DB_VERSION).getWritableDatabase();
	}

	public static MainApp getApp()
	{
		return app;
	}

	public SQLiteDatabase db()
	{
		return db;
	}

	public void recycle()
	{
		if (null != db)
		{
			db.close();
			db = null;
		}
		Process.killProcess(Process.myPid());
	}
}
