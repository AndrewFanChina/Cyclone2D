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
	 * 设置选择框的不同状态对应的ActionID
	 * @param id_uncked  	选择框非选中无焦点状态对应的actionID
	 * @param id_unckedFocus 选择框非选中有焦点状态对应的actionID
	 * @param id_checked	选择框非选中有焦点状态对应的actionID
	 * @param id_checkedFocus	选择框选中无焦点状态对应的actionID
	 */
	public void setID4State(int id_uncked,int id_unckedFocus,int id_checked,int id_checkedFocus)
	{
		m_id_uncked=id_uncked;
		m_id_unckedFocus=id_unckedFocus;
		m_id_checked=id_checked;
		m_id_checkedFocus=id_checkedFocus;
	}
	/** 
	 * 设置按钮状态
	 * @param state 新的按钮状态
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
	 * 执行反选
	 */
	public void doCheck()
	{
		m_checked=!m_checked;
		setAction(m_checked?m_id_checked:m_id_uncked);
	}
	/**
	 * 获得选中状态
	 * @return 选中状态
	 */
	public boolean getCheckState()
	{
		return m_checked;
	}
}
