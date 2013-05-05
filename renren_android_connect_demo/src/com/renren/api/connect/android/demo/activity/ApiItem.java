/**
 * $id$
 */
package com.renren.api.connect.android.demo.activity;

/**
 * An item in the api list
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class ApiItem {
	
	private String label;
	
	private String name;
	/**
	 * The name for invoking
	 */
	public String invokeName;
	
	public String getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}

	public String getInvokeName() {
		return invokeName;
	}

	public ApiItem(String label, String name, String invokeName){
		this.label = label;
		this.name = name;
		this.invokeName = invokeName;
	}
}
