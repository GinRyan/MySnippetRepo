/*
 * HighScoreAdapater
 * runnersHigh 1.0
 * 
 * _DESCRIPTION:
 * 	Table operations for 'rh_highscore'
 * 	Saves and returns highscores from database
 * 
 * 	Child class of DbAdapter
 * 
 * 	TODO: Delete entries, if there are more than SHOW_LIMIT
 */

package com.alarmclocksnoozers.highscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class HighscoreAdapter extends DbAdapter {

	private static final String DATABASE_TABLE = "rh_highscore";

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SCORE = "score";
	public static final String KEY_ISONLINE = "isonline";

	public HighscoreAdapter(Context ctx) {
		super(ctx);
		this.mCtx = ctx;
	}

	// -------------------------------------------------------
	// Create
	public long createHighscore(String score, String name, int isonline) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_SCORE, score);
		initialValues.put(KEY_ISONLINE, isonline);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	// -------------------------------------------------------
	// Update
	public boolean updateScore(long rowId, int isOnline) {
		ContentValues args = new ContentValues();
		args.put(KEY_ROWID, rowId);
		args.put(KEY_ISONLINE, isOnline);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// -------------------------------------------------------
	// Delete
	public boolean delete(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// -------------------------------------------------------
	// Clear table
	public boolean clear() {
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}

	// -------------------------------------------------------
	// Show List
	public Cursor fetchScores(String limit) throws SQLException {

		Cursor mCursor;
		if (limit == "0")
		{
			mCursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_SCORE, KEY_ISONLINE }, null, null, null, null,
					KEY_SCORE + " DESC", null);
		}
		else
		{
			mCursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_SCORE, KEY_ISONLINE }, null, null, null, null,
					KEY_SCORE + " DESC", limit);
		}
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	// -------------------------------------------------------
	// Show Single
	public Cursor fetchSingleScore(long id) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_NAME, KEY_SCORE, KEY_ISONLINE }, KEY_ROWID + "="
				+ id, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}
	
	public Cursor getHighscore(long place)
	{
		
		Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_SCORE, KEY_ISONLINE }, null, null, null, null,
				KEY_SCORE + " DESC", ""+place);

		if (mCursor != null) {
			mCursor.moveToLast();
		}

		return mCursor;
	}

	// -------------------------------------------------------
	// Get Last Entry
	public Cursor fetchLastEntry() throws SQLException {
		Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_SCORE, KEY_ISONLINE }, "isonline = 0", null,
				null, null, KEY_ROWID + " DESC", "1");

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}
}
