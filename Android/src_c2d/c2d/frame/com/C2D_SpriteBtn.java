package c2d.frame.com;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.app.C2D_Device;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.C2D_Sprite;

/**
 * ���鰴ť�� ���ھ���ṹ�����İ�ť�࣬��ʹ�ò�ͬ�ľ���Action����ʾ��ť�Ĳ�ͬ״̬��
 * ����ʹ��setID4State�����ð�ť�ĸ��𣬽��㣬���£��ͷŵ�״̬�� ���鶯����Ĭ�����ó��Զ�����ʱ�䲥�ţ���Ϊ�����Ĳ����漰���¼��Ĵ�����
 * ��ˣ��㲻�����׹رվ��鶯�����Զ����š����⶯������Ĭ���ǲ�ѭ���ġ� ���鰴ťĬ���ǿ���ӵ�н���ġ� �¼�������������:
 * 1������ý���ʱ,�Զ����ð�ť״̬��Ϊ����״̬,���Ҵ����������¼��� 2����ʧȥ����ʱ,�Զ���λ��ť״̬��Ϊ����״̬,���Ҵ������㶪ʧ�¼���
 * 3������ť����ʱ,�Զ����ð�ť״̬��Ϊ����״̬,����Press�����¼�,����ʼ���Ŷ���, �����������,����Press�����¼���
 * 4������ť�ͷ�ʱ,�Զ����ð�ť״̬��Ϊ�ͷ�״̬,����Release��ʼ�¼�,����ʼ���Ŷ���,
 * �����������,����Release�����¼����ڴ����¼�֮ǰ,������ָ�������״̬��
 * 
 * ע�⣬�������˾����ê�������Ч���������ڲ������Ķ��룬û�й̶��ߴ粻�ܻ��Ƴ�����ͼƬ��
 * 
 * @author AndrewFan
 * 
 */
public class C2D_SpriteBtn extends C2D_Sprite implements C2D_Consts
{
	/**
	 * ÿ����ť״̬��ӦactionID
	 */
	private int m_id_float = 0;
	private int m_id_focused = 1;
	private int m_id_press = 2;
	private int m_id_release = 3;
	/** ��ť״̬ */
	protected int m_BtnState;

	public C2D_SpriteBtn(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		super(c2dManager, folderID, spriteID);
		setAutoPlay(AUTOPLAY_TIME);
		setAnimLoop(true);
		setFocusable(true);
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
	 * ���ð�ť�Ĳ�ͬ״̬��Ӧ��ActionID
	 * 
	 * @param id_float
	 *            ��ť����״̬��Ӧ��actionID
	 * @param id_foucs
	 *            ��ť����״̬��Ӧ��actionID
	 * @param id_press
	 *            ��ť����״̬��Ӧ��actionID
	 * @param id_release
	 *            ��ť�ͷ�״̬��Ӧ��actionID
	 */
	public void setID4State(int id_float, int id_foucs, int id_press, int id_release)
	{
		m_id_float = id_float;
		m_id_focused = id_foucs;
		m_id_press = id_press;
		m_id_release = id_release;
		refreshBtnState();
	}

	/**
	 * ���ð�ť״̬
	 * 
	 * @param state
	 *            �µİ�ť״̬
	 */
	protected void setBtnState(int state)
	{
		if (m_BtnState == state)
		{
			return;
		}
		m_BtnState = state;
		if (m_Events_Button != null && m_BtnState != Btn_Float)
		{
			m_Events_Button.onCalled(m_BtnState);
		}
		refreshBtnState();
	}

	private void refreshBtnState()
	{
		if (m_BtnState == Btn_Float)
		{
			setAction(m_id_float);
		}
		else if (m_BtnState == Btn_Focused)
		{
			setAction(m_id_focused);
		}
		else if (m_BtnState == Btn_PressBegin)
		{
			setAction(m_id_press);
		}
		else if (m_BtnState == Btn_ReleaseBegin)
		{
			setAction(m_id_release);
		}
	}

	/**
	 * ��ť���¹����Ƿ����
	 * 
	 * @return ��ť���¹����Ƿ����
	 */
	protected boolean btnPressOver()
	{
		return atActionEnd();
	}

	/**
	 * ��ť�ͷŹ����Ƿ����
	 * 
	 * @return ��ť�ͷŹ����Ƿ����
	 */
	protected boolean btnReleaseOver()
	{
		return atActionEnd();
	}

	/**
	 * �õ�ǰ�ؼ�ӵ�н��㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 ����ǰ�����Ľ���ؼ���������㲻Ӧ���ֶ��������������
	 * @param another ԭ����
	 */
	protected void gotFocus(C2D_Widget another)
	{
		this.setBtnState(Btn_Focused);
		super.gotFocus(another);
	}

	/**
	 * ʧȥ���㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 �㲻Ӧ���ֶ�������������������Ļ�����ǰҳ�潫ʧȥӵ�н���Ŀؼ�
	 * @param another Ŀ�꽹��
	 */
	protected void lostFocus(C2D_Widget another)
	{
		this.setBtnState(Btn_Float);
		super.lostFocus(another);
	}

}
