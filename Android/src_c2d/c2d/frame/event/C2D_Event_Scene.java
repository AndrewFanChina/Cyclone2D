package c2d.frame.event;

import c2d.frame.base.C2D_Scene;

public abstract class C2D_Event_Scene extends C2D_Event
{
	/**
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ�������ڵ�
	 * @param sceneState ������״̬��־
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	protected abstract boolean doEvent(C2D_Scene carrier,int sceneState);

}
