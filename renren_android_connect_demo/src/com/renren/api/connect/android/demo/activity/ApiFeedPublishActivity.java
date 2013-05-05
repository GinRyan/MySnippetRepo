package com.renren.api.connect.android.demo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.demo.R;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.feed.FeedPublishRequestParam;
import com.renren.api.connect.android.feed.FeedPublishResponseBean;

public class ApiFeedPublishActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(
				R.layout.feed_publish_layout, null);
		root.addView(layout);
		
		// 设置标题栏文字
		titlebarText.setText("发布新鲜事");

		Button runButton = (Button) layout.findViewById(R.id.run);
		final EditText editTextLog = (EditText) layout.findViewById(R.id.log);

		final EditText editTextName = (EditText) layout.findViewById(R.id.name);
		final EditText editTextDescription = (EditText) layout
				.findViewById(R.id.description);
		final EditText editTextUrl = (EditText) layout.findViewById(R.id.url);
		final EditText editTextImageUrl = (EditText) layout
				.findViewById(R.id.image);
		final EditText editTextCaption = (EditText) layout
				.findViewById(R.id.caption);
		final EditText editTextActionName = (EditText) layout
				.findViewById(R.id.action_name);
		final EditText editTextActionLink = (EditText) layout
				.findViewById(R.id.action_link);
		final EditText editTextMessage = (EditText) layout
				.findViewById(R.id.message);
		runButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (renren != null) {
					AsyncRenren asyncRenren = new AsyncRenren(renren);
					showProgress();
					String name = editTextName.getText().toString();
					String description = editTextDescription.getText()
							.toString();
					String url = editTextUrl.getText().toString();
					String imageUrl = editTextImageUrl.getText().toString();
					String caption = editTextCaption.getText().toString();
					String actionName = editTextActionName.getText().toString();
					String actionLink = editTextActionLink.getText().toString();
					String message = editTextMessage.getText().toString();
					FeedPublishRequestParam param = new FeedPublishRequestParam(
							name, description, url, imageUrl, caption,
							actionName, actionLink, message);
					AbstractRequestListener<FeedPublishResponseBean> listener = new AbstractRequestListener<FeedPublishResponseBean>() {

						@Override
						public void onComplete(
								final FeedPublishResponseBean bean) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									editTextLog.setText(bean.toString());
									dismissProgress();
								}
							});
						}

						@Override
						public void onRenrenError(final RenrenError renrenError) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									editTextLog.setText(renrenError
											.getMessage());
									dismissProgress();
								}
							});
						}

						@Override
						public void onFault(final Throwable fault) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									editTextLog.setText(fault.getMessage());
									dismissProgress();
								}
							});
						}
					};
					asyncRenren.publishFeed(param, listener, true);

				}

			}
		});

	}

}
