package com.ltnw.common;

public class ProtocolJniClient 
{

	private void load() {
		try {
			System.loadLibrary("speex");
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	
    static public native Byte[] HeartBeatCall(String imei);
}
