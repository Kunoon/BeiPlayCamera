package com.kunoon.imgdistin;

import java.io.File;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ImgDistinguish extends Thread  {
	private static final String TAG = "Kunoon";
	private static final int SHOWRESULT = 0x101;
	private static final int SHOWTREATEDIMG = 0x102;
	
	private Handler myHandler;
	private Bitmap rectBitmap;
	
	public ImgDistinguish(Handler myHandler, Bitmap rectBitmap) {
		// TODO Auto-generated constructor stub
		this.myHandler = myHandler;
		this.rectBitmap = rectBitmap;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		getImgContest(rectBitmap);
	}
	
	private void getImgContest(Bitmap rectBitmap) {
		Bitmap bitmapTreated = ImgPretreatment.converyToGrayImg(rectBitmap);
		Message msg = new Message();
		msg.what = SHOWTREATEDIMG;
		myHandler.sendMessage(msg);
		
		String textResult = doOcr(bitmapTreated,  "chi_sim");
		Log.d(TAG, "@@@==> " + textResult);
		
		Message msg2 = new Message();
		msg2.what = SHOWRESULT;
		msg2.obj = textResult;
		myHandler.sendMessage(msg2);
	}
	
	/**
	 * 进行图片识别
	 * @param bitmap  待识别图片
	 * @param language 识别语言
	 * @return 识别结果字符串
	 */
 	private String doOcr(Bitmap bitmap, String language) {
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.init(getSDPath(), language);
		// 必须加此行，tess-two要求BMP必须为此配置
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		baseApi.setImage(bitmap);
		String text = baseApi.getUTF8Text();
		baseApi.clear();
		baseApi.end();
		return text;
	}
 	
	/**
	 * 获取sd卡的路径
	 * @return 路径的字符串
	 */
	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
		}
		return sdDir.toString();
	}
	
}
