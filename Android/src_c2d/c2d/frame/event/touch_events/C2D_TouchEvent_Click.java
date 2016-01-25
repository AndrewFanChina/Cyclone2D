package c2d.frame.event.touch_events;

import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_Event_Touch;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_TouchData;
/**
 * ����������ĵ���¼���
 * �����ǣ����û�����������е����ͬʱ��������������ɿ�ʱ���Żᴥ��click�¼���
 * ֻ�Ե�һ������Ӧ�����������Ч��ֻ��Ե�һ�ΰ��µİ���������
 * @author Andrew Fan
 *
 */
public abstract class C2D_TouchEvent_Click extends C2D_Event_Touch
{
	private boolean m_pressed=false;
	private boolean m_touchIn=false;
	private int m_pressedID=-1;
	public C2D_TouchEvent_Click(C2D_Widget carrier)
	{
		super(carrier);
	}
	@Override
	protected final boolean doEvent(C2D_TouchData tochData)
	{
		if(tochData==null||m_carrier==null)
		{
			return false;
		}
		if(!m_pressed)
		{
			if(tochData.m_touchCount==1)
			{
				m_pressedID = tochData.getFirstTouchID();
				if(m_pressedID>=0)
				{
					if(m_carrier.crossWithTouchPoint(tochData.getTouchPoint(m_pressedID)))
					{
						m_pressed = true;
						m_touchIn = true;
					}
				}
			}
		}
		else
		{
			C2D_PointF touchPoint = tochData.getTouchPoint(m_pressedID);
			if(touchPoint==null)
			{
				m_pressed = false;
				if(m_touchIn)
				{
					onClicked(m_carrier,touchPoint);
					m_touchIn = false;
				}
			}
			else
			{
				m_touchIn = m_carrier.crossWithTouchPoint(touchPoint);
			}

		}
		return false;
	}
	
	public abstract void onClicked(C2D_Widget widget,C2D_PointF point);


}
