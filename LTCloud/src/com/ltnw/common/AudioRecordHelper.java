package com.ltnw.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ltnw.interFace.AudioCallBack;



import android.R.integer;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Message;
import android.util.Log;

public class AudioRecordHelper {
	/**
	 * AudioRecord 写入缓冲区大小
	 */
	protected int rec_in_buf_size;
	/**
	 * 录制音频对象
	 */
	private AudioRecord rec_in;
	/**
	 * 录入的字节数组
	 */
	private byte[] rec_in_bytes;
	/**
	 * 存放录入字节数组的大小
	 */
//	private LinkedList<byte[]> rec_in_bytelist;
	private ConcurrentLinkedQueue<byte[]> rec_in_bytequeue;
	private byte[] rec_in_buf;
	/**
	 * AudioTrack 播放缓冲大小
	 */
	private int trk_out_buf_size;
	/**
	 * 播放音频对象
	 */
	private AudioTrack trk_out;
	/**
	 * 播放的字节数组
	 */
	private byte[] trk_out_bytes;
	// private LinkedList<Byte> trk_receive_buf;

	/**
	 * 录制音频线程
	 */
	private Thread rec_thread;
	/**
	 * 播放音频线程
	 */
	private Thread trk_thread;
	/**
	 * 发送音频线程
	 */
	private Thread send_thread;
	/**
	 * rec data buf
	 */
	private byte[] udpRec_playin_buf;
	private int udpRec_playin_flag = 0;
	private LinkedList<byte[]> udpRec_playin_bytelist;
	private LinkedList<byte[]> buf_playin_bytelist;
//	private byte[] blankData;
//	private Date startDate;
//	private Date endDate;
//	private Collection collection = null;
	/**
	 * 工作标志
	 */
	private boolean workFlag = false;
	private boolean recordFlag = false;
	private boolean playFlag = false;

	private int speexFrameSize = 320;

	private AudioCallBack mAudioCallBack;

	public void setmAudioCallBack(AudioCallBack mAudioCallBack) {
		this.mAudioCallBack = mAudioCallBack;
	}

	private void SendAudioData(byte[] data) {
		mAudioCallBack.SendRecordSound(data);
	}

	public void GetAudioData(byte[] data) {
		if (udpRec_playin_flag + data.length < trk_out_buf_size) {
			System.arraycopy(data, 0, udpRec_playin_buf, udpRec_playin_flag,
					data.length);
			udpRec_playin_flag += data.length;

		} else if (udpRec_playin_flag + data.length == trk_out_buf_size) {
			System.arraycopy(data, 0, udpRec_playin_buf, udpRec_playin_flag,
					data.length);
			udpRec_playin_bytelist.add(udpRec_playin_buf);
			udpRec_playin_flag = 0;

			if (!playFlag) {
				if (udpRec_playin_bytelist.size() > 10) {
					for (int k = 0; k < 10; k++) {
						buf_playin_bytelist.add(udpRec_playin_bytelist
								.removeFirst());
					}

					playFlag = true;
				} else {
					Log.i("udpRec_playin_bytelist",
							String.format("数据长度:%d",
									udpRec_playin_bytelist.size()));
				}

			}
		} else {
			int subFlag = trk_out_buf_size - udpRec_playin_flag;
			int otherFlag = data.length - subFlag;
			byte[] temp = new byte[subFlag];
			System.arraycopy(data, 0, temp, 0, subFlag);
			System.arraycopy(temp, 0, udpRec_playin_buf, udpRec_playin_flag,
					subFlag);
			udpRec_playin_bytelist.add(udpRec_playin_buf);
			udpRec_playin_flag = 0;
			System.arraycopy(data, subFlag, udpRec_playin_buf, 0, otherFlag);
			udpRec_playin_flag = otherFlag;
		}

		// Log.i("GetAudioData", "装载数据!");
		// Log.i("GetAudioData", String.format("数据长度:%d", data.length));
		// for(int i=0;i<data.length;i++)
		// {
		//
		// //Log.i("GetAudioData", String.format("第:%d个数据",i));
		// if(trk_receive_buf.size()==trk_out_buf_size)
		// {
		// udpRec_playin_bytelist.add(ConvertByteArray2byteArray(
		// trk_receive_buf.toArray(new Byte[trk_receive_buf.size()])));
		//
		// trk_receive_buf.clear();
		//
		// if(!playFlag)
		// {
		// if(udpRec_playin_bytelist.size()>10)
		// {
		// for(int k=0;k<10;k++)
		// {
		// buf_playin_bytelist.add(udpRec_playin_bytelist.removeFirst());
		// }
		//
		// playFlag=true;
		// }
		// else
		// {
		// Log.i("udpRec_playin_bytelist",
		// String.format("数据长度:%d", udpRec_playin_bytelist.size()));
		// }
		//
		// }
		//
		// // if (udpRec_playin_bytelist.size() >= 300)
		// // {
		// // udpRec_playin_bytelist.removeFirst();
		// // }
		//
		//
		// // Log.i("GetAudioData", "===============成功+1=====================");
		// }
		// else if(trk_receive_buf.size()>trk_out_buf_size)
		// {
		// udpRec_playin_bytelist.add(ConvertByteArray2byteArray(
		// trk_receive_buf.toArray(new Byte[trk_out_buf_size])));
		//
		// for(int j=0;j<trk_out_buf_size;j++)
		// {
		// trk_receive_buf.removeFirst();
		// }
		//
		// // Log.i("GetAudioData", "===============成功+1=====================");
		// }
		//
		// trk_receive_buf.add(data[i]);
		//
		// //Log.i("GetAudioData", String.format("数据长度:%d",
		// trk_receive_buf.size()));
		//
		// }

		// udpRec_playin_bytelist.add(data);

	}

	public AudioRecordHelper(int frameSize) {
		InitAudioSetting();
		speexFrameSize = frameSize;
		rec_in_buf = new byte[speexFrameSize];
	}

	public void WorkStart() {
		workFlag = true;

		rec_thread = new Thread(new recordSound());
		trk_thread = new Thread(new playRecord());
		// trk_thread.setPriority(10);
		send_thread = new Thread(new sendSound());

		// 启动录制线程
		rec_thread.start();
		// 启动播放线程
		trk_thread.start();
		// 启动发送线程
		send_thread.start();

//		startDate = new Date(System.currentTimeMillis());
//		endDate = new Date(System.currentTimeMillis());

		Log.i("WorkBreak", "start");
		//
	}

	@SuppressWarnings("deprecation")
	public void WorkStop() {
		recordFlag = false;
		workFlag = false;
		playFlag=false;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rec_in.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			rec_in.release();
			Log.i("AudioRecord", "release ok");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			trk_out.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			trk_out.release();
			Log.i("AudioTrack", "release ok");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// rec_thread.stop();
	}

	public void RecordSwitch(boolean flag) {
		recordFlag = flag;

		if (recordFlag) {
			Log.i("RecordSwitch", "true");
		} else {
			Log.i("RecordSwitch", "false");
		}
	}

	/**
	 * AudioRecord AudioTrack 初始化
	 */
	private void InitAudioSetting() {
		// AudioRecord 得到录制最小缓冲区的大小
		rec_in_buf_size = AudioRecord.getMinBufferSize(8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		// 实例化播放音频对象
		rec_in = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, rec_in_buf_size);
		// 实例化一个字节数组，长度为最小缓冲区的长度
		rec_in_bytes = new byte[rec_in_buf_size];
		// 实例化一个链表，用来存放字节组数
//		rec_in_bytelist = new LinkedList<byte[]>();
		rec_in_bytequeue=new ConcurrentLinkedQueue<byte[]>();

		Log.i("InitAudioSetting",
				String.format("rec_in_buf_size:%d", rec_in_buf_size));

		// AudioTrack 得到播放最小缓冲区的大小
		trk_out_buf_size = AudioTrack.getMinBufferSize(8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		// 实例化播放音频对象
		trk_out = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, trk_out_buf_size,
				AudioTrack.MODE_STREAM);
		// 实例化一个长度为播放最小缓冲大小的字节数组
		trk_out_bytes = new byte[trk_out_buf_size];
		// trk_receive_buf=new LinkedList<Byte>();
		udpRec_playin_buf = new byte[trk_out_buf_size];
		udpRec_playin_bytelist = new LinkedList<byte[]>();
		buf_playin_bytelist = new LinkedList<byte[]>();
//		blankData = new byte[trk_out_buf_size];

		Log.i("InitAudioSetting",
				String.format("trk_out_buf_size:%d", trk_out_buf_size));
	}

	public byte[] ConvertByteArray2byteArray(Byte[] tempBytes) {
		if (tempBytes != null) {
			byte[] buf = new byte[tempBytes.length];

			for (int i = 0; i < buf.length; i++) {
				buf[i] = tempBytes[i].byteValue();
			}

			return buf;
		} else {
			return null;
		}
	}

	/**
	 * 描述 :录音线程
	 */
	class recordSound implements Runnable {
		@Override
		public void run() {
			
			rec_in.startRecording();
			
			while (workFlag) {

				if (recordFlag) {
					
					try 
					{
						rec_in.read(rec_in_bytes, 0, rec_in_buf_size);

						if (AudioRecord.ERROR_INVALID_OPERATION != rec_in_buf_size) 
						{
//							if (rec_in_bytelist.size() >= 3) 
//							{
//								Log.i("recordSound", "begin remove");
//								rec_in_bytelist.removeFirst();
////								Log.i("recordSound", "remove ok");
//								
//							}
							
							if(rec_in_bytequeue.size()>3)
							{
//								Log.i("recordSound", "begin remove");
								rec_in_bytequeue.remove();
							}
							/********************* speex ***********************************/
							int count = 0;

							if (rec_in_bytes.length % speexFrameSize == 0) {
								count = rec_in_bytes.length / speexFrameSize;
							} else {
								count = rec_in_bytes.length / speexFrameSize
										+ 1;
							}

							for (int i = 0; i < count; i++) {
								if (i != count - 1) {
									Log.i("recordSound", "begin add");
									System.arraycopy(rec_in_bytes, i
											* speexFrameSize, rec_in_buf, 0,
											rec_in_buf.length);
//									rec_in_bytelist.add(rec_in_buf);
									rec_in_bytequeue.add(rec_in_buf);
//									Log.i("recordSound", "add ok");
									
								} else {
									Log.i("recordSound", "begin add");
									System.arraycopy(rec_in_bytes, i
											* speexFrameSize, rec_in_buf, 0,
											rec_in_bytes.length - i
													* speexFrameSize);
//									rec_in_bytelist.add(rec_in_buf);
									rec_in_bytequeue.add(rec_in_buf);
//									Log.i("recordSound", "add ok");
								}
							}
							/********************* speex ***********************************/
						}

					} catch (Exception e) {
						// TODO: handle exception
						// Log.i("recordSound",e.getMessage());
						Log.i("recordSound", "error");
					}
					
				}
			}
		}

	}

	/**
	 * 描述 :发送线程
	 */
	class sendSound implements Runnable {
		@Override
		public void run() {
			while (workFlag) {
				if (recordFlag) {
//					if (!rec_in_bytelist.isEmpty()) 
					if(!rec_in_bytequeue.isEmpty())
					{
						try 
						{
//							SendAudioData(rec_in_bytelist.removeFirst());
							SendAudioData(rec_in_bytequeue.remove());
						} 
						catch (Exception e) 
						{
							Log.i("sendSound","error");
						}
						// Log.i("sendSound", "sendSound");
					}
				} else {
					// Log.i("sendSound","stop");
				}
			}
		}

	}

	/**
	 * 描述 :播放线程
	 */
	class playRecord implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// android.os.Process.setThreadPriority(
			// android.os.Process.THREAD_PRIORITY_AUDIO);

			// 开始播放
			trk_out.play();

			while (workFlag) {

				if (playFlag) {
					try {
						// if (udpRec_playin_bytelist.size() > 0)
						if (buf_playin_bytelist.size() > 0) {
							trk_out_bytes = buf_playin_bytelist.removeFirst();

							// trk_out_bytes =
							// udpRec_playin_bytelist.removeFirst();

							trk_out.write(trk_out_bytes, 0,
									trk_out_bytes.length);

							// Log.i("playRecord", "playRecord");

							// endDate = new Date(System.currentTimeMillis());
							// long diff=endDate.getTime()-startDate.getTime();
							// Log.i("playRecord", Long.toString(diff));
							// startDate = new Date(System.currentTimeMillis());

							// udpRec_playin_bytelist.removeFirst();
						} else {
							playFlag = false;

							// trk_out.write(blankData, 0, blankData.length);
							// endDate = new Date(System.currentTimeMillis());
							// long diff=endDate.getTime()-startDate.getTime();
							// Log.i("playRecord", Long.toString(diff));
							// startDate = new Date(System.currentTimeMillis());
						}
					} catch (Exception e) {
						e.printStackTrace();

						playFlag = false;

						Log.i("playRecord", "error");
					}
				} else {
					// trk_out.write(blankData, 0, blankData.length);
					// endDate = new Date(System.currentTimeMillis());
					// long diff=endDate.getTime()-startDate.getTime();
					// Log.i("playRecord", Long.toString(diff));
					// startDate = new Date(System.currentTimeMillis());
				}

			}
		}
	}

}
