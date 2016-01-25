package c2d.frame.ext.scene;

import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_Event_ChangeFocus;
import c2d.frame.event.C2D_Event_MotionEnd;
import c2d.mod.physic.C2D_Motion;

/**
 * 带有浮动选项的场景，场景中含有若干的可选条目，被选中的条目将以动作形式展现（如上下浮动等）
 * @author AndrewFan
 *
 */
public abstract class C2D_MotionItemScene extends C2D_SceneUtil
{
	protected int m_moveX = 0;
	protected int m_moveY = 0;
	protected int m_moveTime = 0;
	protected int[][] m_itemPos;
	protected C2D_Motion m_motionDown;
	protected C2D_Motion m_motionUp;
	protected C2D_Event_MotionEnd m_eventMoDown = new C2D_Event_MotionEnd()
	{
		protected boolean doEvent(C2D_Widget carrier)
		{
			carrier.clearMotions();
			carrier.addMotion(getMotionUp(carrier));
			carrier.Events_MotionEnd().clear();
			carrier.Events_MotionEnd().add(m_eventMoUp);
			return true;
		}
	};
	protected C2D_Event_MotionEnd m_eventMoUp = new C2D_Event_MotionEnd()
	{
		protected boolean doEvent(C2D_Widget carrier)
		{
			carrier.clearMotions();
			carrier.addMotion(getMotionDown(carrier));
			carrier.Events_MotionEnd().clear();
			carrier.Events_MotionEnd().add(m_eventMoDown);
			return true;
		}
	};
	protected C2D_Event_ChangeFocus m_eventChangeFocus = new C2D_Event_ChangeFocus()
	{
		protected boolean doEvent(C2D_Widget carrier, boolean focused,C2D_Widget another)
		{
			if (!focused)
			{
				carrier.clearMotions();
				int id = carrier.getFlag();
				if (m_itemPos != null && id < m_itemPos.length && m_itemPos[id] != null && m_itemPos[id].length >= 2)
				{
					carrier.setPosTo(m_itemPos[id][0], m_itemPos[id][1]);
				}
			}
			else
			{
				carrier.addMotion(getMotionUp(carrier));
				carrier.Events_MotionEnd().add(m_eventMoDown);
			}
			return false;
		}
	};

	/**
	 * @param carrier
	 */
	protected C2D_Motion getMotionDown(C2D_Widget carrier)
	{
		if(m_motionDown==null)
		{
			m_motionDown = new C2D_Motion(carrier, m_moveX, m_moveY, 0, m_moveTime);
		}
		else
		{
			m_motionDown.startMotion(carrier, m_moveX, m_moveY, 0, m_moveTime);
		}
		return m_motionDown;
	}
	/**
	 * @param carrier
	 */
	protected C2D_Motion getMotionUp(C2D_Widget carrier)
	{
		if(m_motionUp==null)
		{
			m_motionUp = new C2D_Motion(carrier, -m_moveX, -m_moveY, 0, m_moveTime);
		}
		else
		{
			m_motionUp.startMotion(carrier,-m_moveX, -m_moveY, 0, m_moveTime);
		}
		return m_motionUp;
	}
	/**
	 * 设置焦点控件，并且让控件开始浮动
	 * 
	 * @param widget
	 */
	protected void setMotionItem(C2D_Widget widget)
	{
		startMotion(widget);
		widget.setFocusable(true);
		setFocusedWidget(widget);
	}

	/**
	 * 让控件开始浮动
	 * 
	 * @param widget
	 */
	protected void startMotion(C2D_Widget widget)
	{
		widget.addMotion(getMotionDown(widget));
		widget.Events_MotionEnd().clear();
		widget.Events_MotionEnd().add(m_eventMoDown);
	}

	/**
	 * 控件停止浮动，并清除所有浮动事件
	 * 
	 * @param widget
	 */
	protected void finishMotion(C2D_Widget widget)
	{
		widget.clearMotions();
		widget.Events_MotionEnd().clear();
	}

	/**
	 * 设置控件的运动参数
	 * 
	 * @param moveX
	 *            x方向运动长度
	 * @param moveX
	 *            y方向运动长度
	 * @param moveTimeT
	 *            运动时间
	 */
	protected void setMotionParam(int moveX, int moveY, int moveTimeT)
	{
		m_moveX = moveX;
		m_moveY = moveY;
		m_moveTime = moveTimeT;
	}
}
