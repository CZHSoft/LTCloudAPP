package com.ltnw.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ltnw.entity.ProtocolAnalysisResult;

import android.R.array;
import android.util.Log;
import android.widget.Switch;

public class ProtocolControler 
{
	
	//turn? 0-3
//	public byte[] intToByteArray(int a)
//	{
//	    byte[] ret = new byte[4];
//	    ret[3] = (byte) (a & 0xFF);   
//	    ret[2] = (byte) ((a >> 8) & 0xFF);   
//	    ret[1] = (byte) ((a >> 16) & 0xFF);   
//	    ret[0] = (byte) ((a >> 24) & 0xFF);
//	    
//	    return ret;
//	}
	
	public byte[] intToByteArray(int a)
	{
	    byte[] ret = new byte[4];
	    ret[0] = (byte) (a & 0xFF);   
	    ret[1] = (byte) ((a >> 8) & 0xFF);   
	    ret[2] = (byte) ((a >> 16) & 0xFF);   
	    ret[3] = (byte) ((a >> 24) & 0xFF);
	    
	    return ret;
	}
	
	public byte[] ushortToByteArray(int a)
	{
	    byte[] ret = new byte[4];
	    ret[0] = (byte) (a & 0xFF);   
	    ret[1] = (byte) ((a >> 8) & 0xFF);   
	    
	    return ret;
	}
	 
	public int byteArrayToUshort(byte[] res) 
	{ 
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000 

//		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
//				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		
		int ret = (res[0] & 0xff) | ((res[1] << 8) & 0xff00);
		
		return ret; 
		
	} 
	
	public int byteArrayToInt(byte[] res) 
	{ 
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000 

		int ret = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		
		
		return ret; 
		
	} 
	
	public Byte[] BaseProtocolData(int cmd,int cmdType,int parm1,int parm2,int parm3,
			String imei,String phoneIP,String address,
			String userName,String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		
		try {
			//4
			byte[] header = ("LTNW").getBytes();
			for (int i = 0; i < header.length; i++) {
				byteList.add(header[i]);
			}
			// 2
			byteList.add((byte) 0x08);
			byteList.add((byte) cmd);
			// 4
			byteList.add((byte) cmdType);
			
			byteList.add((byte) parm1);
			byteList.add((byte) parm2);
			byteList.add((byte) parm3);
            //////-------------10
			
			// 20
			byte[] imeiByte = (imei+'\0').getBytes();
			for (int i = 0; i < imeiByte.length; i++) {
				byteList.add(imeiByte[i]);
			}
			for (int i = imeiByte.length; i < 20; i++) {
				byteList.add((byte) 0x00);
				//Log.i("ltnw", String.valueOf(byteList.size()));
			}
		    //////-------------30
			// 4
			Pattern ipPattern = Pattern.compile(
					 "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
			Matcher matcher = ipPattern.matcher(phoneIP);
			if(!matcher.matches())
			{
				Log.i("ProtocolControler", "BaseProtocolData ip not match");
				return null;
			}
//			Log.i("phoneIP", phoneIP);
			String[] ipStrings = phoneIP.split("\\.");
//			Log.i("ipStrings length", String.valueOf(ipStrings.length));
			byte[] localIPB = new byte[4];
			
			for (int i = 0; i < 4; i++) {
//				Log.i("ipStrings", ipStrings[i]);
//				Log.i("ipStrings", String.valueOf(i));
//				localIPB[i] = Byte.parseByte(ipStrings[i], 10);
				localIPB[i] = (byte)Integer.parseInt(ipStrings[i]);
				byteList.add(localIPB[i]);
			}
		    //////-------------34
			
			// 20
			byte[] addressByte = (address+'\0').getBytes();
			for (int i = 0; i < addressByte.length; i++) {
				byteList.add(addressByte[i]);
			}
			for (int i = addressByte.length; i < 20; i++) {
				byteList.add((byte) 0x00);
			}
		    //////-------------54
			
			// 20
			byte[] usernameByte = (userName).getBytes();
			for (int i = 0; i < usernameByte.length; i++) {
				byteList.add(usernameByte[i]);
			}
			for (int i = usernameByte.length; i < 20; i++) {
				byteList.add((byte) 0x00);
			}
		    //////-------------74
			
			// 20
			byte[] passwordByte = (passWord).getBytes();
			for (int i = 0; i < passwordByte.length; i++) {
				byteList.add(passwordByte[i]);
			}
			for (int i = passwordByte.length; i < 20; i++) {
				byteList.add((byte) 0x00);
			}
		    //////-------------94
			
			return byteList.toArray(new Byte[byteList.size()]);
			
			
		} 
		catch (Exception e) {
			// TODO: handle exception
			Log.i("ProtocolControler", "BaseProtocolData return null");
			e.printStackTrace();
			return null;
		}
	}
	
	public Byte[] HeartBeatCall(String imei)
	{
		Log.i("ProtocolControler", "HeartBeatCall");
		
		List<Byte> byteList = new ArrayList<Byte>();
		
		try {
			//4
			byte[] header = ("LTNW").getBytes();
			for (int i = 0; i < header.length; i++) {
				byteList.add(header[i]);
			}
			// 2
			byteList.add((byte) 0x08);
			byteList.add((byte) 0);
			// 4
			byteList.add((byte) 0);
			
			byteList.add((byte) 0);
			byteList.add((byte) 0);
			byteList.add((byte) 0);
            //////-------------10
			
			// 20
			byte[] imeiByte = (imei+'\0').getBytes();
			for (int i = 0; i < imeiByte.length; i++) {
				byteList.add(imeiByte[i]);
			}
			for (int i = imeiByte.length; i < 20; i++) {
				byteList.add((byte) 0x00);
				Log.i("ltnw", String.valueOf(byteList.size()));
			}
			
			return byteList.toArray(new Byte[byteList.size()]);
			
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.i("ProtocolControler", "BaseProtocolData return null");
			e.printStackTrace();
			return null;
		}

	}
	
	public Byte[] TerConFeedBack(String imei, String phoneIP,String address,
			String userName, String passWord,int type) {
		
		List<Byte> byteList = new ArrayList<Byte>();
		
		try {
			
			Byte[] baseBytes=BaseProtocolData(1, type, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","TerConFeedBack return base null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
		    //////-------------94

			Log.i("ProtocolControler", String.format("%s length:%d", "TerConFeedBack",byteList.size()));

			return byteList.toArray(new Byte[byteList.size()]);

		} 
		catch (Exception e) {
			//System.out.println(e.getMessage());
			Log.i("ProtocolControler", "LoginCall return null");
			
			return null;
		}
	}
	
	public Byte[] LoginCall(String imei, String phoneIP,String address,
			String userName, String passWord,
			int recBufSize,int trkBufSize) {
		
		List<Byte> byteList = new ArrayList<Byte>();
		
		try {
			
			Byte[] baseBytes=BaseProtocolData(8, 1, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","LoginCall return base null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
		    //////-------------94
			
			//4
			byte[] recBufSizeByte=intToByteArray(recBufSize);
			for (int i = 0; i < recBufSizeByte.length; i++) {
				byteList.add(recBufSizeByte[i]);
			}
		//////-------------98
			//4
			byte[] trkBufSizeByte=intToByteArray(trkBufSize);
			for (int i = 0; i < trkBufSizeByte.length; i++) {
				byteList.add(trkBufSizeByte[i]);
			}
		//////-------------102

			Log.i("ProtocolControler", String.format("%s length:%d", "LoginCall",byteList.size()));

			return byteList.toArray(new Byte[byteList.size()]);

		} 
		catch (Exception e) {
			//System.out.println(e.getMessage());
			Log.i("ProtocolControler", "LoginCall return null");
			
			return null;
		}
	}

	public Byte[] LoginOutCall(String imei, String phoneIP,String address,
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		
		try {
			
			Byte[] baseBytes=BaseProtocolData(8, 2, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","LoginOutCall return base null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
		    //////-------------94
			
			Log.i("ProtocolControler", String.format("%s length:%d", "LoginOutCall",byteList.size()));

			return byteList.toArray(new Byte[byteList.size()]);
		}
		catch (Exception e) {
			//System.out.println(e.getMessage());
			Log.i("ProtocolControler", "LoginOutCall return null");
			
			return null;
		}
	}
	
	public Byte[] SwitchInfoCall(String imei, String phoneIP,String address,String userName, String passWord) 
	{
		try 
		{
			Byte[] baseBytes=BaseProtocolData(81, 8, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","SwitchInfoCall return base null");
				return null;
			}
			else 
			{
				Log.i("ProtocolControler", String.format("%s length:%d", "SwitchCall",baseBytes.length));
				
				return baseBytes;
			}

		} catch (Exception e) {
			
			Log.i("ProtocolControler","SwitchInfoCall return null");
			return null;
		}
	}
	
	public Byte[] SwitchInfoModCall(String imei, String phoneIP,String address,String userName, String passWord,
			byte switchNo,byte switchType1,byte switchEnable,byte switchState,byte switchType2,byte[] switchName) 
	{
		try 
		{
			List<Byte> byteList = new ArrayList<Byte>();
			
			Byte[] baseBytes=BaseProtocolData(81, 2, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","SwitchInfoModCall base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<142;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------142	
			byteList.add(switchNo);
			byteList.add(switchType1);
			byteList.add(switchEnable);
			byteList.add(switchState);
			byteList.add(switchType2);
			for(int i=0;i<switchName.length;i++)
			{
				byteList.add(switchName[i]);
			}
			for(int i=switchName.length;i<10;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------157
			Log.i("ProtocolControler", String.format("%s length:%d", "SwitchInfoModCall",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);
			

		} catch (Exception e) {
			
			Log.i("ProtocolControler","SwitchInfoCall return null");
			return null;
		}
		
	}
	
	public Byte[] SwitchStateChangeCall(String imei, String phoneIP,String address, 
			String userName, String passWord, 
			int switchNo,int switchState) {

		try 
		{
			Byte[] baseBytes=BaseProtocolData(81, 1, switchNo, switchState, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","switchStateChangeCall return base null");
				return null;
			}
			else 
			{
				Log.i("ProtocolControler", String.format("%s length:%d", "switchStateChangeCall",baseBytes.length));
				
				return baseBytes;
			}

		} catch (Exception e) {
			
			Log.i("ProtocolControler","switchStateChangeCall return null");
			return null;
		}
	}
	
	public Byte[] UnLockTestCall(String phone, String localIP,
			String remoteAddress, String remoteIP) {

		List<Byte> byteList = new ArrayList<Byte>();
		try {
			// 4
			byte[] header = ("LTNW").getBytes();
			for (int i = 0; i < header.length; i++) {
				byteList.add(header[i]);
			}

			// 2
			byteList.add((byte) 0x08);
			byteList.add((byte) 83);
			// 4
			byteList.add((byte) 0x00);
			byteList.add((byte) 0x00);
			byteList.add((byte) 0x00);
			byteList.add((byte) 0x00);

			// 20
			byte[] phoneB = phone.getBytes();
			Log.i("ltnw", String.valueOf(phoneB.length) + "长(phone)");
			for (int i = 0; i < phoneB.length; i++) {
				byteList.add(phoneB[i]);
			}
			for (int i = phoneB.length; i < 20; i++) {
				byteList.add((byte) 0x00);
				Log.i("ltnw", String.valueOf(byteList.size()));
			}

			// 4
			String[] localStrings = localIP.split("\\.");
			Log.i("ltnw", String.valueOf(localStrings.length) + "长(lIP)");
			// Log.i("ltnw",localStrings[0]);
			// Log.i("ltnw",localStrings[1]);
			// Log.i("ltnw",localStrings[2]);
			// Log.i("ltnw",localStrings[3]);
			byte[] localIPB = new byte[4];
			for (int i = 0; i < localIPB.length; i++) {
				localIPB[i] = Byte.valueOf(localStrings[i]);
				byteList.add(localIPB[i]);
			}

			// 20
			byte[] remoteAddressB = remoteAddress.getBytes();
			Log.i("ltnw", String.valueOf(remoteAddressB.length) + "长(add)");
			for (int i = 0; i < remoteAddressB.length; i++) {
				byteList.add(remoteAddressB[i]);
			}
			for (int i = remoteAddressB.length; i < 20; i++) {
				byteList.add((byte) 0x00);
				Log.i("ltnw", String.valueOf(byteList.size()));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		Log.i("ltnw", String.valueOf(byteList.size()) + "长(byteList)");
		if (byteList.size() == 52) {
			return byteList.toArray(new Byte[byteList.size()]);
		} else {
			return null;
		}
	}

	public Byte[] TalkCall(String imei, String phoneIP,String address, 
			String userName, String passWord) 
	{
		try 
		{
			Byte[] baseBytes=BaseProtocolData(89, 1, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","TalkCall base return null");
				return null;
			}
			else 
			{
				Log.i("ProtocolControler", String.format("%s length:%d", "TalkCall",baseBytes.length));
				return baseBytes;
			}
			
			
		} catch (Exception e) {

			Log.i("ProtocolControler","TalkCall return null");
			return null;
		}
	}
	
	public Byte[] TalkStart(String imei, String phoneIP,String address,byte[] id, 
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		try 
		{
			Byte[] baseBytes=BaseProtocolData(89, 2, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","TalkStart base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
			//byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			
			Log.i("ProtocolControler", String.format("%s length:%d", "TalkStart",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","TalkStart return null");
			return null;
		}
	}
	
	public Byte[] TalkConfirm(String imei, String phoneIP,String address, byte[] id,
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		try 
		{
			Byte[] baseBytes=BaseProtocolData(89, 3, 3, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","TalkConfirm base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
			//byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			
			Log.i("ProtocolControler", String.format("%s length:%d", "TalkConfirm",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","TalkConfirm return null");
			return null;
		}
	}
	
	public Byte[] TalkUp(String imei,String phoneIP,String address,byte[] id, 
			String userName, String passWord, 
			int dataLen,byte[] data)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		
		try 
		{
			Byte[] baseBytes=BaseProtocolData(89, 5, 3, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","TalkUp base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
		//////-------------94
			for(int i=byteList.size();i<98;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------98
			//2
			byte[] portByte=ushortToByteArray(dataLen);
			for (int i = 0; i < portByte.length; i++) {
				byteList.add(portByte[i]);
			}
		//////-------------100
			int dataLength=data.length;
			byte[] lenByte=ushortToByteArray(dataLength);
			for (int i = 0; i < lenByte.length; i++) {
				byteList.add(lenByte[i]);
			}
		//////-------------102
			byteList.add((byte)0x06);
		//////-------------103
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
//			byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			for (int i = 0; i < data.length; i++) {
				byteList.add(data[i]);
			}
		//////-------------142+datalength
			//Log.i("TalkUp", String.valueOf(byteList.size()) + "长(byteList)");

			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","TalkUp return null");
			return null;
		}
	}
	
	public Byte[] TalkEnd(String imei,String phoneIP,String address,byte[] id, 
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		try 
		{
			Byte[] baseBytes=BaseProtocolData(89, 4, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","TalkEnd base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
//			byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			
			Log.i("ProtocolControler", String.format("%s length:%d", "TalkEnd",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","TalkEnd return null");
			return null;
		}
	}
	
	public Byte[] MonitorCall(String imei, String phoneIP,String address, 
			String userName, String passWord) 
	{
		try 
		{
			Byte[] baseBytes=BaseProtocolData(90, 1, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","MonitorCall base return null");
				return null;
			}
			else 
			{
				Log.i("ProtocolControler", String.format("%s length:%d", "MonitorCall",baseBytes.length));
				return baseBytes;
			}
			
			
		} catch (Exception e) {
			Log.i("ProtocolControler","MonitorCall return null");
			return null;
		}
	}
	
	public Byte[] MonitorStart(String imei, String phoneIP,String address,byte[] id, 
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		try 
		{
			Byte[] baseBytes=BaseProtocolData(90, 2, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","MonitorStart base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
//			byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			
		
			Log.i("ProtocolControler", String.format("%s length:%d", "MonitorStart",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","MonitorStart return null");
			return null;
		}
	}
	
	public Byte[] MonitorConfirm(String imei, String phoneIP,String address, byte[] id,
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		try 
		{
			Byte[] baseBytes=BaseProtocolData(90, 3, 3, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","MonitorConfirm base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
			//byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			
			Log.i("ProtocolControler", String.format("%s length:%d", "MonitorConfirm",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","MonitorConfirm return null");
			return null;
		}
	}
	
	public Byte[] MonitorEnd(String imei,String phoneIP,String address,byte[] id, 
			String userName, String passWord)
	{
		List<Byte> byteList = new ArrayList<Byte>();
		try 
		{
			Byte[] baseBytes=BaseProtocolData(90, 4, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","MonitorEnd base return null");
				return null;
			}
			else 
			{
				for (int i = 0; i < baseBytes.length; i++) {
					byteList.add(baseBytes[i]);
				}
			}
			
			for(int i=byteList.size();i<110;i++)
			{
				byteList.add((byte)0x00);
			}
		//////-------------110
			//byte[] idByte = id.getBytes();
			for (int i = 0; i < id.length; i++) {
				byteList.add(id[i]);
			}
		//////-------------142
			
			Log.i("ProtocolControler", String.format("%s length:%d", "MonitorEnd",baseBytes.length));
			return byteList.toArray(new Byte[byteList.size()]);

		} catch (Exception e) {
			
			Log.i("ProtocolControler","MonitorEnd return null");
			return null;
		}
	}

	public Byte[] DynPortReturn(String imei, String phoneIP,String address, 
			String userName, String passWord)
	{
		try 
		{
			Byte[] baseBytes=BaseProtocolData(0, 0, 0, 0, 8, imei, phoneIP, address,userName,passWord);
			if(baseBytes==null)
			{
				Log.i("ProtocolControler","DynPortReturn base return null");
				return null;
			}
			else 
			{
				Log.i("ProtocolControler", String.format("%s length:%d", "DynPortReturn",baseBytes.length));
				return baseBytes;
			}
			
			
		} catch (Exception e) {
			Log.i("ProtocolControler","DynPortReturn return null");
			return null;
		}
	}
	
	public ProtocolAnalysisResult ProtocolAnalysis(byte[] data) 
	{
//		Log.i("ProtocolControler",String.format("DataLength:%d", data.length));
//		Log.i("ProtocolControler",String.format("cmd:%d", data[5]));
//		Log.i("ProtocolControler",String.format("cmdType:%d", data[6]));
//		Log.i("ProtocolControler",String.format("returnType:%d", data[9]));
		
		switch (data[5]) 
		{
		case 0:
			return new ProtocolAnalysisResult(0,data[6], 0, null, null, 0,0,0,0,0, null);
		case 8:
			switch(data[6])
			{
			case 1:
				switch (data[9]) {
				case 0:
					byte[] addByte=new byte[20];
					System.arraycopy(data, 34, addByte, 0, addByte.length);
					String address=String.valueOf(addByte);
					return new ProtocolAnalysisResult(8,data[6], 0, address, null, 0,0,0,0,0, null);
				case 1:
					return new ProtocolAnalysisResult(8,data[6], 1, null, null, 0,0,0,0,0, null);
				case 2:
					return new ProtocolAnalysisResult(8, data[6],2, null, null, 0,0,0,0,0, null);
				default:
					break;
				}
				break;
			case 2:
				return new ProtocolAnalysisResult(8,data[6], 0, null, null, 0,0,0,0,0, null);
			case 3:
				return new ProtocolAnalysisResult(8,data[6], data[9], null, null, 0,0,0,0,0, null);
			}
			break;
		case 81:
			switch(data[6])
			{
			case 1:
				switch (data[9]) {
				case 0:
					return new ProtocolAnalysisResult(81,data[6], 0, null, null, 0,0,0,0,0, null);
				case 1:
					return new ProtocolAnalysisResult(81,data[6], 1, null, null, 0,0,0,0,0, null);
				case 2:
					return new ProtocolAnalysisResult(81, data[6],2, null, null, 0,0,0,0,0, null);
				default:
					break;
				}
				break;
			case 2:
				switch (data[9]) {
				case 0:
					byte[] addByte=new byte[20];
					return new ProtocolAnalysisResult(81,data[6], 0, null, null, 0,0,0,0,0, null);
				case 1:
					return new ProtocolAnalysisResult(81,data[6], 1, null, null, 0,0,0,0,0, null);
				case 2:
					return new ProtocolAnalysisResult(81, data[6],2, null, null, 0,0,0,0,0, null);
				default:
					break;
				}
				break;
			case 8:
				
				byte[] lenByte=new byte[4];
				System.arraycopy(data, 142, lenByte, 0, lenByte.length);
				int len=byteArrayToInt(lenByte);
				byte[] switchdata=new byte[len];
				System.arraycopy(data, 146, switchdata, 0, switchdata.length);
				return new ProtocolAnalysisResult(81,data[6], data[9], null, null, 0,0,0,0,0, switchdata);
			}
			break;
		case 82:
			break;
		case 83:
			break;
		case 89:
			switch (data[6]) 
			{
			case 1:
				switch (data[9]) {
				case 0:
					byte[] des=new byte[2];
					System.arraycopy(data, 98, des, 0, des.length);
					int port=byteArrayToUshort(des);
					Log.i("ProtocolControler", String.format("port:%d", port));
					byte[] id=new byte[32];
					System.arraycopy(data, 110, id, 0, id.length);
					return new ProtocolAnalysisResult(89,data[6], data[9], null, id, port,0,0,0,0, null);
				case 1:
					break;
				case 2:
					break;
				case 6:
					return new ProtocolAnalysisResult(89,data[6], data[9], null, null, 0,0,0,0,0, null);
				default:
					break;
				}
				break;
			case 2:
				switch (data[9]) {
				case 0:
					return new ProtocolAnalysisResult(89,data[6], data[9], null, null, 0,0,0,0,0, null);
				case 1:
					break;
				case 2:
					break;
				default:
					break;
				}
				break;
			case 3:
				//confirm
				return new ProtocolAnalysisResult(89,data[6], data[9], null, null, 0,0,0,0,0, null);
			case 4:
				switch (data[9]) {
				case 0:
					return new ProtocolAnalysisResult(89,data[6], data[9], null, null, 0,0,0,0,0, null);
				case 1:
					break;
				case 2:
					break;
				case 8:
					return new ProtocolAnalysisResult(89,data[6], data[9], null, null, 0,0,0,0,0, null);
				default:
					break;
				}
				break;
			case (-106):
				byte[] des=new byte[2];
				System.arraycopy(data, 158, des, 0, des.length);
				int len=byteArrayToUshort(des);
//				Log.i("ProtocolControler", String.format("len:%d", len));
				byte[] audio=new byte[len];
				System.arraycopy(data, 170, audio, 0, audio.length);
				
				return new ProtocolAnalysisResult(89,data[6], data[9], null, null, 0,0,0,0,0, audio);

			default:
				break;
			}
			break;
		case 90:
			switch (data[6]) 
			{
			case 0:
				byte[] des=new byte[2];
				System.arraycopy(data, 94, des, 0, des.length);
				int port=byteArrayToUshort(des);
				return new ProtocolAnalysisResult(90,data[6], data[9], null, null, port,0,0,0,0, null);
			case 1:
				switch (data[9]) {
				case 0:
					byte[] des1=new byte[2];
					System.arraycopy(data, 98, des1, 0, des1.length);
					int port1=byteArrayToUshort(des1);
					Log.i("ProtocolControler", String.format("port1:%d", port1));
					byte[] id=new byte[32];
					System.arraycopy(data, 110, id, 0, id.length);
					return new ProtocolAnalysisResult(90,data[6], data[9], null, id, port1,0,0,0,0, null);
				case 1:
					break;
					
				case 2:
					break;
				default:
					break;
				}
				break;
			case 2:
				switch (data[9]) {
				case 0:
					return new ProtocolAnalysisResult(90,data[6], data[9], null, null, 0,0,0,0,0, null);
				case 1:
					break;
				case 2:
					break;
				default:
					break;
				}
				break;
			case 3:
				return new ProtocolAnalysisResult(90,data[6], data[9], null, null, 0,0,0,0,0, null);
			case 4:
				switch (data[9]) {
				case 0:
					return new ProtocolAnalysisResult(90,data[6], data[9], null, null, 0,0,0,0,0, null);
				case 1:
					break;
				case 2:
					break;
				default:
					break;
					
				}
				break;
				
			case (-104):
				
				byte[] tempByte=new byte[2];
				System.arraycopy(data, 148, tempByte, 0, tempByte.length);
				int ff=byteArrayToUshort(tempByte);
//				Log.i("ProtocolControler",String.format("FF:%d",ff));
				byte[] tempByte2=new byte[4];
				System.arraycopy(data, 150, tempByte2, 0, tempByte2.length);
				int fc=byteArrayToInt(tempByte2);
//				Log.i("ProtocolControler",String.format("FC:%d",fc));
				System.arraycopy(data, 156, tempByte, 0, tempByte.length);
				int pf=byteArrayToUshort(tempByte);
//				Log.i("ProtocolControler",String.format("PF:%d",pf));
				System.arraycopy(data, 154, tempByte, 0, tempByte.length);
				int pc=byteArrayToUshort(tempByte);
//				Log.i("ProtocolControler",String.format("PC:%d",pc));
				System.arraycopy(data, 158, tempByte, 0, tempByte.length);
				int dl=byteArrayToUshort(tempByte);
//				Log.i("ProtocolControler",String.format("DL:%d",dl));
				byte[] streamBuf=streamBuf=new byte[dl];
				System.arraycopy(data, 170, streamBuf, 0, streamBuf.length);
				
				return new ProtocolAnalysisResult(90,data[6], data[9], null, null, 2,ff,fc,pf,pc, streamBuf);
			
			default:
				break;
			}
			break;
		default:
			break;
		}

		return new ProtocolAnalysisResult(0,0, 0, null, null, 0,0,0,0,0, null);
	}

	public byte[] ConvertByteArray2byteArray(Byte[] tempBytes) {
		if (tempBytes != null) {
			byte[] buf = new byte[tempBytes.length];

			for (int i = 0; i < buf.length; i++) {
				buf[i] = tempBytes[i].byteValue();
			}

			return buf;
		} else {
			return null;
		}
	}
}

