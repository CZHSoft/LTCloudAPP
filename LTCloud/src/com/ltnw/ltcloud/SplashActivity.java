package com.ltnw.ltcloud;

import java.io.IOException;
import java.util.Properties;

import com.ltnw.common.PropertiesHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashActivity extends Activity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	private static final long SPLASH_DELAY_MILLIS = 3000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	private Handler mHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		init();
	}

	private void init() {

		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		isFirstIn = preferences.getBoolean("isFirstIn", true);

		if (!isFirstIn) {

			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	private void goHome() 
	{
		Log.i("splash", "splash->login");
		
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide(){

		PropertiesHelper helper = new PropertiesHelper();

		if (helper.InitConfigFile(this)) 
		{
			Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
		else 
		{
			Log.i("splash", "create pro fail,exit");
			this.finish();
		}
	}
}
