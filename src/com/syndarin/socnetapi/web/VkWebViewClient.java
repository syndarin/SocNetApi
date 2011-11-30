package com.syndarin.socnetapi.web;

import com.syndarin.socnetapi.events.OnVkAccessGrantedListener;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VkWebViewClient extends WebViewClient {

	OnVkAccessGrantedListener onAccessGrantedListener;

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		if(url.contains("access_token")){
			onAccessGrantedListener.onAccessGranted(url);
			//view.loadUrl(url);
			return true;
		}else{
			view.loadUrl(url);
			return true;
		}
		
	}

	public void setOnAccessGrantedListener(
			OnVkAccessGrantedListener onAccessGrantedListener) {
		this.onAccessGrantedListener = onAccessGrantedListener;
	}
}
