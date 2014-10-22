package com.kunoon.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kunoon.playcamera.R;
import com.kunoon.webview.JSInterface;
import com.kunoon.webview.WebViewEx;

public class WebViewActivity extends Activity {

	private WebViewEx contentWebView = null;
	private JSInterface mJsObj;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webv);
		
		mJsObj = new JSInterface();
		contentWebView = (WebViewEx) findViewById(R.id.webview);
		// 启用javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		contentWebView.addJavascriptInterface(mJsObj, "beizhu");
		contentWebView.setWebViewClient(webViewClient);
		// 从assets目录下面的加载html
		contentWebView.loadUrl("http://192.168.1.122:8888/wap");
	}
	
	private WebViewClient webViewClient = new WebViewClient(){   
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}
	};
	
}