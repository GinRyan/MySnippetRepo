package com.demo.sectionlistview;

import java.util.Arrays;

import android.widget.SectionIndexer;

/**
 * Section分段索引
 * 
 * @author Liang
 * 
 */
public class MySectionIndexer implements SectionIndexer {
	private final String[] mSections;// 分段字符数组
	private final int[] mPositions;// 分段位置索引值数组
	private final int mCount;// 计算总数量

	/**
	 * @param sections
	 * @param counts
	 */
	public MySectionIndexer(String[] sections, int[] counts) {
		if (sections == null || counts == null) {
			throw new NullPointerException();
		}
		if (sections.length != counts.length) {
			throw new IllegalArgumentException("The sections and counts arrays must have the same length");
		}
		this.mSections = sections;
		mPositions = new int[counts.length];
		int position = 0;
		for (int i = 0; i < counts.length; i++) {
			if (mSections[i] == null) {
				mSections[i] = "";
			} else {
				mSections[i] = mSections[i].trim();
			}

			mPositions[i] = position;
			position += counts[i];
		}
		mCount = position;
	}

	@Override
	public Object[] getSections() {
		return mSections;
	}

	/**
	 * 根据某段获取位置
	 */
	@Override
	public int getPositionForSection(int section) {
		// change by lcq 2012-10-12 section >mSections.length以为>=
		if (section < 0 || section >= mSections.length) {
			return -1;
		}
		System.out.println("lcq:section:" + section);
		return mPositions[section];
	}

	/**
	 * 提供该段的索引字符串
	 */
	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= mCount) {
			return -1;
		}
		// 注意这个方法的返回值，它就是index<0时，返回-index-2的原因
		// 解释Arrays.binarySearch，如果搜索结果在数组中，刚返回它在数组中的索引，如果不在，刚返回第一个比它大的索引的负数-1
		// 如果没弄明白，请自己想查看api
		int index = Arrays.binarySearch(mPositions, position);
		return index >= 0 ? index : -index - 2; // 当index小于0时，返回-index-2，

	}

}
