package c2d.frame.event;

import c2d.frame.com.list.scroll.C2D_ScrollView;

public abstract class C2D_Event_ClickItem extends C2D_Event
{
	/**
	 * �¼�ִ�к���
	 * @param scrollView �¼����壬������һ��������ͼ
	 * @param itemID �����������ĿID
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	protected abstract boolean doEvent(C2D_ScrollView scrollView,int itemID);

}
