package com.ltnw.ltcloud.fragment;

import com.ltnw.ltcloud.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingFragment extends Fragment
{

	private OnSettingButtonClickListener mListener;
	
	private Button settingButton;
	private EditText serverIPEditText;
	
	public interface OnSettingButtonClickListener 
	{
		public void OnSettingButtonClick(String serverIP);//�ӿ��ж���һ������
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mListener = (OnSettingButtonClickListener) activity;// �����Ǹ���ֵ�ˡ�
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ "must implement OnbtnSendClickListener");// ������ʾ���㲻��Activity��ʵ������ӿڵĻ����Ҿ�Ҫ�׳��쳣����֪����һ���ø����˰ɣ�
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
 
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        serverIPEditText =(EditText)rootView.findViewById(R.id.etServerIP); 
        
        settingButton = (Button) rootView.findViewById(R.id.btnSetting); 
		settingButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListener.OnSettingButtonClick(serverIPEditText.getText().toString());
			}
		});
		
        return rootView;
    }
	
}
