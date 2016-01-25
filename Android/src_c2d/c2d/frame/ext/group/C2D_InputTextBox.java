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
	// ����������ַ���
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

	private String m_strInput; // ��ǰ���������
	private char m_charNext; // �¸�������ַ�
	private int m_lastKey = -1; // �ϴ����밴��
	private int m_keyTime; // �������밴������
	private int m_charNextID; // �¸������ַ�����ID
	private int m_RowLen; // �¸������ַ������еĳ���
	private int m_waitTime; // ��ǰ�ȴ��޸�ʱ��
	private int m_waitMaxTime = 800; // ��������ʱ�ȴ��޸����ʱ��
	private int m_waitMaxTime2 = 1600;// ʹ�÷����ʱ�ȴ��޸����ʱ��
	private boolean m_used9 = false; // �û����������Ƿ�ʹ���˷����
	private int m_maxInput=10;

	private int m_9bg_com = 0x2B2B2B; // δѡ��ʱ�Ź����ɫ
	private int m_9bg_focus = 0xAF360A; // ��ѡ��ʱ�Ź����ɫ
	private int m_9txt_com = 0xFFFFFF;// δѡ��ʱ�Ź����ı�ɫ
	private int m_9txt_focus = 0xFFFFFF;// ��ѡ��ʱ�Ź����ı�ɫ
	private int m_9bg_all = 0x0; // �Ź�������������ɫ
	private int m_size_9 = 30;// �Ź���ĵ�Ԫ��ߴ�

	private C2D_PicTextBox m_txtBox;// �ı���ʾ��
	private C2D_View m_pwd9Box; // �Ź���
	private C2D_PTC m_txtCfg; // �ı�������
	private boolean m_pwdMode;//ʹ������ģʽ
	private C2D_Color m_focusedColor;//ӵ�н���ʱ�ı��򱳾���ɫ
	private boolean m_delByLeft=false;//�Ƿ�ʹ������������������������ʹ�÷��ؼ�����
	/**
	 * ����ʹ������ģʽ
	 * @param pwdMode
	 */
	public void setPwdMode(boolean pwdMode)
	{
		m_pwdMode = pwdMode;
	}
	/**
	 * ����Ƿ�ʹ������ģʽ
	 * @return �Ƿ�ʹ������ģʽ
	 */
	public boolean getPwdMode()
	{
		return m_pwdMode;
	}
	/**
	 * ���û�ý���ʱ�ı���ı�����ɫ
	 * @param color
	 */
	public void setFocusedTxtBgColor(C2D_Color color)
	{
		m_focusedColor=color;
	}
	/**
	 * ����������������λ��
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
	 * ���þŹ���ĵ�Ԫ���С
	 * @param size ��Ԫ���С
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
	 * �������ô�С
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
	// ���ݰ�����ü�ֵ
	private void setNextKey(int keyCode, int inputTime)
	{
		int keyType = keyCode - C2D_Device.key_num0;
		if (keyType < 0 || keyType >= m_charList.length)
		{
			return;
		}
		// �����������ַ�
		m_RowLen = m_charList[keyType].length;
		m_charNextID = inputTime % m_RowLen;
		char next = m_charList[keyType][m_charNextID];
		if (next != m_charNext)
		{
			m_charNext = next;
			m_txtBox.setText(getStar(m_strInput) + m_charNext);
		}
		// ˢ��
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
	 * ���ʹ������ģʽ�����ظ�s����һ�����Ǻ��ַ���
	 * ���򷵻�ԭ�ַ���
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

	// ��λ���밴��
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
	 * �����Ƿ�ʹ������������������������ʹ�÷��ؼ�����
	 * @param delByLeft �Ƿ�ʹ�������������
	 */
	public void setDelByLeft(boolean delByLeft)
	{
		m_delByLeft = delByLeft;
	}
	/**
	 * ����Ƿ�ʹ������������������������ʹ�÷��ؼ�����
	 * @return �Ƿ�ʹ�������������
	 */
	public boolean getDelByLeft()
	{
		return m_delByLeft;
	}
	/**
	 * �Ƿ���������
	 * @param stage
	 * @return �Ƿ���������
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
	 * ���봦��
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
			// ������
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
			// ���ּ�
			else
			{
				if (m_strInput.length() < m_maxInput)
				{
					if (m_charNext == 0)// ��û�еȴ��ַ�
					{
						m_keyTime = 0;
						setNextKey(keyNum, m_keyTime);
					}
					else
					{
						if (m_lastKey == keyNum)// �ظ�����
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
						// �任����
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
		// �����,����ϵͳ��Ӧ�Ĳ��㣬��Ӧ���Ҽ�ʱ��������ƶ�
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
	 * �������ӵ�н��㣬ע��ϵͳ����setFocusedWidget()���������Ч��
	 * ʵ���������������������ӵ�н��㣬������������������ڲ����ı���ӵ�н��㡣
	 */
	public void setFocused()
	{
		if(m_txtBox!=null&&getSceneAt()!=null)
		{
			getSceneAt().setFocusedWidget(m_txtBox);
		}
	}
	/**
	 * ���α���Ľ��㣬���������ӵ�н���
	 */
	public void setFocusable(boolean focusable)
	{
		this.m_focusable = false;
	}
	/**
	 * ��ʼ���������ڲ����
	 */
	private void init()
	{
		if(m_txtCfg==null||!isEmpty())
		{
			return;
		}
		// ��������
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
		// �����¼�
		Events_Update().add(new C2D_Event_Update()
		{
			protected boolean doEvent(C2D_Widget carrier)
			{
				inputLogic();
				return false;
			}
		});
		// �ƶ�����¼�
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
				// �û�ʹ���˷���������õȴ�ʱ����°���
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
		// �Ź���
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
	 * �����Ƿ���������
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
	 * ��þŹ���ߴ�
	 * @return �Ź���ߴ�
	 */
	private int get9BoxSize()
	{
		return (m_size_9 + 1) * 3 + 1;
	}
	/**
	 * ��ð�ť�¼��أ����İ�ť�¼���ת�Ƶ����ڲ����ı�����
	 * @return ��ʾ�¼���
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
	 * �����������
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
	 * ��ȡ�ؼ����ƣ����Ļ�ȡ���Ʊ�ת�Ƶ����ڲ����ı�����
	 * @return �ؼ�����
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
	 * ���ÿؼ����ƣ������������Ʊ�ת�Ƶ����ڲ����ı�����
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
