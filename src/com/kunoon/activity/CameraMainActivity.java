package com.kunoon.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.kunoon.playcamera.R;
import com.stay.pull.PullToRefreshActivity;

public class CameraMainActivity extends Activity implements OnItemClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
        SimpleAdapter saImageItems = new SimpleAdapter(this, initGridViewCon(),//数据来源 
        		                                    R.layout.item,//night_item的XML实现
        		                                    new String[] {"ItemImage","ItemText"}, 	//动态数组与ImageItem对应的子项        
        		                                    //ImageItem的XML文件里面的一个ImageView,两个TextView ID
        		                                    new int[] {R.id.ItemImage,R.id.ItemText});
        gridview.setAdapter(saImageItems);		//添加并且显示
        gridview.setOnItemClickListener(this);		//添加消息处理
	}
	
	private ArrayList<HashMap<String, Object>> initGridViewCon() {	//生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        int[] drawICON = {R.drawable.mm_pqr_back_btn, R.drawable.mm_qr_back_btn, 
        							 R.drawable.mm_idc_back_btn,  R.drawable.mm_webv_back_btn,
        							 R.drawable.mm_refresh_back_btn};
        String[] drawTitle = {"生成二维码", "扫描二维码", "扫描身份证", "WebView展示", "下拉刷新"};
        for(int i = 0; i < drawICON.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("ItemImage", drawICON[i]);//添加图像资源的ID
    		map.put("ItemText", drawTitle[i]);//按序号做ItemText
    		lstImageItem.add(map);
        }
        return lstImageItem;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
		case 0:
			startActivity(new Intent(this, PCaptureActivity.class));
			break;
		case 1:
			startActivity(new Intent(this, CaptureActivity.class));
			break;
		case 2:
			startActivity(new Intent(this, IDCardActivity.class));
			break;
		case 3:
			startActivity(new Intent(this, WebViewActivity.class));
			break;
		case 4:
			startActivity(new Intent(this, PullToRefreshActivity.class));
			break;
		default:
			break;
		}
	}
	
}
