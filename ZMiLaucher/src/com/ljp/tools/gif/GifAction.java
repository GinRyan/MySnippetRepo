package com.ljp.tools.gif;

public abstract interface GifAction {

	/**
	 * gif����۲���
	 * @param parseStatus �����Ƿ�ɹ����ɹ���Ϊtrue
	 * @param frameIndex ��ǰ����ĵڼ�֡����ȫ������ɹ�������Ϊ-1
	 */
	public abstract void parseOk(boolean parseStatus,int frameIndex);
}

