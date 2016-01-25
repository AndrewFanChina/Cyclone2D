package c2d.frame.ext.group;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_PTC;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.event.C2D_EventPool_Button;
import c2d.frame.event.C2D_EventPool_ChangeStrValue;
import c2d.frame.event.C2D_Event_ChangeFocus;
import c2d.frame.event.C2D_Event_Update;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.type.C2D_Color;
import c2d.mod.C2D_Consts;

public class C2D_InputNumBox extends C2D_View
{
	public C2D_InputNumBox(C2D_PTC txtCfg)
	{
		m_txtCfg = txtCfg;
		init();
	}

	private int m_maxInput = 10;
	private String m_strInput; // 当前输入的密码
	private C2D_PicTextBox m_txtBox;// 文本显示框
	private C2D_PTC m_txtCfg; // 文本框配置
	private boolean m_pwdMode;//使用密码模式
	private C2D_Color m_focusedColor;//拥有焦点时文本框背景颜色
	private boolean m_delByLeft = false;//是否使用左键进行消除，不是左键则使用返回键消除
	/** 事件池-获得焦点 */
	protected C2D_EventPool_ChangeStrValue m_Events_value;

	/**
	 * 设置使用密码模式
	 * 
	 * @param pwdMode
	 */
	public void setPwdMode(boolean pwdMode)
	{
		m_pwdMode = pwdMode;
	}

	/**
	 * 改变数值事件池
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_ChangeStrValue Events_ChangeValue()
	{
		if (m_Events_value == null)
		{
			m_Events_value = new C2D_EventPool_ChangeStrValue(this);
		}
		return m_Events_value;
	}

	/**
	 * 检测是否使用密码模式
	 * 
	 * @return 是否使用密码模式
	 */
	public boolean getPwdMode()
	{
		return m_pwdMode;
	}

	/**
	 * 设置获得焦点时文本框的背景颜色
	 * 
	 * @param color
	 */
	public void setFocusedTxtBgColor(C2D_Color color)
	{
		m_focusedColor = color;
	}

	/**
	 * 设置最多允许输入的位数
	 * 
	 * @param maxCount
	 */
	public void setMaxInput(int maxCount)
	{
		if (maxCount <= 0 || maxCount >= 100)
		{
			return;
		}
		m_maxInput = maxCount;
		resetSize();
	}

	/**
	 * 重新设置大小
	 */
	private void resetSize()
	{
		if (m_txtBox == null || m_txtCfg == null)
		{
			return;
		}
		m_txtBox.setLimitSize(m_maxInput * (m_txtCfg.getCharWMax() + m_txtCfg.m_gapX), m_txtCfg.m_charH);
		this.setSize(((m_maxInput) * (m_txtCfg.getCharWMax() + m_txtCfg.m_gapX)), m_txtCfg.m_charH + 4);
	}

	/**
	 * 如果使用密码模式，返回跟s长度一样的星号字符串 否则返回原字符串
	 * 
	 * @param s
	 * @return
	 */
	private String getStar(String s)
	{
		if (!m_pwdMode)
		{
			return s;
		}
		if (s == null || s.length() == 0)
		{
			return "";
		}
		String star = "";
		for (int i = 0; i < s.length(); i++)
		{
			star += "*";
		}
		return star;
	}

	/**
	 * 设置是否使用左键进行消除，不是左键则使用返回键消除
	 * 
	 * @param delByLeft
	 *            是否使用左键进行消除
	 */
	public void setDelByLeft(boolean delByLeft)
	{
		m_delByLeft = delByLeft;
	}

	/**
	 * 获得是否使用左键进行消除，不是左键则使用返回键消除
	 * 
	 * @return 是否使用左键进行消除
	 */
	public boolean getDelByLeft()
	{
		return m_delByLeft;
	}

	/**
	 * 是否是消除键
	 * 
	 * @param stage
	 * @return 是否是消除键
	 */
	private boolean isDelKey(C2D_Stage stage)
	{
		if (stage == null)
		{
			return false;
		}
		boolean del = false;
		if (m_delByLeft && stage.getSingleKey() == C2D_Device.key_left)
		{
			del = true;
		}
		if (stage.isKeyBackOrRSoft())
		{
			del = true;
		}
		return del;
	}

	/**
	 * 输入处理
	 * 
	 */
	private void inputLogic()
	{
		C2D_Stage stage = getStageAt();
		if (stage == null || m_txtBox == null)
		{
			return;
		}
		if (!m_txtBox.isFocused())
		{
			return;
		}
		int keyNum = stage.getSingleKey();
		if ((keyNum >= C2D_Device.key_num0 && keyNum <= C2D_Device.key_num9) || isDelKey(stage))
		{
			String destTxt = null;
			// 消除键
			if (isDelKey(stage))
			{
				if (m_strInput.length() > 0)
				{
					destTxt = m_strInput.substring(0, m_strInput.length() - 1);
				}
			}
			// 数字键
			else
			{
				if (m_strInput.length() < m_maxInput)
				{
					destTxt = m_strInput + (keyNum - C2D_Device.key_num0);
				}
			}
			if (destTxt != null)
			{
				m_strInput = destTxt;
				m_txtBox.setText(getStar(m_strInput));
				if (m_Events_value != null)
				{
					m_Events_value.onCalled(m_strInput);
				}
			}
			stage.releaseKeys();
		}
	}

	/**
	 * 让输入框拥有焦点，注意系统方法setFocusedWidget()对输入框无效。
	 * 实际上输入框容器并不允许拥有焦点，这个方法是设置让其内部的文本框拥有焦点。
	 */
	public void setFocused()
	{
		if (m_txtBox != null && getSceneAt() != null)
		{
			getSceneAt().setFocusedWidget(m_txtBox);
		}
	}

	/**
	 * 屏蔽本身的焦点，输入框不允许拥有焦点
	 */
	public void setFocusable(boolean focusable)
	{
		this.m_focusable = false;
	}

	/**
	 * 初始化，加载内部组件
	 */
	private void init()
	{
		if (m_txtCfg == null || !isEmpty())
		{
			return;
		}
		// 添加密码框
		m_txtBox = new C2D_PicTextBox(m_txtCfg);
		m_txtBox.setPosTo(0, (m_txtCfg.m_charH + 1) / 2);
		m_txtBox.setAnchor(C2D_Consts.VCENTER);
		m_strInput = "";
		m_txtBox.setText(getStar(m_strInput));
		setMaxInput(m_maxInput);
		addChild(m_txtBox);
		m_txtBox.setZOrder(20);
		m_txtBox.setFocusable(true);
		m_txtBox.Events_ChangeFocus().add(new C2D_Event_ChangeFocus()
		{
			protected boolean doEvent(C2D_Widget carrier, boolean focused, C2D_Widget another)
			{
				if (focused)
				{
					m_txtBox.setTxtBgColor(m_focusedColor);
				}
				else
				{
					m_txtBox.setTxtBgColor(null);
				}
				return false;
			}
		});
		// 键盘事件
		Events_Update().add(new C2D_Event_Update()
		{
			protected boolean doEvent(C2D_Widget carrier)
			{
				inputLogic();
				return false;
			}
		});
		resetSize();
	}

	public String getText()
	{
		if (m_strInput != null && m_strInput.length() > 0)
		{
			return m_strInput;
		}
		return null;
	}

	public void setText(String text)
	{
		m_strInput = text;
		if (m_txtBox != null)
		{
			m_txtBox.setText(m_strInput);
		}
	}

	/**
	 * 获得按钮事件池，它的按钮事件被转移到了内部的文本框上
	 * 
	 * @return 显示事件池
	 */
	public C2D_EventPool_Button Events_Button()
	{
		if (m_txtBox == null)
		{
			return null;
		}
		return m_txtBox.Events_Button();
	}

	/**
	 * 清空输入内容
	 */
	public void clearInput()
	{
		m_strInput = "";
		if (m_txtBox != null)
		{
			m_txtBox.setText(m_strInput);
		}
	}

	/**
	 * 获取控件名称，它的获取名称被转移到了内部的文本框上
	 * 
	 * @return 控件名称
	 */
	public String getName()
	{
		if (m_txtBox != null)
		{
			return m_txtBox.getName();
		}
		return null;
	}

	/**
	 * 设置控件名称，它的设置名称被转移到了内部的文本框上
	 * 
	 * @param widgetName
	 */
	public void setName(String widgetName)
	{
		if (m_txtBox != null)
		{
			m_txtBox.setName(widgetName);
		}
	}
}
