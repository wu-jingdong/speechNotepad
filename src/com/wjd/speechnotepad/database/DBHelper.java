package com.wjd.speechnotepad.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	public static final String DB_NAME = "speechnotepad";

	public static final int DB_VERSION = 1;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(String.format("drop table if exists %s",
				NotepadDbWrapper.TABLE_NAME));
		db.execSQL(String.format("create table notepad "
				+ "(%s text primary key not null, %s text not null, "
				+ "%s text, %s text)", NotepadDbWrapper.NOTE_ID,
				NotepadDbWrapper.NOTE_AUDIO_ROUTE,
				NotepadDbWrapper.NOTE_PHOTO_ROUTE,
				NotepadDbWrapper.NOTE_NOTICE_TIME));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
}
