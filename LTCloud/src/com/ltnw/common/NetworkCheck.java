package com.ltnw.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List; 

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

public class NetworkCheck 
{
	/**
	 * 检测网络是否连接
	 * 
	 * @return
	 */
	public boolean IsNetConnected(ConnectivityManager cm1) 
	{
		//ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		ConnectivityManager cm = cm1;
		if (cm != null) {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测wifi是否连接
	 * 
	 * @return
	 */
	public boolean IsWifiConnected(ConnectivityManager cm1) 
	{ 
		ConnectivityManager cm = cm1;
		//ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测3G是否连接
	 * 
	 * @return
	 */
	public boolean Is3gConnected(ConnectivityManager cm1) 
	{
		ConnectivityManager cm = cm1;
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测GPS是否打开
	 * 
	 * @return
	 */
	public boolean IsGpsEnabled(LocationManager lm1) 
	{
		//LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationManager lm =  lm1;
		List<String> accessibleProviders = lm.getProviders(true);
		for (String name : accessibleProviders) {
			if ("gps".equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public String getLocalIpAddress() 
	{
	    try 
	    {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } 
	    catch (SocketException ex) 
	    {
	        //Log.e(LOG_TAG, ex.toString());
	    }
	    return null;
	}
	
}
