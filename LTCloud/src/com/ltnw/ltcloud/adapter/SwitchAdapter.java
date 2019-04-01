package com.ltnw.ltcloud.adapter;

import java.util.List;

import com.ltnw.common.controls.SlideSwitch;
import com.ltnw.common.controls.SlideSwitch.OnSwitchChangedListener;
import com.ltnw.entity.SwitchItem;
import com.ltnw.interFace.SwitchCallBack;
import com.ltnw.ltcloud.R;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchAdapter extends BaseAdapter{

	private Context context;
	private SwitchItem[] switchItems;
	
	private Button ItemButton=null;
	private TextView ItemSwitchNo=null;
	private TextView ItemSwitchType1=null;
	private TextView ItemSwitchEnable=null;
	private TextView ItemSwitchType2=null;
	private SlideSwitch switch1=null;
	private Dialog dialogInfo;
	
	
	private int t_no;
	private int t_type1;
	private int t_enable;
	private int t_state;
	private int t_type2;
	private String t_nameString;
	
	private SwitchCallBack mSwitchCallBack;
	
	public void setmSwitchCallBack(SwitchCallBack mSwitchCallBack) {
		this.mSwitchCallBack = mSwitchCallBack;
	}
	
	public SwitchAdapter(Context context, 
			SwitchItem[] items)
	{
		this.context = context;
		this.switchItems = items;
		
	}
	
	public void ReturnSwitchStateChange(int no,int state)
	{
		switchItems[no].switchState=state;
	}
	
	public void ReturnModSwitchChange(int no,int type1,int enable,int state,int type2,String name)
	{
		switchItems[no].switchType1=type1;
		switchItems[no].switchEnable=enable;
		switchItems[no].switchState=state;
		switchItems[no].switchType2=type2;
		switchItems[no].switchNameString=name;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return switchItems.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Log.i("getView", String.format("position:%d", position));
		final int p=position;
		if (convertView == null) 
		{
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.switch_listview, null);
        }
		
		ItemButton = (Button) convertView.findViewById(R.id.itemButton);
		ItemButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, String.format("position:%d", p),
						Toast.LENGTH_SHORT).show();
				//here input
				mSwitchCallBack.SwitchDialogCallBack(switchItems[p].switchNo,
						switchItems[p].switchType1,
						switchItems[p].switchEnable,
						switchItems[p].switchState,
						switchItems[p].switchType2,
						switchItems[p].switchNameString);
			}
		});
		
		ItemSwitchNo = (TextView) convertView.findViewById(R.id.itemSwitchNo);
		ItemSwitchNo.setText(String.format("No:%d(%s)", switchItems[position].switchNo,switchItems[position].switchNameString));
		
		ItemSwitchType1 = (TextView) convertView.findViewById(R.id.itemSwitchType1);
		if(switchItems[position].switchType1==0)
		{
			ItemSwitchType1.setText("[系统]");
	
		}
		else
		{
			ItemSwitchType1.setText("[自定义]");
		}
		
		ItemSwitchEnable = (TextView) convertView.findViewById(R.id.itemSwitchEnable);
		if(switchItems[position].switchEnable==0)
		{
			ItemSwitchEnable.setText("[可用]");
	
		}
		else
		{
			ItemSwitchEnable.setText("[不可用]");
		}
		
		ItemSwitchType2 = (TextView) convertView.findViewById(R.id.itemSwitchType2);
		if(switchItems[position].switchType2==0)
		{
			ItemSwitchType2.setText("[电器类]");
	
		}
		else
		{
			ItemSwitchType2.setText("[按钮类]");
		}
		
		switch1 = (SlideSwitch) convertView.findViewById(R.id.itemSlideSwitch);
		if(switchItems[position].switchState==0)
		{
			switch1.setStatus(true);
			
		}
		else
		{
			switch1.setStatus(false);
		}
		
		switch1.setOnSwitchChangedListener(new OnSwitchChangedListener() {
			
			@Override
			public void onSwitchChanged(SlideSwitch obj, int status) {
				// TODO Auto-generated method stub
				
				mSwitchCallBack.SwitchItemStateChange(switchItems[p].switchNo, status);
			}
		});
		
		return convertView;
	}

	

	
	
}
