package com.syndarin.socnetapi.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.syndarin.socnetapi.R;
import com.syndarin.socnetapi.sn_classes.TwitterObject;
import com.syndarin.socnetapi.sn_classes.VkontakteObject;

public class SendDialog extends Dialog implements
		android.view.View.OnClickListener {

	private TwitterObject twitter;
	private VkontakteObject vkontakte;
	private CheckBox twitter_checkbox;
	private CheckBox vkontakte_checkbox;
	private String message;

	public SendDialog(Context context, TwitterObject twitter,
			VkontakteObject vkontakte, String message) {
		super(context);
		setContentView(R.layout.send_dialog);
		setTitle("Куда постим?");
		// TODO Auto-generated constructor stub
		this.twitter = twitter;
		this.vkontakte = vkontakte;
		this.message=message;

		Button button_send = (Button) findViewById(R.id.send_dialog_send);
		button_send.setOnClickListener(this);

		Button button_cancel = (Button) findViewById(R.id.send_dialog_cancel);
		button_cancel.setOnClickListener(this);
		
		twitter_checkbox=(CheckBox)findViewById(R.id.twitter_checkbox);
		vkontakte_checkbox=(CheckBox)findViewById(R.id.vkontakte_checkbox);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send_dialog_send:
			if(twitter_checkbox.isChecked()){
				
				if(twitter.isAuthorized()){
					twitter.postToWall(message);
				}else{
					twitter.showAuthDialog();
				}
				
			}
			if(vkontakte_checkbox.isChecked()){
				
				if(vkontakte.isAuthorized()){
					vkontakte.postToWall(message);
				}else{
					vkontakte.showAuthDialog();
				}
				
			}
			if(!twitter_checkbox.isChecked()&&!vkontakte_checkbox.isChecked()){
				Toast.makeText(super.getContext(), "И куда будем это отправлять?", Toast.LENGTH_SHORT).show();
			}
			dismiss();
			break;
		case R.id.send_dialog_cancel:
			dismiss();
			break;
		default:
			break;
		}

	}

}
