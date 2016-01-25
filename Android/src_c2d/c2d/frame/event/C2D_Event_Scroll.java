package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_Scroll extends C2D_Event
{
	/**
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ�������ڵ�
	 * @param itemIndex ���������¼�����ĿID
	 * @param scrollIn true������룬�������ø�����false����ʧȥ����
	 * @return �Ƿ�ִ�����
	 */
	protected abstract boolean doEvent(C2D_Widget carrier,int itemIndex,boolean scrollIn);

}
