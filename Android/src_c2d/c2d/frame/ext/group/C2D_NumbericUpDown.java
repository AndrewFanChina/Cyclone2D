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
	/** 增加按钮图片 */
	public C2D_Button m_IncBtn;
	/** 降低按钮图片 */
	public C2D_Button m_DecBtn;
	/** 输入框 */
	public C2D_PicTextBox m_InputBox;
	/** 最大值 */
	private int m_maxNumber=100;
	/** 最小值 */
	private int m_minNumber=0;
	/** 当前值 */
	private int m_curNumber=m_maxNumber;
	/** 改变幅度*/
	public int m_changeStep=1;
	/** 事件池-改变内容 */
	protected C2D_EventPool_ChangeValue m_Events_value;
	/** 增加事件 */
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
	/** 降低事件 */
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
	 * 文本输入组合框，构造之后，你需要设定输入框和两个按钮的具体坐标，它们的锚点全部采用了水平垂直居中。
	 * 默认输入框会居中存在，增加和降低按钮会右侧分别防在右上角和左下角。
	 * @param bgClip 背景切片图，传入有效值时，将按照此大小设置当前组合框大小
	 * @param inputBox 输入文本框
	 * @param incBtn 增加按钮
	 * @param decBtn 降低按钮
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
		//增加按钮
		m_IncBtn=incBtn;
		if(m_IncBtn!=null)
		{
			addChild(m_IncBtn,1);
			m_IncBtn.setPosTo(getWidth()-m_IncBtn.getContentW()/2,m_IncBtn.getContentH()/2);
		}
		//降低按钮
		m_DecBtn=decBtn;
		if(m_DecBtn!=null)
		{
			addChild(m_DecBtn,2);
			m_DecBtn.setPosTo(getWidth()-m_DecBtn.getContentW()/2,getHeight()-m_DecBtn.getContentH()/2);
		}
		//输入框
		m_InputBox=inputBox;
		if(m_InputBox!=null)
		{
			m_InputBox.setAnchor(C2D_Consts.HVCENTER);
			m_InputBox.setPosTo(getWidth()/2, getHeight()/2);
			addChild(m_InputBox,3);
		}
		//设置初始值
		setNumber(m_minNumber);
		//设置事件
		m_IncBtn.Events_Button().add(incEvent);
		m_DecBtn.Events_Button().add(decEvent);
		
	}
	/**
	 * 设置当前数字
	 * @param numer
	 * @return 是否发生了改变
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
	 * 设置当前最小和最大数字
	 * @param minNumber 最小数字
	 * @param maxNumber 最大数字
	 * @return 是否成功设置
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
	 * 改变当前数字
	 * @param change 改变大小
	 * @return 是否发生了改变
	 */
	public boolean changeNumber(int change)
	{
		int newNumber=m_curNumber+change;
		newNumber=C2D_Math.limitNumber(newNumber, m_minNumber, m_maxNumber);
		return setNumber(newNumber);
	}
	/**
	 * 改变数值事件池
	 * 
	 * @return 事件池
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
	 * 获取当前数值
	 * @return 当前数值
	 */
	public int getNumber()
	{
		return m_curNumber;
	}
}
