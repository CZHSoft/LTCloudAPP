package com.ltnw.ltcloud.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ltnw.common.controls.DragGridView;
import com.ltnw.common.controls.DragGridView.OnChanageListener;
import com.ltnw.common.controls.Panel;
import com.ltnw.common.controls.PinnedHeaderExpandableListView;
import com.ltnw.common.controls.SlideSwitch;
import com.ltnw.common.controls.StickyLayout;
import com.ltnw.entity.DragGridViewItem;
import com.ltnw.entity.Group;
import com.ltnw.entity.People;
import com.ltnw.entity.SlideSwitchItem;
import com.ltnw.entity.SwitchItem;
import com.ltnw.interFace.SwitchCallBack;
import com.ltnw.ltcloud.R;
import com.ltnw.ltcloud.adapter.DragGridViewAdapter;
import com.ltnw.ltcloud.adapter.SwitchAdapter;
import com.ltnw.ltcloud.fragment.SettingFragment.OnSettingButtonClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class SwitchControlFragment extends Fragment
{
	
	private Context context;
    
	private SwitchItem[] switchItems; 
	private SwitchAdapter switchAdapter=null;
    
//	private OnSwitchButtonClickListener mListener;
//	
//	public interface OnSwitchButtonClickListener 
//	{
//		public void OnSwitchButtonClick(String switchName,int switchValue);//接口中定义一个方法
//	}
	
	private SwitchCallBack mSwitchCallBack;
	
	public void setmSwitchCallBack(SwitchCallBack mSwitchCallBack) {
		this.mSwitchCallBack = mSwitchCallBack;
	}
	
	
    public SwitchControlFragment(SwitchItem[] switchItems)
    {
    	this.switchItems=switchItems;
    }
    
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
//		try
//		{
//			mSwitchCallBack = (SwitchCallBack) activity;
////			mListener = (OnSwitchButtonClickListener) activity;// 这句就是赋初值了。
//		}
//		catch (ClassCastException e)
//		{
//			throw new ClassCastException(activity.toString()
//					+ "must implement OnbtnSendClickListener");// 这条表示，你不在Activity里实现这个接口的话，我就要抛出异常咯。知道下一步该干嘛了吧？
//		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_switch, container, false);
        
        ListView list = (ListView) rootView.findViewById(R.id.switchListView);
        
        switchAdapter = new SwitchAdapter(rootView.getContext(),switchItems);
       
        switchAdapter.setmSwitchCallBack(new com.ltnw.interFace.SwitchCallBack() {
			
			@Override
			public void SwitchItemValueChange(int switchNo, int switchType1,
					int switchEnable, int switchState, int switchType2,
					String switchName) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void SwitchItemStateChange(int switchNo, int state) {
				// TODO Auto-generated method stub
//				Toast.makeText(rootView.getContext(), String.format("switchNo:%d status:%d", switchNo,state),
//						Toast.LENGTH_SHORT).show();
				mSwitchCallBack.SwitchItemStateChange(switchNo, state);
				
			}

			@Override
			public void SwitchDialogCallBack(int switchNo, int switchType1,
					int switchEnable, int switchState, int switchType2,
					String switchName) {
				// TODO Auto-generated method stub
				mSwitchCallBack.SwitchDialogCallBack(switchNo,switchType1,switchEnable,switchState,switchType2,switchName);
				
				
			}
		});
        
        
        list.setAdapter(switchAdapter);


        return rootView;
    }
	

}
