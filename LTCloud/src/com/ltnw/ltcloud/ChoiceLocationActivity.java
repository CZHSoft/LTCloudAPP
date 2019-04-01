package com.ltnw.ltcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.ltnw.common.PropertiesHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ChoiceLocationActivity extends Activity {

	private PropertiesHelper propertiesHelper=null;
	private String sidString;
	private String sipString;
	private String lportString;
	private String sportString;
	private String unString;
	private String pwString;
	private String keyString;
	private String verString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_location);
		
		propertiesHelper = new PropertiesHelper();
		Properties prop = propertiesHelper.LoadConfig(this);
		if (prop != null) 
		{
			sidString = prop.getProperty("ServerID");
			sipString = prop.getProperty("ServerIP");
			lportString = prop.getProperty("LoginPort");
			sportString = prop.getProperty("ServerPort");
			unString = prop.getProperty("UserName");
			pwString = prop.getProperty("PassWord");
			keyString = prop.getProperty("Key");
			verString = prop.getProperty("Ver");
		}
		else 
		{
			Log.i("ChoiceLocationActivity", "create pro fail,exit");
			ChoiceLocationActivity.this.finish();
		}
		
		GridView gridview = (GridView) findViewById(R.id.choicelocation);
        
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<1;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("ItemImage", R.drawable.ic_launcher);//添加图像资源的ID
			map.put("ItemText", "蓝天科技");//按序号做ItemText
        	lstImageItem.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
        		                                    lstImageItem,//数据来源 
        		                                    R.layout.choice_list_item,//
        		                                    //动态数组与ImageItem对应的子项        
        		                                    new String[] {"ItemImage","ItemText"}, 
        		                                    //ImageItem的XML文件里面的一个ImageView,两个TextView ID
        		                                    new int[] {R.id.locationItemImage,R.id.locationItemText});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new ItemClickListener());
		
	}

    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class  ItemClickListener implements OnItemClickListener
    {
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened 
				                          View arg1,//The view within the AdapterView that was clicked
				                          int arg2,//The position of the view in the adapter
				                          long arg3//The row id of the item that was clicked
				                          ) 
		{
			//在本例中arg2=arg3
			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			//显示所选Item的ItemText
			//setTitle((String)item.get("ItemText"));
//			Log.i("ItemClickListener", (String)item.get("ItemText"));
//			
//			Log.i("ItemClickListener", String.valueOf(arg2));
			switch(arg2)
			{
			case 0:
				
				if (propertiesHelper.SaveConfig(ChoiceLocationActivity.this, 
						"8888", "61.142.172.230","8888","5887", unString, pwString, keyString, verString)) 
				{
					Intent intent = new Intent(ChoiceLocationActivity.this, LoginActivity.class);
					ChoiceLocationActivity.this.startActivity(intent);
					ChoiceLocationActivity.this.finish();
				}
				else 
				{
					Log.i("ChoiceLocationActivity", "create pro fail,exit");
					ChoiceLocationActivity.this.finish();
				}
				

				break;
			}
		}

    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choice_location, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{

			if(event.getRepeatCount() == 0)
			{
			   Toast.makeText(getApplicationContext(), "长按退出！",
						Toast.LENGTH_SHORT).show();
			   
			}
			else
			{
//				Intent stopIntent = new Intent(this, AppStatusService.class);
//				stopService(stopIntent);
//				Log.i("Login", "AppStatusService stop");
				
				finish();
			}
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{

		}

		return super.onKeyDown(keyCode, event);
	}

}
