package com.ltnw.ltcloud.fragment;

import java.lang.reflect.Field;

import com.ltnw.entity.HomePageContentBean;
import com.ltnw.ltcloud.R;

import android.R.drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TestContentFragment extends Fragment 
{
	private static final String TAG = TestContentFragment.class.getSimpleName();
	//private String title = "Hello";
	private HomePageContentBean homePageContent;
	
	public static TestContentFragment newInstance(HomePageContentBean content) {
		TestContentFragment newFragment = new TestContentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        
        newFragment.setArguments(bundle);

        return newFragment;

    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.d(TAG, "TestContentFragment-----onCreate");
        Bundle args = getArguments();
        
        if(args!=null)
        {
        	//title = args.getString("title");
        	homePageContent=(HomePageContentBean) args.getSerializable("content");
        }
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 View rootView = inflater.inflate(R.layout.fragment_test_content, container, false);
         
		 findView(rootView);
		 
	     return rootView;
	}

	private void findView(View rootView) 
	{
		
		TextView txtLabel = (TextView) rootView.findViewById(R.id.txtLabel);
		txtLabel.setText(homePageContent.getContent());
		//Log.i("test", getResources().getString(R.drawable.new1));
		//Resources.getIdentifier()
		int resID = getResources().getIdentifier(homePageContent.getImageResource(), "drawable", "com.ltnw.ltcloud");
		ImageView imageDemo=(ImageView) rootView.findViewById(R.id.imageDemo);
		imageDemo.setImageResource(resID);
		
		//imageDemo.seti
	}
	
	public  int getResId(String variableName, Class<?> c) {

	    try {
	        Field idField = c.getDeclaredField(variableName);
	        return idField.getInt(idField);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    } 
	}
}
