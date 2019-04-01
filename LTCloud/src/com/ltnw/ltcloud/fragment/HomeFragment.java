package com.ltnw.ltcloud.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ltnw.entity.HomePageContentBean;
import com.ltnw.ltcloud.R;
import com.ltnw.ltcloud.adapter.ContentFragmentPagerAdapter;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


public class HomeFragment extends Fragment
{
	private ViewPager mViewPager;
	//private static final String[] titles = {"蓝天科技1","蓝天科技2"};
	private String[] titles;
	private String[] images;
	
	private List<HomePageContentBean> list;
	private ContentFragmentPagerAdapter mAdapter;
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
	{
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        list = new ArrayList<HomePageContentBean>();
        initData();
        findView(rootView);
        
        return rootView;
    }

	private void initData() 
	{
		titles = getResources().getStringArray(R.array.home_titles);  
		images = getResources().getStringArray(R.array.home_images); 
		
		//Log.i("HomeFragment", String.format("titles count:%d", titles.length));
			
		for(int i=0;i<titles.length;i++)
		{
			HomePageContentBean cb = new HomePageContentBean();
			cb.setTitle(titles[i]);
			cb.setContent(titles[i]+"_介绍:");
			cb.setImageResource(images[i]);
			//Log.i("HomeFragment", String.format("images source:%s", images[i]));
			list.add(cb);
		}
	}

	private void findView(View rootView) 
	{
		mViewPager = (ViewPager) rootView.findViewById(R.id.mViewPager);
		
		PagerTabStrip mPagerTabStrip = (PagerTabStrip) rootView.findViewById(R.id.mPagerTabStrip);
		mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.select_text_color)); 
		
		mAdapter = new ContentFragmentPagerAdapter(getActivity().getSupportFragmentManager(),list);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
	}
	
	@Override
	public void onStart() 
	{
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}
		
		super.onStart();
	}
}
