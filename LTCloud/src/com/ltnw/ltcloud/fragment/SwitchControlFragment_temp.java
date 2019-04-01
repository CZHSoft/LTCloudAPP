package com.ltnw.ltcloud.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ltnw.common.controls.DragGridView;
import com.ltnw.common.controls.DragGridView.OnChanageListener;
import com.ltnw.common.controls.Panel;
import com.ltnw.common.controls.PinnedHeaderExpandableListView;
import com.ltnw.common.controls.SlideSwitch;
import com.ltnw.common.controls.StickyLayout;
import com.ltnw.entity.DragGridViewItem;
import com.ltnw.entity.Group;
import com.ltnw.entity.People;
import com.ltnw.entity.SlideSwitchItem;
import com.ltnw.ltcloud.R;
import com.ltnw.ltcloud.adapter.DragGridViewAdapter;
import com.ltnw.ltcloud.fragment.SettingFragment.OnSettingButtonClickListener;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchControlFragment_temp extends Fragment
{
	
	private Context context;
	private LayoutInflater inflaterT;
	private OnSwitchButtonClickListener mListener;
	
	private DragGridViewAdapter dragGridViewAdapter=null;
	private ArrayList<DragGridViewItem> mDragGridViewItem;
	private ArrayList<SlideSwitchItem> mSlideSwitchItems;
	
	private static final String[] positions={
		"厕所1","房间1","厨房1","阳台1","大厅1","..."};  
	
	private Panel panel;
	public LinearLayout container;
	public DragGridView gridview;
	private LinearLayout listLayout;
	
    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;
    private ArrayList<Group> groupList;
    private ArrayList<List<People>> childList;
    private MyexpandableListAdapter adapter;
    
	public interface OnSwitchButtonClickListener 
	{
		public void OnSwitchButtonClick(String switchName,int switchValue);//接口中定义一个方法
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mListener = (OnSwitchButtonClickListener) activity;// 这句就是赋初值了。
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ "must implement OnbtnSendClickListener");// 这条表示，你不在Activity里实现这个接口的话，我就要抛出异常咯。知道下一步该干嘛了吧？
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.main, container, false);
        
        context=rootView.getContext();
        inflaterT=inflater;
//        findView(rootView);
       
        expandableListView = (PinnedHeaderExpandableListView) rootView.findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout)rootView.findViewById(R.id.sticky_layout);
        if(stickyLayout!=null)
        {
//        	stickyLayout.InitData();
        }
        
        initData();

        adapter = new MyexpandableListAdapter(rootView.getContext());
        expandableListView.setAdapter(adapter);

        // 展开所有group
//        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
//            expandableListView.expandGroup(i);
//        }

        expandableListView.setOnHeaderUpdateListener(new PinnedHeaderExpandableListView.OnHeaderUpdateListener() {
			
			@Override
		    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		        Group firstVisibleGroup = (Group) adapter.getGroup(firstVisibleGroupPos);
		        TextView textView = (TextView) headerView.findViewById(R.id.group);
		        textView.setText(firstVisibleGroup.getTitle());
		    }
			
			@Override
			public View getPinnedHeader() {
				// TODO Auto-generated method stub
		        View headerView = (ViewGroup) inflaterT.inflate(R.layout.group, null);
		        headerView.setLayoutParams(new LayoutParams(
		                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		        return headerView;
			}
			
		});
        
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
			
			@Override
		    public boolean giveUpTouchEvent(MotionEvent event) {
		        if (expandableListView.getFirstVisiblePosition() == 0) {
		            View view = expandableListView.getChildAt(0);
		            if (view != null && view.getTop() >= 0) {
		                return true;
		            }
		        }
		        return false;
		    }
		});
        

        return rootView;
    }
	
	private void findView(View rootView) 
	{
        container=(LinearLayout)rootView.findViewById(R.id.switch_layout);
        gridview=null;
//        		(DragGridView)rootView.findViewById(R.id.switch_dragGridView);
        panel=new Panel(
        		rootView.getContext(),
        		gridview,
        		rootView.getWidth()*4/5,
        		LayoutParams.WRAP_CONTENT);
        
        container.addView(panel);
        
        listLayout=new LinearLayout(rootView.getContext());
		listLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams = new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		listLayout.setLayoutParams(layoutParams);
		
		
		LinearLayout tempLayout=new LinearLayout(rootView.getContext());
		tempLayout.setOrientation(LinearLayout.HORIZONTAL);

    	TextView txtView1 = new TextView(rootView.getContext());
    	
    	txtView1.setText("开关1"+":");
    	txtView1.setTag("lable1");
    	txtView1.setPadding(3, 3, 3, 3);

    	Spinner spinner=new Spinner(rootView.getContext());
    	ArrayAdapter<String> adapter1=new ArrayAdapter<String>(
    			rootView.getContext(),
    			android.R.layout.simple_spinner_item,
    			positions); 
    	adapter1.setDropDownViewResource(
    			android.R.layout.simple_spinner_dropdown_item);  
    	spinner.setAdapter(adapter1);
    	
		SlideSwitch switch1 =new SlideSwitch(rootView.getContext());
		switch1.setText("开", "关");

		tempLayout.addView(txtView1);
		tempLayout.addView(spinner);
		tempLayout.addView(switch1);
    	
		SlideSwitch switch2 =new SlideSwitch(rootView.getContext());
		switch2.setText("开", "关");
		switch2.setLayoutParams(
				new LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		
		SlideSwitch switch3 =new SlideSwitch(rootView.getContext());
		switch3.setText("开", "关");
		switch3.setLayoutParams(
				new LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		
		SlideSwitch switch4 =new SlideSwitch(rootView.getContext());
		switch4.setText("开", "关");
		switch4.setLayoutParams(
				new LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		
		listLayout.addView(tempLayout);
		listLayout.addView(switch2);
		listLayout.addView(switch3);
		listLayout.addView(switch4);
		
		//鍔犲叆鍒癙anel閲岄潰
		panel.fillPanelContainer(listLayout);

//		panel.setPanelClosedEvent(panelClosedEvent);
//		panel.setPanelOpenedEvent(panelOpenedEvent);

		mSlideSwitchItems=new ArrayList<SlideSwitchItem>();
		mSlideSwitchItems.add(new SlideSwitchItem(rootView.getContext(),10,"厕所1","开关1",0,1));
		
		mDragGridViewItem=new ArrayList<DragGridViewItem>();
		
		mDragGridViewItem.add(new DragGridViewItem("厕所1",
				R.drawable.switch_toilet));
		
		mDragGridViewItem.add(new DragGridViewItem("房间1",
				R.drawable.switch_room));

		mDragGridViewItem.add(new DragGridViewItem("厨房1",
				R.drawable.switch_kitchen));
		
		mDragGridViewItem.add(new DragGridViewItem("阳台1",
				R.drawable.switch_balcony));
		
		mDragGridViewItem.add(new DragGridViewItem("大厅1",
				R.drawable.switch_hall));
		
//		mDragViewItems.add(new DragViewItem("添加",
//				R.drawable.add));
		
		DragGridView mDragGridView = null;
//				(DragGridView) rootView.findViewById(R.id.switch_dragGridView);

		dragGridViewAdapter=new DragGridViewAdapter(rootView.getContext(),mDragGridViewItem,mSlideSwitchItems);
		
		mDragGridView.setAdapter(dragGridViewAdapter);
		
		mDragGridView.setOnChangeListener(new OnChanageListener() {
			
			@Override
			public void onChange(int from, int to) {

				
				
//				mDragViewItems.set(from, mDragViewItems.get(to));
//				mDragViewItems.set(to, item);
				
				DragGridViewItem item=mDragGridViewItem.get(from);
				
				if (from < to) {
					for (int i = from; i < to; i++) {
						Collections.swap(mDragGridViewItem, i, i + 1);
						Log.i("swap", String.format("%d-->%d", i,i+1));
					}
				} else if (from > to) {
					for (int i = from; i > to; i--) {
						Collections.swap(mDragGridViewItem, i, i - 1);
						Log.i("swap", String.format("%d-->%d", i,i-1));
					}
				}
				
				mDragGridViewItem.set(to, item);
				
				
//				for (DragViewItem item1 : mDragViewItems) {
//					Log.i("DragViewItem", item1.getTitle());
//				}
				
				
				dragGridViewAdapter.notifyDataSetChanged();
				
			}
		});
		
		
	}
	
	
    /***
     * InitData
     */
    void initData() {
        groupList = new ArrayList<Group>();
        Group group = null;
        for (int i = 0; i < 3; i++) {
            group = new Group();
            group.setTitle("group-" + i);
            groupList.add(group);
        }

        childList = new ArrayList<List<People>>();
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<People> childTemp;
            if (i == 0) {
                childTemp = new ArrayList<People>();
                for (int j = 0; j < 13; j++) {
                    People people = new People();
                    people.setName("yy-" + j);
                    people.setAge(30);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else if (i == 1) {
                childTemp = new ArrayList<People>();
                for (int j = 0; j < 8; j++) {
                    People people = new People();
                    people.setName("ff-" + j);
                    people.setAge(40);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else {
                childTemp = new ArrayList<People>();
                for (int j = 0; j < 23; j++) {
                    People people = new People();
                    people.setName("hh-" + j);
                    people.setAge(20);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            }
            childList.add(childTemp);
        }

    }
	
    /***
     * 数据源
     * 
     * @author Administrator
     * 
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            groupHolder.textView.setText(((Group) getGroup(groupPosition))
                    .getTitle());
            if (isExpanded)// ture is Expanded or false is not isExpanded
                groupHolder.imageView.setImageResource(R.drawable.expanded);
            else
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.child, null);

                childHolder.textName = (TextView) convertView
                        .findViewById(R.id.name);
                childHolder.textAge = (TextView) convertView
                        .findViewById(R.id.age);
                childHolder.textAddress = (TextView) convertView
                        .findViewById(R.id.address);
                childHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                Button button = (Button) convertView
                        .findViewById(R.id.sign_in_setting);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(, "clicked pos=", Toast.LENGTH_SHORT).show();
                    }
                });

                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }

            childHolder.textName.setText(((People) getChild(groupPosition,
                    childPosition)).getName());
            childHolder.textAge.setText(String.valueOf(((People) getChild(
                    groupPosition, childPosition)).getAge()));
            childHolder.textAddress.setText(((People) getChild(groupPosition,
                    childPosition)).getAddress());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView textName;
        TextView textAge;
        TextView textAddress;
        ImageView imageView;
    }









	
}
