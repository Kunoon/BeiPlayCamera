package com.kunoon.activity;

import com.kunoon.playcamera.R;

import com.kunoon.camera.CameraInterface;
import com.kunoon.camera.CameraInterface.CamOpenOverCallback;
import com.kunoon.camera.preview.CameraSurfaceView;
import com.kunoon.ui.MaskView;
import com.kunoon.util.DisplayUtil;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

public class IDCardActivity extends Activity implements CamOpenOverCallback {
	private static final String TAG = "Kunoon";
	private static final int SHOWRESULT = 0x101;
	private static final int SHOWTREATEDIMG = 0x102;
	private CameraSurfaceView surfaceView = null;
	private ImageButton shutterBtn;
	private TextView tv1;
	private MaskView maskView = null;
	private float previewRate = -1f;
	private int DST_CENTER_RECT_WIDTH; //单位是dip
	private int DST_CENTER_RECT_HEIGHT;//单位是dip
	private Point rectPictureSize = null;
	private boolean isShutter = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread openThread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CameraInterface.getInstance().doOpenCamera(IDCardActivity.this);
				CameraInterface.getInstance().setHandler(myHandler);
			}
		};
		openThread.start();
		setContentView(R.layout.idcard);
		initUI();
		initViewParams();
		
		shutterBtn.setOnClickListener(new BtnListeners());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(isShutter) {
			if(rectPictureSize == null){
				rectPictureSize = createCenterPictureRect(DisplayUtil.dip2px(IDCardActivity.this, DST_CENTER_RECT_WIDTH)
						,DisplayUtil.dip2px(IDCardActivity.this, DST_CENTER_RECT_HEIGHT));
			}
			CameraInterface.getInstance().doTakePicture(rectPictureSize.x, rectPictureSize.y);
			isShutter = false;
		}
		return super.onTouchEvent(event);
	}

	private void initUI(){
		surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
		maskView = (MaskView)findViewById(R.id.view_mask);
		tv1 = (TextView) findViewById(R.id.tv1);
	}
	
	private void initViewParams(){
		LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		DST_CENTER_RECT_WIDTH = (p.x)/2 - 20;
		DST_CENTER_RECT_HEIGHT = (p.y)/16;
		Log.i(TAG, "screen: w = " + DST_CENTER_RECT_WIDTH + " y = " + DST_CENTER_RECT_HEIGHT);
		previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
		surfaceView.setLayoutParams(params);

		//手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
		LayoutParams p2 = shutterBtn.getLayoutParams();
		p2.width = DisplayUtil.dip2px(this, 80);
		p2.height = DisplayUtil.dip2px(this, 80);
		shutterBtn.setLayoutParams(p2);

		int tmpInt = (p.y)/2-DST_CENTER_RECT_HEIGHT/2;
		tv1.setPadding( 0,  tmpInt - 100,  0,  tmpInt);
	}

	@Override
	public void cameraHasOpened() {
		// TODO Auto-generated method stub
		SurfaceHolder holder = surfaceView.getSurfaceHolder();
		CameraInterface.getInstance().doStartPreview(holder, previewRate);
		if(maskView != null){
			Rect screenCenterRect = createCenterScreenRect(DisplayUtil.dip2px(this, DST_CENTER_RECT_WIDTH)
					,DisplayUtil.dip2px(this, DST_CENTER_RECT_HEIGHT));
			maskView.setCenterRect(screenCenterRect);
		}
	}
	
	private class BtnListeners implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_shutter:
				if(isShutter) {
					if(rectPictureSize == null){
						rectPictureSize = createCenterPictureRect(DisplayUtil.dip2px(IDCardActivity.this, DST_CENTER_RECT_WIDTH)
								,DisplayUtil.dip2px(IDCardActivity.this, DST_CENTER_RECT_HEIGHT));
					}
					CameraInterface.getInstance().doTakePicture(rectPictureSize.x, rectPictureSize.y);
					isShutter = false;
				}
				break;
			default:break;
			}
		}
	}
	
	/**生成拍照后图片的中间矩形的宽度和高度
	 * @param w 屏幕上的矩形宽度，单位px
	 * @param h 屏幕上的矩形高度，单位px
	 * @return
	 */
	private Point createCenterPictureRect(int w, int h){
		
		int wScreen = DisplayUtil.getScreenMetrics(this).x;
		int hScreen = DisplayUtil.getScreenMetrics(this).y;
		int wSavePicture = CameraInterface.getInstance().doGetPrictureSize().y; //因为图片旋转了，所以此处宽高换位
		int hSavePicture = CameraInterface.getInstance().doGetPrictureSize().x; //因为图片旋转了，所以此处宽高换位
		float wRate = (float)(wSavePicture) / (float)(wScreen);
		float hRate = (float)(hSavePicture) / (float)(hScreen);
		float rate = (wRate <= hRate) ? wRate : hRate;//也可以按照最小比率计算
		
		int wRectPicture = (int)( w * wRate);
		int hRectPicture = (int)( h * hRate);
		return new Point(wRectPicture, hRectPicture);
	}
	/**
	 * 生成屏幕中间的矩形
	 * @param w 目标矩形的宽度,单位px
	 * @param h	目标矩形的高度,单位px
	 * @return
	 */
	private Rect createCenterScreenRect(int w, int h){
//		int x1 = DisplayUtil.getScreenMetrics(this).x / 2 - w / 2;
//		int y1 = DisplayUtil.getScreenMetrics(this).y / 2 - h / 2;
//		int x2 = x1 + w;
//		int y2 = y1 + h;
//		return new Rect(x1, y1, x2, y2);
		int x2 = DisplayUtil.getScreenMetrics(this).x;
		int y1 = DisplayUtil.getScreenMetrics(this).y / 2 - h / 2;
		int y2 = y1 + h;
		return new Rect(0, y1, x2, y2);
	}
	
	// 该handler用于处理修改结果的任务
		private Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SHOWRESULT:
					isShutter = true;
					if (msg.obj.toString().equals(""))
						tv1.setText("识别失败");
					else {
						tv1.setText(msg.obj.toString());
					}
					break;
				case SHOWTREATEDIMG:
					tv1.setText("识别中......");
					break;
				}
				super.handleMessage(msg);
			}
		};

}
