package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_ChangeFocus extends C2D_Event
{
	/**
	 * 
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ�������ڵ�
	 * @param focused ��û���ʧȥ����
	 * @param another ����һ����û���ʧȥ����������
	 * ��������ý��㣬��another��ԭ���㣬�������ʧȥ����,��another��Ŀ�꽹��
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	protected abstract boolean doEvent(C2D_Widget carrier,boolean focused,C2D_Widget another);
}
