package com.demo.sectionlistview;

import java.util.HashMap;
import java.util.Map;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.demo.sectionlistview.PinnedHeaderListView.PinnedHeaderAdapter;
import com.demo.sectionlistview.SectionListActivity.StandardArrayAdapter;

/**
 * Adapter for sections.
 */
public class SectionListAdapter implements ListAdapter, 
			OnItemClickListener, PinnedHeaderAdapter, SectionIndexer, OnScrollListener{
	private SectionIndexer mIndexer;
	private String[] mSections;//所有分组的名字
	private int[] mCounts;//所有分组的个数
	private int mSectionCounts = 0; 
    private final DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateTotalCount();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            updateTotalCount();
        };
    };

    private final StandardArrayAdapter linkedAdapter;
//    private final Map<Integer, String> sectionPositions = new LinkedHashMap<Integer, String>();
//    private final Map<Integer, Integer> itemPositions = new LinkedHashMap<Integer, Integer>();
    private final Map<String, View> currentViewSections = new HashMap<String, View>();
    private int viewTypeCount;
    protected final LayoutInflater inflater;

    private View transparentSectionView;

    private OnItemClickListener linkedListener;
    
    public SectionListAdapter(final LayoutInflater inflater,
            final StandardArrayAdapter linkedAdapter) {
        this.linkedAdapter = linkedAdapter;
        this.inflater = inflater;
        linkedAdapter.registerDataSetObserver(dataSetObserver);
        updateTotalCount();
        mIndexer = new MySectionIndexer(mSections, mCounts);
    }

    private boolean isTheSame(final String previousSection,
            final String newSection) {
        if (previousSection == null) {
            return newSection == null;
        } else {
            return previousSection.equals(newSection);
        }
    }

    //add by lcq:2011-12-17 模拟得到indexer的数据
    private void fillSections() {
    	mSections = new String[mSectionCounts];
    	mCounts = new int[mSectionCounts];
    	final int count = linkedAdapter.getCount();
    	String currentSection = null;
    	int newSectionIndex = 0;
    	int newSectionCounts = 0;
    	String previousSection = null;
    	for (int i = 0; i < count; i++) {
    		newSectionCounts++;
    		currentSection = linkedAdapter.items[i].section;
    		if (!isTheSame(previousSection, currentSection)) {
    			mSections[newSectionIndex] = currentSection;
    			previousSection = currentSection;
    			if (newSectionIndex == 1) {//如果是首次开始，则减1(因为第一次进入循环时，前一个为空，相当于indexCount多加了一次)
    				mCounts[0] = newSectionCounts-1;
    			} else if(newSectionIndex != 0){
    				mCounts[newSectionIndex-1] = newSectionCounts;
    			}
    			if(i != 0) {//首次进入，计数不置0，其他情况，重新计数
    				newSectionCounts = 0;
    			}
    			newSectionIndex++;
    		} else if(i == count-1){//如果是最后一个,因为进入的时候把newSectionCounts置为0，下次不会计数，少加了一次
            	mCounts[newSectionIndex-1] = newSectionCounts+1;
            }
    		
    	}
//    	for(String a : mSections) {
//    		System.out.println(a);
//    	}
//    	for(int a : mCounts)
//    		System.out.println(a);
    }
    
    private synchronized void updateTotalCount() {
        String currentSection = null;
        viewTypeCount = linkedAdapter.getViewTypeCount() + 1;
        final int count = linkedAdapter.getCount();
        for (int i = 0; i < count; i++) {
            final SectionListItem item = (SectionListItem) linkedAdapter.getItem(i);
            if (!isTheSame(currentSection, item.section)) {
            	mSectionCounts++;
            	currentSection = item.section;
            }
        }
        fillSections();
    }

    @Override
    public synchronized int getCount() {
//        return sectionPositions.size() + itemPositions.size();
        return linkedAdapter.getCount();
    }

    @Override
    public synchronized Object getItem(final int position) {
//        if (isSection(position)) {
//            return sectionPositions.get(position);
//        } else {
            final int linkedItemPosition = getLinkedPosition(position);
            return linkedAdapter.getItem(linkedItemPosition);
//        }
    }

//    public synchronized boolean isSection(final int position) {
//        return sectionPositions.containsKey(position);
//    }

    public synchronized String getSectionName(final int position) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
    	return linkedAdapter.getItemId(getLinkedPosition(position));
    }

    protected Integer getLinkedPosition(final int position) {
        return position;
    }

    @Override
    public int getItemViewType(final int position) {
        return linkedAdapter.getItemViewType(getLinkedPosition(position));
    }

    private View getSectionView(final View convertView, final String section) {
        View theView = convertView;
        if (theView == null) {
//            theView = createNewSectionView();
        }
        setSectionText(section, theView);
        replaceSectionViewsInMaps(section, theView);
        return theView;
    }

    protected void setSectionText(final String section, final View sectionView) {
        final TextView textView = (TextView) sectionView.findViewById(R.id.header);
        textView.setText(section);
    }

    protected synchronized void replaceSectionViewsInMaps(final String section,
    		final View theView) {
        if (currentViewSections.containsKey(theView)) {
            currentViewSections.remove(theView);
        }
        currentViewSections.put(section, theView );
    }

//    protected View createNewSectionView() {
//        return inflater.inflate(R.layout.section_view, null);
//    }

    @Override
    public View getView(final int position, final View convertView,
            final ViewGroup parent) {
//        if (isSection(position)) {
//            return getSectionView(convertView, sectionPositions.get(position));
//        }
//    	int section = getRealPosition(position);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.section_list_item, null);
        }
        final SectionListItem currentItem = linkedAdapter.items[position];
        if (currentItem != null) {//to set every item's text
        	final TextView header = (TextView) view.findViewById(R.id.header);
            final TextView textView = (TextView) view.findViewById(R.id.example_text_view);
            if (textView != null) {
                textView.setText(currentItem.item.toString());
            }
            if (header != null) {
            	header.setText(currentItem.section);
            }
			int section = getSectionForPosition(position);
            if (getPositionForSection(section) == position){
        		//显示标题
            	view.findViewById(R.id.header_parent).setVisibility(View.VISIBLE);
            	header.setVisibility(View.VISIBLE);
        	} else {
        		//隐藏标题
        		view.findViewById(R.id.header_parent).setVisibility(View.GONE);
        		header.setVisibility(View.GONE);
        	}
            
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return viewTypeCount;
    }

    @Override
    public boolean hasStableIds() {
        return linkedAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return linkedAdapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        linkedAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        linkedAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return linkedAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(final int position) {
        return linkedAdapter.isEnabled(getLinkedPosition(position));
    }


    public int getRealPosition(int pos) {
    	return pos-1;
    }
    
    public synchronized View getTransparentSectionView() {
        if (transparentSectionView == null) {
//            transparentSectionView = createNewSectionView();
        }
        return transparentSectionView;
    }

    protected void sectionClicked(final String section) {
        // do nothing
    }

    @Override
    public void onItemClick(final AdapterView< ? > parent, final View view,
            final int position, final long id) {
         if (linkedListener != null) {
            linkedListener.onItemClick(parent, view,getLinkedPosition(position), id);
         }
        
    }

    public void setOnItemClickListener(final OnItemClickListener linkedListener) {
        this.linkedListener = linkedListener;
    }
    
	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;//这里没什么别的意思，主要是通讯录中，listview中第一个显示的是所有的联系人，
									//不是真实的数据，所以会-1,这里偷懒，直接把后面的去掉，还有其他有类似的地方，原因一样
		if (mIndexer == null) {
			return PINNED_HEADER_GONE;
		}
		if (realPosition < 0) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);//得到此item所在的分组位置
		int nextSectionPosition = getPositionForSection(section + 1);//得到下一个分组的位置
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
        int realPosition = position;
        int section = getSectionForPosition(realPosition);

        String title = (String)mIndexer.getSections()[section];
        ((TextView)header.findViewById(R.id.header_text)).setText(title);
    
		
	}

	@Override
	public Object[] getSections() {
		if (mIndexer == null) {
			return new String[] { "" };
		} else {
			return mIndexer.getSections();
		}
	}

	@Override
	public int getPositionForSection(int section) {
		if (mIndexer == null) {
			return -1;
		}
		return mIndexer.getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (mIndexer == null) {
			return -1;
		}
		return mIndexer.getSectionForPosition(position);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
    
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
            if(view instanceof PinnedHeaderListView) {
            	((PinnedHeaderListView)view).configureHeaderView(firstVisibleItem);
            }
		
	}
    
    
}
