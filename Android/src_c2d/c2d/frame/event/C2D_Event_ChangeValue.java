package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_ChangeValue extends C2D_Event
{
	/**
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ�������ڵ�
	 * @param newValue �仯���ֵ
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	protected abstract boolean doEvent(C2D_Widget carrier,int newValue);
}
