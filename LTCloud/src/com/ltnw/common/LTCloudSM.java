package com.ltnw.common;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Message;
import android.util.Log;

import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.ltnw.entity.LTCloudEnum;
import com.ltnw.interFace.LTCloudSMCallBack;

public class LTCloudSM extends StateMachine
{
	private static final String TAG = "LTCloudSM";

	public static final int TYPE_CONNECT=8;
	public static final int TYPE_SWITCHINFO=818;
	public static final int TYPE_SWITCHMOD=812;
	public static final int TYPE_SWITCHSTATECHANGE=811;
	public static final int TYPE_TALK=89;
	public static final int TYPE_MONITOR=90;
	
    public static final int CMD_FREE = 0;
    
    public static final int CMD_CONNECTCALL=8;
    
    public static final int CMD_SWITCHINFO=818;
    public static final int CMD_SWITCHMOD=812;
    public static final int CMD_SWITCHSTATECHANGE=811;
    
    public static final int CMD_TALKCALL = 891;
    public static final int CMD_TALKANSWER = 8916;
    public static final int CMD_TALKSTARK = 892;
    public static final int CMD_TARKUPDOWN = 893;
    public static final int CMD_TALKEND = 894;
    
    public static final int CMD_MONITORCALL = 901;
    public static final int CMD_MONITORSTARK = 902;
    public static final int CMD_MONITORUPDOWN = 903;
    public static final int CMD_MONITOREND = 904;
    
    private LTCloudEnum ltCloudState=LTCloudEnum.Free;
    
    private LTCloudSMCallBack mSMCallBack;
    
	public void setmSMCallBack(LTCloudSMCallBack mSMCallBack) {
		this.mSMCallBack = mSMCallBack;
	}
    
    private Free mFree = new Free();
    
    private ConnectCall mConnectCall = new ConnectCall();
    private SwitchInfoCall mSwitchInfoCall=new SwitchInfoCall();
    private SwitchModCall mSwitchModCall=new SwitchModCall();
    private SwitchStateChangeCall mSwitchStateChangeCall=new SwitchStateChangeCall();
    
    private TalkCall mTalkCall = new TalkCall();
    private TalkAnswer mTalkAnswer = new TalkAnswer();
    private TalkStart mTalkStart = new TalkStart();
    private TalkUpDown mTalkUpDown = new TalkUpDown();
    private TalkEnd mTalkEnd = new TalkEnd();
    private MonitorCall mMonitorCall = new MonitorCall();
    
    private MonitorStart mMonitorStart = new MonitorStart();
    private MonitorUpDown mMonitorUpDown = new MonitorUpDown();
    private MonitorEnd mMonitorEnd = new MonitorEnd();
    
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private static int count = 0;
	private boolean isStop = true;
	private static int delay = 1000;  //1s
	private static int period = 1000;  //1s
	
	private static int type=0;
	
	private void startTimer(final int cb)
	{
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() 
				{
//					Log.i(TAG, "count: "+String.valueOf(count));

					if(count==cb)
					{
						stopTimer(false);
					}
					
					do {
						try 
						{
							//Log.i(TAG, "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}	
					} while (false);
					
					count ++;  
				}
			};
		}

		if(mTimer != null && mTimerTask != null )
			mTimer.schedule(mTimerTask, delay, period);
	}
	
	private void stopTimer(boolean flag){
		
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}	

		count = 0;

		if(!flag)
		{
			ReturnSMFail();
		}
		
	}
	
	private void ReturnSMFail()
	{
		if(ltCloudState==LTCloudEnum.TalkCall)
		{
			mSMCallBack.TalkCallCallBack(false);
		}
		
		if(ltCloudState==LTCloudEnum.TalkStart)
		{
			mSMCallBack.TalkStarkCallBack(false);
		}
		
		if(ltCloudState==LTCloudEnum.TalkUpDown)
		{
			mSMCallBack.TalkUpDownCallBack(false);
		}
		
		if(ltCloudState==LTCloudEnum.TalkEnd)
		{
			mSMCallBack.TalkEndCallBack(false);
		}
		
		sendMessage(obtainMessage(CMD_FREE));
		Log.i(TAG, "ReturnSMFail Free");
	}
	
    public static LTCloudSM makeLTCloudSM(int type) 
    {
    	LTCloudSM sm = new LTCloudSM("LTCloudSM",type);
        sm.start();
        return sm;
    }
    
	protected LTCloudSM(String name,int type) {
		
		super(name);
		// TODO Auto-generated constructor stub
		addState(mFree);
		
		this.type=type;
		Log.i(TAG, String.format("set type[%d]", this.type));
		
		if(type==TYPE_CONNECT)
		{
			addState(mConnectCall, mFree);
		}
		else if(type==TYPE_SWITCHINFO)
		{
			addState(mSwitchInfoCall, mFree);
		}
		else if(type==TYPE_SWITCHMOD)
		{
			addState(mSwitchModCall, mFree);
		}
		else if(type==TYPE_SWITCHSTATECHANGE)
		{
			addState(mSwitchStateChangeCall, mFree);
		}
		else if(type==TYPE_TALK)
        {
        	 addState(mTalkCall, mFree);
             addState(mTalkAnswer, mTalkCall);
             addState(mTalkStart, mTalkAnswer);
             addState(mTalkUpDown, mTalkStart);
             addState(mTalkEnd, mTalkUpDown);
        }
        else if(type==TYPE_MONITOR)
        {
            addState(mMonitorCall, mFree);
            addState(mMonitorStart, mMonitorCall);
            addState(mMonitorUpDown, mMonitorStart);
            addState(mMonitorEnd, mMonitorUpDown);
        }

        
        setInitialState(mFree);
	}
	
	public LTCloudEnum GetState()
	{
		return ltCloudState;
	}
	
    void onHalting() 
    {
        Log.d(TAG, "halting");
        ltCloudState=LTCloudEnum.Free;
        mSMCallBack.SMOverCallBack();
        synchronized (this) 
        {
            this.notifyAll();
        }
    }
	
    class Free extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "Free enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "Free processMessage what=" + message.what);
            switch(message.what) {
            case CMD_CONNECTCALL:
            	if(type!=TYPE_CONNECT)
            	{
            		Log.i(TAG, "Free getType ERROR,will Exit!");
                	transitionToHaltingState();
                	break;
            	}
            	Log.i(TAG, "Free getMessage CMD_CONNECTCALL");
            	ltCloudState=LTCloudEnum.ConnectCall;
            	transitionTo(mConnectCall);
                
                startTimer(5);
                Log.i(TAG, "Free delay 5!");
                retVal = HANDLED;
                break;
            case CMD_SWITCHINFO:
            	if(type!=TYPE_SWITCHINFO)
            	{
            		Log.i(TAG, "Free getType ERROR,will Exit!");
                	transitionToHaltingState();
                	break;
            	}
            	Log.i(TAG, "Free getMessage CMD_SWITCHINFO");
            	ltCloudState=LTCloudEnum.SwitchInfoCall;
            	transitionTo(mSwitchInfoCall);
                
                startTimer(5);
                Log.i(TAG, "Free delay 5!");
                retVal = HANDLED;
            	break;
            case CMD_SWITCHMOD:
            	if(type!=TYPE_SWITCHMOD)
            	{
            		Log.i(TAG, "Free getType ERROR,will Exit!");
                	transitionToHaltingState();
                	break;
            	}
            	Log.i(TAG, "Free getMessage CMD_SWITCHMOD");
            	ltCloudState=LTCloudEnum.SwitchModCall;
            	transitionTo(mSwitchModCall);
                
                startTimer(5);
                Log.i(TAG, "Free delay 5!");
                retVal = HANDLED;
            	break;
            case CMD_SWITCHSTATECHANGE:
            	if(type!=TYPE_SWITCHSTATECHANGE)
            	{
            		Log.i(TAG, "Free getType ERROR,will Exit!");
                	transitionToHaltingState();
                	break;
            	}
            	Log.i(TAG, "Free getMessage TYPE_SWITCHStateChange");
            	ltCloudState=LTCloudEnum.SwitchStateChangeCall;
            	transitionTo(mSwitchModCall);
                
                startTimer(5);
                Log.i(TAG, "Free delay 5!");
                retVal = HANDLED;
            	break;
            case CMD_TALKCALL:
            	
            	if(type!=TYPE_TALK)
            	{
            		Log.i(TAG, "Free getType ERROR,will Exit!");
                	transitionToHaltingState();
                	break;
            	}
            	
            	Log.i(TAG, "Free getMessage CMD_TALKCALL");
            	ltCloudState=LTCloudEnum.TalkCall;
            	transitionTo(mTalkCall);
                
                startTimer(5);
                Log.i(TAG, "Free delay 5!");
                retVal = HANDLED;
                break;
            case CMD_MONITORCALL:
            	
            	if(type!=TYPE_MONITOR)
            	{
            		Log.i(TAG, "Free getType ERROR,will Exit!");
                	transitionToHaltingState();
                	break;
            	}
            	
            	Log.i(TAG, "Free getMessage CMD_MONITORCALL");
            	ltCloudState=LTCloudEnum.MonitorCall;
            	transitionTo(mMonitorCall);
                
                startTimer(5);
                Log.i(TAG, "Free delay 5!");
                retVal = HANDLED;
                break;
            default:
            	Log.i(TAG, "Free getMessage ERROR,will Exit!");
            	transitionToHaltingState();
            	break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "Free exit");
        }
    }
	
    class ConnectCall extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "ConnectCall enter");
            mSMCallBack.ConnectEnterCallBack();
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "ConnectCall processMessage what=" + message.what);
            switch(message.what) {
            case CMD_FREE:
            	stopTimer(true);
            	Log.i(TAG, "ConnectCall getMessage CMD_FREE");
//                mSMCallBack.TalkCallCallBack(true);
//                transitionTo(mTalkAnswer);
                Log.i(TAG, "ConnectCall Action ok!");
            	ltCloudState=LTCloudEnum.Free;

                retVal = HANDLED;
                transitionToHaltingState();
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "ConnectCall exit");
        }
    }
    
    class SwitchInfoCall extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "SwitchInfoCall enter");
            mSMCallBack.SwitchInfoEnterCallBack();
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "SwitchInfoCall processMessage what=" + message.what);
            switch(message.what) {
            case CMD_FREE:
            	stopTimer(true);
            	Log.i(TAG, "SwitchInfoCall getMessage CMD_FREE");
//                mSMCallBack.TalkCallCallBack(true);
//                transitionTo(mTalkAnswer);
                Log.i(TAG, "SwitchInfoCall Action ok!");
            	ltCloudState=LTCloudEnum.Free;

                retVal = HANDLED;
                transitionToHaltingState();
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "SwitchInfoCall exit");
        }
    }
    
    class SwitchModCall extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "SwitchModCall enter");
            mSMCallBack.SwitchModEnterCallBack();
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "SwitchModCall processMessage what=" + message.what);
            switch(message.what) {
            case CMD_FREE:
            	stopTimer(true);
            	Log.i(TAG, "SwitchModCall getMessage CMD_FREE");
//                mSMCallBack.TalkCallCallBack(true);
//                transitionTo(mTalkAnswer);
                Log.i(TAG, "SwitchModCall Action ok!");
            	ltCloudState=LTCloudEnum.Free;

                retVal = HANDLED;
                transitionToHaltingState();
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "SwitchModCall exit");
        }
    }
    
    class SwitchStateChangeCall extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "SwitchStateChangeCall enter");
            mSMCallBack.SwitchStateChangeEnterCallBack();
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "SwitchStateChangeCall processMessage what=" + message.what);
            switch(message.what) {
            case CMD_FREE:
            	stopTimer(true);
            	Log.i(TAG, "SwitchStateChangeCall getMessage CMD_FREE");
//                mSMCallBack.TalkCallCallBack(true);
//                transitionTo(mTalkAnswer);
                Log.i(TAG, "SwitchStateChangeCall Action ok!");
            	ltCloudState=LTCloudEnum.Free;

                retVal = HANDLED;
                transitionToHaltingState();
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "SwitchStateChangeCall exit");
        }
    }
    
    class TalkCall extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "TalkCall enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "TalkCall processMessage what=" + message.what);
            switch(message.what) {
            case CMD_TALKANSWER:
            	stopTimer(true);
            	Log.i(TAG, "TalkCall getMessage CMD_TALKANSWER");
                mSMCallBack.TalkCallCallBack(true);
                transitionTo(mTalkAnswer);
                Log.i(TAG, "TalkCall Action ok!");
            	ltCloudState=LTCloudEnum.TalkAnswer;

                startTimer(30);
                Log.i(TAG, "delay 30!");
                retVal = HANDLED;
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "TalkCall exit");
        }
    }
    
    class TalkAnswer extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "TalkAnswer enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "TalkAnswer processMessage what=" + message.what);
            switch(message.what) {
            case CMD_TALKSTARK:
            	stopTimer(true);
            	Log.i(TAG, "TalkAnswer getMessage CMD_TALKSTARK");
                mSMCallBack.TalkAnswerCallBack(true);
                ltCloudState=LTCloudEnum.TalkStart;
                transitionTo(mTalkStart);
                Log.i(TAG, "TalkAnswer Action ok!");
                
                startTimer(5);
                Log.i(TAG, "delay 5!");
                retVal = HANDLED;
                break;
//            case CMD_FREE:
//            	transitionToHaltingState();
//            	break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "TalkAnswer exit");
        }
    }
    
    class TalkStart extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "TalkStart enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "TalkStart processMessage what=" + message.what);
            switch(message.what) {
            case CMD_TARKUPDOWN:
            	stopTimer(true);
            	Log.i(TAG, "TalkStart getMessage CMD_TALKANSWER");
                mSMCallBack.TalkStarkCallBack(true);
                ltCloudState=LTCloudEnum.TalkUpDown;
                transitionTo(mTalkUpDown);
                Log.i(TAG, "TalkStart Action ok!");
            	
                startTimer(60*3);
                Log.i(TAG, "delay 3*60!");
                retVal = HANDLED;
                break;
            case CMD_FREE:
            	transitionToHaltingState();
            	break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "TalkStart exit");
        }
    }

    class TalkUpDown extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "TalkUpDown enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "TalkUpDown processMessage what=" + message.what);
            switch(message.what) {
            case CMD_TALKEND:
            	stopTimer(true);
            	Log.i(TAG, "TalkUpDown getMessage CMD_TALKANSWER");
                mSMCallBack.TalkUpDownCallBack(true);
                ltCloudState=LTCloudEnum.TalkEnd;
                transitionTo(mTalkEnd);
                Log.i(TAG, "TalkUpDown Action ok!");
      
                startTimer(5);
                Log.i(TAG, "delay 5!");
                retVal = HANDLED;
                break;
            case CMD_FREE:
            	transitionToHaltingState();
            	break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "TalkUpDown exit");
        }
    }
    
    class TalkEnd extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "TalkEnd enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "TalkEnd processMessage what=" + message.what);
            switch(message.what) {
            case CMD_FREE:
            	stopTimer(true);
            	Log.i(TAG, "TalkEnd getMessage CMD_TALKANSWER");
                mSMCallBack.TalkEndCallBack(true);
                ltCloudState=LTCloudEnum.Free;
                Log.i(TAG, "TalkEnd Action ok!");
            	
                retVal = HANDLED;
                transitionToHaltingState();
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "TalkEnd exit");
        }
    }
    
    class MonitorCall extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "MonitorCall enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "MonitorCall processMessage what=" + message.what);
            switch(message.what) {
            case CMD_MONITORSTARK:
            	stopTimer(true);
            	Log.i(TAG, "MonitorCall getMessage CMD_MONITORSTARK");
                mSMCallBack.MonitorCallCallBack(true);
                transitionTo(mMonitorStart);
                Log.i(TAG, "MonitorCall Action ok!");
            	ltCloudState=LTCloudEnum.MonitorStart;

                startTimer(5);
                Log.i(TAG, "MonitorCall delay 5!");
                retVal = HANDLED;
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "MonitorCall exit");
        }
    }
    
    class MonitorStart extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "MonitorStart enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "MonitorStart processMessage what=" + message.what);
            switch(message.what) {
            case CMD_MONITORUPDOWN:
            	stopTimer(true);
            	Log.i(TAG, "MonitorStart getMessage CMD_MONITORUPDOWN");
                mSMCallBack.MonitorStarkCallBack(true);
                ltCloudState=LTCloudEnum.MonitorUpDown;
                transitionTo(mMonitorUpDown);
                Log.i(TAG, "MonitorStart Action ok!");
            	
                startTimer(5);
                Log.i(TAG, "MonitorStart delay 5!");
                retVal = HANDLED;
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "MonitorStart exit");
        }
    }

    class MonitorUpDown extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "MonitorUpDown enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "MonitorUpDown processMessage what=" + message.what);
            switch(message.what) {
            case CMD_MONITOREND:
            	stopTimer(true);
            	Log.i(TAG, "MonitorUpDown getMessage CMD_MONITOREND");
                mSMCallBack.MonitorUpDownCallBack(true);
                ltCloudState=LTCloudEnum.MonitorEnd;
                transitionTo(mMonitorEnd);
                Log.i(TAG, "MonitorUpDown Action ok!");
      
                startTimer(30);
                Log.i(TAG, "MonitorUpDown delay 30!");
                retVal = HANDLED;
                break;
            default:
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "MonitorUpDown exit");
        }
    }
    
    class MonitorEnd extends State 
    {
        @Override public void enter() 
        {
            Log.i(TAG, "MonitorEnd enter");
        }
        
        @Override public boolean processMessage(Message message) 
        {
            boolean retVal=NOT_HANDLED;
            Log.i(TAG, "MonitorEnd processMessage what=" + message.what);
            switch(message.what) {
            case CMD_FREE:
            	stopTimer(true);
            	Log.i(TAG, "MonitorEnd getMessage CMD_FREE");
                mSMCallBack.MonitorEndCallBack(true);
                ltCloudState=LTCloudEnum.Free;
                Log.i(TAG, "MonitorEnd Action ok!");
            	
                retVal = HANDLED;
                transitionToHaltingState();
                break;
            default:
            	retVal = NOT_HANDLED;
                break;
            }
            return retVal;
        }
        
        @Override public void exit() 
        {
            Log.i(TAG, "MonitorEnd exit");
        }
    }
    
    
}
