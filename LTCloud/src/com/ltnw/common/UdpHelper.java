package com.ltnw.common;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class UdpHelper {

	public boolean listenFlag;

	private boolean isLocalNW=false;
	
	public boolean isLocalNW() {
		return isLocalNW;
	}
	public void setLocalNW(boolean isLocalNW) {
		this.isLocalNW = isLocalNW;
	}

	private InetAddress serverAddress;
	private DatagramSocket receDS;
	private DatagramPacket receDP;
	private byte[] receBuf;
	private String listenAddString;
	private int listenPort;

	private InetAddress sendAddress;
	private DatagramSocket sendDS;
	private DatagramPacket sendDP;
	private byte[] sendBuf;
	private int sendPort;
	private String sendAddString;

	private OnUdpReceiveListener mListener;

	public void setUdpReceiveListener(OnUdpReceiveListener mListener) {
		this.mListener = mListener;
	}

	public interface OnUdpReceiveListener {
		public void DoUdpReceiveListener(byte[] receBuf, int length);// 接口中定义一个方法
	}

	public boolean InitListen(String add, int port) 
	{
		listenAddString = add;
		listenPort=port;
		
		receBuf = new byte[1024 * 50];
		sendBuf = new byte[1024 * 10];

		try 
		{
			receDS = new DatagramSocket(port, serverAddress);
			sendDS = new DatagramSocket();
			Log.i("UdpHelper", String.format("udp start!listen port:%d", port));
		} 
		catch (Exception e) 
		{
			return false;
		}

		return true;
	}

	public void ListenStart() {

		try 
		{
			listenFlag = true;
			new Thread(new UdpServer()).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("UdpHelper", "ListenStart fail");
			listenFlag = false;
		}

	}

	public void ListenStop() 
	{
		listenFlag = false;
		
		if (receDS != null) {
			receDS.disconnect();
			receDS.close();
			receDS = null;
		}

		if (sendDS != null) 
		{
			sendDS = null;
		}

		System.gc();
	}

	public void SendData(byte[] data, String address, int port) {
		try {
			sendBuf = data;
			sendAddString = address;
			sendPort = port;

			new Thread(new UdpSender()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("UdpHelper", "send fail");
		}
	}

	private class UdpSender implements Runnable {
		@Override
		public void run() {
			try {
				
				sendDS = new DatagramSocket();
				sendAddress = InetAddress.getByName(sendAddString);
				sendDP = new DatagramPacket(sendBuf, sendBuf.length,
						sendAddress, sendPort);
				
				if(!isLocalNW)
				{
					receDS.send(sendDP);
//					Log.i("UdpHelper", "send by server");
				}
				else
				{
					sendDS.send(sendDP);
//					Log.i("UdpHelper", "send by local");
				}

//				Log.i("UdpHelper", String.format("had send data to %s:%d by port:%d", sendAddString,sendPort,listenPort));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("UdpHelper", "send data error");
			}
		}
	}

	private class UdpServer implements Runnable {
		@Override
		public void run() {
			try {
				serverAddress = InetAddress.getByName(listenAddString);
				
				Log.i("UdpHelper", String.format("server start,port %d...", listenPort));
				
			} catch (Exception e) {
				Log.i("UdpHelper", "server start error");
			}
			while (listenFlag) {
				try {
					receDP = new DatagramPacket(receBuf, receBuf.length);

					receDS.receive(receDP);

//					Log.i("UdpHelper", String.format("%d server recLen:%d",listenPort, receDP.getLength()));
					
					mListener.DoUdpReceiveListener(receBuf, receDP.getLength());

				} catch (Exception e) {
					Log.i("UdpHelper", "server receive error");
					//Log.i("UdpHelper", e.getMessage());
				}
			}
			
			Log.i("UdpHelper", String.format("server stop,port %d...", listenPort));

		}
	}

}
