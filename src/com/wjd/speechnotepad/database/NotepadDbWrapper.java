package com.wjd.speechnotepad.database;

import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.util.DateUtil;

public class NotepadDbWrapper
{

	public static final String TABLE_NAME = "notepad";

	public static final String NOTE_ID = "note_id";

	public static final String NOTE_AUDIO_ROUTE = "note_audio_route";

	public static final String NOTE_NOTICE_TIME = "note_notice_time";

	public static final String NOTE_PHOTO_ROUTE = "note_photo_route";

	public static final String NOTE_DURATION = "note_duration";

	public static final String NOTE_YEAR = "NOTE_YEAR";

	public static final String NOTE_MONTH = "NOTE_MONTH";

	public static final String NOTE_DAY = "NOTE_DAY";

	public static void getNotes(List<NotepadEntity> notes, SQLiteDatabase db)
	{
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
				String.format("%s desc", NOTE_ID));
		if (null == cursor)
		{
			return;
		}
		if (cursor.moveToFirst())
		{
			do
			{
				NotepadEntity note = new NotepadEntity();
				note.setId(cursor.getString(0));
				note.setAudioRoute(cursor.getString(1));
				note.setPhotoRoute(cursor.getString(2));
				note.setNoticeTime(cursor.getString(3));
				note.setDuration(cursor.getInt(4));
				notes.add(note);
			} while (cursor.moveToNext());
		}
		cursor.close();
		cursor = null;
	}

	public static void getDayNotes(List<NotepadEntity> notes, int year,
			int month, int day, SQLiteDatabase db)
	{
		Cursor cursor = db.query(TABLE_NAME, null, NOTE_YEAR + " = ? and "
				+ NOTE_MONTH + " =? and " + NOTE_DAY + "= ? ",
				new String[] { String.valueOf(year), String.valueOf(month),
						String.valueOf(day) }, null, null,
				String.format("%s desc", NOTE_ID));
		if (null == cursor)
		{
			return;
		}
		if (cursor.moveToFirst())
		{
			do
			{
				NotepadEntity note = new NotepadEntity();
				note.setId(cursor.getString(0));
				note.setAudioRoute(cursor.getString(1));
				note.setPhotoRoute(cursor.getString(2));
				note.setNoticeTime(cursor.getString(3));
				note.setDuration(cursor.getInt(4));
				notes.add(note);
			} while (cursor.moveToNext());
		}
		cursor.close();
		cursor = null;
	}

	public static void getDays(List<Integer> days, SQLiteDatabase db,
			String beginTime, String endTime)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[] { String.format(
				Locale.getDefault(), "distinct(%s)", NOTE_DAY) }, String
				.format(Locale.getDefault(), "%s >= ? and %s<= ?", NOTE_ID,
						NOTE_ID), new String[] { beginTime, endTime }, null,
				null, String.format("%s asc", NOTE_ID));
		if (null == cursor)
		{
			return;
		}
		if (cursor.moveToFirst())
		{
			do
			{
				days.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		cursor = null;
	}

	public static void deleteNote(String noteId, SQLiteDatabase db)
	{
		db.delete(TABLE_NAME, String.format("%s = ?", NOTE_ID),
				new String[] { noteId });
	}

	public static void insertNote(NotepadEntity entity, SQLiteDatabase db)
	{
		ContentValues values = new ContentValues();
		values.put(NOTE_ID, entity.getId());
		values.put(NOTE_AUDIO_ROUTE, entity.getAudioRoute());
		values.put(NOTE_PHOTO_ROUTE, entity.getPhotoRoute());
		values.put(NOTE_NOTICE_TIME, entity.getNoticeTime());
		values.put(NOTE_DURATION, entity.getDuration());
		values.put(NOTE_YEAR, DateUtil.getYear(Long.valueOf(entity.getId())));
		values.put(NOTE_MONTH, DateUtil.getMonth(Long.valueOf(entity.getId())));
		values.put(NOTE_DAY, DateUtil.getDay(Long.valueOf(entity.getId())));
		db.insert(TABLE_NAME, null, values);
	}
}
