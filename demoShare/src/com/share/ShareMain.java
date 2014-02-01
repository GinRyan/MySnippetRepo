package com.share;

import java.util.ArrayList;
import java.util.List;

import com.test.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShareMain extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        
        
        Button button=(Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
    }
	@Override
	public void onClick(View arg0) {
//		 Intent intent=new Intent(Intent.ACTION_SEND);
//	     intent.setType("text/plain");
//	     intent.putExtra(Intent.EXTRA_SUBJECT, "share");
//	     intent.putExtra(Intent.EXTRA_TEXT, "I would like to share this with you...");
//	     startActivity(Intent.createChooser(intent, getTitle()));
		List list=getShareTargets();
	
		String str[]=new String[list.size()];
		for(int i=0;i<list.size();i++){
			ShareItemInfo sinfo=(ShareItemInfo) list.get(i);
			str[i]=sinfo.label;
		}
		new AlertDialog.Builder(this).setTitle("分享").setItems(str, null).setNegativeButton(
			     "确定", null).show();
	}
    
	/* 获得支持ACTION_SEND的应用列表 */
	private List<ShareItemInfo> getShareTargets() {
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pm = this.getPackageManager();
		List<ResolveInfo> lisResolveInfo=pm.queryIntentActivities(intent,PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		List<ShareItemInfo> list=new ArrayList<ShareItemInfo>();
		for(int i=0;i<lisResolveInfo.size();i++) {
            ResolveInfo resolve = lisResolveInfo.get(i);
            ShareItemInfo shareItem = new ShareItemInfo();
            shareItem.setIcon(resolve.loadIcon(pm));
            shareItem.setLabel(resolve.loadLabel(pm).toString());
            shareItem.setPackageName(resolve.activityInfo.packageName);
            list.add(shareItem);
		}
		return list;
	}
	
	
	class ShareItemInfo{	
		
		Drawable icon;
		String label;
		String packageName;
		public Drawable getIcon() {
			return icon;
		}
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		
		
		
	}

}
	
