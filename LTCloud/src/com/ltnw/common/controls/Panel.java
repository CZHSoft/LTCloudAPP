package com.ltnw.common.controls;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Panel extends LinearLayout implements GestureDetector.OnGestureListener{
	
	public interface PanelClosedEvent {
		void onPanelClosed(View panel);
	}
	
	public interface PanelOpenedEvent {
		void onPanelOpened(View panel);
	}
	
	private final static int HANDLE_WIDTH=30;
	private final static int MOVE_WIDTH=20;
	private Button btnHandler;
	private LinearLayout panelContainer;
	private int mRightMargin=0;
	private Context mContext;
	private GestureDetector mGestureDetector;
	private boolean mIsScrolling=false;
	private float mScrollX;
	private PanelClosedEvent panelClosedEvent=null;
	private PanelOpenedEvent panelOpenedEvent=null;
	
	public Panel(Context context,View otherView,int width,int height) {
		super(context);
		this.mContext=context;
		
		//定义手势识别
		mGestureDetector = new GestureDetector(mContext,this);
		mGestureDetector.setIsLongpressEnabled(false);
		
		//改变Panel附近组件的属�?
		LayoutParams otherLP=(LayoutParams) otherView.getLayoutParams();
		otherLP.weight=1;
		otherView.setLayoutParams(otherLP);
		
		//设置Panel本身的属�?
		LayoutParams lp=new LayoutParams(width, height);

		lp.rightMargin=-lp.width+HANDLE_WIDTH;
		mRightMargin=Math.abs(lp.rightMargin);
		this.setLayoutParams(lp);
		this.setOrientation(LinearLayout.HORIZONTAL);//asdf H
		
		//设置Handler的属�?
		btnHandler=new Button(context);
		btnHandler.setLayoutParams(new LayoutParams(HANDLE_WIDTH,height));
		//btnHandler.setOnClickListener(handlerClickEvent);
		btnHandler.setOnTouchListener(handlerTouchEvent);
		this.addView(btnHandler);
		
		//设置Container的属�?
		panelContainer=new LinearLayout(context);
		panelContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		this.addView(panelContainer);

	}

	private View.OnTouchListener handlerTouchEvent=new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_UP && //onScroll时的ACTION_UP
					mIsScrolling==true)
			{
				LayoutParams lp=(LayoutParams) Panel.this.getLayoutParams();
				if (lp.rightMargin >= (-mRightMargin/2)) {//�?��超过�?��
					new AsynMove().execute(new Integer[] { MOVE_WIDTH });// 正数展开
				} 
				else if (lp.rightMargin < (-mRightMargin/2)) {//�?��拖拉
					new AsynMove().execute(new Integer[] { -MOVE_WIDTH });// 负数收缩
				}
			}
			return mGestureDetector.onTouchEvent(event); 
		}
	};

	/**
	 * 定义收缩时的回调函数
	 * @param event
	 */
	public void setPanelClosedEvent(PanelClosedEvent event)
	{
		this.panelClosedEvent=event;
	}
	
	/**
	 * 定义展开时的回调函数
	 * @param event
	 */
	public void setPanelOpenedEvent(PanelOpenedEvent event)
	{
		this.panelOpenedEvent=event;
	}
	
	/**
	 * 把View放在Panel的Container
	 * @param v
	 */
	public void fillPanelContainer(View v)
	{
		panelContainer.addView(v);
	}
	
	/**
	 * 异步移动Panel
	 * @author hellogv 
	 */
	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times;
			if (mRightMargin % Math.abs(params[0]) == 0)// 整除
				times = mRightMargin / Math.abs(params[0]);
			else
				// 有余�?
				times = mRightMargin / Math.abs(params[0]) + 1;

			for (int i = 0; i < times; i++) {
				publishProgress(params);
				try {
					Thread.sleep(Math.abs(params[0]));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... params) {
			LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
			if (params[0] < 0)
				lp.rightMargin = Math.max(lp.rightMargin + params[0],
						(-mRightMargin));
			else
				lp.rightMargin = Math.min(lp.rightMargin + params[0], 0);

			if(lp.rightMargin==0 && panelOpenedEvent!=null){//展开之后
				panelOpenedEvent.onPanelOpened(Panel.this);//调用OPEN回调函数
			}
			else if(lp.rightMargin==-(mRightMargin) && panelClosedEvent!=null){//收缩之后
				panelClosedEvent.onPanelClosed(Panel.this);//调用CLOSE回调函数
			}
			Panel.this.setLayoutParams(lp);
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		mScrollX=0;
		mIsScrolling=false;
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
		if (lp.rightMargin < 0)// CLOSE的状�?
			new AsynMove().execute(new Integer[] { MOVE_WIDTH });// 正数展开
		else if (lp.rightMargin >= 0)// OPEN的状�?
			new AsynMove().execute(new Integer[] { -MOVE_WIDTH });// 负数收缩
		return false;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		mIsScrolling=true;
		mScrollX+=distanceX;
		
		LayoutParams lp=(LayoutParams) Panel.this.getLayoutParams();
		if (lp.rightMargin < -1 && mScrollX > 0) {//�?��拖拉
			lp.rightMargin = Math.min((lp.rightMargin + (int) mScrollX),0);
			Panel.this.setLayoutParams(lp);
			Log.e("onScroll",lp.rightMargin+"");
		} 
		else if (lp.rightMargin > -(mRightMargin) && mScrollX < 0) {//�?��拖拉
			lp.rightMargin = Math.max((lp.rightMargin + (int) mScrollX),-mRightMargin);
			Panel.this.setLayoutParams(lp);
		}
		
		if(lp.rightMargin==0 && panelOpenedEvent!=null){//展开之后
			panelOpenedEvent.onPanelOpened(Panel.this);//调用OPEN回调函数
		}
		else if(lp.rightMargin==-(mRightMargin) && panelClosedEvent!=null){//收缩之后
			panelClosedEvent.onPanelClosed(Panel.this);//调用CLOSE回调函数
		}
		Log.e("onScroll",lp.rightMargin+"");
		
		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {return false;}
	@Override
	public void onLongPress(MotionEvent e) {}
	@Override
	public void onShowPress(MotionEvent e) {}

}
