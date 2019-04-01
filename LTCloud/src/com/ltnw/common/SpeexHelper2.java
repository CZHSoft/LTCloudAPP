package com.ltnw.common;

import java.nio.ByteOrder;

import android.util.Log;

public class SpeexHelper2 
{
	private int encoder_packagesize = 1200;
	private int aecFramesize=160;
	private SpeexCore encodeSpeex;
	private SpeexCore decoderSpeex;
	private SpeexCore aecSpeex;
	
	private short[] encodeShortDataBuf=null;
	private byte[] encodeDataBuf=null;
	private byte[] encodeData=null;
	
	private short[] decodeShortDataBuf;
	private short[] decodeDataBuf;
	private byte[] decodeData=null;
	
	private short[] captureShortDataBuf=null;
	private short[] captureDataBuf=null;
	
	
	public SpeexHelper2(int framesize,int filterlength)
	{
		encodeSpeex=new SpeexCore();
		encodeSpeex.init();
		
		encodeShortDataBuf=new short[160];
		encodeDataBuf=new byte[320];
		encodeData =new byte[20];
		
		decoderSpeex=new SpeexCore();
		decoderSpeex.init();
		
		decodeShortDataBuf=new short[320];
		decodeDataBuf=new short[160];
		decodeData=new byte[320];
		
		aecSpeex=new SpeexCore();
		aecFramesize=framesize;
		aecSpeex.echoinit(framesize, filterlength);
		
		captureShortDataBuf=new short[160];
		captureDataBuf=new short[aecFramesize];
	}
	
	public byte[] Encode(byte[] data)
	{
		//320 ->160
		encodeShortDataBuf=Bytes2Shorts(data);
		//Log.i("EncodeFromRecord", String.format("short len:%d,temp len:%d", shortData.length,tempData.length));
		
		int getSize =encodeSpeex.encode(encodeShortDataBuf, 0, encodeDataBuf, encodeShortDataBuf.length);
//		Log.i("EncodeFromRecord", String.format("getSize:%d", getSize));
		if(getSize>0)
		{
			System.arraycopy(encodeDataBuf, 0, encodeData, 0, getSize);
			return encodeData;
		}
		
		return null;
	}
	
	public byte[] Decode(byte[] data)
	{
		int getSize = decoderSpeex.decode(data, decodeShortDataBuf, data.length);
//		Log.i("DecodeFromReceive", String.format("getSize:%d", getSize));
		if(getSize>0)
		{
			System.arraycopy(decodeShortDataBuf, 0, decodeDataBuf, 0, getSize);
			return Shorts2Bytes(decodeDataBuf);
		}
		
		return null;
	}
	
	public byte[] Capture(byte[] data)
	{
		captureShortDataBuf=Bytes2Shorts(data);

		aecSpeex.echocapture(captureShortDataBuf,captureDataBuf);
		
		return Shorts2Bytes(captureDataBuf);
	}
	
	public void Playback(byte[] data)
	{
		short[] play=Bytes2Shorts(data);
		aecSpeex.echoplayback(play);
	}
	
	public void FreeSpeex()
	{
		encodeSpeex.close();
		decoderSpeex.close();
		aecSpeex.echoclose();
	}
	
	private boolean testCPU() {  
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {  
            // System.out.println("is big ending");  
            return true;  
        } else {  
            // System.out.println("is little ending");  
            return false;  
        }  
    }
    
    private byte[] getBytes(short s, boolean bBigEnding) {  
        byte[] buf = new byte[2];  
        if (bBigEnding)  
            for (int i = buf.length - 1; i >= 0; i--) {  
                buf[i] = (byte) (s & 0x00ff);  
                s >>= 8;  
            }  
        else  
            for (int i = 0; i < buf.length; i++) {  
                buf[i] = (byte) (s & 0x00ff);  
                s >>= 8;  
            }  
        return buf;  
    }  
    
    private short getShort(byte[] buf, boolean bBigEnding) {  
        if (buf == null) {  
            throw new IllegalArgumentException("byte array is null!");  
        }  
        if (buf.length > 2) {  
            throw new IllegalArgumentException("byte array size > 2 !");  
        }  
        short r = 0;  
        if (bBigEnding) {  
            for (int i = 0; i < buf.length; i++) {  
                r <<= 8;  
                r |= (buf[i] & 0x00ff);  
            }  
        } else {  
            for (int i = buf.length - 1; i >= 0; i--) {  
                r <<= 8;  
                r |= (buf[i] & 0x00ff);  
            }  
        }  
  
        return r;  
    }  
    
    private short getShort(byte[] buf) {  
        return getShort(buf, this.testCPU());  
    }  
    private byte[] getBytes(short s) {  
        return getBytes(s, this.testCPU());  
    }  
    
    private short[] Bytes2Shorts(byte[] buf) {  
        byte bLength = 2;  
        short[] s = new short[buf.length / bLength];  
        for (int iLoop = 0; iLoop < s.length; iLoop++) {  
            byte[] temp = new byte[bLength];  
            for (int jLoop = 0; jLoop < bLength; jLoop++) {  
                temp[jLoop] = buf[iLoop * bLength + jLoop];  
            }  
            s[iLoop] = getShort(temp);  
        }  
        return s;  
    }  
  
    private byte[] Shorts2Bytes(short[] s) {  
        byte bLength = 2;  
        byte[] buf = new byte[s.length * bLength];  
        for (int iLoop = 0; iLoop < s.length; iLoop++) {  
            byte[] temp = getBytes(s[iLoop]);  
            for (int jLoop = 0; jLoop < bLength; jLoop++) {  
                buf[iLoop * bLength + jLoop] = temp[jLoop];  
            }  
        }  
        return buf;  
    }  
	

}
