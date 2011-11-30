package com.syndarin.socnetapi.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.syndarin.socnetapi.R;
import com.syndarin.socnetapi.events.OnTwitterPinReceived;

public class TwitterAuthDialog extends Dialog implements
		android.view.View.OnClickListener {

	private WebView browser;
	private String auth_url;
	private EditText pin_text;
	private OnTwitterPinReceived onTwitterPinReceived;

	public TwitterAuthDialog(Context context, String auth_url) {
		super(context);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.twitter_dialog);
		setTitle("Авторизация");
		
		this.auth_url = auth_url;
		browser = (WebView) findViewById(R.id.twitter_browser);
		browser.getSettings().setJavaScriptEnabled(true);

		Button apply_pin = (Button) findViewById(R.id.twitter_apply_pin);
		apply_pin.setOnClickListener(this);
		
		Button cancel_auth=(Button)findViewById(R.id.twitter_cancel_auth);
		cancel_auth.setOnClickListener(this);
		
		pin_text=(EditText)findViewById(R.id.pin_text);
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
		case R.id.twitter_apply_pin:
			String pin=pin_text.getText().toString();
			if(!pin.trim().equals("")){
				onTwitterPinReceived.onTwitterPinReceived(pin);
				dismiss();
			}else{
				pin_text.setText("Введите PIN");
			}
			break;
		case R.id.twitter_cancel_auth:
			dismiss();
			break;
		default:
			break;
		}

	}

	public void setOnTwitterPinReceived(OnTwitterPinReceived onTwitterPinReceived) {
		this.onTwitterPinReceived = onTwitterPinReceived;
	}
	
	

}
