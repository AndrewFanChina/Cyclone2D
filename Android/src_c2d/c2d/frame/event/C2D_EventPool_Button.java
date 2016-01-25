package c2d.frame.event;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Array;
import c2d.mod.C2D_Consts;

public class C2D_EventPool_Button extends C2D_EventPool implements C2D_Consts
{
	public C2D_EventPool_Button(C2D_Widget carrier)
	{
		super(carrier);
	}
	/**
	 * �����¼����������ظ�
	 * @param event �¼�
	 * @return �Ƿ�ɹ����ӣ�false��ʾ�����ظ���δ��ִ������
	 */
	public boolean add(C2D_Event_Button event)
	{
		return super.add(event);
	}
	/** ��ť״̬ */
	protected int m_BtnState;
	/** ��ťĳ״̬�����й���֡�� */
	protected int m_StateFrame;
	/**
	 * ���ð�ť״̬
	 * 
	 * @param state
	 *            �µİ�ť״̬
	 */
	protected void setBtnState(int state)
	{
		if (m_BtnState!= state)
		{
			m_BtnState = state;
			m_StateFrame=0;
		}
		if (m_BtnState != Btn_Float)
		{
			onCalled(m_BtnState);
		}
		refreshBtnState();
	}
	/**
	 * ��ϵͳ����ִ���¼���ִ��֮����Զ�ɾ��ִ����ϵ��¼�
	 * @param btnState ��ť״̬0,1,2,3�ֱ��Ӧ���¿�ʼ�����½������ͷſ�ʼ���ͷŽ���
	 */
	private final void onCalled(int btnState)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			for(int i=0;i<events.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)events.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_Button event = (C2D_Event_Button)message.m_event;
					if(event.doEvent(message.m_carrier,btnState))
					{
						events.removeElementAt(i);
						i--;
					}
				}

			}
		}
	}

	/**
	 * ����ť��Ӧ
	 * 
	 * @param stage
	 */
	protected void processBtnCalls2(C2D_Stage stage)
	{
		if (stage == null)
		{
			return;
		}
		int keyNow = stage.getSingleKey();
		boolean passCheck;
		switch (m_BtnState)
		{
		case Btn_Focused:
			if (!Plat_Tochable)
			{
				passCheck = m_focused && keyNow == C2D_Device.key_enter;
			}
			else
			{
				passCheck = m_focusable && hasTouchPoint();
			}
			if (passCheck)
			{
				setBtnState(Btn_PressBegin);
			}
			break;
		case Btn_PressBegin:
			if (btnPressOver())
			{
				setBtnState(Btn_PressEnd);
			}
			break;
		case Btn_PressEnd:
			if (!Plat_Tochable)
			{
				passCheck = keyNow != C2D_Device.key_enter;
			}
			else
			{
				passCheck = !hasTouchPoint();
			}
			if (passCheck)
			{
				setBtnState(Btn_ReleaseBegin);
			}
			break;
		case Btn_ReleaseBegin:
			if (btnReleaseOver())
			{
				setBtnState(Btn_ReleaseEnd);
			}
			break;
		case Btn_ReleaseEnd:
			setBtnState(Btn_Float);
			break;
		}
	}
	/**
	 * ����ť��Ӧ
	 * 
	 * @param stage
	 */
	protected void processBtnCalls(C2D_Stage stage)
	{
		if (stage == null)
		{
			return;
		}
		m_StateFrame++;
		int keyNow = stage.getSingleKey();
		boolean passCheck;
		switch (m_BtnState)
		{
		case Btn_Float:
			if (!Plat_Tochable)
			{
				break;
			}
		case Btn_Focused:
			if (!Plat_Tochable)
			{
				passCheck = m_focused && keyNow == C2D_Device.key_enter;
			}
			else
			{
				passCheck = m_focusable && hasTouchPoint();
			}
			if (passCheck)
			{
				setBtnState(Btn_PressBegin);
			}
			break;
		case Btn_PressBegin:
			setBtnState(Btn_PressEnd);
			break;
		case Btn_PressEnd:
			if (!Plat_Tochable)
			{
				passCheck = keyNow != C2D_Device.key_enter;
			}
			else
			{
				passCheck = !hasTouchPoint();
			}
			if (passCheck)
			{
				setBtnState(Btn_ReleaseBegin);
			}
			break;
		case Btn_ReleaseBegin:
			setBtnState(Btn_ReleaseEnd);
			break;
		case Btn_ReleaseEnd:
			setBtnState(m_focused ? Btn_Focused : Btn_Float);
			break;
		}
	}

	public void processBtnCall(C2D_Stage stage)
	{
		if(stage==null||m_carrier==null)
		{
			return;
		}
		if(C2D_Stage.Plat_Tochable)
		{
			if(m_carrier.getFocusable() && m_carrier.hasTouchPoint())
			{
				onCalled(C2D_Consts.Btn_PressBegin);
				stage.releaseKeys();
			}
		}
		else
		{
			int keyCode = stage.getSingleKey();
			if(m_carrier.isFocused()&&keyCode == C2D_Device.key_enter && !m_carrier.anyKeyUsed(keyCode))
			{
				onCalled(C2D_Consts.Btn_PressBegin);
				stage.releaseKeys();
			}
		}

	}
	public void onFocused(boolean b, C2D_Widget another)
	{
		// TODO Auto-generated method stub
		
	}
}
