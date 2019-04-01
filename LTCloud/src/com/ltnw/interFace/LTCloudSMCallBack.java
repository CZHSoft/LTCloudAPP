package com.ltnw.interFace;

public interface LTCloudSMCallBack 
{
	public void FreeCallBack(boolean flag);
	
	public void ConnectEnterCallBack();
	
	public void SwitchInfoEnterCallBack();
	public void SwitchModEnterCallBack();
	public void SwitchStateChangeEnterCallBack();
	public void SwitchInfoBack();
	public void SwitchModBack();
	public void SwitchActionCallBack();
	public void SwitchActionReturnBack();
	
	public void DefenceInfoCallCallBack();
	public void DefenceInfoBack();
	public void DefenceActionCallBack();
	public void DefenceActionReturnBack();
	
	public void TalkCallCallBack(boolean flag);
	public void TalkAnswerCallBack(boolean flag);
	public void TalkStarkCallBack(boolean flag);
	public void TalkUpDownCallBack(boolean flag);
	public void TalkEndCallBack(boolean flag);
	
	public void MonitorCallCallBack(boolean flag);
	public void MonitorStarkCallBack(boolean flag);
	public void MonitorUpDownCallBack(boolean flag);
	public void MonitorEndCallBack(boolean flag);
	
	public void SMOverCallBack();
}
