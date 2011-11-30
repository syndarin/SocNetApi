package com.syndarin.socnetapi.sn_classes;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.syndarin.socnetapi.events.OnVkAccessGrantedListener;
import com.syndarin.socnetapi.tools.VkAuthDialog;
import com.syndarin.socnetapi.web.VkWebViewClient;

public class VkontakteObject implements OnVkAccessGrantedListener {

	private final static String AUTH_ROOT = "http://api.vk.com/oauth/authorize?";
	private final static String APP_ID = "2697291";
	private final static String REDIRECT_URI = "http://api.vk.com/blank.html";
	private final static String SETTINGS = "8192";
	private final static String DISPLAY = "touch";
	private final static String RESPONSE_TYPE = "token";
	private final static String WALL_URL = "https://api.vk.com/method/wall.post?";

	private Context context;
	private SharedPreferences preferences;
	private VkAuthDialog dialog;

	private String access_token;
	private String expires_in;
	private String user_id;
	
	private boolean authorized;

	public VkontakteObject(Context context, SharedPreferences preferences) {
		super();
		this.context = context;
		this.preferences = preferences;
		authorized=false;
		restoreKeys();
	}

	public void showAuthDialog() {

		StringBuilder uriBuilder = new StringBuilder(AUTH_ROOT);
		uriBuilder.append("client_id=").append(APP_ID).append("&");
		uriBuilder.append("scope=").append(SETTINGS).append("&");
		uriBuilder.append("redirect_uri=").append(REDIRECT_URI).append("&");
		uriBuilder.append("display=").append(DISPLAY).append("&");
		uriBuilder.append("response_type=").append(RESPONSE_TYPE);

		String auth_url = uriBuilder.toString();

		VkWebViewClient vkWebViewClient = new VkWebViewClient();
		vkWebViewClient.setOnAccessGrantedListener(this);

		dialog = new VkAuthDialog(context, auth_url, vkWebViewClient);
		dialog.show();
	}

	@Override
	public void onAccessGranted(String url) {
		// TODO Auto-generated method stub
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		url = url.replace('#', '?');
		Uri access_data = Uri.parse(url);
		access_token = access_data.getQueryParameter("access_token");
		expires_in = access_data.getQueryParameter("expires_in");
		user_id = access_data.getQueryParameter("user_id");
		Toast.makeText(context, "јккаунт ¬контакте успешно настроен!", Toast.LENGTH_LONG).show();

		authorized=true;
	}

	public boolean postToWall(String message) {
		boolean result = true;

		StringBuilder request_builder = new StringBuilder(WALL_URL);
		request_builder.append("owner_id=").append(user_id).append("&");
		request_builder.append("message=").append(URLEncoder.encode(message))
				.append("&");
		request_builder.append("access_token=").append(access_token);

		HttpClient hc = new DefaultHttpClient();

		HttpGet request = new HttpGet(request_builder.toString());

		try {
			HttpResponse hr = hc.execute(request);
			HttpEntity he = hr.getEntity();
			String a = EntityUtils.toString(he);
			if (a.contains("post_id")) {
				Toast.makeText(
						context,
						"¬контакте: ¬аше сообщение успешно опубликовано на стене!",
						Toast.LENGTH_LONG).show();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "¬контакте: ошибка отправки сообщени€!",
					Toast.LENGTH_LONG).show();
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "¬контакте: ошибка подключени€!",
					Toast.LENGTH_LONG).show();
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	public void storeKeys() {
		if (access_token != null && user_id != null && expires_in != null) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("vk_access_token", access_token);
			editor.putString("vk_user_id", user_id);
			editor.putString("vk_expires_in", expires_in);
			editor.commit();
		}

	}
	
	public void restoreKeys(){
		access_token=preferences.getString("vk_access_token", null);
		user_id=preferences.getString("vk_user_id", null);
		expires_in=preferences.getString("vk_expires_in", null);
		
		if(access_token != null && user_id != null && expires_in != null){
			authorized=true;
		}
	}

	public boolean isAuthorized() {
		return authorized;
	}
}
