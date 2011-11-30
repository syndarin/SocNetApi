package com.syndarin.socnetapi.sn_classes;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.syndarin.socnetapi.events.OnTwitterPinReceived;
import com.syndarin.socnetapi.tools.TwitterAuthDialog;

public class TwitterObject implements OnTwitterPinReceived {

	private final static String CONSUMER_KEY = "R9GMS1wJ8HTlcHT4gHfh7g";
	private final static String CONSUMER_SECRET = "rD5aPQngVjNNYuYx7bjU5YTfsvTnqHeqas8lEb8oaY";

	private Twitter twitter;
	private Context context;
	private TwitterAuthDialog dialog;
	private RequestToken requestToken;
	private AccessToken accessToken;
	private SharedPreferences preferences;
	
	private boolean authorized;

	public TwitterObject(Context context, SharedPreferences preferences) {
		super();
		this.context = context;
		this.twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		this.preferences = preferences;
		authorized=false;
		restoreKeys();
	}

	public void showAuthDialog() {
		try {
			requestToken = twitter.getOAuthRequestToken("");
			String auth_url = requestToken.getAuthorizationURL();
			dialog = new TwitterAuthDialog(context, auth_url);
			dialog.setOnTwitterPinReceived(this);
			dialog.show();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "Проблема с авторизацией twitter-аккаунта",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	public boolean postToWall(String message) {
		boolean result = true;
		try {
			Status status=twitter.updateStatus(message);
			Toast.makeText(context, "Twitter: "+status.getText(),
					Toast.LENGTH_LONG).show();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			Toast.makeText(context, "Twitter: "+e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		return result;
	}

	@Override
	public void onTwitterPinReceived(String pin) {
		// TODO Auto-generated method stub
		try {
			accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			twitter.setOAuthAccessToken(accessToken);
			authorized=true;
			Toast.makeText(context, "Twitter успешно настроен!",
					Toast.LENGTH_LONG).show();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "Проблема авторизации twitter-аккаунта",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	public void storeKeys() {

		if (null != accessToken) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("twitter_token", accessToken.getToken());
			editor.putString("twitter_token_secret",
					accessToken.getTokenSecret());
			editor.commit();
		}

	}

	public void restoreKeys() {
		String token=preferences.getString("twitter_token", null);
		String token_secret=preferences.getString("twitter_token_secret", null);
		
		if(token!=null&&token_secret!=null){
			accessToken=new AccessToken(token, token_secret);
			twitter.setOAuthAccessToken(accessToken);
			if(accessToken!=null){
				authorized=true;
			}		
		}
	}

	public boolean isAuthorized() {
		return authorized;
	}
	
	

}
