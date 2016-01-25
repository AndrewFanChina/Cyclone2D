package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_WaitTime extends C2D_Event
{
	/** �ȴ�ʱ�䣬����*/
	public int m_waitTime;
	/** �ȴ�ʱ�䣬����*/
	public int m_passedTime;
	public C2D_Event_WaitTime(int waitTime)
	{
		m_waitTime=waitTime;
		m_passedTime=0;
	}
	/**
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ�������ڵ�
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	protected abstract boolean doEvent(C2D_Widget carrier);

}
