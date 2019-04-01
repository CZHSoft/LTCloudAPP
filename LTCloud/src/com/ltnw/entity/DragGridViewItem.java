package com.ltnw.entity;

import android.R.string;
import android.widget.FrameLayout;

public class DragGridViewItem 
{
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private int icon;
	
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	
	
	public DragGridViewItem(){}
	
	public DragGridViewItem(String t,int i)
	{
		title=t;
		icon=i;
	}
	
}
