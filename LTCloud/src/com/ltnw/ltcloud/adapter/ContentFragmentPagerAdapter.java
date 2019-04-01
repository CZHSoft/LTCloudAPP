package com.ltnw.ltcloud.adapter;

import java.util.List;

import com.ltnw.entity.HomePageContentBean;
import com.ltnw.ltcloud.fragment.TestContentFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContentFragmentPagerAdapter extends FragmentStatePagerAdapter 
{

	private List<HomePageContentBean> list;
	
	public ContentFragmentPagerAdapter(FragmentManager fm) 
	{
		super(fm);
	}
	
	public ContentFragmentPagerAdapter(FragmentManager fm,List<HomePageContentBean> list) 
	{
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) 
	{
		return TestContentFragment.newInstance(list.get(arg0));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getTitle();
	}

}
