/**
The MIT License (MIT)

Copyright (c) 2014 singwhatiwanna
https://github.com/singwhatiwanna
http://blog.csdn.net/singwhatiwanna

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.ltnw.common.controls;

import java.util.NoSuchElementException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;


public class StickyLayout extends LinearLayout {
    private static final String TAG = "StickyLayout";
    private static final boolean DEBUG = true;

    public interface OnGiveUpTouchEventListener {
        public boolean giveUpTouchEvent(MotionEvent event);
    }

    private View mHeader=null;
    private View mContent=null;
    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

    // header鐨勯珮搴� 鍗曚綅锛歱x
    private int mOriginalHeaderHeight;
    private int mHeaderHeight;

    private int mStatus = STATUS_EXPANDED;
    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;

    private int mTouchSlop;

    // 鍒嗗埆璁板綍涓婃婊戝姩鐨勫潗鏍�    private int mLastX = 0;
    private int mLastY = 0;

    // 鍒嗗埆璁板綍涓婃婊戝姩鐨勫潗鏍�onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    // 鐢ㄦ潵鎺у埗婊戝姩瑙掑害锛屼粎褰撹搴婊¤冻濡備笅鏉′欢鎵嶈繘琛屾粦鍔細tan a = deltaX / deltaY > 2
    private static final int TAN = 2;

    private boolean mIsSticky = true;
	private int mLastX=0;
	private int destHeight=0;

    public StickyLayout(Context context) {
        super(context);

    }

    public StickyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void InitData()
    {
    	if((mHeader == null || mContent == null))
    	{
    		initData();
    	}
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StickyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && (mHeader == null || mContent == null)) {
            initData();
        }
    }

    private void initData() {
        int headerId= getResources().getIdentifier("header", "id", getContext().getPackageName());
        int contentId = getResources().getIdentifier("content", "id", getContext().getPackageName());
        Log.i(TAG, String.format("headerId:%d contentId:%d", headerId,contentId));
        
        if (headerId != 0 && contentId != 0) {
            mHeader = findViewById(headerId);
            mContent = findViewById(contentId);
            mOriginalHeaderHeight = mHeader.getMeasuredHeight();
            mHeaderHeight = mOriginalHeaderHeight;
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            if (DEBUG) {
                Log.d(TAG, "mTouchSlop = " + mTouchSlop);
            }
        } else {
            throw new NoSuchElementException("Did your view with id \"header\" or \"content\" exists?");
        }
    }

    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            mLastXIntercept = x;
            mLastYIntercept = y;
            mLastX = x;
            mLastY = y;
            intercepted = 0;
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastXIntercept;
            int deltaY = y - mLastYIntercept;
            if (mStatus == STATUS_EXPANDED && deltaY <= -mTouchSlop) {
                intercepted = 1;
            } else if (mGiveUpTouchEventListener != null) {
                if (mGiveUpTouchEventListener.giveUpTouchEvent(event) && deltaY >= mTouchSlop) {
                    intercepted = 1;
                }
            }
            break;
        }
        case MotionEvent.ACTION_UP: {
            intercepted = 0;
            mLastXIntercept = mLastYIntercept = 0;
            break;
        }
        default:
            break;
        }

        if (DEBUG) {
            Log.d(TAG, "intercepted=" + intercepted);
        }
        return intercepted != 0 && mIsSticky;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastX;
            int deltaY = y - mLastY;
            if (DEBUG) {
                Log.d(TAG, "mHeaderHeight=" + mHeaderHeight + "  deltaY=" + deltaY + "  mlastY=" + mLastY);
            }
            mHeaderHeight += deltaY;
            setHeaderHeight(mHeaderHeight);
            break;
        }
        case MotionEvent.ACTION_UP: {
            // 杩欓噷鍋氫簡涓嬪垽鏂紝褰撴澗寮�墜鐨勬椂鍊欙紝浼氳嚜鍔ㄥ悜涓よ竟婊戝姩锛屽叿浣撳悜鍝竟婊戯紝瑕佺湅褰撳墠鎵�鐨勪綅缃�            int destHeight = 0;
            if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
                destHeight = 0;
                mStatus = STATUS_COLLAPSED;
            } else {
                destHeight = mOriginalHeaderHeight;
                mStatus = STATUS_EXPANDED;
            }
            // 鎱㈡參婊戝悜缁堢偣
            this.smoothSetHeaderHeight(mHeaderHeight, destHeight, 500);
            break;
        }
        default:
            break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public void smoothSetHeaderHeight(final int from, final int to, long duration) {
        smoothSetHeaderHeight(from, to, duration, false);
    }

    public void smoothSetHeaderHeight(final int from, final int to, long duration, final boolean modifyOriginalHeaderHeight) {
        final int frameCount = (int) (duration / 1000f * 30) + 1;
        final float partation = (to - from) / (float) frameCount;
        new Thread("Thread#smoothSetHeaderHeight") {

            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    final int height;
                    if (i == frameCount - 1) {
                        height = to;
                    } else {
                        height = (int) (from + partation * i);
                    }
                    post(new Runnable() {
                        public void run() {
                            setHeaderHeight(height);
                        }
                    });
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (modifyOriginalHeaderHeight) {
                    setOriginalHeaderHeight(to);
                }
            };

        }.start();
    }

    public void setOriginalHeaderHeight(int originalHeaderHeight) {
        mOriginalHeaderHeight = originalHeaderHeight;
    }

    public void setHeaderHeight(int height, boolean modifyOriginalHeaderHeight) {
        if (modifyOriginalHeaderHeight) {
            setOriginalHeaderHeight(height);
        }
        setHeaderHeight(height);
    }

    public void setHeaderHeight(int height) {
        if (DEBUG) {
            Log.d(TAG, "setHeaderHeight height=" + height);
        }
        if (height < 0) {
            height = 0;
        } else if (height > mOriginalHeaderHeight) {
            height = mOriginalHeaderHeight;
        }

        if (mHeader != null && mHeader.getLayoutParams() != null) {
            mHeader.getLayoutParams().height = height;
            mHeader.requestLayout();
            mHeaderHeight = height;
        } else {
            if (DEBUG) {
                Log.w(TAG, "null LayoutParams when setHeaderHeight");
            }
        }
    }

    public void setSticky(boolean isSticky) {
        mIsSticky = isSticky;
    }

}
