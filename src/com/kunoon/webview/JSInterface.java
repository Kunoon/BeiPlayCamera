package com.kunoon.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JSInterface {
	
	/**
	 * 设置请求来源
	 * @return
	 */
	@JavascriptInterface
    public String onSetClient() {
        Log.i("$$$$$$$ onSetClient ===> ", "android");
        return "android";
    }
	
	/**
	 * 获取页面header
	 * @param title
	 */
	@JavascriptInterface
    public void onGetHeader(String title) {
        Log.i("$$$$$$$ onGetHeader ===> ", title);
    }
	
    @JavascriptInterface
    public String onIDButtonClick(String text) {
        final String str = text;
        Log.i("$$$$$$$ onIDButtonClick ===> ", str);
        return "This text is returned from Java layer.  js text = " + text;
    }
    
    @JavascriptInterface
    public void onQRButtonClick(String url, int width, int height) {
        final String str = "onImageClick: text = " + url + "  width = " + width + "  height = " + height;
        Log.i("$$$$$$$ onImageClick ===> ", str);
    }
    
    @JavascriptInterface
    public void onGoButtonClick() {
        Log.i("$$$$$$$ onImageClick ===> ", "正在跳转到其他页面！！！");
    }
}
