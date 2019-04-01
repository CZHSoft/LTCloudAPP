package com.ltnw.interFace;

public interface DialogCallBack 
{
	public void MonitorClose();
	public void TalkClose();
	public void Loginout();
	public void ReLogin();
	
	public void GetSwtichDetail(int no,int type1,int enable,int state,int type2,String name);
	
	public void SaveSwtichDetail(int no,int type1,int enable,int state,int type2,String name);
	
}
