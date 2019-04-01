package com.ltnw.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import com.ltnw.interFace.GetPublicIpCallBack;


import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class NetWorkStateHelper 
{
    private GetPublicIpCallBack mGetPublicIpCallBack;
	
	public void GetPublicIp(String result)
	{
		mGetPublicIpCallBack.getPublicIp(result);
	}
	
	public void setOnGetPublicIpCallBack(GetPublicIpCallBack getPublicIpCallBack)
	{
		this.mGetPublicIpCallBack=getPublicIpCallBack;
	}
	
	
	/**
	 * 138IP
	 * 
	 */
	private String GetNetIpFromIP138() {
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			// http://iframe.ip138.com/ic.asp
			// infoUrl = new URL("http://city.ip138.com/city0.asp");
			infoUrl = new URL("http://iframe.ip138.com/ic.asp");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();
				// 从反馈的结果中提取出IP地址
				int start = strber.indexOf("[");
				int end = strber.indexOf("]", start + 1);
				line = strber.substring(start + 1, end);
				return line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 局域网地址
	 */
	public String getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
					.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
				NetworkInterface intf = mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf
						.getInetAddresses(); enumIPAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIPAddr.nextElement();
					// 如果不是回环地址
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
						// 直接返回本地IP地址
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			//System.err.print("error");
			return null;
		}
		return null;
		
	}

	
	/**
	 * http://www.cz88.net/ip/viewip778.aspx
	 * 
	 * @param strUrl
	 * @return
	 */
	private String getNetIpFromCZ88() {
		try 
		{

			
			URL url = new URL("http://www.cz88.net/ip/viewip778.aspx");
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) 
			{
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						url.openStream()));

				String s = "";
				StringBuffer sb = new StringBuffer("");
				while ((s = br.readLine()) != null) {
					sb.append(s + "\r\n");
				}
				br.close();

				String webContent = "";
				webContent = sb.toString();

				if (webContent.equals(null) || webContent.equals(""))
					return null;

				String flagofForeignIPString = "IPMessage";
				int startIP = webContent.indexOf(flagofForeignIPString)
						+ flagofForeignIPString.length() + 2;
				int endIP = webContent.indexOf("</span>", startIP);
				return webContent.substring(startIP, endIP);
			}
			else {
				return null;
			}
			
			
//			URL url = new URL("http://www.cz88.net/ip/viewip778.aspx");
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					url.openStream()));
//
//			String s = "";
//			StringBuffer sb = new StringBuffer("");
//			while ((s = br.readLine()) != null) {
//				sb.append(s + "\r\n");
//			}
//			br.close();
//
//			String webContent = "";
//			webContent = sb.toString();
//
//			if (webContent.equals(null) || webContent.equals(""))
//				return null;
//
//			String flagofForeignIPString = "IPMessage";
//			int startIP = webContent.indexOf(flagofForeignIPString)
//					+ flagofForeignIPString.length() + 2;
//			int endIP = webContent.indexOf("</span>", startIP);
//			return webContent.substring(startIP, endIP);


			// String flagofLocationString ="AddrMessage";
			// int startLoc =
			// webContent.indexOf(flagofLocationString)+flagofLocationString.length()+2;
			// int endLoc = webContent.indexOf("</span>",startLoc);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void GetPublicIP()
	{
		new Thread(new GetNetIP()).start();;
	}
	
	public boolean Ping(String str) {
		boolean resault = false;
			Process p;
			try {
				//ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒 
				p = Runtime.getRuntime().exec("ping -c 1 -w 1 -i 1 " + str);
				int status = p.waitFor();

//				InputStream input = p.getInputStream();
//				BufferedReader in = new BufferedReader(new InputStreamReader(input));
//			    StringBuffer buffer = new StringBuffer();
//			    String line = "";
//			    while ((line = in.readLine()) != null){
//			      buffer.append(line);
//			    }
			    //System.out.println("Return ============" + buffer.toString());

				if (status == 0) {
					resault = true;
				} else {
					resault = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

		return resault;
	}
	
//	private class NetPing extends AsyncTask<String, String, String> 
//	{
//
//		@Override
//		protected boolean doInBackground(String... params) {
//			boolean s = false;
//			s = Ping("www.baidu.com");
//
//			return s;
//		}
//	}
	
	private class GetNetIP implements Runnable {
		@Override
		public void run() {
			try 
			{
				String ipString=getNetIpFromCZ88();
//				Message msg1 = new Message(); 
//                msg1.what = 2; 
//                msg1.obj =ipString;
//
//                MainActivity.handler.sendMessage(msg1);
				
				GetPublicIp(ipString);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				GetPublicIp(null);
				
			}
		}
	}

}
