package c2d.frame.event;

import c2d.frame.base.C2D_Stage;

public abstract class C2D_Event_Stage extends C2D_Event
{
	/**
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ����̨����
	 * @param sceneState ��̨��״̬��־
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	public abstract boolean doEvent(C2D_Stage carrier,int stageState);

}
