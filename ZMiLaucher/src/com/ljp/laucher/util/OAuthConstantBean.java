package com.ljp.laucher.util;

import weibo4android.Weibo;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;

public class OAuthConstantBean {
	private static Weibo weibo=null;
	private static OAuthConstantBean instance=null;
	private RequestToken requestToken;
	private AccessToken accessToken;
	private String token;
	private String tokenSecret;
	private OAuthConstantBean(){};
	public static synchronized OAuthConstantBean getInstance(){
		if(instance==null)
			instance= new OAuthConstantBean();
		return instance;
	}
	public Weibo getWeibo(){
		if(weibo==null)
			weibo= new Weibo();
		return weibo;
	}
	public static Weibo getUserWeibo(){
		weibo= new Weibo();
		return weibo;
	}
	public static void init(){
		weibo=null;
		instance =null;
	}
	public AccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
		this.token=accessToken.getToken();
		this.tokenSecret=accessToken.getTokenSecret();
	}
	public RequestToken getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	
}
