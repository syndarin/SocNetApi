package com.syndarin.socnetapi.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.syndarin.socnetapi.R;
import com.syndarin.socnetapi.web.VkWebViewClient;

public class VkAuthDialog extends Dialog implements
		android.view.View.OnClickListener {

	private WebView browser;
	private String auth_url;

	public VkAuthDialog(Context context, String auth_url,
			VkWebViewClient vkWebViewClient) {
		super(context);
		this.auth_url = auth_url;
		// TODO Auto-generated constructor stub
		setContentView(R.layout.vk_dialog);
		setTitle("Авторизация");

		Button button_cancel = (Button) findViewById(R.id.vk_auth_cancel);
		button_cancel.setOnClickListener(this);

		browser = (WebView) findViewById(R.id.vk_auth_browser);
		browser.setWebViewClient(vkWebViewClient);
		browser.getSettings().setJavaScriptEnabled(true);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		browser.loadUrl(auth_url);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.vk_auth_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}
}
