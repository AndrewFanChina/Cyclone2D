package c2d.frame.ext.group;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_PTC;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_EventPool_Button;
import c2d.frame.event.C2D_Event_ChangeFocus;
import c2d.frame.event.C2D_Event_Update;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.type.C2D_Color;
import c2d.mod.C2D_Consts;

public class C2D_InputTextBox extends C2D_View
{
	public C2D_InputTextBox(C2D_PTC txtCfg)
	{
		m_txtCfg=txtCfg;
		init();
	}
	// 可以输入的字符表
	private char[][] m_charList = new char[][]
	{
	{ '0' },
	{ '1' },
	{ '2', 'a', 'b', 'c', 'A', 'B', 'C' },
	{ '3', 'd', 'e', 'f', 'D', 'E', 'F' },
	{ '4', 'g', 'h', 'i', 'G', 'H', 'I' },
	{ '5', 'j', 'k', 'l', 'J', 'K', 'L' },
	{ '6', 'm', 'n', 'o', 'M', 'N', 'O' },
	{ '7', 'p', 'q', 'r', 's', 'P', 'Q', 'R', 'S' },
	{ '8', 't', 'u', 'v', 'T', 'U', 'V' },
	{ '9', 'w', 'x', 'y', 'z', 'W', 'X', 'Y', 'Z' }, };

	private String m_strInput; // 当前输入的密码
	private char m_charNext; // 下个输入的字符
	private int m_lastKey = -1; // 上次输入按键
	private int m_keyTime; // 连续输入按键次数
	private int m_charNextID; // 下个输入字符所在ID
	private int m_RowLen; // 下个输入字符所在行的长度
	private int m_waitTime; // 当前等待修改时间
	private int m_waitMaxTime = 800; // 单键输入时等待修改最大时间
	private int m_waitMaxTime2 = 1600;// 使用方向键时等待修改最大时间
	private boolean m_used9 = false; // 用户本次输入是否使用了方向键
	private int m_maxInput=10;

	private int m_9bg_com = 0x2B2B2B; // 未选中时九宫格底色
	private int m_9bg_focus = 0xAF360A; // 已选中时九宫格底色
	private int m_9txt_com = 0xFFFFFF;// 未选中时九宫格文本色
	private int m_9txt_focus = 0xFFFFFF;// 已选中时九宫格文本色
	private int m_9bg_all = 0x0; // 九宫格整个背景底色
	private int m_size_9 = 30;// 九宫格的单元格尺寸

	private C2D_PicTextBox m_txtBox;// 文本显示框
	private C2D_View m_pwd9Box; // 九宫格
	private C2D_PTC m_txtCfg; // 文本框配置
	private boolean m_pwdMode;//使用密码模式
	private C2D_Color m_focusedColor;//拥有焦点时文本框背景颜色
	private boolean m_delByLeft=false;//是否使用左键进行消除，不是左键则使用返回键消除
	/**
	 * 设置使用密码模式
	 * @param pwdMode
	 */
	public void setPwdMode(boolean pwdMode)
	{
		m_pwdMode = pwdMode;
	}
	/**
	 * 检测是否使用密码模式
	 * @return 是否使用密码模式
	 */
	public boolean getPwdMode()
	{
		return m_pwdMode;
	}
	/**
	 * 设置获得焦点时文本框的背景颜色
	 * @param color
	 */
	public void setFocusedTxtBgColor(C2D_Color color)
	{
		m_focusedColor=color;
	}
	/**
	 * 设置最多允许输入的位数
	 * @param maxCount
	 */
	public void setMaxInput(int maxCount)
	{
		if(maxCount<=0||maxCount>=100)
		{
			return;
		}
		m_maxInput=maxCount;
		resetSize();
	}
	/**
	 * 设置九宫格的单元格大小
	 * @param size 单元格大小
	 */
	public void setSizeOf9Box(int size)
	{
		if(size<=0)
		{
			return;
		}
		m_size_9 =size;
		if(m_pwd9Box==null)
		{
			return;
		}
		for (int i = 0; i < 9; i++)
		{
			C2D_View cellI = (C2D_View)m_pwd9Box.getChildAt(i);
			cellI.setSize(m_size_9, m_size_9);
			cellI.setPosTo((i % 3) * (m_size_9 + 1) + 1, (i / 3) * (m_size_9 + 1) + 1);
			cellI.setBGColor(m_9bg_com);
		}
		resetSize();
	}
	/**
	 * 重新设置大小
	 */
	private void resetSize()
	{
		if(m_txtBox==null||m_pwd9Box==null||m_txtCfg==null)
		{
			return;
		}
		m_txtBox.setLimitSize(m_maxInput*(m_txtCfg.getCharWMax()+m_txtCfg.m_gapX), m_txtCfg.m_charH);
		this.setSize((m_pwd9Box.getWidth()+ (m_maxInput-1)*(m_txtCfg.getCharWMax()+m_txtCfg.m_gapX)),
				m_txtCfg.m_charH+4+m_pwd9Box.getHeight());

	}
	// 根据按键获得键值
	private void setNextKey(int keyCode, int inputTime)
	{
		int keyType = keyCode - C2D_Device.key_num0;
		if (keyType < 0 || keyType >= m_charList.length)
		{
			return;
		}
		// 设置新输入字符
		m_RowLen = m_charList[keyType].length;
		m_charNextID = inputTime % m_RowLen;
		char next = m_charList[keyType][m_charNextID];
		if (next != m_charNext)
		{
			m_charNext = next;
			m_txtBox.setText(getStar(m_strInput) + m_charNext);
		}
		// 刷新
		if (inputTime == 0 && !m_pwd9Box.getVisible())
		{
			m_pwd9Box.setVisible(true);
			m_txtBox.setFocusable(false);
			int charCount = m_strInput == null ? 0 : m_strInput.length();
			m_pwd9Box.setPosTo(charCount * (m_txtCfg.getCharWMax() + m_txtCfg.getGapX()), m_txtCfg.m_charH+4);
			for (int i = 0; i < 9; i++)
			{
				C2D_View viewI = (C2D_View) m_pwd9Box.getChildAt(i);
				C2D_TextBox txtBox = (C2D_TextBox) viewI.getChildAt(0);
				if (i < m_charList[keyType].length)
				{
					txtBox.setText("" + m_charList[keyType][i]);
					if (i == 0)
					{
						txtBox.setColor(m_9txt_focus);
						viewI.setBGColor(m_9bg_focus);
						getSceneAt().setFocusedWidget(viewI);
					}
					else
					{
						txtBox.setColor(m_9txt_com);
						viewI.setBGColor(m_9bg_com);
					}
					viewI.setFocusable(true);
				}
				else
				{
					txtBox.setText("");
					viewI.setBGColor(m_9bg_com);
					viewI.setFocusable(false);
				}
			}
		}
		m_waitTime = 0;
	}

	/**
	 * 如果使用密码模式，返回跟s长度一样的星号字符串
	 * 否则返回原字符串
	 * @param s
	 * @return
	 */
	private String getStar(String s)
	{
		if(!m_pwdMode)
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

	// 复位输入按键
	private void restInput()
	{
		m_charNext = 0;
		m_charNextID = -1;
		m_waitTime = 0;
		m_lastKey = -1;
		m_keyTime = 0;
		m_used9 = false;
		m_pwd9Box.setVisible(false);
		m_txtBox.setFocusable(true);
		getSceneAt().setFocusedWidget(m_txtBox);
	}
	/**
	 * 设置是否使用左键进行消除，不是左键则使用返回键消除
	 * @param delByLeft 是否使用左键进行消除
	 */
	public void setDelByLeft(boolean delByLeft)
	{
		m_delByLeft = delByLeft;
	}
	/**
	 * 获得是否使用左键进行消除，不是左键则使用返回键消除
	 * @return 是否使用左键进行消除
	 */
	public boolean getDelByLeft()
	{
		return m_delByLeft;
	}
	/**
	 * 是否是消除键
	 * @param stage
	 * @return 是否是消除键
	 */
	private boolean isDelKey(C2D_Stage stage)
	{
		if(stage==null)
		{
			return false;
		}
		if(m_delByLeft)
		{
			int keyNum = stage.getSingleKey();
			return keyNum==C2D_Device.key_left;
		}
		else
		{
			return  stage.isKeyBackOrRSoft();
		}
	}
	/**
	 * 输入处理
	 * 
	 */
	private void inputLogic()
	{
		C2D_Stage stage = getStageAt();
		if (stage == null||m_txtBox==null)
		{
			return;
		}
		if(!m_txtBox.isFocused()&&!m_pwd9Box.getVisible())
		{
			return;
		}
		int keyNum = stage.getSingleKey();
		if ((keyNum >= C2D_Device.key_num0 && keyNum <= C2D_Device.key_num9) || isDelKey(stage))
		{
			// 消除键
			if (isDelKey(stage))
			{
				if (m_charNext == 0)
				{
					if (m_strInput.length() > 0)
					{
						m_strInput = m_strInput.substring(0, m_strInput.length() - 1);
						m_txtBox.setText(getStar(m_strInput));
					}
				}
				else
				{
					m_txtBox.setText(getStar(m_strInput));
				}
				restInput();
				stage.releaseKeys();
			}
			// 数字键
			else
			{
				if (m_strInput.length() < m_maxInput)
				{
					if (m_charNext == 0)// 还没有等待字符
					{
						m_keyTime = 0;
						setNextKey(keyNum, m_keyTime);
					}
					else
					{
						if (m_lastKey == keyNum)// 重复按键
						{
							m_keyTime++;
							setNextKey(keyNum, m_keyTime);
							if (m_charNextID >= 0)
							{
								C2D_View view = (C2D_View) (m_pwd9Box.getChildAt(m_charNextID));
								getSceneAt().setFocusedWidget(view);
							}

						}
						else
						// 变换按键
						{
							m_strInput += m_charNext;
							m_txtBox.setText(getStar(m_strInput));
							restInput();
							if (m_strInput.length() < m_maxInput)
							{
								setNextKey(keyNum, m_keyTime);
							}
						}
					}
					m_lastKey = keyNum;
				}
			}
			stage.releaseKeys();
		}
		// 方向键,补齐系统响应的不足，响应左右键时光标上下移动
		else if (keyNum >= C2D_Device.key_up && keyNum <= C2D_Device.key_left)
		{
			if (keyNum == C2D_Device.key_right && (m_charNextID % 3 == 2 || m_charNextID == m_RowLen - 1))
			{
				int nextID;
				if (m_charNextID == m_RowLen - 1)
				{
					nextID = 0;
				}
				else
				{
					nextID = m_charNextID + 1;
				}
				C2D_View view = (C2D_View) (m_pwd9Box.getChildAt(nextID));
				getSceneAt().setFocusedWidget(view);
				stage.releaseKeys();
			}
			if (keyNum == C2D_Device.key_left && m_charNextID % 3 == 0)
			{
				int nextID;
				if (m_charNextID == 0)
				{
					nextID = m_RowLen - 1;
				}
				else
				{
					nextID = m_charNextID - 1;
				}
				C2D_View view = (C2D_View) (m_pwd9Box.getChildAt(nextID));
				getSceneAt().setFocusedWidget(view);
				stage.releaseKeys();
			}
			return;
		}
		else if(keyNum==C2D_Device.key_empty)
		{
			if (m_charNext != 0)
			{
				m_waitTime += stage.getTimePassed();
				int max = m_used9 ? m_waitMaxTime2 : m_waitMaxTime;
				if (m_waitTime >= max)
				{
					m_strInput += m_charNext;
					m_txtBox.setText(getStar(m_strInput));
					m_charNext = 0;
					restInput();
					m_pwd9Box.setVisible(false);
					m_txtBox.setFocusable(true);
					getSceneAt().setFocusedWidget(m_txtBox);
				}
			}
		}
	}
	/**
	 * 让输入框拥有焦点，注意系统方法setFocusedWidget()对输入框无效。
	 * 实际上输入框容器并不允许拥有焦点，这个方法是设置让其内部的文本框拥有焦点。
	 */
	public void setFocused()
	{
		if(m_txtBox!=null&&getSceneAt()!=null)
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
		if(m_txtCfg==null||!isEmpty())
		{
			return;
		}
		// 添加密码框
		m_txtBox = new C2D_PicTextBox(m_txtCfg);
		m_txtBox.setPosTo(0, (m_txtCfg.m_charH+1)/2);
		m_txtBox.setAnchor(C2D_Consts.VCENTER);
		m_strInput = "";
		m_txtBox.setText(getStar(m_strInput));
		setMaxInput(10);
		addChild(m_txtBox);
		m_txtBox.setZOrder(20);
		m_txtBox.setFocusable(true);
		m_txtBox.Events_ChangeFocus().add(new C2D_Event_ChangeFocus()
		{
			protected boolean doEvent(C2D_Widget carrier, boolean focused,C2D_Widget another)
			{
				if(focused)
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
		// 移动光标事件
		C2D_Event_ChangeFocus cfe = new C2D_Event_ChangeFocus()
		{
			protected boolean doEvent(C2D_Widget carrier, boolean focused,C2D_Widget another)
			{
				C2D_View view = (C2D_View) (carrier);
				C2D_TextBox tb = (C2D_TextBox) (view.getChildAt(0));
				if (focused)
				{
					view.setBGColor(m_9bg_focus);
					tb.setColor(m_9txt_focus);
				}
				else
				{
					view.setBGColor(m_9bg_com);
					tb.setColor(m_9txt_com);
				}
				// 用户使用了方向键，设置等待时间和新按键
				if (focused)
				{
					int keyNum = getStageAt().getSingleKey();
					if (keyNum >= C2D_Device.key_up && keyNum <= C2D_Device.key_left)
					{
						if (m_charNext != 0 && m_lastKey != -1)
						{
							m_used9 = true;
							m_waitTime = 0;
							int id = view.getID();
							m_keyTime = id;
							setNextKey(m_lastKey, m_keyTime);
						}
					}
				}

				return false;
			}
		};
		// 九宫格
		m_pwd9Box = new C2D_View();
		m_pwd9Box.setSize(get9BoxSize(), get9BoxSize());
		m_pwd9Box.setBGColor(m_9bg_all);
		m_pwd9Box.setBesiege(true);
		for (int i = 0; i < 9; i++)
		{
			C2D_View cellI = new C2D_View();
			cellI.setSize(m_size_9, m_size_9);
			cellI.setPosTo((i % 3) * (m_size_9 + 1) + 1, (i / 3) * (m_size_9 + 1) + 1);
			cellI.setBGColor(m_9bg_com);
			m_pwd9Box.addChild(cellI);
			cellI.setZOrder(i);
			cellI.setFocusable(true);
			cellI.Events_ChangeFocus().add(cfe);
			C2D_TextBox tbxI = new C2D_TextBox();
			tbxI.setColor(m_9txt_com);
			tbxI.setPosTo(m_size_9 / 2, m_size_9 / 2);
			tbxI.setAnchor(C2D_Consts.HVCENTER);
			cellI.addChild(tbxI);
		}
		addChild(m_pwd9Box);
		m_pwd9Box.setPosTo(0, m_txtCfg.m_charH+4);
		m_pwd9Box.setVisible(false);
		m_pwd9Box.setZOrder(30);
		resetSize();
	}
	/**
	 * 返回是否正在输入
	 * @return
	 */
	public boolean isInputing()
	{
		return m_charNext!=0;
	}
	public String getText()
	{
		if(m_strInput!=null&&m_strInput.length()>0)
		{
			return m_strInput;
		}
		return null;
	}
	/**
	 * 获得九宫格尺寸
	 * @return 九宫格尺寸
	 */
	private int get9BoxSize()
	{
		return (m_size_9 + 1) * 3 + 1;
	}
	/**
	 * 获得按钮事件池，它的按钮事件被转移到了内部的文本框上
	 * @return 显示事件池
	 */
	public C2D_EventPool_Button Events_Button()
	{
		if(m_txtBox==null)
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
		m_strInput="";
		if(m_txtBox!=null)
		{
			m_txtBox.setText(m_strInput);
		}
	}
	/**
	 * 获取控件名称，它的获取名称被转移到了内部的文本框上
	 * @return 控件名称
	 */
	public String getName() 
	{
		if(m_txtBox!=null)
		{
			return m_txtBox.getName();
		}
		return null;
	}
	/**
	 * 设置控件名称，它的设置名称被转移到了内部的文本框上
	 * @param widgetName
	 */
	public void setName(String widgetName) 
	{
		if(m_txtBox!=null)
		{
			m_txtBox.setName(widgetName);
		}
	}
}
