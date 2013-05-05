package com.krislq.cache;

import android.view.View.OnClickListener;
/**
 * item click listener.<br>
 * accept a id variable. may be a string or integer.<br>
 * app will try to convert the id variable between string and integre.<br>
 * so the user can set a string id,and get a integer id.
 * 
 * @author <a href="mailto:kris@matchmovegames.com">Kris.lee</a>
 * @since  2011-12-13  1:07:31 pm
 * @version 1.0.0
 */
public abstract class ItemClickListener implements OnClickListener {
	protected String StrID;
	public ItemClickListener(String id)
	{
		StrID = id;
	}
	public String getStrID() {
		return StrID;
	}
	public void setStrID(String strID) {
		StrID = strID;
	}
}
