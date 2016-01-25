package c2d.frame.com;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.app.C2D_Device;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.C2D_Sprite;

/**
 * 精灵按钮类 基于精灵结构创建的按钮类，它使用不同的精灵Action来表示按钮的不同状态。
 * 可以使用setID4State来设置按钮的浮起，焦点，按下，释放等状态。 精灵动画会默认设置成自动按照时间播放，因为动画的播放涉及到事件的触发，
 * 因此，你不能轻易关闭精灵动画的自动播放。另外动画播放默认是不循环的。 精灵按钮默认是可以拥有焦点的。 事件触发机制如下:
 * 1、当获得焦点时,自动设置按钮状态成为焦点状态,并且触发焦点获得事件。 2、当失去焦点时,自动复位按钮状态成为浮起状态,并且触发焦点丢失事件。
 * 3、当按钮按下时,自动设置按钮状态成为按下状态,触发Press结束事件,并开始播放动画, 动画播放完毕,触发Press结束事件。
 * 4、当按钮释放时,自动设置按钮状态成为释放状态,触发Release开始事件,并开始播放动画,
 * 动画播放完毕,触发Release结束事件。在触发事件之前,将精灵恢复到浮起状态。
 * 
 * 注意，它集成了精灵的锚点对齐无效。精灵由于采用中心对齐，没有固定尺寸不能绘制出焦点图片。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_SpriteBtn extends C2D_Sprite implements C2D_Consts
{
	/**
	 * 每个按钮状态对应actionID
	 */
	private int m_id_float = 0;
	private int m_id_focused = 1;
	private int m_id_press = 2;
	private int m_id_release = 3;
	/** 按钮状态 */
	protected int m_BtnState;

	public C2D_SpriteBtn(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		super(c2dManager, folderID, spriteID);
		setAutoPlay(AUTOPLAY_TIME);
		setAnimLoop(true);
		setFocusable(true);
	}

	/**
	 * 处理按钮响应
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
	 * 设置按钮的不同状态对应的ActionID
	 * 
	 * @param id_float
	 *            按钮浮起状态对应的actionID
	 * @param id_foucs
	 *            按钮焦点状态对应的actionID
	 * @param id_press
	 *            按钮按下状态对应的actionID
	 * @param id_release
	 *            按钮释放状态对应的actionID
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
	 * 设置按钮状态
	 * 
	 * @param state
	 *            新的按钮状态
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
	 * 按钮按下过程是否结束
	 * 
	 * @return 按钮按下过程是否结束
	 */
	protected boolean btnPressOver()
	{
		return atActionEnd();
	}

	/**
	 * 按钮释放过程是否结束
	 * 
	 * @return 按钮释放过程是否结束
	 */
	protected boolean btnReleaseOver()
	{
		return atActionEnd();
	}

	/**
	 * 让当前控件拥有焦点，这个函数将自动将控件设置成可以拥有焦点。 将当前场景的焦点控件变成自身。你不应该手动调用这个函数。
	 * @param another 原焦点
	 */
	protected void gotFocus(C2D_Widget another)
	{
		this.setBtnState(Btn_Focused);
		super.gotFocus(another);
	}

	/**
	 * 失去焦点，这个函数将自动将控件设置成可以拥有焦点。 你不应该手动调用这个函数，那样的话，当前页面将失去拥有焦点的控件
	 * @param another 目标焦点
	 */
	protected void lostFocus(C2D_Widget another)
	{
		this.setBtnState(Btn_Float);
		super.lostFocus(another);
	}

}
