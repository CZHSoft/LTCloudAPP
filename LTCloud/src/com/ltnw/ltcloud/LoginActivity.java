package com.ltnw.ltcloud;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ltnw.common.AESCBCLib;
import com.ltnw.common.PropertiesHelper;
import com.ltnw.common.SoapHelper;
import com.ltnw.interFace.LoginCallBack;
import com.ltnw.ltcloud.fragment.RightMenuFragment.OnRightMenuFragmentClickListener;

import android.R.bool;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 * 20140425 add the login normal or not flag
 */
public class LoginActivity extends Activity 
{
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	private PropertiesHelper propertiesHelper=null;
	
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private TextView mVerTextView;
	
	private String sidString;
	private String sipString;
	private String lportString;
	private String sportString;
	private String unString;
	private String pwString;
	private String keyString;
	private String verString;

	private int flag=0;
	
	//public static Handler handler1=null;
	
	private SoapHelper soap;
	
	private AESCBCLib aeslib;
	
	private Dialog dialogLoginSetting;
	private AlertDialog isSaveAlertDialog;
	
	private boolean loginActionFlag=false;
	private boolean loginSuccessFlag=false;
	private boolean loginNormalFlag=true;
	private String loginKeyString=null;
	
	private String imeiString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

//		Intent intent = new Intent(getBaseContext(), AppStatusService.class);
//		startService(intent);
//		Log.i("Login", "start app status service");
		
		aeslib=new AESCBCLib();
		
		propertiesHelper = new PropertiesHelper();
		Properties prop = propertiesHelper.LoadConfig(this);
		if (prop != null) {
			
			sidString = prop.getProperty("ServerID");
			sipString = prop.getProperty("ServerIP");
			lportString = prop.getProperty("LoginPort");
			sportString = prop.getProperty("ServerPort");
			unString = prop.getProperty("UserName");
			pwString = prop.getProperty("PassWord");
			keyString = prop.getProperty("Key");
			verString = prop.getProperty("Ver");
			
			Log.i("Login", String.format("unString:%s", unString));
			Log.i("Login", String.format("pwString:%s", pwString));
			Log.i("Login", String.format("keyString:%s", keyString));
			Log.i("Login", String.format("sipString:%s", sipString));
			Log.i("Login", String.format("sportString:%s", sportString));
			Log.i("Login", String.format("lportString:%s", lportString));
			
		}
		else 
		{
			Log.i("Login", "prop null");
		}

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		// mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		if(unString.equals("admin"))
		{
			mEmailView.setText(unString);
			mPasswordView.setText(pwString);
		}
		else
		{
		
			String un=aeslib.DEncrypt(unString, keyString, keyString).replaceAll("\u0000","");
			String pw=aeslib.DEncrypt(pwString, keyString, keyString).replaceAll("\u0000","");
			
			Log.i("Login", String.format("un:%s", un));
			Log.i("Login", String.format("pw:%s", pw));
			
			mEmailView.setText(un);
			mPasswordView.setText(pw);
		}
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		mVerTextView=(TextView) findViewById(R.id.ver_textview);
		mVerTextView.setText("Ver:"+verString);
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.sign_in_register).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptRegister();
			}
		});
		
		findViewById(R.id.sign_in_rechoice).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,ChoiceLocationActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		findViewById(R.id.sign_in_setting).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//
				ShowLoginSettingDialog(sidString,sipString,sportString);
			}
		});
		
		
		soap=new SoapHelper();

		soap.setOnLoginCallBack(new LoginCallBack() {
			
			@Override
			public void getLoginResult(String result,boolean normal)
			{
				// TODO Auto-generated method stub
//				loginSuccessFlag=result;

				loginKeyString=result;
				loginNormalFlag=normal;
				loginActionFlag=true;
				
				Log.i("Login", "Login try complete");
			}

			@Override
			public void getRegResult(String result, boolean flag) {
				// TODO Auto-generated method stub
				
				if (result != null && !result.equals("")) {

					Looper.prepare();
					Toast.makeText(getApplicationContext(), result,
							Toast.LENGTH_LONG).show();
					Looper.loop();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{

			if(event.getRepeatCount() == 0)
			{
			   Toast.makeText(getApplicationContext(), "长按退出！",
						Toast.LENGTH_SHORT).show();
			   
			}
			else
			{
//				Intent stopIntent = new Intent(this, AppStatusService.class);
//				stopService(stopIntent);
//				Log.i("Login", "AppStatusService stop");
				
				finish();
			}
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{

		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {

//		System.out.println("MainActivity.onDestroy()");

		Log.i("Login", "LoginActivity.onDestroy()");
		
		super.onDestroy();
	}
	
	public boolean isIp(String IP){//判断是否是一个IP  
        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){  
            String s[] = IP.split("\\.");  
            if(Integer.parseInt(s[0])<255)  
                if(Integer.parseInt(s[1])<255)  
                    if(Integer.parseInt(s[2])<255)  
                        if(Integer.parseInt(s[3])<255)  
                           return true;  
        }  
        return false;  
    }  
	
	public void ShowLoginSettingDialog(String id,String ip,String port)
	{
		dialogLoginSetting = new Dialog(this);
		dialogLoginSetting.setContentView(R.layout.login_setting_dialog);
		dialogLoginSetting.setTitle("登录设置...");
		
		final EditText etServID= (EditText) dialogLoginSetting.findViewById(R.id.etServID);
		etServID.setText(sidString);
		final EditText etServIP= (EditText) dialogLoginSetting.findViewById(R.id.etServIP);
		etServIP.setText(sipString);
		final EditText etSoapPort= (EditText) dialogLoginSetting.findViewById(R.id.etSoapPort);
		etSoapPort.setText(lportString);
		final EditText etServPort= (EditText) dialogLoginSetting.findViewById(R.id.etServPort);
		etServPort.setText(sportString);
		
		Button dialogButton = (Button) dialogLoginSetting.findViewById(R.id.login_set_OK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//save data
				
				if(!isIp(etServIP.getText().toString()))
				{
					etServIP.setFocusable(true);
					etServIP.requestFocus();
					
					Toast.makeText(getApplicationContext(), "IP格式不合法,请在输入框输入对应IP！",
					Toast.LENGTH_SHORT).show();
					return;  
				}
				
				
				Pattern pattern = Pattern.compile("[0-9]*"); 
				if(!pattern.matcher(etServPort.getText().toString()).matches())
				{
					etServPort.setFocusable(true);
					etServPort.requestFocus();
					
					Toast.makeText(getApplicationContext(), "端口格式不合法,请在输入框输入对应端口！",
					Toast.LENGTH_SHORT).show();
					return;  
				}
			      
				
				ShowLoginSettingAlertDialog(
						etServID.getText().toString(),
						etServIP.getText().toString(),
						etSoapPort.getText().toString(),
						etServPort.getText().toString()
						);
				
				
				
			}
		});

		Button dialogCancelButton = (Button) dialogLoginSetting.findViewById(R.id.login_set_Cancel);
		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogLoginSetting.dismiss();
			}
		});
		
		dialogLoginSetting.show();
	}
	
	public void ShowLoginSettingAlertDialog(final String id,final String ip,final String lport,final String sport) {
		// 创建退出对话框
		isSaveAlertDialog = new AlertDialog.Builder(this).create();
		// 设置对话框标题
		isSaveAlertDialog.setTitle("系统提示");
		// 设置对话框消息
		isSaveAlertDialog.setMessage("确定要保持设置？");
		// 添加选择按钮并注册监听
		isSaveAlertDialog.setButton("确定", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				
				sidString = id;
				sipString = ip;
				lportString = lport;
				sportString = sport;
				
				Log.i("Login", String.format("sidString:%s", sidString));
				Log.i("Login", String.format("sipString:%s", sipString));
				Log.i("Login", String.format("lportString:%s", lportString));
				Log.i("Login", String.format("sportString:%s", sportString));

				dialog.dismiss();
				
				dialogLoginSetting.dismiss();
			}
		});
		isSaveAlertDialog.setButton2("取消", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		// 显示对话框

		isSaveAlertDialog.show();
	}
	
	public void attemptRegister()
	{

		if(isMobileNO(mEmailView.getText().toString()))
		{
			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);

			String imei = tm.getDeviceId();
			soap.setImeiString(imei);
			soap.setPhoneString(mEmailView.getText().toString());
			soap.setServerIPString(sipString);
			soap.setServerPort(Integer.parseInt(lportString));
			soap.DoRegByPhone();
		}
		else
		{
			mEmailView.setFocusable(true);
			mEmailView.requestFocus();
			
			Toast.makeText(getApplicationContext(), "手机号码不合法,请在输入框输入对应手机号码！",
			Toast.LENGTH_SHORT).show();
		}

	}
	
	public  boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		imeiString = tm.getDeviceId();
		
		boolean cancel = false;
		View focusView = null;
		
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		
		// else if (!mEmail.contains("@"))
		// {
		// mEmailView.setError(getString(R.string.error_invalid_email));
		// focusView = mEmailView;
		// cancel = true;
		// }

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			loginActionFlag=false;
			loginSuccessFlag=false;
			
			soap.setLoginNameString(mEmailView.getText().toString());
			soap.setPassWordString(mPasswordView.getText().toString());
			soap.setImeiString(imeiString);
			soap.setServerIPString(sipString);
			soap.setServerPort(Integer.parseInt(lportString));
			soap.DoLoginByPhone();
			
			Log.i("Login", "Login begin");
			
			while(!loginActionFlag)
			{
				try 
				{
					// Simulate network access.
					Thread.sleep(100);
				} 
				catch (InterruptedException e) 
				{
					Log.i("doInBackground Thread","error");
					return false;
				}
			}
			
			// TODO: register the new account here.
			return loginNormalFlag;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) 
			{
				if(loginKeyString.length()==16)
				{

					String unEString = aeslib.Encrypt(
							mEmailView.getText().toString(), 
							loginKeyString, 
							loginKeyString);
					
					String pwEString = aeslib.Encrypt(
							mPasswordView.getText().toString(), 
							loginKeyString, 
							loginKeyString);
					
					propertiesHelper.SaveConfig(
							getApplicationContext(),
							sidString, sipString, lportString, sportString, unEString, pwEString, loginKeyString ,verString);
					
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					
					Bundle bundle = new Bundle();
					bundle.putString("username", mEmailView.getText().toString());
					bundle.putString("password", mPasswordView.getText().toString());
					bundle.putString("serverIP", sipString);
					bundle.putString("serverPort", sportString);
					bundle.putString("UE",unEString.substring(0, 20));
					bundle.putString("PE", pwEString.substring(0, 20));
//					bundle.putString("loginKey", loginKeyString);
					 
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
				else if(loginKeyString.equals("loginout"))
				{
					Toast.makeText(getApplicationContext(), "用户重复登录错误，现已登出，请重新登录！",
							Toast.LENGTH_LONG).show();
				}
				else
				{
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
				
			} 
			else 
			{
				
				Toast.makeText(getApplicationContext(), "登录超时失败！",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}
