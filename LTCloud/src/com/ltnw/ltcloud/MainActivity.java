package com.ltnw.ltcloud;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.ltnw.common.AppStatusService;
import com.ltnw.common.DialogHelper;
import com.ltnw.common.LTCloudSM;
import com.ltnw.common.NetWorkStateHelper;
import com.ltnw.common.NetworkStateService;
import com.ltnw.common.ProtocolControler;
import com.ltnw.common.UdpHelper;
import com.ltnw.entity.LTCloudEnum;
import com.ltnw.entity.ProtocolAnalysisResult;
import com.ltnw.entity.SwitchItem;
import com.ltnw.interFace.AudioCallBack;
import com.ltnw.interFace.DialogCallBack;
import com.ltnw.interFace.GetPublicIpCallBack;
import com.ltnw.interFace.LTCloudSMCallBack;
import com.ltnw.interFace.MonitorCallBack;
import com.ltnw.interFace.SwitchCallBack;
import com.ltnw.ltcloud.fragment.AboutFragment;
import com.ltnw.ltcloud.fragment.HomeFragment;
import com.ltnw.ltcloud.fragment.LeftMenuFragment;
import com.ltnw.ltcloud.fragment.MonitorFragment;
import com.ltnw.ltcloud.fragment.PhotosFragment;
import com.ltnw.ltcloud.fragment.RightMenuFragment;
import com.ltnw.ltcloud.fragment.SettingFragment;
import com.ltnw.ltcloud.fragment.SwitchControlFragment;
import com.ltnw.ltcloud.fragment.TalkP2PFragment;
import com.ltnw.ltcloud.fragment.LeftMenuFragment.SLMenuListOnItemClickListener;
import com.ltnw.ltcloud.fragment.RightMenuFragment.OnRightMenuFragmentClickListener;
import com.ltnw.ltcloud.fragment.SettingFragment.OnSettingButtonClickListener;

//import android.view.Menu;
import com.actionbarsherlock.view.Menu;
//import android.view.MenuItem;
import com.actionbarsherlock.view.MenuItem;

import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 
 * @author ada 
 * Workflow LOG: 
 * 1.init all Component 
 * 2.check network state case ok:
 *         InitUdpServer\GetPhoneInfo else nothingdo
 */

public class MainActivity extends SlidingFragmentActivity implements
		SLMenuListOnItemClickListener, OnSettingButtonClickListener,
		OnRightMenuFragmentClickListener {

	/**
	 * control
	 */
	private SlidingMenu mSlidingMenu = null;

	private Fragment fragment = null;
	private int fragmentFlag = 0;
	private RightMenuFragment rightMenuFragment = null;

	private ProtocolControler protocol = null;
	private UdpHelper appUdpHelper = null;
	private UdpHelper dynUdpHelper = null;
	private byte[] udpSendData = null;

	private PopupWindow mPopupWindow = null;
	private TextView popupTextView = null;
	private ImageButton popupCloseImageButton = null;

	private DialogHelper dialogHelper=null;

	private NetWorkStateHelper netWorkStateHelper = null;

	private Timer sendHeartBeatTimer = null;

	private Object syncToken = new Object();
	
	private LTCloudSM ltSM=null;
	private int next_flag_sm=ltSM.CMD_FREE;
	private int type_sm=ltSM.TYPE_TALK;
	/**
	 * system state
	 */
	private boolean isNetWorkFlag = false;
	private boolean isLoginFlag = false;
//	private boolean isLocalNW = false;
	private boolean isListening = false;
	private boolean isNWConnectRemind = false;
	private boolean isTerminalConFlag = false;
	private boolean isDoorConFlag = false;

	/**
	 * login info
	 */
	private String usernameString = null;
	private String unEString = null;
	private String pwEString = null;
	private String serverIPString = null;
	private int serverPort=0;
	private String loginAddressString = null;
	private String publicIPString = null;
	private String localIPString = null;
	private String imeiString = null;

	private int dynPort = 0;
	private byte[] tempID = null;
	private Bitmap dynBitmap = null;
	private HashMap<Integer, byte[]> imageMap = null;

	private SwitchItem[] switchList;
	
	/**
	 * Handler use for network
	 */
	public static Handler handler = null;

	/********************************************************************/

	private void GetLoginInfo() {
		Bundle bundle = this.getIntent().getExtras();
		usernameString = bundle.getString("username");
		bundle.getString("password");
		serverIPString = bundle.getString("serverIP");
		serverPort=Integer.parseInt(bundle.getString("serverPort"));	
		
		unEString = bundle.getString("UE");
		pwEString = bundle.getString("PE");

		Log.i("Main", String.format("userName:%s", usernameString));
	}

	private void GetIpInfo() {
		if (netWorkStateHelper == null) {
			netWorkStateHelper = new NetWorkStateHelper();
			netWorkStateHelper
					.setOnGetPublicIpCallBack(new GetPublicIpCallBack() {
						@Override
						public void getPublicIp(String result) {
							// TODO Auto-generated method stub

							publicIPString = result;
							Log.i("Main", String.format("publicIPString:%s", publicIPString));
						}
					});
		}
		
		if (localIPString == null) {
			localIPString = netWorkStateHelper.getLocalIPAddress();
			Log.i("Main", String.format("localIPString:%s", localIPString));
		}
		
		if (publicIPString == null) {
			netWorkStateHelper.GetPublicIP();
		}
	}

	@SuppressLint("NewApi")
	private void InitSlidingMenu() {
		// set the Behind View
		setBehindContentView(R.layout.frame_left_menu);

		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 璁剧疆宸﹀彸閮藉彲浠ュ垝鍑篠lidingMenu鑿滃崟
		mSlidingMenu.setSecondaryMenu(R.layout.frame_right_menu); // 璁剧疆鍙充晶鑿滃崟鐨勫竷灞?枃浠?

		// mSlidingMenu.setSecondaryShadowDrawable(R.drawable.drawer_shadow);
		// mSlidingMenu.setShadowWidth(5);
		// mSlidingMenu.setBehindOffset(100);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);// 设置阴影图片
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽度
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); // SlidingMenu划出时主页面显示的剩余宽度
		mSlidingMenu.setFadeDegree(0.35f);
		// 璁剧疆SlidingMenu 鐨勬墜鍔挎ā寮? //TOUCHMODE_FULLSCREEN
		// 鍏ㄥ睆妯″紡锛屽湪鏁翠釜content椤甸潰涓紝婊戝姩锛屽彲浠ユ墦寮?lidingMenu
		// TOUCHMODE_MARGIN
		// 杈圭紭妯″紡锛屽湪content椤甸潰涓紝濡傛灉鎯虫墦寮?lidingMenu,浣犻渶瑕佸湪灞忓箷杈圭紭婊戝姩鎵嶅彲浠ユ墦寮?lidingMenu
		// TOUCHMODE_NONE 涓嶈兘閫氳繃鎵嬪娍鎵撳紑SlidingMenu
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// 璁剧疆 SlidingMenu 鍐呭
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.left_menu, new LeftMenuFragment());
		rightMenuFragment = new RightMenuFragment(loginAddressString, "离线");
		fragmentTransaction.replace(R.id.right_menu, rightMenuFragment);
		fragmentTransaction.replace(R.id.content, new HomeFragment());
		
		fragmentTransaction.commit();

		// / 浣跨敤宸︿笂鏂筰con鍙偣锛岃繖鏍峰湪onOptionsItemSelected閲岄潰鎵嶅彲浠ョ洃鍚埌R.id.home
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setLogo(R.drawable.ic_logo);

		fragmentFlag = 0;

	}

	/**
	 * PopupWindow for network
	 */
	private void InitPopupView() {
		View popupView = getLayoutInflater().inflate(
				R.layout.activity_popupwindow, null);

		popupView.getBackground().setAlpha(100);

		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		mPopupWindow.getContentView().setFocusableInTouchMode(true);
		mPopupWindow.getContentView().setFocusable(true);
		mPopupWindow.setAnimationStyle(R.style.anim_popubwindow_bottombar);

		popupTextView = (TextView) popupView
				.findViewById(R.id.popupwindow_content_textview);

		popupTextView.setText("当前没有网络接入，请进行必要的设置！");

		popupCloseImageButton = (ImageButton) popupView
				.findViewById(R.id.popupwindow_close_imagebutton);
		popupCloseImageButton
				.setOnClickListener(new ImageButton.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mPopupWindow != null && mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
						}
					}
				});
		
		isNWConnectRemind=false;
	}

	/**
	 * Handler send Msg
	 */
	private void InitHandler() {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:// network
					if ((Boolean) msg.obj) {

						Log.i("Main in handel", "NW check connectted");

						if (!isNetWorkFlag) 
						{
							isNetWorkFlag = true;
							GetIpInfo();
						}
						
						if(isNWConnectRemind)
						{
							if (mPopupWindow != null && mPopupWindow.isShowing()) 
							{
								mPopupWindow.dismiss();
							}
							
							isNWConnectRemind=false;
						}

						if (appUdpHelper == null) 
						{
							Log.i("Main in handel", "InitUdpServer");
							InitAppUdpServer();
						}

						if (imeiString == null) 
						{
							GetPhoneInfo();
						}

						if (!isLoginFlag) 
						{
							Login2Server();
						} 
						else 
						{
							rightMenuFragment.setUserLoginStateString("在线");
						}

						Log.i("Main", "InitHeartBeatSender");
						InitHeartBeatSender();

					} 
					else 
					{
						Toast.makeText(getApplicationContext(), "无网络连接！",
								Toast.LENGTH_SHORT).show();

						isNetWorkFlag = false;

						if(mPopupWindow==null)
						{
							InitPopupView();
						}
						
						if (mPopupWindow != null && !mPopupWindow.isShowing()) 
						{
							mPopupWindow.showAtLocation(
									findViewById(R.id.content), Gravity.BOTTOM,
									0, 0);

							if (!isLoginFlag) 
							{
								rightMenuFragment.setUserLoginStateString("离线");
							} 
							else 
							{
								rightMenuFragment.setUserLoginStateString("脱机");
							}
						}
					}
					break;
				case 0:
					DialogOperate(8, false, 0);
					break;
				case 2:
					publicIPString = (String) msg.obj;
					Log.i("Main in handel", String.format("publicIPString:%s", publicIPString));
					break;
				case 4:
					// talk p2p close
					if ((Boolean) msg.obj) {

						DialogOperate(4, false, 0);
					}
					break;
				case 8:
					Log.i("Main in handel", "SMOperate 8");
					if(!SMOperate(8, true,	8))
					{
						return;
					}
					rightMenuFragment.setUserNameString(usernameString);
					rightMenuFragment.setUserLoginStateString("在线");

					Toast.makeText(getApplicationContext(), "用户接入成功！",
							Toast.LENGTH_LONG).show();
					break;

				case 831:
					TerConFeedBack(1);
					Toast.makeText(getApplicationContext(), "室内分机接入成功！",
							Toast.LENGTH_LONG).show();
					break;
				case 832:
					TerConFeedBack(2);
					Toast.makeText(getApplicationContext(), "门口机接入成功！",
							Toast.LENGTH_LONG).show();
					break;
					
				case 818:
					if(fragmentFlag !=0)
					{
						return;
					}
					fragment = new SwitchControlFragment(switchList);
					fragmentFlag = 1;
					
					((SwitchControlFragment)fragment).setmSwitchCallBack(new SwitchCallBack() {
						
						@Override
						public void SwitchItemValueChange(int switchNo, int switchType1,
								int switchEnable, int switchState, int switchType2,
								String switchName) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void SwitchItemStateChange(int switchNo, int state) {
							// TODO Auto-generated method stub
							//switch change
							
							
						}
						
						@Override
						public void SwitchDialogCallBack(int switchNo, int switchType1,
								int switchEnable, int switchState, int switchType2,
								String switchName) {
							// TODO Auto-generated method stub
							dialogHelper.ShowSwitchDetailDialog(
									switchNo, 
									switchType1, 
									switchEnable, 
									switchState, 
									switchType2, 
									switchName);
						}
					});
					
					if (fragment != null) {
						FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.content, fragment).commit();

						setTitle("开关控制");
						mSlidingMenu.showContent();

					} else {
						// error in creating fragment
						Log.i("handle", "Error in creating fragment");
					}
					
					break;
				case 812:
					Toast.makeText(getApplicationContext(), "开关信息修改成功！",
							Toast.LENGTH_LONG).show();
					break;
				case 811:
					Toast.makeText(getApplicationContext(), "开关设置成功！",
							Toast.LENGTH_LONG).show();
					break;
				case 891:
					if(SMOperate(891, true,	891))
					{
						Log.i("handle",
								"SMOperate 891 check ok...");
						
						SMOperate(8916, false, 8916);
						
						InitDynUdpServer();
						
					}
					else
					{
						Log.i("handle",
								"SMOperate 891 check fail...");
					}
					break;
				case 8916:
					if(!SMOperate(8916, true, 8916))
					{
						Log.i("handle",
								"SMOperate 8916 check fail...");
						return;
					}
					else
					{
						Log.i("handle",
								"SMOperate 8916 check ok...");
					}
					TalkStartOperate();
					break;
				case 892:
					if(!SMOperate(892,true,892))
					{
						Log.i("handle",
								"SMOperate 892 check fail...");
						return;
					}
					else {
						Log.i("handle",
								"SMOperate 892 check ok...");
					}
					
					fragment = new TalkP2PFragment(serverIPString, dynPort, 320);
					((TalkP2PFragment) fragment)
							.setmAudioCallBack(new AudioCallBack() {

								@Override
								public void SendRecordSound(byte[] data) {
									// TODO Auto-generated method stub
									TalkUpOperate(data);
								}

								@Override
								public void TalkClose() {
									// TODO Auto-generated method stub
									Log.i("TalkClose", "--------------------------------");
									TalkEndOperate();
									selectItem(0, "主页");
								}
							});
					

					if (fragment != null) 
					{
						FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.content, fragment).commit();

						setTitle("呼叫");
						mSlidingMenu.showContent();
						fragmentFlag = 4;
						SMOperate(893,false,893);

					} else 
					{
						// error in creating fragment
						Log.i("Main in handel", "892 Error in creating fragment");
					}
					
					break;
				case 894:
					if(!SMOperate(894, true, 894))
					{
						Log.i("handle",
								"SMOperate 894 check fail...");
						return;
					}
					else
					{
						Log.i("handle",
								"SMOperate 894 check ok...");
					}
					
					selectItem(0, "主页");
					Toast.makeText(getApplicationContext(), "对方终端结束通话！",
							Toast.LENGTH_LONG).show();
					break;
				case 901:
					if(SMOperate(901, true,	901))
					{
						Log.i("handle",
								"SMOperate 901 check ok...");
						
						
						
						InitDynUdpServer();
						
						MonitorStartOperate(tempID);
						
					}
					else
					{
						Log.i("handle",
								"SMOperate 901 check fail...");
					}
					
					break;
				case 902:
					if(!SMOperate(902, true, 902))
					{
						return;
					}
					
					
					
					fragment = new MonitorFragment();
					((MonitorFragment) fragment)
							.setmMonitorCallBack(new MonitorCallBack() {

								@Override
								public void MonitorClose() {
									// TODO Auto-generated method stub
									MonitorEndOperate();
									selectItem(0, "主页");
								}
							});
					

					if (fragment != null) 
					{
						FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.content, fragment).commit();

						setTitle("监控");
						mSlidingMenu.showContent();
						fragmentFlag = 3;
						SMOperate(903, false, 903);
						

					} else {
						// error in creating fragment
						Log.i("Main in handel", "Error in creating fragment");
					}
					SMOperate(904, false, 904);
					break;
				case 903:
					if(SMOperate(903, true, 903))
					{
						SMOperate(904, false, 904);
					}
					else if(!SMOperate(904, true, 904))
					{
						return;
					}
					
					if (fragmentFlag == 3) 
					{
						((MonitorFragment) fragment)
								.ReflashImageView(dynBitmap);
						Log.i("Main in handel", "MonitorFragment ReflashImageView");
					}
					
					break;
				case 904:
					if(!SMOperate(904, true, 904))
					{
						return;
					}
					
					selectItem(0, "主页");
					Toast.makeText(getApplicationContext(), "监视已结束！",
							Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}

		};
	}

	/**
	 * app Udp Control
	 */
	private void InitAppUdpServer() {
		protocol = new ProtocolControler();
		appUdpHelper = new UdpHelper();
		appUdpHelper
				.setUdpReceiveListener(new UdpHelper.OnUdpReceiveListener() {

					@Override
					public void DoUdpReceiveListener(byte[] receBuf, int length) {
						try {
							// TODO Auto-generated method stub

							byte[] temp = new byte[length];
							System.arraycopy(receBuf, 0, temp, 0, length);

							ProtocolAnalysisResult result = protocol
									.ProtocolAnalysis(temp);
						    
							Log.i("app DoUdpReceiveListener", String.format("come begin:%d,%d,%d", 
						    		result.getProtocolType(),
						    		result.getCmdType(),
						    		result.getReturnType()));
							
							switch (result.getProtocolType()) {
							case 8:
								if (result.getCmdType() == 1) 
								{

									switch (result.getReturnType()) {
									case 0:
										// success
										Log.i("app DoUdpReceiveListener", "SMOperate 8");
										
										if(!SMOperate(8, true, 8))
										{
											return;
										}
										
										
										isLoginFlag = true;
										Log.i("app DoUdpReceiveListener",
												"login success set loginflag");
										
//										SMOperate(404, false, 404);
										
										DialogOperate(8, false, 8);

										Message msg1 = new Message();
										msg1.what = 8;
										MainActivity.handler.sendMessage(msg1);

										Log.i("app DoUdpReceiveListener",
												String.format("add:%s",
														result.getAddress()));
										
										loginAddressString = result
												.getAddress();

										break;

									case 1:
										// fail
										break;
									case 2:
										// fail
										break;
									default:
										// warnning
										break;
									}
								} 
								else if (result.getCmdType() == 2) 
								{
									//
								} 
								else if (result.getCmdType() == 3)
								{
									//
									if(result.getReturnType()==1)
									{
										isTerminalConFlag=true;
										Message msg1 = new Message();
										msg1.what = 831;
										MainActivity.handler.sendMessage(msg1);
									}
									else if(result.getReturnType()==2)
									{
										isDoorConFlag=true;
										Message msg1 = new Message();
										msg1.what = 832;
										MainActivity.handler.sendMessage(msg1);
									}
								}
								break;
							case 81:
								if(result.getCmdType() == 8)
								{
									if(result.getReturnType()==0)
									{
										Log.i("8888 DoUdpReceiveListener",
												"switchinfo success");
										
										if(!SMOperate(818, true, 818))
										{
											return;
										}
										
										byte[] switchData=result.getData();
										
										int count=switchData[0];
										
										switchList=new SwitchItem[count];
										
										for(int i=0;i<count;i++)
										{
											switchList[i]=new SwitchItem();
											switchList[i].switchNo=switchData[1+15*i];
											switchList[i].switchType1=switchData[2+15*i];
											switchList[i].switchEnable=switchData[3+15*i];
											switchList[i].switchState=switchData[4+15*i];
											switchList[i].switchType2=switchData[5+15*i];
											
											byte[] stringByte=new byte[10];
											System.arraycopy(switchData, 6+15*i, stringByte, 0, stringByte.length);
											
//											switchList[i].switchNameString=new String(stringByte,"UTF-8");
											switchList[i].switchNameString=new String(stringByte,"GB2312");
											
										}
										
										DialogOperate(8, false, 8);
										
										Message msg1 = new Message();
										msg1.what = 818;
										MainActivity.handler.sendMessage(msg1);

									}

								}
								else if(result.getCmdType() == 2)
								{
									if(result.getReturnType()==0)
									{
										if(!SMOperate(812, true, 812))
										{
											return;
										}
										
									
										DialogOperate(8, false, 8);
										
										
										Message msg1 = new Message();
										msg1.what = 812;
										MainActivity.handler.sendMessage(msg1);
									}
								}
								else if(result.getCmdType() == 1)
								{
									if(result.getReturnType()==0)
									{
										if(!SMOperate(811, true, 811))
										{
											return;
										}
										
									
										DialogOperate(8, false, 8);
										
										
										Message msg1 = new Message();
										msg1.what = 811;
										MainActivity.handler.sendMessage(msg1);
									}
								}
								break;
								
							case 89:
								switch (result.getCmdType()) 
								{
								case 1:
			
									if (result.getReturnType() == 0) 
									{
										if(!SMOperate(891, true, 891))
										{
											Log.i("app DoUdpReceiveListener",
													"SMOperate 891 check fail...");
											return;
										}
										else
										{
											Log.i("app DoUdpReceiveListener",
													"SMOperate 891 check ok...");
										}
										
										DialogOperate(8, false, 8);

										Log.i("app DoUdpReceiveListener","8910");
										
										tempID = result.getGuid();
										dynPort = result.getPort();
										
										//Log.i("app DoUdpReceiveListener","第二部，等待开始响应");

										Message msg1 = new Message();
										msg1.what = 891;
										MainActivity.handler.sendMessage(msg1);

									} 
									else if (result.getReturnType() == 6) 
									{
										if(!SMOperate(8916, true, 8916))
										{
											Log.i("app DoUdpReceiveListener",
													"SMOperate 8916 check ok...");
											return;
										}
										else
										{
											Log.i("app DoUdpReceiveListener",
													"SMOperate 8916 check fail...");
										}
										
										
										Log.i("app DoUdpReceiveListener",
												"8916");
										
										dialogHelper.CloseCustomProgressDialog();
										
										//talk start call
										Message msg1 = new Message();
										msg1.what = 8916;
										MainActivity.handler.sendMessage(msg1);
									}
									break;
								case 2:
									if (result.getReturnType() == 0) 
									{
										if(!SMOperate(892, true, 892))
										{
											Log.i("app DoUdpReceiveListener",
													"SMOperate 892 check fail...");

											return;
										}
										else
										{
											Log.i("app DoUdpReceiveListener",
													"SMOperate 892 check ok...");
										}
											
										Log.i("8888 DoUdpReceiveListener",
												"8920");
										
										dialogHelper.CloseCustomProgressDialog();

										//create fragment
										Message msg1 = new Message();
										msg1.what = 892;
										MainActivity.handler.sendMessage(msg1);

									}

									break;
								case 3:
									if(!SMOperate(893, true, 893))
									{
										Log.i("app DoUdpReceiveListener",
												"SMOperate 893 check ok...");
										return;
									}
									else
									{
										Log.i("app DoUdpReceiveListener",
												"SMOperate 893 check fail...");
									}
									
									TalkConfirmOperate();
									break;
								case 4:
									if(!SMOperate(894, true, 894))
									{
										Log.i("app DoUdpReceiveListener",
												"SMOperate 894 check fail...");
										return;
									}
									else {
										Log.i("app DoUdpReceiveListener",
												"SMOperate 894 check ok...");
									}
									
									Log.i("app DoUdpReceiveListener",
											"talk prepare close...");
									if (fragmentFlag == 4) 
									{
										//close udp and fragment
										Message msg1 = new Message();
										msg1.what = 894;
										MainActivity.handler.sendMessage(msg1);
									} 
									else 
									{
										Log.i("app DoUdpReceiveListener",
												String.format(
														"fragmentFlag:%d",
														fragmentFlag));
										Log.i("app DoUdpReceiveListener",
												"talk close fail...");
									}

									break;
								default:
									break;
								}
								break;
							case 90:
								switch (result.getCmdType()) {
								case 0:
									Log.i("app DoUdpReceiveListener",
											"monitor receive dyn port");
									dynPort = result.getPort();
									break;
								case 1:
									if (result.getReturnType() == 0) 
									{
										if(!SMOperate(901, true, 901))
										{
											return;
										}
										
										DialogOperate(8, false, 8);
										
										Log.i("app DoUdpReceiveListener",
												"monitor call success");

										dynPort = result.getPort();
										Log.i("app DoUdpReceiveListener", String
												.format("monitor port:%d",
														dynPort));

										tempID = result.getGuid();
										Log.i("app DoUdpReceiveListener", String
												.format("monitor id len:%d",
														tempID.length));

										Message msg1 = new Message();
										msg1.what = 901;
										MainActivity.handler.sendMessage(msg1);
										
										
									}
									break;
								case 2:
									if (result.getReturnType() == 0) {
										
										if(!SMOperate(902, true, 902))
										{
											return;
										}
										
										DialogOperate(8, false, 8);
										
										Log.i("app DoUdpReceiveListener",
												"monitor start success");
										// here open the framgment

										Message msg1 = new Message();
										msg1.what = 902;
										MainActivity.handler.sendMessage(msg1);
									}

									break;
								case 3:
									if(!SMOperate(903, true, 903))
									{
										return;
									}
									
									MonitorConfirmOperate();
									
									break;
								case 4:
									
									if(!SMOperate(904, true, 904))
									{
										return;
									}
									
									Log.i("app DoUdpReceiveListener",
											"监控接收到退出...");
									if (fragmentFlag == 3) {
										Message msg1 = new Message();
										msg1.what = 904;
										MainActivity.handler.sendMessage(msg1);
									}
									break;
								}
								break;
							default:
								break;
							}
						} catch (Exception e) {
							// TODO: handle exception
							Log.i("app DoUdpReceiveListener Error",
									e.getMessage());
						}

					}
				});

		if (appUdpHelper.InitListen("localhost", 8888)) {
			appUdpHelper.ListenStart();
			isListening = true;
		} else {
			isListening = false;
			Toast.makeText(getApplicationContext(), "程序监听失败！",
					Toast.LENGTH_LONG).show();
			// System.exit(0);
			finish();
		}
	}

	/**
	 * dyn Udp Control
	 */
	private void InitDynUdpServer() {
		dynUdpHelper = new UdpHelper();
		dynUdpHelper
				.setUdpReceiveListener(new UdpHelper.OnUdpReceiveListener() {
					@Override
					public void DoUdpReceiveListener(byte[] receBuf, int length) {
						try {
							// TODO Auto-generated method stub
							 Log.i("Dyn DoUdpReceiveListener","come");

							byte[] temp = new byte[length];
							System.arraycopy(receBuf, 0, temp, 0, length);

							ProtocolAnalysisResult result = protocol
									.ProtocolAnalysis(temp);

							switch (result.getProtocolType()) {

							case 0:
								Log.i("Dyn DoUdpReceiveListener",
										"--------Get Test data ok!--------");
								break;
							case 89:
								switch (result.getCmdType()) 
								{
									case 3:
										//Log.i("Dyn DoUdpReceiveListener", "接收到在线确认");
										break;
									case (-106):
	//									 Log.i("Dyn DoUdpReceiveListener",String.format("音频数据长度：%d", result.getData().length));
//										if(!SMOperate(893, true))
//										{
//											return;
//										}
										((TalkP2PFragment) fragment)
												.PushAudioData(result.getData());
										break;
								}
								break;
							case 90:
								switch (result.getCmdType()) {
								case (-104):
									
//									if(!SMOperate(903, true))
//									{
//										return;
//									}
									
									imageMap.put(result.getPacketFlag(),
											result.getData());

									if (result.getPacketFlag() == result
											.getPacketCount()) {
										byte[] byteArray = new byte[result
												.getFrameCount()];

										for (int i = 1; i < result
												.getPacketCount() + 1; i++) {
											if (i == (result.getPacketCount())) {
												System.arraycopy(
														result.getData(), 0,
														byteArray,
														(i - 1) * 1200,
														result.getData().length);
												
											} else {
												System.arraycopy(
														imageMap.get(i), 0,
														byteArray,
														(i - 1) * 1200, 1200);
											}

										}

										try 
										{
											dynBitmap = Bytes2Bitmap(byteArray);

											Message msg1 = new Message();
											msg1.what = 903;
											MainActivity.handler.sendMessage(msg1);
										} 
										catch (Exception e) {
											// TODO: handle exception
										}
										
									}
									break;
								default:
									break;
								}
								break;

							default:
								break;
							}

						} catch (Exception e) {
							Log.i("dyn DoUdpReceiveListener Error",
									e.getMessage());
						}
					}
				});

		if (dynUdpHelper.InitListen("localhost", dynPort)) {
			dynUdpHelper.ListenStart();

			DynPortReturnOperater();
			Log.i("dyn DoUdpReceiveListener",String.format("发送端口%d", dynPort));
			

		} else {
			Toast.makeText(getApplicationContext(), "动态UDP监听失败！",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * start Service for network
	 */
	private void InitNetworkStateService() {
		Intent intent = new Intent(getBaseContext(), NetworkStateService.class);
		startService(intent);
	}

	/**
	 * start Service for app status
	 */
	private void InitAppStatusService() {
		Intent intent = new Intent(getBaseContext(), AppStatusService.class);
		startService(intent);
	}

	/**
	 * new DialogHelper
	 */
	private void InitDialog() {

		dialogHelper=new DialogHelper(this);
		dialogHelper.setmDialogCallBack(new DialogCallBack() {
			
			@Override
			public void TalkClose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void MonitorClose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void Loginout() {
				// TODO Auto-generated method stub
				LoginOut();
				finish();
			}

			@Override
			public void GetSwtichDetail(int no, int type1, int enable,
					int state, int type2, String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void SaveSwtichDetail(int no, int type1, int enable,
					int state, int type2, String name) {
				// TODO Auto-generated method stub
				
				byte[] namebyte = new byte[10];
				try {
					namebyte = name.getBytes("GB2312");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SwitchInfoModOperate((byte)no, (byte)type1, (byte)enable, (byte)state, (byte)type2, namebyte);
				
			}

			@Override
			public void ReLogin() {
				// TODO Auto-generated method stub
				LoginOut();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void GetPhoneInfo() {
		try {
			Cursor myCursor = getContentResolver().query(
					Uri.parse("content://sms"), new String[] {/*
															 * "msg_id",
															 * "contact_id",
															 */
					"(select address from addr where type = 151) as address" },
					null, null, "date desc");
			if (myCursor != null) {
				myCursor.moveToFirst();
				Log.i("number",
						"number="
								+ myCursor.getString(myCursor
										.getColumnIndex("address")));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		// String tel = tm.getLine1Number();
		imeiString = tm.getDeviceId();
	}

	private void InitHeartBeatSender() 
	{
		if (sendHeartBeatTimer == null) 
		{
			sendHeartBeatTimer = new Timer();

			sendHeartBeatTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (isNetWorkFlag) {
						if (isListening && isLoginFlag) 
						{

							if(protocol==null || appUdpHelper==null)
							{
								return;
							}
							
							udpSendData = null;

							udpSendData = protocol
									.ConvertByteArray2byteArray(protocol
											.HeartBeatCall(imeiString));

							if (udpSendData != null) 
							{
								appUdpHelper.SendData(udpSendData,
										serverIPString, serverPort);

								Log.i("Main in HeartBeatSender", "sendHeartBeatTimer");

							} else {
								Log.i("Main in HeartBeatSender",
										"sendHeartBeatTimer send data error");
							}
						}
					}
				}
			}, 60000);
		}
	}

	private class StartSM implements Runnable {
		@Override
		public void run() 
		{
			
			if(ltSM!=null)
			{
				Log.i("StartSM", "sm is working,so not allow call");
				return;
			}
			
			ltSM= LTCloudSM.makeLTCloudSM(type_sm);
			
			ltSM.setmSMCallBack(new LTCloudSMCallBack() 
			{
				
				@Override
				public void ConnectEnterCallBack() {
					// TODO Auto-generated method stub
					Log.i("Main LTCloudSM", "ConnectEnterCallBack");
					appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
					
				}
				
				@Override
				public void SwitchInfoEnterCallBack() {
					// TODO Auto-generated method stub
					//DialogOperate(8, false, 8);
					Log.i("Main LTCloudSM", "SwitchInfoCallCallBack");
					appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
				}
				
				@Override
				public void SwitchModEnterCallBack() {
					// TODO Auto-generated method stub
//					DialogOperate(8, false, 8);
					Log.i("Main LTCloudSM", "SwitchModEnterCallBack");
					appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
				}

				@Override
				public void SwitchStateChangeEnterCallBack() {
					// TODO Auto-generated method stub
					Log.i("Main LTCloudSM", "SwitchStateChangeEnterCallBack");
					appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
				}

				@Override
				public void SwitchModBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				@Override
				public void TalkAnswerCallBack(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "TalkAnswerCallBack");
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
					
				}
				
				@Override
				public void TalkUpDownCallBack(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "TalkUpDownCallBack");
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void TalkStarkCallBack(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "TalkStarkCallBack");
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void TalkEndCallBack(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "TalkEndCallBack");
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void TalkCallCallBack(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "TalkCallCallBack");
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void SwitchInfoBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void SwitchActionReturnBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void SwitchActionCallBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void SMOverCallBack() {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "SMOverCallBack");
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void MonitorUpDownCallBack(boolean flag) {
					// TODO Auto-generated method stub
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
					
				}
				
				@Override
				public void MonitorStarkCallBack(boolean flag) {
					// TODO Auto-generated method stub
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void MonitorEndCallBack(boolean flag) {
					// TODO Auto-generated method stub
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void MonitorCallCallBack(boolean flag) {
					// TODO Auto-generated method stub
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void FreeCallBack(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("LTCloudSM", "FreeCallBack");
					if(!flag)
					{
						DialogOperate(8, false, 8);
					}
				}
				
				@Override
				public void DefenceInfoCallCallBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void DefenceInfoBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void DefenceActionReturnBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}
				
				@Override
				public void DefenceActionCallBack() {
					// TODO Auto-generated method stub
					DialogOperate(8, false, 8);
				}

			});
			
			synchronized (ltSM) 
			{
				SendSMMessage(next_flag_sm);

			    Log.i("StartSM", "-------------------------------------");
			    
			    try 
			    {
			          // wait for the messages to be handled

			    	ltSM.wait();
			        Log.i("sm", "wait over");
			        
			    } catch (InterruptedException e) {
			          Log.e("", "exception while waiting " + e.getMessage());
			    }
			    
			    Log.i("sm", "over");
			    
			    ltSM=null;
			}
		}
	}
	
	private void SendSMMessage(int what)
	{
		Message msg = new Message();
		msg.what = what;
		
		ltSM.sendMessage(msg);
	    Log.i("msg", String.format("SendSMMessage %d over!", what));
	}
	
	/********************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Home");

		// setTitle(R.string.sliding_title);

		setContentView(R.layout.frame_content);

		Log.i("Main", "GetLoginInfo");
		GetLoginInfo();

		Log.i("Main", "GetPhoneInfo");
		GetPhoneInfo();

		Log.i("Main", "GetIpInfo");
		GetIpInfo();

		Log.i("Main", "InitMsgHandler");
		InitHandler();

		Log.i("Main", "InitSlidingMenu");
		InitSlidingMenu();

		Log.i("Main", "InitPopupView");
		InitPopupView();

		Log.i("Main", "InitDialog");
		InitDialog();

//		Log.i("Main", "InitControlDelay");
//		InitControlDelay(1000 * 5);

		Log.i("Main", "InitNetworkStateService");
		InitNetworkStateService();

		// Log.i("Main", "InitAppStatusService");
		// InitAppStatusService();

		imageMap = new HashMap<Integer, byte[]>();
		for (int i = 0; i < 50; i++) {
			imageMap.put(i, new byte[1200]);
		}
		
		//test flag
		isTerminalConFlag = true;
		isDoorConFlag = true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (!isNetWorkFlag) 
			{
				if(!isNWConnectRemind)
				{
					if (mPopupWindow != null && !mPopupWindow.isShowing()) 
					{
						mPopupWindow.showAtLocation(findViewById(R.id.content),
								Gravity.BOTTOM, 0, 0);
					}
					
					isNWConnectRemind=true;
					
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			dialogHelper.ShowExitAlertDialog();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void OnSettingButtonClick(String serverIPString) {
		// here send the setting data
		Log.i("Main", "Setting");
		Toast.makeText(getApplicationContext(), "设置测试，当前没有任何操作！",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnRightMenuFragmentClick(int operateObject) {
		// TODO Auto-generated method stub
		switch (operateObject) {
		case 0:
			dialogHelper.ShowExitAlertDialog();
			break;
		case 1:
			dialogHelper.ShowReLoginAlertDialog();
			break;
		case 11:
			Toast.makeText(getApplicationContext(), "我的跟帖!!！",
					Toast.LENGTH_SHORT).show();
			break;
		case 12:
			Toast.makeText(getApplicationContext(), "我的收藏!!！",
					Toast.LENGTH_SHORT).show();
			break;
		case 13:
			Toast.makeText(getApplicationContext(), "消息推送!!！",
					Toast.LENGTH_SHORT).show();
			break;
		case 9:
			Toast.makeText(getApplicationContext(), "返回", Toast.LENGTH_SHORT)
					.show();
			break;
		}

		mSlidingMenu.showContent();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		getSherlock().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			toggle(); // 鍔ㄦ?鍒ゆ柇鑷姩鍏抽棴鎴栧紑鍚疭lidingMenu
			// getSlidingMenu().showMenu();//鏄剧ずSlidingMenu
			// getSlidingMenu().showContent();//鏄剧ず鍐呭
			return true;
		case R.id.action_refresh:

			Toast.makeText(getApplicationContext(), R.string.refresh,
					Toast.LENGTH_SHORT).show();

			return true;
		case R.id.action_person:

			if (mSlidingMenu.isSecondaryMenuShowing()) {
				mSlidingMenu.showContent();
			} else {
				mSlidingMenu.showSecondaryMenu();

			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void selectItem(int position, String title) {
		if (position == fragmentFlag) {
			mSlidingMenu.showContent();
			return;
		} 
		
		if(ltSM!=null)
		{
			if(ltSM.GetState()!=LTCloudEnum.TalkEnd &&
			   ltSM.GetState()!=LTCloudEnum.MonitorEnd &&
			   ltSM.GetState()!=LTCloudEnum.Free)
			{
				
				Log.i("Main in item", 
						"sm is action now,not allow to turn page!");
				return;
			}
			
		}
		
		switch (fragmentFlag) {
		case 4:
			if (dynUdpHelper != null) {
				dynUdpHelper.ListenStop();
				dynUdpHelper = null;
				
				((TalkP2PFragment) fragment).Stop();
				
				SMOperate(404, false, 404);
				
			}
			Log.i("Main", "release TalkP2PFragment");
			break;
		case 3:
			if (dynUdpHelper != null) {
				dynUdpHelper.ListenStop();
				dynUdpHelper = null;
				
				SMOperate(404, false, 404);
			}
			Log.i("Main", "release MonitorFragment");
			break;
		}
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			fragmentFlag = position;
			break;
		case 1:
			SwitchInfoOperate();
			break;
		case 2:
			Toast.makeText(getApplicationContext(), "布防功能后续开发！",
					Toast.LENGTH_LONG).show();
			break;
		case 3:
			MonitorCallOperate();
			// fragment = new MonitorFragment("198.168.0.65", 8999);
			// fragmentFlag = position;
			break;
		case 4:
			TalkCallOperate();
			break;
		case 5:
			Toast.makeText(getApplicationContext(), "系统设置功能后续开发！",
					Toast.LENGTH_LONG).show();
			break;
		case 6:
			fragment = new AboutFragment();
			fragmentFlag = position;
			break;
		default:
			break;
		}
		
		if (fragment != null) {
			if(fragmentFlag==position)
			{
				try 
				{
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content, fragment)
							.commit();
					// update selected item and title, then close the drawer
					setTitle(title);
					mSlidingMenu.showContent();
				} 
				catch (Exception e) 
				{
					// TODO: handle exception
				}

			}
		} else {
			// error in creating fragment
			Log.i("Main", "Error in creating fragment");
		}
	}

	@Override
	protected void onDestroy() {

		Log.i("Main", "MainActivity.onDestroy()");

		selectItem(0, "主页");

		appUdpHelper.ListenStop();

		if (dynUdpHelper != null) {
			dynUdpHelper.ListenStop();
		}

		Intent stopIntent = new Intent(this, NetworkStateService.class);
		stopService(stopIntent);

		sendHeartBeatTimer.cancel();
		
		super.onDestroy();
	}

	/********************************************************************/

	private boolean CheckIsLocalNetWork() {
		if (serverIPString != null && publicIPString != null) {
			if (publicIPString.equals(serverIPString)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			Log.i("Main", "Bytes2Bitmap ok");
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			Log.i("Main", "Bytes2Bitmap null");
			return null;
		}
	}

	private void DialogOperate(int type,boolean flag,int id)
	{
		if(dialogHelper==null)
		{
			Log.i("Main in DialogOperate", "dialogHelper null");
			return;
		}
		
		switch (type) {
		case 8:
			if(flag)
			{
				dialogHelper.ShowCustomProgressDialog(id);
			}
			else {
				dialogHelper.CloseCustomProgressDialog();
			}
			break;
		case 1:
			dialogHelper.ShowReLoginAlertDialog();
			break;
		case 2:
			dialogHelper.ShowExitAlertDialog();
			break;
		case 3:
			dialogHelper.ShowCloseMonitorAlertDialog();
			break;
		case 4:
			dialogHelper.ShowCloseTalkP2PAlertDialog();
			break;
		default:
			break;
		}
	}
	
	private boolean SMOperate(int flag,boolean checkFlag,int id)
	{
		if(ltSM == null)
		{
			if(!checkFlag)
			{
				if (flag == 8) 
				{
					next_flag_sm = ltSM.CMD_CONNECTCALL;
					type_sm = ltSM.TYPE_CONNECT;
					new Thread(new StartSM()).start();
					showDialog(8);

					return true;
				}
				else if (flag == 818) {
					next_flag_sm = ltSM.CMD_SWITCHINFO;
					type_sm = ltSM.TYPE_SWITCHINFO;
					new Thread(new StartSM()).start();
					showDialog(818);

					return true;
				} else if (flag == 812) {
					next_flag_sm = ltSM.CMD_SWITCHMOD;
					type_sm = ltSM.TYPE_SWITCHMOD;
					new Thread(new StartSM()).start();
					showDialog(812);

					return true;
				} 
				else if (flag == 811) 
				{
					next_flag_sm = ltSM.CMD_SWITCHSTATECHANGE;
					type_sm = ltSM.TYPE_SWITCHSTATECHANGE;
					new Thread(new StartSM()).start();
					showDialog(811);

					return true;
				} 
				else if (flag == 891) {
					next_flag_sm = ltSM.CMD_TALKCALL;
					type_sm = ltSM.TYPE_TALK;
					new Thread(new StartSM()).start();
					showDialog(891);

					return true;
				} 
				else if (flag == 901) {
					next_flag_sm = ltSM.CMD_MONITORCALL;
					type_sm = ltSM.TYPE_MONITOR;
					new Thread(new StartSM()).start();
					showDialog(901);

					return true;
				} 
				else {
					Log.i("Main in SMOperate", "state is not start!");
					return false;
				}
			}

		}

		switch (flag) 
		{
		case 8:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.ConnectCall)
				{
					Log.i("Main in SMOperate", "ConnectCall state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "ConnectCall state is check right!");
					return true;
				}
			}
			else 
			{
				return false;
			}
		case 818:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.SwitchInfoCall)
				{
					Log.i("Main in SMOperate", "SwitchInfoCall state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "SwitchInfoCall state is check right!");
					return true;
				}
			}
			else 
			{
				return false;
			}
		case 812:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.SwitchModCall)
				{
					Log.i("Main in SMOperate", "SwitchModCall state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "SwitchModCall state is check right!");
					return true;
				}
			}
			else 
			{
				return false;
			}
		case 891:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.TalkCall)
				{
					Log.i("Main in SMOperate", "TalkCall state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "TalkCall state is check right!");
					return true;
				}
			}
			else 
			{
				return false;
			}
		case 8916:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.TalkAnswer)
				{
					Log.i("Main in SMOperate", "TalkAnswer state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "TalkAnswer state is check right!");
					return true;
				}
			}
			else
			{
				showDialog(id);
				SendSMMessage(ltSM.CMD_TALKANSWER);
				return true;
			}

		case 892:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.TalkStart)
				{
					Log.i("Main in SMOperate", "TalkStart state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "TalkStart state is check right!");
					return true;
				}
			}
			else {
				showDialog(id);
				SendSMMessage(ltSM.CMD_TALKSTARK);
				return true;
			}
	
		case 893:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.TalkUpDown)
				{
					Log.i("Main in SMOperate", "TalkUpDown state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "TalkUpDown state is check right!");
					return true;
				}
			}
			else {
				SendSMMessage(ltSM.CMD_TARKUPDOWN);
				return true;
			}

		case 894:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.TalkEnd)
				{
					Log.i("Main in SMOperate", "TalkEnd state is not right!");
					return false;
				}
				else {
					Log.i("Main in SMOperate", "TalkEnd state is check right!");
					return true;
				}
			}
			else {
				SendSMMessage(ltSM.CMD_TALKEND);
				return true;
			}

		case 901:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.MonitorCall)
				{
					Log.i("Main in SMOperate", "MonitorCall state is not right!");
					return false;
				}
				else {
					return true;
				}
			}
			else {
				return true;
			}

		case 902:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.MonitorStart)
				{
					Log.i("Main in SMOperate", "MonitorStart state is not right!");
					return false;
				}
				else {
					return true;
				}
			}
			else {
				SendSMMessage(ltSM.CMD_MONITORSTARK);
				showDialog(902);
				return true;
			}

		case 903:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.MonitorUpDown)
				{
					Log.i("Main in SMOperate", "MonitorUpDown state is not right!");
					return false;
				}
				else {
					return true;
				}
			}
			else {
				SendSMMessage(ltSM.CMD_MONITORUPDOWN);
				return true;
			}

		case 904:
			if(checkFlag)
			{
				if(ltSM.GetState()!=LTCloudEnum.MonitorEnd)
				{
					Log.i("Main in SMOperate", "MonitorEnd state is not right!");
					return false;
				}
				else {
					return true;
				}
			}
			else {
				SendSMMessage(ltSM.CMD_MONITOREND);
				return true;
			}
		case 404:
			SendSMMessage(ltSM.CMD_FREE);
			break;
		default:
			break;
		}
		
		return false;
	}
	
	/********************************************************************/

	private void Login2Server() 
	{

		if (!isListening) {
			Toast.makeText(getApplicationContext(), "请检查网络后再试...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Log.i("Main", "Login2Server send data begin...");
		
		udpSendData = null;

		udpSendData = protocol.ConvertByteArray2byteArray(protocol.LoginCall(
				imeiString, localIPString, "", unEString, pwEString,
				AudioRecord.getMinBufferSize(8000,
						AudioFormat.CHANNEL_CONFIGURATION_MONO,
						AudioFormat.ENCODING_PCM_16BIT), AudioTrack
						.getMinBufferSize(8000,
								AudioFormat.CHANNEL_CONFIGURATION_MONO,
								AudioFormat.ENCODING_PCM_16BIT)));

		if (udpSendData != null) {

			
			SMOperate(8, false, 8);

//			_8888UdpHelper.SendData(udpSendData, serverIPString, 8888);
            
			
		} else {
			Log.i("Main", "Login2Server send data error");
		}
	}
	
	private void TerConFeedBack(int type) 
	{

		if (!isListening) {
			Toast.makeText(getApplicationContext(), "请检查网络后再试...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Log.i("Main", "TerConFeedBack send data begin...");
		
		udpSendData = null;

		udpSendData = protocol.ConvertByteArray2byteArray(protocol.TerConFeedBack(
				imeiString, localIPString, "", unEString, pwEString, type));

		if (udpSendData != null) {
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
			Log.i("TerConFeedBack send data", serverIPString);
			
			
		} else {
			Log.i("Main", "TerConFeedBack send data error");
		}
	}
	
	private void LoginOut() {

		if (!isLoginFlag) {
			return;
		}

		udpSendData = null;

		udpSendData = protocol.ConvertByteArray2byteArray(protocol
				.LoginOutCall(imeiString, localIPString, "", unEString,
						pwEString));

		if (udpSendData != null) {

			// new Thread(new Client()).start();
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
			Log.i("LoginOut send data", serverIPString);
			// udpHelper8888.SendTest();
		} else {
			Log.i("LoginOut send data", "error");
		}
	}

	private void SwitchInfoOperate() 
	{

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;

		udpSendData = protocol.ConvertByteArray2byteArray(protocol.SwitchInfoCall(
				imeiString, localIPString, "", unEString, pwEString));

		if (udpSendData != null) {
			// new Thread(new Client()).start();
			
			SMOperate(818, false, 818);
			
//			_8888UdpHelper.SendData(udpSendData, serverIPString, 8888);
		}
	}
	
	private void SwitchInfoModOperate(byte switchNo,
			byte switchType1,byte switchEnable,byte switchState,byte switchType2,
			byte[] switchName)
	{

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;

		udpSendData = protocol.ConvertByteArray2byteArray(protocol.SwitchInfoModCall(
				imeiString, localIPString, "", unEString, pwEString, 
				switchNo, switchType1, switchEnable, switchState, switchType2, switchName)
				);

		if (udpSendData != null) {
			// new Thread(new Client()).start();
			
			SMOperate(812, false, 812);
//			_8888UdpHelper.SendData(udpSendData, serverIPString, 8888);
		}
	}
	
	private void SwitchStatechangeOperate(String switchName, int switchState) {

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;

		udpSendData = protocol.ConvertByteArray2byteArray(protocol.SwitchStateChangeCall(
				imeiString, localIPString, "", unEString, pwEString,
				Integer.parseInt(switchName), switchState));

		if (udpSendData != null) {
			// new Thread(new Client()).start();
			
			SMOperate(811, false, 811);
			
//			_8888UdpHelper.SendData(udpSendData, serverIPString, 8888);
		}

	}

	private void TalkCallOperate() {

		if(ltSM!=null)
		{
			Log.i("TalkCallOperate", "sm is not allow to call");
			return;
		}
		
//		Log.i("Get state", sm.GetState().toString());
		
		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.TalkCall(
				imeiString, localIPString, "", unEString, pwEString));

		if (udpSendData != null) 
		{

			Log.i("TalkCallOperate", "No.1 TalkCallOperate");
			
			SMOperate(891, false, 891);
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}
	}

	private void TalkStartOperate() 
	{
		if(ltSM==null)
		{
			Log.i("TalkCallOperate", "sm is not allow to call");
			return;
		}
		

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.TalkStart(
				imeiString, publicIPString, loginAddressString, tempID,
				unEString, pwEString));

		if (udpSendData != null) {
			// new Thread(new Client()).start();
			Log.i("TalkStartOperate", "No.2 TalkStartOperate");
			
			SMOperate(892, false, 892);
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}
	}

	private void TalkConfirmOperate() 
	{
		if(ltSM==null)
		{
			Log.i("TalkConfirmOperate", "sm is not allow to call");
			return;
		}

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.TalkConfirm(
				imeiString, publicIPString, loginAddressString, tempID,
				unEString, pwEString));

		if (udpSendData != null) {
			// new Thread(new Client()).start();
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}

	}

	private void TalkUpOperate(byte[] data) 
	{
		if(ltSM==null)
		{
			Log.i("TalkUpOperate", "sm is not allow to call");
			return;
		}

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.TalkUp(
				imeiString, publicIPString, loginAddressString, tempID,
				unEString, pwEString,data.length,data));

		if (udpSendData != null) {
			// new Thread(new Client()).start();
			dynUdpHelper.SendData(udpSendData, serverIPString, dynPort);
		}

	}
	
	private void TalkEndOperate()
	{
		if(ltSM==null)
		{
			Log.i("TalkEndOperate", "sm is not allow to call");
			return;
		}

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isTerminalConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.TalkEnd(
				imeiString, publicIPString, loginAddressString, tempID,
				unEString, pwEString));

		if (udpSendData != null) {

			SMOperate(894, false, 894);
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}


	}

	private void DynPortReturnOperater() {
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol
				.DynPortReturn(imeiString, localIPString, "", unEString,
						pwEString));

		if (udpSendData != null && dynUdpHelper != null) {
			// new Thread(new Client()).start();
			dynUdpHelper.SendData(udpSendData, serverIPString, dynPort);
			Log.i("DynPortReturnOperater", "dyn 发送结束！");
		} else {
			Toast.makeText(getApplicationContext(), "动态端口发送校验失败！",
					Toast.LENGTH_SHORT).show();
			// Log.i("DynPortReturnOperater", "dyn 发送失败！");
		}
	}

	private void MonitorCallOperate() 
	{
		if(ltSM!=null)
		{
			Log.i("MonitorCallOperate", "sm is not allow to call");
			return;
		}

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isDoorConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.MonitorCall(
				imeiString, localIPString, "", unEString, pwEString));

		if (udpSendData != null) 
		{
			SMOperate(901, false, 901);
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}

		
	}

	private void MonitorStartOperate(byte[] id) 
	{
		if(ltSM==null)
		{
			Log.i("MonitorStartOperate", "sm is not allow to call");
			return;
		}
		
		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isDoorConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol
				.MonitorStart(imeiString, publicIPString, loginAddressString,
						id, unEString, pwEString));

		if (udpSendData != null) {

			SMOperate(902, false, 902);
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}

		


	}

	private void MonitorConfirmOperate() {

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isDoorConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol
				.MonitorConfirm(imeiString, publicIPString, loginAddressString,
						tempID, unEString, pwEString));

		if (udpSendData != null) {
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}

	}

	private void MonitorEndOperate() 
	{
		if(ltSM==null)
		{
			Log.i("MonitorEndOperate", "sm is not allow to call");
			return;
		}

		if (!isLoginFlag) {
			Toast.makeText(getApplicationContext(), "请连接服务器后再尝试该操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if(!isDoorConFlag)
		{
			Toast.makeText(getApplicationContext(), "对于终端没有响应，请稍等后再操作...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		udpSendData = null;
		udpSendData = protocol.ConvertByteArray2byteArray(protocol.MonitorEnd(
				imeiString, publicIPString, loginAddressString, tempID,
				unEString, pwEString));

		if (udpSendData != null) {
			
			//SMOperate(904, false, 904);
			
			appUdpHelper.SendData(udpSendData, serverIPString, serverPort);
		}



	}

	/********************************************************************/
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return dialogHelper.ShowCustomProgressDialog(id);
	}

	
}
