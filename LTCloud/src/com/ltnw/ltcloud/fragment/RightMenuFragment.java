package com.ltnw.ltcloud.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ltnw.ltcloud.R;

@SuppressLint("ValidFragment")
public class RightMenuFragment extends Fragment{

	private OnRightMenuFragmentClickListener mListener;
	
	public interface OnRightMenuFragmentClickListener 
	{
		@SuppressLint("ValidFragment")
		public void OnRightMenuFragmentClick(int operateObject);//接口中定义一个方法
	}
	
	public RightMenuFragment(String name,String state)
	{
		userNameString=name;
		userLoginStateString=state;
	}
	
	private Button followButton;
	private Button collectButton;
	private Button msgpushButton;
	private Button loginButton;
	private Button exitButton;
	
	
	private ImageButton returnButton;

	private TextView userNameTextView;
	private String userNameString;
	
	public String getUserNameString() {
		return userNameString;
	}

	public void setUserNameString(String userNameString) {
		
		this.userNameString = userNameString;
		
		userNameTextView.setText(userNameString);
		
		Log.i("RightMenuFragment", String.format("用户名变更:%s", userNameString));
	}

	private TextView userLoginStateTextView;
	private String userLoginStateString;
	
	public String getUserLoginStateString() {
		return userLoginStateString;
	}

	public void setUserLoginStateString(String userLoginStateString) {
		
		this.userLoginStateString = userLoginStateString;
		
		userLoginStateTextView.setText(userLoginStateString);
		
		Log.i("RightMenuFragment", String.format("状态变更:%s", userLoginStateString));
	}

	//private String loginState="离线";
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		try
		{
			mListener = (OnRightMenuFragmentClickListener) activity;// 这句就是赋初值了。
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ "must implement OnbtnSendClickListener");// 这条表示，你不在Activity里实现这个接口的话，我就要抛出异常咯。知道下一步该干嘛了吧？
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_right_menu, null);
		
		findView(rootView);
		
		return rootView;
	}

	private void findView(View rootView) 
	{
		/*----------------------------*/
		exitButton= (Button) rootView
				.findViewById(R.id.right_permsg_center_btn_exit);
		exitButton.setOnClickListener(ButtonOnClickListener);
		
		loginButton= (Button) rootView
				.findViewById(R.id.right_permsg_center_btn_login);
		loginButton.setOnClickListener(ButtonOnClickListener);
		
		returnButton = (ImageButton) rootView
				.findViewById(R.id.right_permsg_center_imgbtn_select);
		returnButton.setOnClickListener(ButtonOnClickListener);
		
		/*----------------------------*/
		
//		followButton = (Button) rootView
//				.findViewById(R.id.right_permsg_center_btn_follow);
//		followButton.setOnClickListener(ButtonOnClickListener);
//		
//		collectButton = (Button) rootView
//				.findViewById(R.id.right_permsg_center_btn_collect);
//		collectButton.setOnClickListener(ButtonOnClickListener);
		
		msgpushButton = (Button) rootView
				.findViewById(R.id.right_permsg_center_btn_msgpush);
		msgpushButton.setOnClickListener(ButtonOnClickListener);


		/*----------------------------*/
		
		userNameTextView=(TextView) rootView.findViewById(R.id.right_permsg_center_tv_name);
		userNameTextView.setText("用户:"+userNameString);
		
		userLoginStateTextView=(TextView) rootView.findViewById(R.id.right_permsg_center_tv_state);
		userLoginStateTextView.setText(userLoginStateString);
	}

	private Button.OnClickListener ButtonOnClickListener =new Button.OnClickListener() 
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) 
			{
//				case R.id.right_permsg_center_btn_follow:
//					mListener.OnRightMenuFragmentClick(11);
//					break;
//				case R.id.right_permsg_center_btn_collect:
//					mListener.OnRightMenuFragmentClick(12);
//					break;
				case R.id.right_permsg_center_btn_msgpush:
					mListener.OnRightMenuFragmentClick(13);
					break;
				case R.id.right_permsg_center_btn_exit:
					mListener.OnRightMenuFragmentClick(0);
					break;
				case R.id.right_permsg_center_btn_login:
					mListener.OnRightMenuFragmentClick(1);
					break;
				case R.id.right_permsg_center_imgbtn_select:
					mListener.OnRightMenuFragmentClick(9);
					break;
				
			}
		}
	};
}
