package com.syndarin.socnetapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.syndarin.socnetapi.sn_classes.TwitterObject;
import com.syndarin.socnetapi.sn_classes.VkontakteObject;
import com.syndarin.socnetapi.tools.SendDialog;

public class MainActivity extends Activity implements OnTouchListener,
		OnClickListener {
	/** Called when the activity is first created. */
	private final static int MAIN_PAGE = 0;
	private final static int SETTINGS_PAGE = 1;
	private final static int MOVE_DENSITY = 50;
	private final static String PREFERENCES_NAME = "SocNetApiPreferences";

	private float startX;

	private ViewFlipper flipper;
	private SharedPreferences preferences;
	private EditText message_text;

	private int page;

	private VkontakteObject vkontakte;
	private TwitterObject twitter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// initialize variables
		page = MAIN_PAGE;
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		vkontakte = new VkontakteObject(this, preferences);
		twitter = new TwitterObject(this, preferences);

		// initializing widgets
		flipper = (ViewFlipper) findViewById(R.id.flipper);

		LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);
		main_layout.setOnTouchListener(this);

		LinearLayout settings_layout = (LinearLayout) findViewById(R.id.settings_layout);
		settings_layout.setOnTouchListener(this);

		Button button_back = (Button) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		Button button_post=(Button)findViewById(R.id.button_post);
		button_post.setOnClickListener(this);

		ImageButton enable_twitter = (ImageButton) findViewById(R.id.enable_twitter);
		enable_twitter.setOnClickListener(this);

		ImageButton enable_vk = (ImageButton) findViewById(R.id.enable_vk);
		enable_vk.setOnClickListener(this);
		
		message_text=(EditText)findViewById(R.id.message_text);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			return true;// без последующего перехвата, поэтому true
		case MotionEvent.ACTION_UP:
			float endX = event.getX();
			if (startX - MOVE_DENSITY > endX) {
				if (page == MAIN_PAGE) {
					flipper.setInAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.push_left_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.push_left_out));
					flipper.showNext();
					page = SETTINGS_PAGE;
				}
				return true;
			} else if (startX + MOVE_DENSITY < endX) {
				if (page == SETTINGS_PAGE) {
					flipper.setInAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.push_right_out));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.push_right_in));
					flipper.showPrevious();
					page = MAIN_PAGE;
				}
				return true;
			}
		default:
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int sender = v.getId();
		switch (sender) {
		case R.id.button_back:// close settings
			flipper.setInAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.push_right_out));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.push_right_in));
			flipper.showPrevious();
			page = MAIN_PAGE;
			break;
		case R.id.enable_twitter:// click on twitter icon
			if (!twitter.isAuthorized()) {
				twitter.showAuthDialog();
			} else {
				Toast.makeText(getApplicationContext(),
						"Twitter уже настроен!", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.enable_vk:// click on vk icon
			if (!vkontakte.isAuthorized()) {
				vkontakte.showAuthDialog();
			} else {
				Toast.makeText(getApplicationContext(),
						"¬контакте уже настроен!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button_post:
			String message=message_text.getText().toString();
			if(message.equals("")){
				Toast.makeText(getApplicationContext(), "¬ведите хоть что-нибудь... ну пожалуйста :)", Toast.LENGTH_SHORT).show();
			}else if(message.length()>140){
				Toast.makeText(getApplicationContext(), "—лишком много дл€ твиттера! ”ложитесь в 140 символов!", Toast.LENGTH_SHORT).show();
			}else{
				SendDialog sendDialog=new SendDialog(this, twitter, vkontakte, message);
				sendDialog.show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		vkontakte.storeKeys();
		twitter.storeKeys();
		super.onPause();
	}

}