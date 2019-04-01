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
				mCustomProgressDialog.setMessage("���ڼ���������...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 8:
				mCustomProgressDialog.setMessage("���ڵ�¼��...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 818:
				mCustomProgressDialog.setMessage("���ڻ�ȡ�����б���Ϣ...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 812:
				mCustomProgressDialog.setMessage("���ڽ��п�����Ϣ�޸�...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 811:
				mCustomProgressDialog.setMessage("���ڽ��п��ز���...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			case 891: {
				mCustomProgressDialog.setMessage("�������Ӻ����ն�...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 8916: {
				mCustomProgressDialog.setMessage("���ڵȴ������ն���Ӧ...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 892: {
				mCustomProgressDialog.setMessage("����ͬ�������ն�...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 894: {
//				customProgressDialog.setMessage("�����жϺ����ն�...");
//				customProgressDialog.show();
	//
//				return customProgressDialog;
			}
			case 901: {
				mCustomProgressDialog.setMessage("�������Ӽ����ն�...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 902: {
				mCustomProgressDialog.setMessage("����ͬ�������ն�...");
				mCustomProgressDialog.show();

				return mCustomProgressDialog;
			}
			case 904: {
				mCustomProgressDialog.setMessage("���ڹرռ����ն�...");
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
				mProgressDialog.setTitle("���ӷ�����");
				mProgressDialog.setMessage("���ڵȴ���������Ӧ...");
				mProgressDialog.setIcon(R.drawable.ic_launcher);
	
				mProgressDialog.setButton2("���·���",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
	//							Login2Server();
	
							}
						});
	
				mProgressDialog.setButton3("ȡ����¼",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
	//							rightMenuFragment.setUserLoginStateString("�ѻ�");
								mProgressDialog.cancel();
							}
						});
	
				mProgressDialog.setButton("�˳�",
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
				mProgressDialog.setTitle("���ӷ�����");
				mProgressDialog.setMessage("���ڵȴ���������Ӧ...");
				mProgressDialog.setIcon(R.drawable.ic_launcher);
	
				mProgressDialog.setButton3("ȡ����¼",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
	//							rightMenuFragment.setUserLoginStateString("�ѻ�");
								mProgressDialog.cancel();
							}
						});
	
				mProgressDialog.setButton("�˳�",
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
		dialogLoginSetting.setTitle("��¼����...");

		Button dialogButton = (Button) dialogLoginSetting.findViewById(R.id.login_set_OK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// �����˳��Ի���
				AlertDialog isSave = new AlertDialog.Builder(context).create();
				// ���öԻ������
				isSave.setTitle("ϵͳ��ʾ");
				// ���öԻ�����Ϣ
				isSave.setMessage("ȷ��Ҫ�������ã�");
				// ���ѡ��ť��ע�����
				isSave.setButton("ȷ��", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						//save data
						
						dialog.dismiss();
					}
				});
				isSave.setButton2("ȡ��", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				// ��ʾ�Ի���

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
		dialogSwitchDetail.setTitle(String.format("����:%d(%s)", no,nameString));
		
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
		// �����˳��Ի���
		AlertDialog isSave = new AlertDialog.Builder(context).create();
		// ���öԻ������
		isSave.setTitle("ϵͳ��ʾ");
		// ���öԻ�����Ϣ
		isSave.setMessage(String.format("����%d,ȷ��Ҫ����?!%s", no,nameString));
		// ���ѡ��ť��ע�����
		isSave.setButton("ȷ��", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				mDialogCallBack.SaveSwtichDetail(no, type1, enable, state, type2, nameString);
				dialog.dismiss();
			}
		});
		isSave.setButton2("ȡ��", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		// ��ʾ�Ի���

		isSave.show();
	}
	
	public void ShowExitAlertDialog() {
		// �����˳��Ի���
		AlertDialog isExit = new AlertDialog.Builder(context).create();
		// ���öԻ������
		isExit.setTitle("ϵͳ��ʾ");
		// ���öԻ�����Ϣ
		isExit.setMessage("ȷ��Ҫ�˳���");
		// ���ѡ��ť��ע�����
		isExit.setButton("ȷ��", exitDialogOnClickListener);
		isExit.setButton2("ȡ��", exitDialogOnClickListener);
		// ��ʾ�Ի���

		isExit.show();
	}

	public void ShowCloseTalkP2PAlertDialog() {
		// �����˳��Ի���
		AlertDialog isClose = new AlertDialog.Builder(context).create();
		// ���öԻ������
		isClose.setTitle("�رնԽ���ʾ");
		// ���öԻ�����Ϣ
		isClose.setMessage("ȷ��Ҫ�رյ�ǰ�Խ�������");
		// ���ѡ��ť��ע�����
		isClose.setButton("ȷ��", closeTalkP2PDialogOnClickListener);
		isClose.setButton2("ȡ��", closeTalkP2PDialogOnClickListener);
		// ��ʾ�Ի���

		isClose.show();
	}

	public void ShowCloseMonitorAlertDialog() {
		// �����˳��Ի���
		AlertDialog isClose = new AlertDialog.Builder(context).create();
		// ���öԻ������
		isClose.setTitle("�رռ�����ʾ");
		// ���öԻ�����Ϣ
		isClose.setMessage("ȷ��Ҫ�رյ�ǰ���ӹ�����");
		// ���ѡ��ť��ע�����
		isClose.setButton("ȷ��", closeMonitorDialogOnClickListener);
		isClose.setButton2("ȡ��", closeMonitorDialogOnClickListener);
		// ��ʾ�Ի���

		isClose.show();
	}

	public void ShowReLoginAlertDialog() {

		// �����˳��Ի���
		AlertDialog isReLogin = new AlertDialog.Builder(context).create();
		// ���öԻ������
		isReLogin.setTitle("ϵͳ��ʾ");
		// ���öԻ�����Ϣ
		isReLogin.setMessage("�Ƿ������µ�¼����...");
		// ���ѡ��ť��ע�����
		isReLogin.setButton("ȷ��", reLoginDialogOnClickListener);
		isReLogin.setButton2("ȡ��", reLoginDialogOnClickListener);
		// ��ʾ�Ի���

		isReLogin.show();
	}
	
	DialogInterface.OnClickListener exitDialogOnClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				mDialogCallBack.Loginout();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};

	DialogInterface.OnClickListener closeTalkP2PDialogOnClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����

				mDialogCallBack.TalkClose();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};

	DialogInterface.OnClickListener closeMonitorDialogOnClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				mDialogCallBack.MonitorClose();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
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
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				mDialogCallBack.ReLogin();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};


	
	
}
