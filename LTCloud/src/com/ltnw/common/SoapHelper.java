package com.ltnw.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.ltnw.interFace.LoginCallBack;
import com.ltnw.ltcloud.LoginActivity;

import android.os.Message;
import android.widget.Toast;

public class SoapHelper 
{
	private String imeiString;
	private String phoneString;
	
	private String loginNameString;
	private String passWordString;
	
	private String serverIPString;
	private int serverPort;
	
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerIPString() {
		return serverIPString;
	}

	public void setServerIPString(String serverIPString) {
		this.serverIPString = serverIPString;
	}

	public String getLoginNameString() {
		return loginNameString;
	}

	public void setLoginNameString(String loginNameString) {
		this.loginNameString = loginNameString;
	}

	public String getPassWordString() {
		return passWordString;
	}

	public void setPassWordString(String passWordString) {
		this.passWordString = passWordString;
	}

	public String getImeiString() {
		return imeiString;
	}

	public void setImeiString(String imeiString) {
		this.imeiString = imeiString;
	}

	public String getPhoneString() {
		return phoneString;
	}

	public void setPhoneString(String phoneString) {
		this.phoneString = phoneString;
	}

	/**
	 * 
	 */
    private LoginCallBack mLoginCallBack;
	
    public void GetReg(String result,boolean flag)
    {
    	mLoginCallBack.getRegResult(result, flag);
    }
    
	public void GetLogin(String result,boolean normal)
	{
		mLoginCallBack.getLoginResult(result,normal);
	}
	
	public void setOnLoginCallBack(LoginCallBack loginCallBack)
	{
		this.mLoginCallBack=loginCallBack;
	}

	
	
	private void RegByPhone(String serverIP,String imei,String phone) {
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(new Date(System.currentTimeMillis()));

			SoapObject request = new SoapObject("http://LTNW", "RegByPhone");

			request.addProperty("imei", imei);
			request.addProperty("phone", phone);
			request.addProperty("date",dateString);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					String.format("http://%s:%d/LTNW/Service/SmarkService", serverIP,serverPort));
			//"http://198.168.0.17:8887/LTNW/Service/SmarkService"
			androidHttpTransport.call("http://LTNW/ISmark/RegByPhone", envelope);
//			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			Object  response=  envelope.getResponse(); 

//			Message msg1 = new Message(); 
//            msg1.what = 1; 
//            msg1.obj =Boolean.parseBoolean(result.toString());
//
//            LoginActivity.handler1.sendMessage(msg1);
			
			GetReg(response.toString(),true);
			
			
		} catch (Exception e) {
			e.printStackTrace();
          
//			Message msg1 = new Message(); 
//            msg1.what = 1; 
//            msg1.obj =false;
//
//            LoginActivity.handler1.sendMessage(msg1);
			GetReg("·¢ËÍÊ§°Ü£¡",false);

		}
	}
	
	private class GetRegByPhone implements Runnable {
		@Override
		public void run() {
			try 
			{
				RegByPhone(serverIPString,imeiString,phoneString);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void DoRegByPhone()
	{
		new Thread(new GetRegByPhone()).start();
	}
	
	private void LoginByPhone(String serverIP,String name,String password,String imei) {
		try {

			SoapObject request = new SoapObject("http://LTNW", "LoginByPhone");

			request.addProperty("name", name);
			request.addProperty("password", password);
            request.addProperty("imei", imei);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

//			HttpTransportSE androidHttpTransport = new HttpTransportSE(
//					"http://198.168.0.17:8887/LTNW/Service/SmarkService");
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					String.format("http://%s:%d/LTNW/Service/SmarkService", serverIP,serverPort));
			androidHttpTransport.call("http://LTNW/ISmark/LoginByPhone", envelope);
//			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
//			GetLogin(result.toString(),true);
			Object  response=  envelope.getResponse(); 
			GetLogin(response.toString(),true);
			
//			Message msg1 = new Message(); 
//            msg1.what = 2; 
//            msg1.obj =Boolean.parseBoolean(result.toString());
//
//            LoginActivity.handler1.sendMessage(msg1);
			
//			GetLogin(Boolean.parseBoolean(result.toString()),true);
			
		} catch (Exception e) {
			e.printStackTrace();
          
//			Message msg1 = new Message(); 
//            msg1.what = 2; 
//            msg1.obj =false;
//
//            LoginActivity.handler1.sendMessage(msg1);
			GetLogin("µÇÂ¼Ê§°Ü!",false);
		}
	}
	
	private class GetLoignByPhone implements Runnable {
		@Override
		public void run() {
			try 
			{
				LoginByPhone(serverIPString,loginNameString,passWordString,imeiString);
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void DoLoginByPhone()
	{
		new Thread(new GetLoignByPhone()).start();
	}
	
}
