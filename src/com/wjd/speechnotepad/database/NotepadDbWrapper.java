package com.wjd.speechnotepad.database;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjd.speechnotepad.entity.NotepadEntity;

public class NotepadDbWrapper
{

	public static final String TABLE_NAME = "notepad";

	public static final String NOTE_ID = "note_id";

	public static final String NOTE_AUDIO_ROUTE = "note_audio_route";

	public static final String NOTE_NOTICE_TIME = "note_notice_time";

	public static final String NOTE_PHOTO_ROUTE = "note_photo_route";

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
				notes.add(note);
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
		db.insert(TABLE_NAME, null, values);
	}

	public static void insertNotes(List<NotepadEntity> notes, SQLiteDatabase db)
	{
		db.beginTransaction();
		ContentValues values = new ContentValues();
		for (int i = 0, n = notes.size(); i < n; ++i)
		{
			NotepadEntity entity = notes.get(i);
			values.put(NOTE_ID, entity.getId());
			values.put(NOTE_AUDIO_ROUTE, entity.getAudioRoute());
			values.put(NOTE_PHOTO_ROUTE, entity.getPhotoRoute());
			values.put(NOTE_NOTICE_TIME, entity.getNoticeTime());
			db.insert(TABLE_NAME, null, values);
			values.clear();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public static void modifyNote(NotepadEntity note, SQLiteDatabase db)
	{
		ContentValues values = new ContentValues();
		values.put(NOTE_ID, note.getId());
		values.put(NOTE_AUDIO_ROUTE, note.getAudioRoute());
		values.put(NOTE_PHOTO_ROUTE, note.getPhotoRoute());
		values.put(NOTE_NOTICE_TIME, note.getNoticeTime());
		db.update(TABLE_NAME, values, String.format("%s = ?", NOTE_ID),
				new String[] { note.getId() });
	}

}
