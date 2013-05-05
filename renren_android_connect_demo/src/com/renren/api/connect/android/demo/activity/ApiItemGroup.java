/**
 * $id$
 */
package com.renren.api.connect.android.demo.activity;

import java.util.ArrayList;

/**
 * A group of api items in the same category
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class ApiItemGroup {
	private String title;
	
	private ArrayList<ApiItem> apiItems;
	
	public ApiItemGroup(String title) {
		this.title = title;
		this.apiItems = new ArrayList<ApiItem>();
	}
	
	public ApiItemGroup(String title, ArrayList<ApiItem> apiItems) {
		this.title = title;
		this.apiItems = apiItems;
	}
	
	public synchronized ArrayList<ApiItem> addApiItem(ApiItem apiItem) {
		if(apiItem != null) {
			apiItems.add(apiItem);
		}
		return apiItems;
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * Get the api item list
	 * @return
	 */
	public final ArrayList<ApiItem> getApiItems() {
		return apiItems;
	}
}
