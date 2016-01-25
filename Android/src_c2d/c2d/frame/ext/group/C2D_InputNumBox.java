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
	private String m_strInput; // ��ǰ���������
	private C2D_PicTextBox m_txtBox;// �ı���ʾ��
	private C2D_PTC m_txtCfg; // �ı�������
	private boolean m_pwdMode;//ʹ������ģʽ
	private C2D_Color m_focusedColor;//ӵ�н���ʱ�ı��򱳾���ɫ
	private boolean m_delByLeft = false;//�Ƿ�ʹ������������������������ʹ�÷��ؼ�����
	/** �¼���-��ý��� */
	protected C2D_EventPool_ChangeStrValue m_Events_value;

	/**
	 * ����ʹ������ģʽ
	 * 
	 * @param pwdMode
	 */
	public void setPwdMode(boolean pwdMode)
	{
		m_pwdMode = pwdMode;
	}

	/**
	 * �ı���ֵ�¼���
	 * 
	 * @return �¼���
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
	 * ����Ƿ�ʹ������ģʽ
	 * 
	 * @return �Ƿ�ʹ������ģʽ
	 */
	public boolean getPwdMode()
	{
		return m_pwdMode;
	}

	/**
	 * ���û�ý���ʱ�ı���ı�����ɫ
	 * 
	 * @param color
	 */
	public void setFocusedTxtBgColor(C2D_Color color)
	{
		m_focusedColor = color;
	}

	/**
	 * ����������������λ��
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
	 * �������ô�С
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
	 * ���ʹ������ģʽ�����ظ�s����һ�����Ǻ��ַ��� ���򷵻�ԭ�ַ���
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
	 * �����Ƿ�ʹ������������������������ʹ�÷��ؼ�����
	 * 
	 * @param delByLeft
	 *            �Ƿ�ʹ�������������
	 */
	public void setDelByLeft(boolean delByLeft)
	{
		m_delByLeft = delByLeft;
	}

	/**
	 * ����Ƿ�ʹ������������������������ʹ�÷��ؼ�����
	 * 
	 * @return �Ƿ�ʹ�������������
	 */
	public boolean getDelByLeft()
	{
		return m_delByLeft;
	}

	/**
	 * �Ƿ���������
	 * 
	 * @param stage
	 * @return �Ƿ���������
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
	 * ���봦��
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
			// ������
			if (isDelKey(stage))
			{
				if (m_strInput.length() > 0)
				{
					destTxt = m_strInput.substring(0, m_strInput.length() - 1);
				}
			}
			// ���ּ�
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
	 * �������ӵ�н��㣬ע��ϵͳ����setFocusedWidget()���������Ч��
	 * ʵ���������������������ӵ�н��㣬������������������ڲ����ı���ӵ�н��㡣
	 */
	public void setFocused()
	{
		if (m_txtBox != null && getSceneAt() != null)
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
		if (m_txtCfg == null || !isEmpty())
		{
			return;
		}
		// ��������
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
		// �����¼�
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
	 * ��ð�ť�¼��أ����İ�ť�¼���ת�Ƶ����ڲ����ı�����
	 * 
	 * @return ��ʾ�¼���
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
	 * �����������
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
	 * ��ȡ�ؼ����ƣ����Ļ�ȡ���Ʊ�ת�Ƶ����ڲ����ı�����
	 * 
	 * @return �ؼ�����
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
	 * ���ÿؼ����ƣ������������Ʊ�ת�Ƶ����ڲ����ı�����
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
