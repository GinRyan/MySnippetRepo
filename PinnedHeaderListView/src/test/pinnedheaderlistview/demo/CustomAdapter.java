package test.pinnedheaderlistview.demo;

import java.util.List;

import test.pinnedheaderlistview.demo.PinnedHeaderListView.PinnedHeaderAdapter;
import test.pinnedheaderlistview.demo.PinnedHeaderListViewActivity.ItemEntity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter  
		implements OnScrollListener , PinnedHeaderAdapter {
	
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG = CustomAdapter.class.getSimpleName();
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Context mContext;
	private List<ItemEntity> mData;
	private LayoutInflater mLayoutInflater;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public CustomAdapter(Context pContext, List<ItemEntity> pData) {
		mContext = pContext;
		mData = pData;
		
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	 // 常见的优化ViewHolder
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.listview_item, null);
            
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title); 
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.contentIcon = (ImageView) convertView.findViewById(R.id.content_icon);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取数据
        ItemEntity itemEntity = (ItemEntity) getItem(position);
        viewHolder.content.setText(itemEntity.getContent());
        viewHolder.contentIcon.setImageResource(R.drawable.ic_launcher);

        if ( needTitle(position) ) {
            // 显示标题并设置内容 
            viewHolder.title.setText(itemEntity.getTitle());
            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);
        }
        
        return convertView;
	}
	
	@Override
	public int getCount() {
		if (null != mData) {
			return mData.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null != mData && position < getCount()) {
			return mData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if ( view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);	
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	
	
	@Override
	public int getPinnedHeaderState(int position) {
		if (getCount() == 0 || position < 0) {
			return PinnedHeaderAdapter.PINNED_HEADER_GONE;
		}
		
		if (isMove(position) == true) {
			return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
		}
		
		return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
	}


	@Override
	public void configurePinnedHeader(View headerView, int position, int alpaha) {
		// 设置标题的内容
		ItemEntity itemEntity = (ItemEntity) getItem(position);
		String headerValue = itemEntity.getTitle();
		
		Log.e(TAG, "header = " + headerValue);
		
		if (!TextUtils.isEmpty(headerValue)) {
			TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
			headerTextView.setText( headerValue );
		}
		
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * 判断是否需要显示标题
	 * 
	 * @param position
	 * @return
	 */
	private boolean needTitle(int position) {
		// 第一个肯定是分类
		if (position == 0) {
			return true;
		}
		
		// 异常处理
        if (position < 0) {
            return false;
        }
		 
		// 当前  // 上一个
		ItemEntity currentEntity = (ItemEntity) getItem(position);
		ItemEntity previousEntity = (ItemEntity) getItem(position - 1);
		if (null == currentEntity || null == previousEntity) {
            return false;
        }
		
		String currentTitle = currentEntity.getTitle();
		String previousTitle = previousEntity.getTitle();
		if (null == previousTitle || null == currentTitle) {
            return false;
        }
		
		// 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
		if (currentTitle.equals(previousTitle)) {
			return false;
		}
		
		return true;
	}


	private boolean isMove(int position) {
	    // 获取当前与下一项
	    ItemEntity currentEntity = (ItemEntity) getItem(position);
	    ItemEntity nextEntity = (ItemEntity) getItem(position + 1);
	    if (null == currentEntity || null == nextEntity) {
            return false;
        }

	    // 获取两项header内容
	    String currentTitle = currentEntity.getTitle();
	    String nextTitle = nextEntity.getTitle();
	    if (null == currentTitle || null == nextTitle) {
            return false;
        }
	    
	    // 当前不等于下一项header，当前项需要移动了
	    if (!currentTitle.equals(nextTitle)) {
            return true;
        }
	    
	    return false;
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	private class ViewHolder {
        TextView title;
        TextView content;
        ImageView contentIcon;
    }
	
}
