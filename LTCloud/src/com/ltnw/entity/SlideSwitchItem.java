package com.ltnw.entity;

import android.R.string;
import android.content.Context;
import android.util.Log;

public class SlideSwitchItem {
	

	private String titleN;
	private String switchN;
	private int id;
	private int groupN;
	private int indexN;

	private boolean initF=false;
	
	public boolean isInitF() {
		return initF;
	}

	public void setInitF(boolean initF) 
	{
		this.initF = initF;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	
	public String getTitleN() {
		return titleN;
	}

	public void setTitleN(String titleN) {
		this.titleN = titleN;
	}

	public String getSwitchN() {
		return switchN;
	}

	public void setSwitchN(String switchN) {
		this.switchN = switchN;
	}

	public int getGroupN() {
		return groupN;
	}

	public void setGroupN(int groupN) {
		this.groupN = groupN;
	}

	public int getIndexN() {
		return indexN;
	}

	public void setIndexN(int indexN) {
		this.indexN = indexN;
	}

	
	
	public SlideSwitchItem(){}
	
	public SlideSwitchItem(Context c,int id,String t,String s,int g,int i)
	{
		this.id=id;
		titleN=t;
		switchN=s;
		groupN=g;
		indexN=i;
		
	}
	

}
