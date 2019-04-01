package com.ltnw.ltcloud.adapter;

import java.util.List;

import com.ltnw.entity.DragGridViewItem;
import com.ltnw.entity.SlideSwitchItem;
import com.ltnw.ltcloud.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DragGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<DragGridViewItem> dragViewItems;
	private List<SlideSwitchItem> switchItems;
	
	private int nextPosition=0;
	private FrameLayout layout=null;
	private ImageView imgIcon=null;
	private TextView txtTitle=null;

	
	public DragGridViewAdapter(Context context, 
			List<DragGridViewItem> dragViewItems,
			List<SlideSwitchItem> switchItems)
	{
		this.context = context;
		this.dragViewItems = dragViewItems;
		this.switchItems = switchItems;
		
		for (DragGridViewItem item : dragViewItems) {
			Log.i("DragViewItem", item.getTitle());
		}
	}

    public void refresh(List<DragGridViewItem> d) 
    {    
    		dragViewItems = d;    
            notifyDataSetChanged();    
    }  
	
	@Override
	public int getCount() {
		return dragViewItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return dragViewItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		

		Log.i("getView", String.format("position:%d", position));
		
		if(position>dragViewItems.size()-1)
		{
			return convertView;
		}
		
		if (convertView == null) 
		{
			if(position==0)
			{
				if(nextPosition==0)
				{
					nextPosition++;
				}
				else
				{
					position=nextPosition;
					
					if(nextPosition+1<dragViewItems.size())
					{
						nextPosition++;
					}
				}
			}
			else
			{
				nextPosition++;
			}
				
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.draggrid_list_item, null);
 
            
            
        }
         
		
		layout = (FrameLayout) convertView.findViewById(R.id.draggrid_layout);
        imgIcon = (ImageView) convertView.findViewById(R.id.draggrid_item_image);
        txtTitle = (TextView) convertView.findViewById(R.id.draggrid_item_text);

        imgIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        try {
        	imgIcon.setImageResource(dragViewItems.get(position).getIcon());        
            txtTitle.setText(dragViewItems.get(position).getTitle());
            
		} catch (Exception e) {
			// TODO: handle exception
		}
        

        
//		for (SlideSwitchItem item : switchItems) 
//		{
//			Log.i("switch", String.format("id:%d,position:%d,title:%s,Stitle:%s", 
//					item.getId(),
//					position,
//					dragViewItems.get(position).getTitle(),
//					item.getTitleN()));
//			
//			
//			if (item.getTitleN().equals(dragViewItems.get(position).getTitle())) 
//			{
//				if(item.getGroupN()==position)
//				{
//					if(!item.isInitF())
//					{
//						layout.addView(item.switch1);
//						Log.i("switch add", String.format("position:%d", position));
//						item.setInitF(true);
//					}
//				}
//				else
//				{
//					
//				}
//				
//
//				
//				
//			}
//			else
//			{
//				if(item.getGroupN()==position)
//				{
//					if(item.isInitF())
//					{
//						layout.removeView(item.switch1);
//						Log.i("switch remove", String.format("position:%d", position));				
//						item.setInitF(false);
//					}
//				}
//
//			}
//			
//
//		}

        return convertView;
		
		
	}
}
