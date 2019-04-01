package com.ltnw.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

//–¥»Î≈‰÷√£∫
//Properties prop = new Properties();
//prop.put("prop1", "abc");
//prop.put("prop2", 1);
//prop.put("prop3", 3.14);
//saveConfig(this, "/sdcard/config.dat", prop);
//
//∂¡»°≈‰÷√£∫
//Properties prop = loadConfig(this, "/sdcard/config.dat");
//String prop1 = prop.get("prop1");

public class PropertiesHelper 
{
	public Properties LoadConfig(Context context) {
		
		Properties properties = new Properties();
		
		String propertiesPath = context.getFilesDir().getPath().toString()
				+ "/ltapp.properties";
		
		Log.i("LoadConfig", propertiesPath);
		
		FileInputStream s=null;
		
		try 
		{
			
			s = new FileInputStream(propertiesPath);
			Log.i("LoadConfig", "1");
			properties.load(s);
			Log.i("LoadConfig", "2");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Log.i("LoadConfig", "fail");
		}
		finally
		{
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties;
	}

	public boolean SaveConfig(Context context,
			String sID,String sIP,String lPort,String sPort,
			String un,String pw,String key,
			String ver){
		
		String propertiesPath = context.getFilesDir().getPath().toString()
				+ "/ltapp.properties";
		Properties prop = new Properties();
		prop.put("ServerID", sID);
		prop.put("ServerIP", sIP);
		prop.put("LoginPort", lPort);
		prop.put("ServerPort", sPort);
		prop.put("UserName", un);
		prop.put("PassWord", pw);
		prop.put("Key", key);
		prop.put("Ver", ver);
		
		FileOutputStream s=null;
		
		try {
			s = new FileOutputStream(propertiesPath, false);
			prop.store(s, "");
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean InitConfigFile(Context context) {
		String propertiesPath = context.getFilesDir().getPath().toString()
				+ "/ltapp.properties";

		Properties prop = new Properties();
		prop.put("ServerID", "8888");
		prop.put("ServerIP", "61.142.172.230");
		prop.put("LoginPort", "8888");
		prop.put("ServerPort", "5887");
		prop.put("UserName", "admin");
		prop.put("PassWord", "admin");
		prop.put("Key","abcdefghijklmnop");
		prop.put("Ver", "1.0");
		
		FileOutputStream s=null;
		
		try {
			s = new FileOutputStream(propertiesPath, false);
			prop.store(s, "");
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
