package com.ltnw.common;

import com.ltnw.common.controls.CustomProgressDialog;
import com.ltnw.interFace.DialogCallBack;
import com.ltnw.interFace.LTCloudSMCallBack;
import com.ltnw.ltcloud.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DialogHelper 
{
	private Context context;
	
	private ProgressDialog mProgressDialog = null;
	private CustomProgressDialog mCustomProgressDialog = null;
	
    private DialogCallBack mDialogCallBack;
    
	public void setmDialogCallBack(DialogCallBack mDialogCallBack) {
		this.mDialogCallBack = mDialogCallBack;
	}
	
	public DialogHelper(Context parent)
	{
		context=parent;
		mCustomProgressDialog = CustomProgressDialog.createDialog(context);
		mCustomProgressDialog.setCancelable(false);
	}
	
	public CustomProgressDialog ShowCustomProgressDialog(int id)
	{
		switch(id)
		{

			case 1: {
				mCustomProgressDialog.setMessage("正在检测可用网络...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 8:
				mCustomProgressDialog.setMessage("正在登录中...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 818:
				mCustomProgressDialog.setMessage("正在获取开关列表信息...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 812:
				mCustomProgressDialog.setMessage("正在进行开关信息修改...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 811:
				mCustomProgressDialog.setMessage("正在进行开关操作...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 891: {
				mCustomProgressDialog.setMessage("正在连接呼叫终端...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 8916: {
				mCustomProgressDialog.setMessage("正在等待呼叫终端响应...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 892: {
				mCustomProgressDialog.setMessage("正在同步呼叫终端...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 894: {
//				customProgressDialog.setMessage("正在中断呼叫终端...");
//				customProgressDialog.show();
	//
//				return customProgressDialog;
			}
			case 901: {
				mCustomProgressDialog.setMessage("正在连接监视终端...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 902: {
				mCustomProgressDialog.setMessage("正在同步监视终端...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 904: {
				mCustomProgressDialog.setMessage("正在关闭监视终端...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			default:
				break;
		}
		
		return null;
	}
	
	public void CloseCustomProgressDialog()
	{
		mCustomProgressDialog.dismiss();
	}
	
	public ProgressDialog ShowProgressDialog(int id)
	{
		switch (id) 
		{
			case 888:
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setTitle("连接服务器");
				mProgressDialog.setMessage("正在等待服务器反应...");
				mProgressDialog.setIcon(R.drawable.ic_launcher);
	
				mProgressDialog.setButton2("重新发送",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
	//							Login2Server();
	
							}
						});
	
				mProgressDialog.setButton3("取消登录",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
	//							rightMenuFragment.setUserLoginStateString("脱机");
								mProgressDialog.cancel();
							}
						});
	
				mProgressDialog.setButton("退出",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								// System.exit(0);
	//							finish();
							}
						});
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCancelable(false);
				return mProgressDialog;
				
			case 777: 
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setTitle("连接服务器");
				mProgressDialog.setMessage("正在等待服务器反应...");
				mProgressDialog.setIcon(R.drawable.ic_launcher);
	
				mProgressDialog.setButton3("取消登录",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
	//							rightMenuFragment.setUserLoginStateString("脱机");
								mProgressDialog.cancel();
							}
						});
	
				mProgressDialog.setButton("退出",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								// System.exit(0);
	//							finish();
							}
						});
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCancelable(false);
				return mProgressDialog;

		default:
			break;
		}
		
		return null;
	}
	
	public void ShowLoginSettingDialog(String id,String ip,String port)
	{
		final Dialog dialogLoginSetting = new Dialog(context);
		dialogLoginSetting.setContentView(R.layout.switch_dialog);
		dialogLoginSetting.setTitle("登录设置...");

		Button dialogButton = (Button) dialogLoginSetting.findViewById(R.id.login_set_OK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// 创建退出对话框
				AlertDialog isSave = new AlertDialog.Builder(context).create();
				// 设置对话框标题
				isSave.setTitle("系统提示");
				// 设置对话框消息
				isSave.setMessage("确定要保持设置？");
				// 添加选择按钮并注册监听
				isSave.setButton("确定", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						//save data
						
						dialog.dismiss();
					}
				});
				isSave.setButton2("取消", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				// 显示对话框

				isSave.show();
				
				
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
	}
	
	public void ShowSwitchDetailDialog(
			final int no,
			int type1,
			int enable,
			int state,
			int type2,
			String nameString)
	{
		mDialogCallBack.GetSwtichDetail(no, type1, enable, state, type2, nameString);
		
		final Dialog dialogSwitchDetail = new Dialog(context);
		dialogSwitchDetail.setContentView(R.layout.switch_dialog);
		dialogSwitchDetail.setTitle(String.format("开关:%d(%s)", no,nameString));
		
		final RadioGroup rgSwitchType1=(RadioGroup) dialogSwitchDetail.findViewById(R.id.rgSwitchType1);
		if(type1==0)
		{
			rgSwitchType1.check(R.id.rbSwitchType1_0);
		}
		else if(type1==1)
		{
			rgSwitchType1.check(R.id.rbSwitchType1_1);
		}
		
//		rgSwitchType1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				// TODO Auto-generated method stub
//				if (checkedId == R.id.rbSwitchType1_0) {
//					t_type1 = 0;
//					Log.i("t_type1", String.format("%d", t_type1));
//				} else if (checkedId == R.id.rbSwitchType1_1) {
//					t_type1 = 1;
//					Log.i("t_type1", String.format("%d", t_type1));
//				}
//			}
//		});
		
		final RadioGroup rgSwitchEnable=(RadioGroup) dialogSwitchDetail.findViewById(R.id.rgSwitchEnable);
		if(enable==0)
		{
			rgSwitchEnable.check(R.id.rbSwitchEnable_0);
		}
		else if(enable==1)
		{
			rgSwitchEnable.check(R.id.rbSwitchEnable_1);
		}
//		rgSwitchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				// TODO Auto-generated method stub
//				if (checkedId == R.id.rbSwitchEnable_0) {
//					t_enable = 0;
//					Log.i("t_enable", String.format("%d", t_enable));
//				} else if (checkedId == R.id.rbSwitchEnable_1) {
//					t_enable = 1;
//					Log.i("t_enable", String.format("%d", t_enable));
//				}
//			}
//		});
		
		final RadioGroup rgSwitchState=(RadioGroup) dialogSwitchDetail.findViewById(R.id.rgSwitchState);
		if(state==0)
		{
			rgSwitchState.check(R.id.rbSwitchState_0);
		}
		else if(state==1)
		{
			rgSwitchState.check(R.id.rbSwitchState_1);
		}
//		rgSwitchState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				// TODO Auto-generated method stub
//				if (checkedId == R.id.rbSwitchState_0) {
//					t_state = 0;
//					Log.i("t_state", String.format("%d", t_state));
//				} else if (checkedId == R.id.rbSwitchState_1) {
//					t_state = 1;
//					Log.i("t_state", String.format("%d", t_state));
//				}
//			}
//		});
		
		
		final RadioGroup rgSwitchType2=(RadioGroup) dialogSwitchDetail.findViewById(R.id.rgSwitchType2);
		if(type2==0)
		{
			rgSwitchType2.check(R.id.rbSwitchType2_0);
		}
		else if(type2==1)
		{
			rgSwitchType2.check(R.id.rbSwitchType2_1);
		}
//		rgSwitchType2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				// TODO Auto-generated method stub
//				if (checkedId == R.id.rbSwitchType2_0) {
//					t_type2 = 0;
//					Log.i("t_type2", String.format("%d", t_type2));
//				} else if (checkedId == R.id.rbSwitchType2_1) {
//					t_type2 = 1;
//					Log.i("t_type2", String.format("%d", t_type2));
//				}
//			}
//		});
		
		
		final EditText etName= (EditText) dialogSwitchDetail.findViewById(R.id.etSwitchName);
		etName.setText(nameString);
		
		
//		RadioButton rbst1_0= (RadioButton) dialog.findViewById(R.id.rbSwitchType1_0);
//		rbst1_0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		Button dialogButton = (Button) dialogSwitchDetail.findViewById(R.id.switch_set_OK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				dialog.dismiss();
				int t_type1=0;
				int t_enable=0;
				int t_state=0;
				int t_type2=0;

				if(rgSwitchType1.getCheckedRadioButtonId()==R.id.rbSwitchType1_0)
				{
					t_type1=0;
				}
				else
				{
					t_type1=1;
				}
				
				if(rgSwitchEnable.getCheckedRadioButtonId()==R.id.rbSwitchEnable_0)
				{
					t_enable=0;
				}
				else
				{
					t_enable=1;
				}
				
				if(rgSwitchState.getCheckedRadioButtonId()==R.id.rbSwitchState_0)
				{
					t_state=0;
				}
				else
				{
					t_state=1;
				}
				
				if(rgSwitchType2.getCheckedRadioButtonId()==R.id.rbSwitchType2_0)
				{
					t_type2=0;
				}
				else
				{
					t_type2=1;
				}
				
				ShowSwitchAlertDialog(no,t_type1,t_enable,t_state,t_type2,etName.getText().toString());
			}
		});

		Button dialogCancelButton = (Button) dialogSwitchDetail.findViewById(R.id.switch_set_Cancel);
		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogSwitchDetail.dismiss();
			}
		});
		
		dialogSwitchDetail.show();
	}
	
	public void ShowSwitchAlertDialog(			
			final int no,
			final int type1,
			final int enable,
			final int state,
			final int type2,
			final String nameString) {
		// 创建退出对话框
		AlertDialog isSave = new AlertDialog.Builder(context).create();
		// 设置对话框标题
		isSave.setTitle("系统提示");
		// 设置对话框消息
		isSave.setMessage(String.format("开关%d,确定要保存?!%s", no,nameString));
		// 添加选择按钮并注册监听
		isSave.setButton("确定", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				mDialogCallBack.SaveSwtichDetail(no, type1, enable, state, type2, nameString);
				dialog.dismiss();
			}
		});
		isSave.setButton2("取消", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		// 显示对话框

		isSave.show();
	}
	
	public void ShowExitAlertDialog() {
		// 创建退出对话框
		AlertDialog isExit = new AlertDialog.Builder(context).create();
		// 设置对话框标题
		isExit.setTitle("系统提示");
		// 设置对话框消息
		isExit.setMessage("确定要退出吗");
		// 添加选择按钮并注册监听
		isExit.setButton("确定", exitDialogOnClickListener);
		isExit.setButton2("取消", exitDialogOnClickListener);
		// 显示对话框

		isExit.show();
	}

	public void ShowCloseTalkP2PAlertDialog() {
		// 创建退出对话框
		AlertDialog isClose = new AlertDialog.Builder(context).create();
		// 设置对话框标题
		isClose.setTitle("关闭对讲提示");
		// 设置对话框消息
		isClose.setMessage("确定要关闭当前对讲功能吗");
		// 添加选择按钮并注册监听
		isClose.setButton("确定", closeTalkP2PDialogOnClickListener);
		isClose.setButton2("取消", closeTalkP2PDialogOnClickListener);
		// 显示对话框

		isClose.show();
	}

	public void ShowCloseMonitorAlertDialog() {
		// 创建退出对话框
		AlertDialog isClose = new AlertDialog.Builder(context).create();
		// 设置对话框标题
		isClose.setTitle("关闭监视提示");
		// 设置对话框消息
		isClose.setMessage("确定要关闭当前监视功能吗");
		// 添加选择按钮并注册监听
		isClose.setButton("确定", closeMonitorDialogOnClickListener);
		isClose.setButton2("取消", closeMonitorDialogOnClickListener);
		// 显示对话框

		isClose.show();
	}

	public void ShowReLoginAlertDialog() {

		// 创建退出对话框
		AlertDialog isReLogin = new AlertDialog.Builder(context).create();
		// 设置对话框标题
		isReLogin.setTitle("系统提示");
		// 设置对话框消息
		isReLogin.setMessage("是否尝试重新登录操作...");
		// 添加选择按钮并注册监听
		isReLogin.setButton("确定", reLoginDialogOnClickListener);
		isReLogin.setButton2("取消", reLoginDialogOnClickListener);
		// 显示对话框

		isReLogin.show();
	}
	
	DialogInterface.OnClickListener exitDialogOnClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				mDialogCallBack.Loginout();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	DialogInterface.OnClickListener closeTalkP2PDialogOnClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序

				mDialogCallBack.TalkClose();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	DialogInterface.OnClickListener closeMonitorDialogOnClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				mDialogCallBack.MonitorClose();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	DialogInterface.OnClickListener reLoginDialogOnClickListener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				mDialogCallBack.ReLogin();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};


	
	
}
