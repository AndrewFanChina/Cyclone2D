package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_TouchData;
/**
 * �����¼���ע��������¼���ͬ�������¼���Ӧ�ñ�������ͬʱʹ�á�
 * �������ݽ��ụ��Ӱ�죬��ɴ��ҡ���˻����У�Ҳ�����˵�ǰ������
 * ���󡣿���ʹ��setListenOn���л������������޷�ͬʱ�����������
 * ���������͵��¼��ǿ���ͬʱ��������������ʹ�õġ���������ͬʱ������
 * ���������Ϊ���������¼��ڲ����������ڸ��ӵ�˽�����ݹ�ϵ��
 * @author AndrewFan
 *
 */
public abstract class C2D_Event_Touch extends C2D_Event
{
	protected C2D_Widget m_carrier;
	protected C2D_Event_Touch(C2D_Widget carrier)
	{
		setListenOn(carrier);
	}
	public void setListenOn(C2D_Widget carrier)
	{
		m_carrier=carrier;
	}
	/**
	 * �¼�ִ�к���
	 * @param carrier �¼����壬������һ�������ڵ�
	 * @param tochData ������Ϣ
	 * @return ���¼��Ƿ���ᣬ��������Ҫ��Ӧ
	 */
	protected abstract boolean doEvent(C2D_TouchData tochData);

}
