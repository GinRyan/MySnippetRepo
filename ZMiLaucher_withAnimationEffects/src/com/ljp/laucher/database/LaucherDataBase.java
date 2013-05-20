package com.ljp.laucher.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ljp.laucher.databean.ContentItem;
import com.ljp.laucher.util.Configure;

public class LaucherDataBase {
	private static final String DB_NAME = "laucher.db";
	private static final int DB_VERSION = 1;
	private Context sContext = null;

	private static final String TB_ITEMS = "items"; // 表情
	private static final String TID = "tid";
	private static final String FROM = "item_from";
	private static final String FROM_ICON = "item_from_icon";
	private static final String ICON = "item_icon";
	private static final String TEXT = "item_text";
	private static final String CHOICE = "item_choice";
	private static final String CREATE_TB_ITEMS = "create table " + TB_ITEMS
			+ "(" + TID + " integer primary key," + FROM + " varchar,"+ FROM_ICON + " integer,"+ ICON
			+ " integer," + TEXT + " varchar," + CHOICE + " integer)";

	private static final String TB_LAUNCHER = "launchers"; // 表情
	private static final String LAUNCHER_TEXT = "item_from";
	private static final String LAUNCHER_ICON = "item_icon";
	private static final String CREATE_TB_LAUNCHER = "create table "
			+ TB_LAUNCHER + "(" + TID + " integer primary key," + LAUNCHER_TEXT
			+ " varchar," + LAUNCHER_ICON + " varchar)";

	private SQLiteDatabase sdb = null;
	private SqliteHelper dbHelper = null;

	private static class SqliteHelper extends SQLiteOpenHelper {
		public SqliteHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TB_ITEMS);
			db.execSQL(CREATE_TB_LAUNCHER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + TB_ITEMS);
			db.execSQL("DROP TABLE IF EXISTS " + TB_LAUNCHER);
			onCreate(db);
		}
	}

	public LaucherDataBase(Context context) {
		sContext = context;
	}

	public boolean open() {
		if (Configure.isDatabaseOprating) {
			return false;
		}
		dbHelper = new SqliteHelper(sContext);
		sdb = dbHelper.getWritableDatabase();
		Configure.isDatabaseOprating = true;
		return true;
	}

	public void close() {
		dbHelper.close();
		Configure.isDatabaseOprating = false;
	}


	// 是否存在ID为的微博
	public boolean isItemExit() {
		Boolean is = false;
		Cursor cursor = sdb.query(TB_ITEMS, null, null, null, null, null, null);
		is = cursor.moveToFirst();
		cursor.close();
		return is;
	}

	public void updateChoice(String text, boolean choice) {
		ContentValues values = new ContentValues();
		values.put(CHOICE, choice ? 1 : 0);
		sdb.update(TB_ITEMS, values, TEXT + "= '" + text + "'", null);
	}

	public void insertItems(List<ContentItem> items) {
		sdb.beginTransaction();
		sdb.setTransactionSuccessful();
		int sum = items.size();
		for (int i = 0; i < sum; i++) {
			ContentItem item = items.get(i);
			ContentValues ivalues = new ContentValues();
			ivalues.put(FROM, item.getFrom());
			ivalues.put(FROM_ICON, item.getFrom_icon());
			ivalues.put(ICON, item.getIcon());
			ivalues.put(TEXT, item.getText());
			ivalues.put(CHOICE, item.isChoice());
			sdb.insert(TB_ITEMS, TID, ivalues);
		}
		sdb.endTransaction();
	}

	public List<ContentItem> getItems(String from) {
		ArrayList<ContentItem> items = new ArrayList<ContentItem>();
		Cursor cursor = sdb.query(TB_ITEMS, null, FROM + "= '" + from + "'",
				null, null, null, null);
		cursor.moveToFirst();
		do {
			ContentItem item = new ContentItem();
			item.setFrom(from);
			item.setFrom_icon(cursor.getInt(2));
			item.setIcon(cursor.getInt(3));
			item.setText(cursor.getString(4));
			item.setChoice(cursor.getInt(5) > 0);
			items.add(item);
		} while (cursor.moveToNext());
		cursor.close();
		return items;
	}
	public int getItemsUrl(String text) {
		Cursor cursor = sdb.query(TB_ITEMS, null, TEXT + "= '" + text + "'",
				null, null, null, null);
		if(cursor.moveToFirst())
			return cursor.getInt(3);
		cursor.close();
		return -1;
	}
	public List<ContentItem> getItems() {
		ArrayList<ContentItem> items = new ArrayList<ContentItem>();
		String selection = "1=1 group by " + FROM;
		Cursor cursor = sdb.query(TB_ITEMS, null, selection, null, null, null,
				null);
		while (cursor.moveToNext()) {
			ContentItem item = new ContentItem();
			item.setFrom(cursor.getString(1));
			item.setFrom_icon(cursor.getInt(2));
			items.add(item);
		}
		;
		cursor.close();
		return items;
	}

	// 判断JOkes表中是否有某条数据
	public boolean hasItems() {
		Boolean is = false;
		Cursor cursor = sdb.query(TB_ITEMS, null, null, null, null, null, null);
		is = cursor.moveToFirst();
		cursor.close();
		return is;
	}

	// 删除当天数据
	public Boolean deleteItems() {
		return sdb.delete(TB_ITEMS, null, null) > 0;
	}

	// ===============================================================================================================================================
	// private static final String CREATE_TB_LAUNCHER = "create table "
	// + TB_LAUNCHER + "(" + TID + " integer primary key," + LAUNCHER_TEXT +
	// " varchar," + LAUNCHER_ICON + " integer)";

	
	public boolean hasLauncher() {
		Boolean is = false;
		Cursor cursor = sdb.query(TB_LAUNCHER, null, null, null, null, null, null);
		is = cursor.moveToFirst();
		cursor.close();
		return is;
	}
	
	public Boolean deleteLauncher() {
		return sdb.delete(TB_LAUNCHER, null, null) > 0;
	}

	public ArrayList<ContentItem> getLauncher() {
		ArrayList<ContentItem> items = new ArrayList<ContentItem>();
		Cursor cursor = sdb.query(TB_LAUNCHER, null, null, null, null,
				null, null);
		if(cursor.moveToFirst()){
			do{
				ContentItem item = new ContentItem();
				item.setText(cursor.getString(1));
				item.setIcon(cursor.getInt(2));
				items.add(item);
			}while (cursor.moveToNext());
		}
		cursor.close();
		return items;
	}

	public void insertLauncher(List<ContentItem> items) {
		sdb.beginTransaction();
		sdb.setTransactionSuccessful();
		int sum = items.size();
		for (int i = 0; i < sum; i++) {
			ContentItem item = items.get(i);
			ContentValues ivalues = new ContentValues();
			ivalues.put(LAUNCHER_ICON, item.getIcon());
			ivalues.put(LAUNCHER_TEXT, item.getText());
			sdb.insert(TB_LAUNCHER, TID, ivalues);
		}
		sdb.endTransaction();
	}

}
