package com.ltnw.ltcloud.fragment;

import com.ltnw.interFace.MonitorCallBack;
import com.ltnw.ltcloud.R;

import android.R.string;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


@SuppressLint("ValidFragment")
public class MonitorFragment extends Fragment 
{
//	private UdpHelper udpHelper;
//	private ProtocolControler protocol;
	
	private ImageView monitorImageView=null;
	private Button closeButton;
	private Bitmap tempBitmap=null;

	private MonitorCallBack mMonitorCallBack;
	public void setmMonitorCallBack(MonitorCallBack mMonitorCallBack) {
		this.mMonitorCallBack = mMonitorCallBack;
	}
	
	private void StopMonitor()
	{
		mMonitorCallBack.MonitorClose();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_monitor, container, false);
        
        findView(rootView);
        
        return rootView;
    }
	
	private void findView(View rootView) 
	{
		monitorImageView=(ImageView) rootView
				.findViewById(R.id.monitorImageView);
		
		closeButton= (Button) rootView
				.findViewById(R.id.monitorCloseBtn);
		closeButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				StopMonitor();
			}
		});
	}
	

	public void ReflashImageView(Bitmap bitmap)
	{
		monitorImageView.setImageBitmap(bitmap); 
	}
	

}
