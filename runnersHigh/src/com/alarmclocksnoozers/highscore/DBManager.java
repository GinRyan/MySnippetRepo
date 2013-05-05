/*
 * DbManager
 * runnersHigh 1.0
 * 
 * _DESCRIPTION:
 * 	Creates database tables; Updates database
 * 	To update database table(s): increase DATABASE_VERSION + 1
 */

package com.alarmclocksnoozers.highscore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBManager extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "rHighDb";
	private static final int DATABASE_VERSION = 5;

	/*
	 * ----------------------------------------------- Database TABLES
	 */
	private static final String DB_CREATE_HIGHSCORE = "CREATE TABLE rh_highscore "
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "name TEXT NOT NULL,"
			+ "score INTEGER NOT NULL," + "isonline INTEGER NOT NULL" + ");";

	// -----------------------------------------------
	DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE_HIGHSCORE);
		Log.i("SQL", "created tables");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("DBManagerClass", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS rh_highscore");
		onCreate(db);
	}
}
