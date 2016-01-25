package game.tutorial.c2d;

import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_Event_KeyPress;
import c2d.lang.app.C2D_Device;
import c2d.lang.app.C2D_Keys;

public interface LessonUtil extends C2D_Keys
{
	public static final int m_speed = 4;
	public static final C2D_Event_KeyPress m_moveEvent = new C2D_Event_KeyPress()
	{
		protected boolean doEvent(C2D_Widget carrier, int keyCode)
		{
			switch (keyCode)
			{
			case C2D_Device.key_left:
				carrier.setPosTo(carrier.m_x-m_speed, carrier.m_y);
				break;
			case C2D_Device.key_right:
				carrier.setPosTo(carrier.m_x+m_speed, carrier.m_y);
				break;
			case C2D_Device.key_up:
				carrier.setPosTo(carrier.m_x, carrier.m_y-m_speed);
				break;
			case C2D_Device.key_down:
				carrier.setPosTo(carrier.m_x, carrier.m_y+m_speed);
				break;
			default:
				break;
			}
//			switch (keyCode)
//			{
//			case C2D_Device.key_left:
//				carrier.setPosBy(-m_speed, 0);
//				break;
//			case C2D_Device.key_right:
//				carrier.setPosBy(m_speed, 0);
//				break;
//			case C2D_Device.key_up:
//				carrier.setPosBy(0, -m_speed);
//				break;
//			case C2D_Device.key_down:
//				carrier.setPosBy(0, m_speed);
//				break;
//			default:
//				break;
//			}
			return false;
		}
	};
}
