package com.ltnw.common;

public class SpeexCore 
{

	/*
	 * quality 1 : 4kbps (very noticeable artifacts, usually intelligible) 2 :
	 * 6kbps (very noticeable artifacts, good intelligibility) 4 : 8kbps
	 * (noticeable artifacts sometimes) 6 : 11kpbs (artifacts usually only
	 * noticeable with headphones) 8 : 15kbps (artifacts not usually noticeable)
	 */
	private static final int DEFAULT_COMPRESSION = 4;

	public SpeexCore() 
	{
		
	}

	public void init() {
		load();
		open(DEFAULT_COMPRESSION);
	}

	private void load() {
		try {
			System.loadLibrary("speex");
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public native int open(int compression);
	public native int getFrameSize();
	public native int decode(byte encoded[], short lin[], int size);
	public native int encode(short lin[], int offset, byte encoded[], int size);
	public native void close();
	
	/*echo cancellation*/
	public native int echoinit(int framesize,int filterlength);
	public native int echoplayback(short[] play);
	public native int echocapture(short[] rec ,short[] out);
	public native void echoclose();

}
