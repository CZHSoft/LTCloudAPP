package com.ltnw.ltcloud.fragment;

import com.ltnw.common.AudioRecordHelper;
import com.ltnw.common.SpeexHelper2;
import com.ltnw.interFace.AudioCallBack;
import com.ltnw.ltcloud.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class TalkP2PFragment extends Fragment
{
	private boolean talkFlag=false;
	private ImageButton talkButton;
	private TextView talkText;
	private Button closeButton;
	
	private AudioRecordHelper audioHelper=null;
//	private SpeexHelper speexHelper=null;
	private SpeexHelper2 speexHelper2=null;
	private byte[] decodeData=null;
	private byte[] aecData=null;
	public TalkP2PFragment()
	{
		decodeData=new byte[320];
		aecData=new byte[320];
	}
	
	private AudioCallBack mAudioCallBack;
	public void setmAudioCallBack(AudioCallBack mAudioCallBack) {
		this.mAudioCallBack = mAudioCallBack;
	}
	
	private void SendAudioData(byte[] data) 
	{
		//encode
		aecData=speexHelper2.Capture(data);
		mAudioCallBack.SendRecordSound(speexHelper2.Encode(aecData));
	}
	
	private void StopP2P()
	{

		mAudioCallBack.TalkClose();
	}
	
	public TalkP2PFragment(String ip,int port,int speexSize)
	{
		audioHelper=new AudioRecordHelper(speexSize);
//		speexHelper=new SpeexHelper();
		speexHelper2=new SpeexHelper2(160, 1024);
		
		audioHelper.setmAudioCallBack(new AudioCallBack() 
		{
			@Override
			public void SendRecordSound(byte[] data) 
			{
				// TODO Auto-generated method stub
				SendAudioData(data);
			}

			@Override
			public void TalkClose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		audioHelper.WorkStart();
//
//		if(helper.InitUdpSetting(ip, port))
//		{
//			helper.WorkStart();
//		}
	}
	
	public void Stop()
	{
		audioHelper.WorkStop();
		speexHelper2.FreeSpeex();
	}
	
	public void PushAudioData(byte[] data)
	{
		//decode
//		decodeData=speexHelper2.Decode(data);
//		audioHelper.GetAudioData(speexHelper2.Capture(decodeData));
		audioHelper.GetAudioData(speexHelper2.Decode(data));
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_talk_p2p, container, false);
        
        findView(rootView);
        
        return rootView;
    }
	
	private void findView(View rootView) 
	{
		talkText = (TextView) rootView
				.findViewById(R.id.talkStateLabel);
		
		talkButton = (ImageButton) rootView
				.findViewById(R.id.talkTouchButton);

		talkButton.setBackgroundResource(R.drawable.talk_normal);
		
		talkButton.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				talkFlag=true;
				talkButton.setBackgroundResource(R.drawable.talk_pressed);

				audioHelper.RecordSwitch(true);
				
				talkText.setText("开始说话");
				
				return true;
			}
		});
		
		talkButton.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) 
				{
				case MotionEvent.ACTION_UP:
					talkFlag=false;
					talkButton.setBackgroundResource(R.drawable.talk_normal);

					audioHelper.RecordSwitch(false);
					
					talkText.setText("按住说话");
					
					break;

				default:
					break;
				}
				return false;
			}
		});
		
		closeButton= (Button) rootView
				.findViewById(R.id.talkCloseBtn);
		closeButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				StopP2P();
			}
		});
	}
}
