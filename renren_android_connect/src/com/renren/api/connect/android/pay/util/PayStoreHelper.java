package com.renren.api.connect.android.pay.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.renren.api.connect.android.pay.bean.PayOrder;

public class PayStoreHelper extends SQLiteOpenHelper {
	/**
	 * db version 12，版本号变高会重建数据库
	 */
	private final static int DATABASE_VERSION = 12;
	private final static String DATABASE_NAME = "rrsdk_db";
	private static final String TABLE_NAME = "rrsdk_payment";
	private static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME
			+ " (" + " orderNumber varchar(21) primary key ,"
			+ " appId varchar(11) NOT NULL ,"
			+ " amount integer unsigned NOT NULL ,"
			+ " userId integer unsigned NOT NULL ," + " payment varchar(255),"
			+ " bid varchar(32)," + " descr varchar(255),"
			+ " payResultEncode varchar(32),"
			+ " payStatusCode integer NOT NULL," + " dealTime bigint,"
			+ " orderTime bigint NOT NULL, " + " serverState tinyint NOT NULL,"
			+ " sandBox boolean NOT NULL,"
			+ " localEncode varchar(32) NOT NULL" + ");";
	private static PayStoreHelper instance;

	public synchronized static PayStoreHelper getInstance(Context c) {
		if (instance == null) {
			instance = new PayStoreHelper(c);
		}
		return instance;
	}

	// 本地存储功能默认关闭
	private boolean enbleLocalStore = false;

	private PayStoreHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs,
				groupBy, having, orderBy);
		return cursor;
	}

	public long addOrUpdatePay(PayOrder order) {
		if (!enbleLocalStore) {
			return -1L;
		}
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = payOrder2cv(order);

		long row = -1L;
		try {
			row = db.insertOrThrow(TABLE_NAME, null, cv);
		} catch (SQLException e) {
			String where = "userId= ? AND orderNumber= ? ";
			String[] value = { String.valueOf(order.getUserId()),
					order.getOrderNumber() };
			row = db.update(TABLE_NAME, cv, where, value);
		}
		return row;
	}

	/**
	 * 根据payOrder对象生成数据库需要的数据
	 * 
	 * @param order
	 * @return
	 */
	private ContentValues payOrder2cv(PayOrder order) {

		ContentValues cv = new ContentValues();
		cv.put("orderNumber", order.getOrderNumber());
		cv.put("appId", order.getAppId());
		cv.put("amount", order.getAmount());
		cv.put("userId", order.getUserId());
		cv.put("payment", order.getPayment());
		cv.put("bid", order.getBid());
		cv.put("descr", order.getDescr());
		cv.put("payResultEncode", order.getPayResultEncode());
		cv.put("payStatusCode", order.getPayStatusCode());
		cv.put("dealTime", order.getDealTime().getTime());
		cv.put("orderTime", order.getOrderTime().getTime());
		cv.put("serverState", order.getServerState());
		cv.put("sandBox", order.isSandBox());
		Log.d("dddd",String.valueOf(order.isSandBox()));
		cv.put("localEncode", order.getLocalEncode());
		return cv;
	}

	/**
	 * 根据appId和userId获得用户的所有订单对象集合 该集合从不会为null，但size可能为0
	 * 
	 * @param appId
	 * @param userId
	 * @return
	 */
	public List<PayOrder> getPayOrder(int appId, int userId) {
		if (!enbleLocalStore) {
			return null;
		}
		List<PayOrder> l = new ArrayList<PayOrder>();
		String selectSql = "appId = ? and userId = ?";
		String[] selectArgs = { String.valueOf(appId), String.valueOf(userId) };
		Cursor cur = select(null, selectSql, selectArgs, null, null,
				"orderTime desc");
		// 如果游标没有在开始之前，没有在结束之后
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			PayOrder order = createPayOrder(cur);
			if (null != order)
				l.add(order);
		}
		cur.close();
		return l;
	}

	/**
	 * 将数据库记录转换为PayOrder对象 如果验证失败则返回null
	 * 
	 * @param cur
	 * @return
	 */
	private PayOrder createPayOrder(Cursor cur) {
		PayOrder order = null;
		if (cur != null && !cur.isClosed()) {
			order = new PayOrder();
			order.setOrderNumber(cur.getString(0));
			order.setAppId(cur.getString(1));
			order.setAmount(cur.getInt(2));
			order.setUserId(cur.getLong(3));
			order.setPayment(cur.getString(4));
			order.setBid(cur.getString(5));
			order.setDescr(cur.getString(6));
			order.setPayResultEncode(cur.getString(7));
			order.setPayStatusCode(cur.getInt(8));
			order.setDealTime(new Date(cur.getLong(9)));
			order.setOrderTime(new Date(cur.getLong(10)));
			order.setServerState(cur.getInt(11));
			// sqlite用long 0 1来区分boolean
			order.setSandBox(cur.getLong(12) == 1 ? true : false);
			if (!order.getLocalEncode().equals(cur.getString(13))) {
				order = null;
			}
		}
		return order;
	}

	/**
	 * 删除一个用户的所有记录
	 * 
	 * @param userId
	 */
	public void removeAllByUid(int userId) {
		if (!enbleLocalStore) {
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		String where = " userId " + " =? ";
		String[] whereValue = { Integer.toString(userId) };
		db.delete(TABLE_NAME, where, whereValue);
	}

	/**
	 * 删除一条记录
	 * 
	 * @param userId
	 * @param orderNumber
	 */
	public void removeByUidAndOrderNumber(int userId, String orderNumber) {
		if (!enbleLocalStore) {
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		String where = " userId  =? and orderNumber = ?";
		String[] whereValue = { Integer.toString(userId), orderNumber };
		db.delete(TABLE_NAME, where, whereValue);
	}

	public void enableLocalStore(boolean canDoStore) {
		this.enbleLocalStore = canDoStore;
	}

	public boolean isCanDoStore() {
		return enbleLocalStore;
	}
}