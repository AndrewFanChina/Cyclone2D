package c2d.frame.com;

import c2d.mod.C2D_FrameManager;

public class C2D_CheckBox extends C2D_SpriteBtn
{
	private int m_id_uncked=0;
	private int m_id_unckedFocus=1;
	private int m_id_checked=2;
	private int m_id_checkedFocus=3;
	private boolean m_checked;
	public C2D_CheckBox(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		super(c2dManager, folderID, spriteID);
	}
	/**
	 * ����ѡ���Ĳ�ͬ״̬��Ӧ��ActionID
	 * @param id_uncked  	ѡ����ѡ���޽���״̬��Ӧ��actionID
	 * @param id_unckedFocus ѡ����ѡ���н���״̬��Ӧ��actionID
	 * @param id_checked	ѡ����ѡ���н���״̬��Ӧ��actionID
	 * @param id_checkedFocus	ѡ���ѡ���޽���״̬��Ӧ��actionID
	 */
	public void setID4State(int id_uncked,int id_unckedFocus,int id_checked,int id_checkedFocus)
	{
		m_id_uncked=id_uncked;
		m_id_unckedFocus=id_unckedFocus;
		m_id_checked=id_checked;
		m_id_checkedFocus=id_checkedFocus;
	}
	/** 
	 * ���ð�ť״̬
	 * @param state �µİ�ť״̬
	 */
	protected void setBtnState(int state)
	{
		if(m_BtnState==state)
		{
			return;
		}
		super.setBtnState(state);
		if(m_BtnState==Btn_Float)
		{
			setAction(m_checked?m_id_checked:m_id_uncked);
		}
		else if(m_BtnState==Btn_Focused)
		{
			setAction(m_checked?m_id_checkedFocus:m_id_unckedFocus);
		}
		else if(m_BtnState==Btn_ReleaseBegin)
		{
			doCheck();
		}
	}
	/**
	 * ִ�з�ѡ
	 */
	public void doCheck()
	{
		m_checked=!m_checked;
		setAction(m_checked?m_id_checked:m_id_uncked);
	}
	/**
	 * ���ѡ��״̬
	 * @return ѡ��״̬
	 */
	public boolean getCheckState()
	{
		return m_checked;
	}
}
