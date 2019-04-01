package com.ltnw.entity;

import android.R.integer;
import android.provider.ContactsContract.Contacts.Data;

public class ProtocolAnalysisResult 
{
	private int protocolType;
	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	private int cmdType;
	
	public int getCmdType() {
		return cmdType;
	}

	public void setCmdType(int cmdType) {
		this.cmdType = cmdType;
	}

	private int returnType;
	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	private String address;
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private int port;
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private byte[] data;
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}


	private byte[] guid;
	public byte[] getGuid() {
		return guid;
	}

	public void setGuid(byte[] guid) {
		this.guid = guid;
	}
	private int frameFlag;
	private int frameCount;
	private int packetFlag;
	private int packetCount;
	
	public int getFrameFlag() {
		return frameFlag;
	}

	public void setFrameFlag(int frameFlag) {
		this.frameFlag = frameFlag;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public int getPacketFlag() {
		return packetFlag;
	}

	public void setPacketFlag(int packetFlag) {
		this.packetFlag = packetFlag;
	}

	public int getPacketCount() {
		return packetCount;
	}

	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}


	
	public ProtocolAnalysisResult(int pt,int ct,int rt,
			String add,byte[] id,
			int p,int ff,int fc,int pf,int pc,byte[] d)
	{
		protocolType=pt;
		cmdType=ct;
		returnType=rt;
		address=add;
		guid=id;
		port=p;
		frameFlag=ff;
		frameCount=fc;
		packetFlag=pf;
		packetCount=pc;
		data=d;
	}
}
