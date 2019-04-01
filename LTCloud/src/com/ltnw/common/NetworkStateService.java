package com.ltnw.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.ltnw.ltcloud.MainActivity;

public class NetworkStateService extends Service
{

	private static final String tag="tag";
	private ConnectivityManager connectivityManager;
    private NetworkInfo info;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) 
            {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();  
                if(info != null && info.isAvailable()) 
                {
                    String name = info.getTypeName();
                    
                	Message msg1 = new Message(); 
                    msg1.what = 1; 
                    msg1.obj =true;
  
                    MainActivity.handler.sendMessage(msg1);
                } 
                else 
                {
                    Message msg = new Message(); 
                    msg.what = 1; 
                    msg.obj =false;
  
                    MainActivity.handler.sendMessage(msg);
                }
            }
        }
    };
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
}

