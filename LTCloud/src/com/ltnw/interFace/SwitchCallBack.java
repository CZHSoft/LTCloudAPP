package com.ltnw.interFace;

public interface SwitchCallBack {
	public void SwitchItemStateChange(int switchNo,int state);
	
	public void SwitchItemValueChange(
			int switchNo,
			int switchType1,
			int switchEnable,
			int switchState,
			int switchType2,
			String switchName);
	
	public void SwitchDialogCallBack(
			int switchNo,
			int switchType1,
			int switchEnable,
			int switchState,
			int switchType2,
			String switchName);
}
