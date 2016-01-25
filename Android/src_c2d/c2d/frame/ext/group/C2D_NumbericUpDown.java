package c2d.frame.ext.group;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_Button;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.event.C2D_EventPool_ChangeValue;
import c2d.frame.event.C2D_Event_Button;
import c2d.lang.math.C2D_Math;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_ImageClip;

public class C2D_NumbericUpDown extends C2D_View
{
	/** ���Ӱ�ťͼƬ */
	public C2D_Button m_IncBtn;
	/** ���Ͱ�ťͼƬ */
	public C2D_Button m_DecBtn;
	/** ����� */
	public C2D_PicTextBox m_InputBox;
	/** ���ֵ */
	private int m_maxNumber=100;
	/** ��Сֵ */
	private int m_minNumber=0;
	/** ��ǰֵ */
	private int m_curNumber=m_maxNumber;
	/** �ı����*/
	public int m_changeStep=1;
	/** �¼���-�ı����� */
	protected C2D_EventPool_ChangeValue m_Events_value;
	/** �����¼� */
	C2D_Event_Button incEvent = new C2D_Event_Button()
	{
		protected boolean doEvent(C2D_Widget carrier, int btnState)
		{
			if(btnState==C2D_Consts.Btn_PressBegin)
			{
				if(m_curNumber<m_maxNumber)
				{
					changeNumber(m_changeStep);
				}
			}
			return false;
		}
	};
	/** �����¼� */
	C2D_Event_Button decEvent = new C2D_Event_Button()
	{
		protected boolean doEvent(C2D_Widget carrier, int btnState)
		{
			if(btnState==C2D_Consts.Btn_PressBegin)
			{
				if(m_curNumber>m_minNumber)
				{
					changeNumber(-m_changeStep);
				}
			}
			return false;
		}
	};
	/**
	 * �ı�������Ͽ򣬹���֮������Ҫ�趨������������ť�ľ������꣬���ǵ�ê��ȫ��������ˮƽ��ֱ���С�
	 * Ĭ����������д��ڣ����Ӻͽ��Ͱ�ť���Ҳ�ֱ�������ϽǺ����½ǡ�
	 * @param bgClip ������Ƭͼ��������Чֵʱ�������մ˴�С���õ�ǰ��Ͽ��С
	 * @param inputBox �����ı���
	 * @param incBtn ���Ӱ�ť
	 * @param decBtn ���Ͱ�ť
	 */
	public C2D_NumbericUpDown(
			C2D_ImageClip bgClip,
			C2D_PicTextBox inputBox,
			C2D_Button incBtn,C2D_Button decBtn)
	{
		if(bgClip!=null)
		{
			setBGImage(bgClip);
			setSizeOfBGImg();			
		}
		//���Ӱ�ť
		m_IncBtn=incBtn;
		if(m_IncBtn!=null)
		{
			addChild(m_IncBtn,1);
			m_IncBtn.setPosTo(getWidth()-m_IncBtn.getContentW()/2,m_IncBtn.getContentH()/2);
		}
		//���Ͱ�ť
		m_DecBtn=decBtn;
		if(m_DecBtn!=null)
		{
			addChild(m_DecBtn,2);
			m_DecBtn.setPosTo(getWidth()-m_DecBtn.getContentW()/2,getHeight()-m_DecBtn.getContentH()/2);
		}
		//�����
		m_InputBox=inputBox;
		if(m_InputBox!=null)
		{
			m_InputBox.setAnchor(C2D_Consts.HVCENTER);
			m_InputBox.setPosTo(getWidth()/2, getHeight()/2);
			addChild(m_InputBox,3);
		}
		//���ó�ʼֵ
		setNumber(m_minNumber);
		//�����¼�
		m_IncBtn.Events_Button().add(incEvent);
		m_DecBtn.Events_Button().add(decEvent);
		
	}
	/**
	 * ���õ�ǰ����
	 * @param numer
	 * @return �Ƿ����˸ı�
	 */
	public boolean setNumber(int numer)
	{
		if(numer<m_minNumber||numer>m_maxNumber||m_curNumber==numer)
		{
			return false;
		}
		m_curNumber=numer;
		if(m_InputBox!=null)
		{
			m_InputBox.setText(""+m_curNumber);
			if (m_Events_value != null)
			{
				m_Events_value.onCalled(m_curNumber);
			}
			return true;
		}
		return false;
	}
	/**
	 * ���õ�ǰ��С���������
	 * @param minNumber ��С����
	 * @param maxNumber �������
	 * @return �Ƿ�ɹ�����
	 */
	public boolean setLimitNumber(int minNumber,int maxNumber)
	{
		if(minNumber>maxNumber)
		{
			return false;
		}
		m_minNumber=minNumber;
		m_maxNumber=maxNumber;
		setNumber(C2D_Math.limitNumber(m_curNumber, m_minNumber, m_maxNumber));
		return true;
	}
	/**
	 * �ı䵱ǰ����
	 * @param change �ı��С
	 * @return �Ƿ����˸ı�
	 */
	public boolean changeNumber(int change)
	{
		int newNumber=m_curNumber+change;
		newNumber=C2D_Math.limitNumber(newNumber, m_minNumber, m_maxNumber);
		return setNumber(newNumber);
	}
	/**
	 * �ı���ֵ�¼���
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_ChangeValue Events_ChangeValue()
	{
		if (m_Events_value == null)
		{
			m_Events_value = new C2D_EventPool_ChangeValue(this);
		}
		return m_Events_value;
	}
	/**
	 * ��ȡ��ǰ��ֵ
	 * @return ��ǰ��ֵ
	 */
	public int getNumber()
	{
		return m_curNumber;
	}
}
