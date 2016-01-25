package c2d.frame.base;

import c2d.frame.event.C2D_EventPool_Button;
import c2d.frame.event.C2D_EventPool_ChangeFocus;
import c2d.frame.event.C2D_EventPool_KeyPress;
import c2d.frame.event.C2D_EventPool_MotionEnd;
import c2d.frame.event.C2D_EventPool_Touch;
import c2d.frame.event.C2D_EventPool_Update;
import c2d.frame.event.C2D_EventPool_WaitTime;
import c2d.frame.event.C2D_Event_RemovedFromView;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;
import c2d.lang.math.type.C2D_TouchData;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectI;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.mod.physic.C2D_Motion;
import c2d.mod.physic.C2D_PhysicBox;
import c2d.mod.physic.C2D_PhysicBoxCreator;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * ÿ�������ڲ�ʹ����״�ṹ���й���ÿ���ؼ�������״�ؼ����Ͻṹ�е�һ���ڵ㡣 �ؼ������ࡣ
 * ��һ������ͨ�ؼ������Ǵ����ڵڶ���ؼ���������ͼ���С�ÿ���ؼ���ӵ�����ꡢê�㡢Z����ֵ�Ȼ������ԡ� ���ǵ��������������꣬������丸����
 * ����ˣ������������ƶ�ʱ�����е�����������һ���ƶ����ڻ�ȡ��Щ���Ե�ʱ�� ��Ҫʹ�ú�
 * ��getXXX�����õ�ʱ����Ҫʹ��setXXX����ʹ�̳е���Ҳ��Ҫ�������ã�һ������ֱ�� �ı�����ֵ��Z����
 * ֵ�������ֵܽڵ�֮������򣬴���ֵԽС���ؼ�Խ�����²㡣 �ڶ���ؼ�����ͼ����������Ŀؼ�������Ҳ����ͼ���� ��ͼ��
 * һ�������������Լ����ӽڵ�(addChild)�������Ƴ��ڵ�(removeChild) ����ýڵ�(getChildAt)��
 * �����ڵ�(C2D_Scene)�ǿؼ����ĸ��ڵ㣬���еĿؼ���������׷�ݵ�����ڵĳ�����ֻ����̨(C2D_Stage)�ĵ�
 * ǰ�����ſ�����ʾ������ӵ���²㱳��ɫ���б���ͼƬ���ϲ������Ӹ�����ͨ�ؼ�������㲻���ñ���ɫ�ͱ���ͼƬ�� ��ͼ�� ��Ϊһ��͸����������
 * 
 * @author AndrewFan
 */
public abstract class C2D_Widget extends C2D_Object implements C2D_Order, C2D_Consts
{
	/** ���ƣ����ڱ�ǿؼ� */
	protected String m_widgetName;
	/** ������ʾ��X����.������꽫����m_xF�����仯 */
	public float m_x;
	/** ������ʾ��Y����,������꽫����m_yF�����仯 */
	public float m_y;
	/**
	 * ������ʾ����Զ�����ͼ��X���꣬������꽫����m_x�����仯 ע��������丸����к��л�����ͼ(BufferedView)�Ļ����������
	 * ������������ӽ�����Ļ�����ͼ
	 */
	protected float m_xToTop;
	/**
	 * ������ʾ����Զ�����ͼ��Y���꣬������꽫����m_y�����仯 ע��������丸��㼶�к��л�����ͼ(BufferedView)�Ļ����������
	 * ������������ӽ�����Ļ�����ͼ
	 */
	protected float m_yToTop;
	/** z���� */
	protected int m_zOrder;
	/** �ؼ����Ͻǵ���Ը��ڵ����꣬ע�ⲻ��ê������ */
	protected C2D_PointF m_LT2Root = new C2D_PointF();
	/** �Ƿ���Ҫ����XYƽ��������� */
	protected boolean m_needUpdatePos = true;
	/** �Ƿ���Ҫ���»�ȡ���Ͻ����� */
	protected boolean m_needUpdateLT = true;
	/** �Ƿ���Ҫ���¿ɼ��� */
	protected boolean m_needUpdateVisible = false;
	/** ê������. */
	protected int m_anchor = 0;
	/** �Ƿ�ɼ������ڱ�ʶ�ؼ��Ŀɼ�״̬ */
	protected boolean m_visible = true;
	/** �Ƿ񱻸���ؼ������� */
	protected boolean m_hiddenByParent = false;
	/** �Ƿ����������ͷ�У�����Ҫ�õ������ͼ������Ҫʹ�õ�������� */
	public boolean m_inCamera = true;
	/** �����Ƿ�����Ļ��Χ�ڣ�ʼ��ִ�и����߼� */
	public boolean m_alwaysUpdate;
	/** ���ڵ� */
	protected C2D_View m_parentNode;
	/** �Ƿ���Ҫ����������Z���� */
	protected boolean m_needOrder = true;
	/**
	 * �����㣬��������ʱ��������Ϣ��ֻ�ᱻ�������͵���ǰ������� �����д��������û�������Widget�ĸ����¼�����ǰ�ػ񰴼�������
	 */
	protected boolean m_focusable = false;
	/** ��ǰ�ؼ��Ƿ�ӵ�н��� **/
	protected boolean m_focused = false;
	/** ����ͼƬ�����ؼ���Ϊ����ʱ������ʾ��ͼƬ */
	protected C2D_ImageClip m_focusImgClip;
	/** ����ͼƬ��λ�����꣬����ڵ�ǰ�ؼ����� */
	protected float m_focusX, m_focusY;
	/** ��ǰ�ؼ�������������������ڵ�ǰ�ؼ��������һ������ */
	protected C2D_RectF m_hotRegion;
	/** �Ƿ񼤻����� */
	protected boolean m_useHotRegion = false;
	/** �¼���-�ƶ���� */
	private C2D_EventPool_MotionEnd m_Events_MotionEnd;
	/** �¼���-��ý��� */
	protected C2D_EventPool_ChangeFocus m_Events_Focus;
	/** �����ƶ����󼯺ϣ���Щ�ƶ�����ͬʱ���� */
	private C2D_Array m_motions;
	/** �û��Զ�����. */
	protected int m_iFlag = -1;
	/** �û��Զ�����. */
	protected String m_strFlag;
	/** ����󶨺� */
	public C2D_PhysicBox m_physicBox;
	/** �¼���-�ȴ�ʱ�� */
	private C2D_EventPool_WaitTime m_Events_WaitTime;
	/** �¼���-���� */
	private C2D_EventPool_Update m_Events_Update;
	/** ��ť�¼��� */
	protected C2D_EventPool_Button m_Events_Button;
	/** �����¼��� */
	protected C2D_EventPool_KeyPress m_Events_KeyPress;
	/** �����¼��� */
	protected C2D_EventPool_Touch m_Events_Touch;
	/** ���Ӹ������Ƴ�ʱ����Ӧ���¼� */
	protected C2D_Event_RemovedFromView m_removedEvt;
	/** ���ñ��� */
	protected static C2D_PointF point_com1 = new C2D_PointF();
	protected static C2D_PointF point_com2 = new C2D_PointF();
	protected static C2D_RectI rect_com1 = new C2D_RectI();
	/** ���ڵ���̨ */
	protected C2D_Stage m_atStage;
	/** ���ڵĳ��� */
	protected C2D_Scene m_atScene;

	/**
	 * ��ȡ�ؼ�����
	 * 
	 * @return �ؼ�����
	 */
	public String getName()
	{
		return m_widgetName;
	}

	/**
	 * ���ÿؼ�����
	 * 
	 * @param widgetName
	 */
	public void setName(String widgetName)
	{
		this.m_widgetName = widgetName;
	}

	/**
	 * ����zOrder��Ϊ����Ȩֵ
	 */
	public int getOrderValue(int orderType)
	{
		return m_zOrder;
	}

	/**
	 * ����zOrder
	 */
	public int getZOrder()
	{
		return m_zOrder;
	}

	/**
	 * ��ȡ�Լ�λ�ڸ������е�ID
	 * 
	 * @return ID
	 */
	public int getID()
	{
		if (m_parentNode != null)
		{
			return m_parentNode.m_nodeList.indexOf(this);
		}
		return -1;
	}

	/**
	 * ��������Ȩֵ��ԽС����Խ������Ļ�ڲ������������������򣬻��ڱ���ѭ���Ļ�ͼ֮ǰ�����ȷ��˳��
	 * �����Ҫ���̻����ȷ��˳����Ҫ�ֶ������丸������orderChildren()
	 * 
	 * @param zOrder
	 */
	public void setZOrder(int zOrder)
	{
		m_zOrder = zOrder;
		layoutChanged();
		if (m_parentNode != null)
		{
			m_parentNode.m_needOrder = true;
		}
	}

	/**
	 * ��ȡX����.
	 * 
	 * @return float X����
	 */
	public float getX()
	{
		return m_x;
	}

	/**
	 * ��ȡ��Զ���ڵ�ľ���X����.
	 * 
	 * @return float X����
	 */
	public float getXToTop()
	{
		return m_xToTop;
	}

	/**
	 * ����X����.
	 * 
	 * @param posX
	 *            ��X����
	 * @return �����Ƿ����ı�
	 */
	public boolean setXTo(float posX)
	{
		if (m_x == posX)
		{
			return false;
		}
		// ���¾�������
		m_x = posX;
		layoutChanged();
		return true;
	}

	/**
	 * ��ȡY����.
	 * 
	 * @return float Y����
	 */
	public float getY()
	{
		return m_y;
	}

	/**
	 * ��ȡ��Զ���ڵ�ľ���Y����.
	 * 
	 * @return float Y����
	 */
	public float getYToTop()
	{
		return m_yToTop;
	}

	/**
	 * ��ȡ�ؼ��ľ������꣬ע��������丸����к��л�����ͼ(BufferedView)�Ļ���������� ������������ӽ�����Ļ�����ͼ
	 * 
	 * @param value
	 *            ��ŷ��ؽ���Ķ������
	 * @return �Ƿ��������
	 */
	public boolean getPosToTop(C2D_PointF value)
	{
		if (value == null)
		{
			return false;
		}
		value.setValue(m_xToTop, m_yToTop);
		return true;
	}

	/**
	 * ����Y����.
	 * 
	 * @param posY
	 *            ��X����
	 * @return �Ƿ�ɹ�����
	 */
	public boolean setYTo(float posY)
	{
		m_y = posY;
		layoutChanged();
		return true;
	}

	/**
	 * ���ÿؼ���ָ��λ��.
	 * 
	 * @param x
	 *            float X����
	 * @param y
	 *            float Y����
	 * @return �Ƿ�ɹ�����
	 */
	public boolean setPosTo(float x, float y)
	{
		m_x = x;
		m_y = y;
		layoutChanged();
		return true;
	}

	/**
	 * ���ÿؼ���ָ��λ��.ʹ����Ը������İٷֱ�����
	 * 
	 * @param x
	 *            float X�ٷֱ�����
	 * @param y
	 *            float Y�ٷֱ�����
	 */
	public boolean setPosPer(float x, float y)
	{
		if (m_parentNode == null)
		{
			return false;
		}
		return setPosTo(m_parentNode.m_width * x / 100, m_parentNode.m_height * y / 100);
	}

	/**
	 * ���ÿؼ���ָ��λ��.
	 * 
	 * @param pos
	 *            ����
	 */
	public boolean setPosTo(C2D_PointF pos)
	{
		if (pos == null)
		{
			return false;
		}
		m_x = pos.m_x;
		m_y = pos.m_y;
		layoutChanged();
		return true;
	}

	/**
	 * ����X�����ڵ�ǰ�����Ͻ���ƫ��.
	 * 
	 * @param offset
	 *            ƫ������
	 */
	public void setXBy(float offset)
	{
		// ���¾�������
		m_x += offset;
		layoutChanged();
	}

	/**
	 * ����Y�����ڵ�ǰ�����Ͻ���ƫ��.
	 * 
	 * @param offset
	 *            ƫ������
	 */
	public void setYBy(float offset)
	{
		// ���¾�������
		m_y += offset;
		layoutChanged();
	}

	/**
	 * ����X��Y�����ڵ�ǰ�����Ͻ���ƫ��.
	 * 
	 * @param offsetX
	 *            X����ƫ������
	 * @param offsetY
	 *            Y����ƫ������
	 */
	public void setPosBy(float offsetX, float offsetY)
	{
		// ���¾�������
		m_x += offsetX;
		m_y += offsetY;
		layoutChanged();
	}

	/**
	 * ˢ�����꣬�⽫�Ѿ�ȷ�����ֵ���ݸ��������꣬ ��������Զ�����ͼ������͸�����ʾ��ռ������
	 * һ����˵���������Ĭ�ϻ�����������Ϣ�����仯ʱ�Զ������ã������ֶ����á�
	 */
	public void refreshPos()
	{
		if (m_parentNode != null)
		{
			m_parentNode.getPosByChild(point_com1);
			m_xToTop = point_com1.m_x + m_x;
			m_yToTop = point_com1.m_y + m_y;
		}
		else
		{
			m_xToTop = m_x;
			m_yToTop = m_y;
		}
	}

	/**
	 * ����ê������.
	 * 
	 * @param anchor
	 *            ��ê������
	 */
	public void setAnchor(int anchor)
	{
		m_anchor = anchor;
		layoutChanged();
	}

	/**
	 * ��ȡê������
	 * 
	 * @return ê������
	 */
	public int getAnchor()
	{
		return m_anchor;
	}

	/**
	 * ���ظ��ڵ�
	 * 
	 * @return ���ڵ�
	 */
	public C2D_View getParentNode()
	{
		return m_parentNode;
	}

	/**
	 * ���ı䲼�֣���Ҫ���¸������꣬���Ͻ�����ȡ�
	 */
	public void layoutChanged()
	{
		m_needUpdatePos = true;
		m_needUpdateLT = true;
		getStageAt();
		if (m_atStage != null)
		{
			m_atStage.m_needRepaint = true;
		}
		noticeParent();
	}

	/**
	 * ��֪����ı�
	 */
	protected void noticeParent()
	{
		if (m_parentNode != null)
		{
			m_parentNode.noticedByChild();
		}
	}

	/**
	 * ������Ҫ����λ��
	 */
	protected void setUpdatePos()
	{
		m_needUpdatePos = true;
		m_needUpdateLT = true;
	}

	protected static C2D_RectI rectR1 = new C2D_RectI();

	/**
	 * ��ȡ���ڵ���̨����
	 * 
	 * @return ���ڵ���̨����
	 */
	public C2D_Stage getStageAt()
	{
		if (m_atStage == null)
		{
			m_atStage = accountStage();
		}
		return m_atStage;
	}

	/**
	 * �������ڵ���̨����
	 * 
	 * @return ���ڵ���̨����
	 */
	public C2D_Stage accountStage()
	{
		C2D_Widget topElement = this;
		while (topElement.m_parentNode != null)
		{
			topElement = topElement.m_parentNode;
		}
		if (!(topElement instanceof C2D_Scene))
		{
			return null;
		}
		m_atStage = topElement.accountStage();
		return m_atStage;
	}

	/**
	 * �������ڵĳ�������
	 * 
	 * @return ���ڵĳ�������
	 */
	public C2D_Scene getSceneAt()
	{
		if (m_atScene == null)
		{
			m_atScene = accountScene();
		}
		return m_atScene;
	}

	/**
	 * �������ڵĳ�������
	 * 
	 * @return ���ڵĳ�������
	 */
	public C2D_Scene accountScene()
	{
		C2D_Widget topElement = this;
		while (topElement.m_parentNode != null)
		{
			topElement = topElement.m_parentNode;
		}
		if (!(topElement instanceof C2D_Scene))
		{
			return null;
		}
		m_atScene = topElement.accountScene();
		return m_atScene;
	}

	/**
	 * ���ƽڵ�
	 * 
	 * @param g
	 *            ����
	 */
	protected abstract void onPaint(C2D_Graphics g);

	protected static C2D_Array array_Paint = new C2D_Array();

	/**
	 * ���ƽ��㣬ʹ������Ŀ�������
	 * 
	 * @param g
	 *            ����
	 * @param layW
	 *            �ؼ�ռ�ݵĿ��
	 * @param layH
	 *            �ؼ�ռ�ݵĸ߶�
	 */
	protected void paintFocus(C2D_Graphics g, float layW, float layH)
	{
		// ���ƽ���
		if (m_focused && m_focusImgClip != null)
		{
			C2D_PointF pointF = C2D_GdiGraphics.applyAnchor(m_xToTop, m_yToTop, layW, layH, m_anchor);
			m_focusImgClip.draw(pointF.m_x + m_focusX, pointF.m_y + m_focusY);
		}
	}

	/**
	 * ���ƽ��㣬ʹ������Ŀ��������˫�����޶�
	 * 
	 * @param g
	 *            ����
	 * @param layW
	 *            �ؼ�ռ�ݵĿ��
	 * @param layH
	 *            �ؼ�ռ�ݵĸ߶�
	 * @param rects
	 *            �������򼯺�
	 */
	protected void paintFocus(C2D_Graphics g, int layW, int layH, C2D_Array rects)
	{
		boolean paintFox = (m_focused && m_focusImgClip != null && g.applyClip(m_xToTop, m_yToTop, layW, layH, m_anchor, null, point_com2));
		if (paintFox)
		{
			array_Paint.removeAllElements();
			array_Paint.addElement(null);
			m_focusImgClip.draw(point_com2.m_x + m_focusX, point_com2.m_y + m_focusY, 0);
			array_Paint.removeAllElements();
		}
	}

	private C2D_Array m_motionsBuf = new C2D_Array();

	/**
	 * ���������ӽڵ㣬ִ�и���
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		updateSelf(stage);
	}

	/**
	 * �����ж������������˶�����
	 */
	public void autoMotion()
	{
		if (m_physicBox != null)
		{
			m_physicBox.autoMotion();
		}
	}

	/**
	 * �����Լ�
	 * 
	 * @param stage
	 */
	protected void updateSelf(C2D_Stage stage)
	{
		// �����ƶ�
		if (m_motions != null)
		{
			m_motionsBuf.clear();
			// ����¼�
			int numMotion = m_motions.size();
			for (int i = 0; i < numMotion; i++)
			{
				m_motionsBuf.addElement(m_motions.m_datas[i]);
			}
			// ��ʼִ��
			int numMotionBuf = m_motions.size();
			for (int i = 0; i < numMotionBuf; i++)
			{
				C2D_Motion m_currentMotion = (C2D_Motion) m_motionsBuf.m_datas[i];
				if (m_currentMotion != null && !m_currentMotion.isOver())
				{
					if (m_currentMotion.doMotion(stage.getTimePassed()))
					{
						// �ƶ�����¼�
						if (m_Events_MotionEnd != null)
						{
							m_Events_MotionEnd.onCalled(m_currentMotion);
						}
						// �Ƴ���ǰ�ƶ�
						if (m_motions != null)
						{
							m_motions.remove(m_currentMotion);
						}
						m_currentMotion.doRelease();
					}
				}
			}
		}
		// ����ȴ�
		if (m_Events_WaitTime != null)
		{
			m_Events_WaitTime.onCalled(stage.getTimePassed());
		}
		// �������
		if (m_Events_Update != null)
		{
			m_Events_Update.onCalled();
		}
		// ��������Ӧ
		processKeyCalls(stage);
		// �������¼���Ӧ���������
		processNavigations(stage);
		// ����ť��Ӧ
		processBtnCalls(stage);
	}

	/**
	 * ��������Ӧ�� �˷�������ϵͳ���ã���Ӧ�����ⲿ�ֶ����á�
	 * 
	 * @param stage
	 */
	protected void processKeyCalls(C2D_Stage stage)
	{
		int keyCode = stage.getSingleKey();
		if (keyCode >= C2D_Device.key_up && keyCode < C2D_Device.key_other)
		{
			if (m_Events_KeyPress != null)
			{
				m_Events_KeyPress.onCalled(keyCode);
			}
		}
	}

	/**
	 * ��Ӧ�ڿؼ��ϲ����������󰴼���ʵ�ֽ�����ƶ��� �˷�������ϵͳ���ã���Ӧ�����ⲿ�ֶ����á�
	 * 
	 * @param stage
	 *            ���ڳ���
	 */
	protected void processNavigations(C2D_Stage stage)
	{
		if (m_atScene == null || !m_focused || !m_visible)
		{
			return;
		}
		// �������
		int keyCode = stage.getSingleKey();
		if (!anyKeyUsed(keyCode))
		{
			if (keyCode >= C2D_Device.key_up && keyCode <= C2D_Device.key_left)
			{
				if (m_atScene.moveFocus(this, keyCode))
				{
					stage.releaseKeys();
				}
			}
		}
	}

	/**
	 * ��������İ�ť��Ӧ�� �˷�������ϵͳ���ã���Ӧ�����ⲿ�ֶ����á�
	 * 
	 * @param stage
	 *            ���ڳ���
	 */
	protected void processBtnCalls(C2D_Stage stage)
	{
		if (!m_visible||m_Events_Button == null)
		{
			return;
		}
		m_Events_Button.processBtnCall(stage);
	}

	/**
	 * �Ƿ�ĳ�������Ѿ���ʹ�ã�����Ѿ�������ؼ�ʹ�õģ������ȱ�����ؼ���Ӧ
	 * 
	 * @param key
	 *            ����
	 * @return �Ƿ�ʹ��
	 */
	public boolean anyKeyUsed(int key)
	{
		return false;
	}

	/** ��ǰ�ؼ���������Ӧ������ʱ���� */
	private static C2D_RectF CountHotRegion = new C2D_RectF();

	/**
	 * ����Ƿ�����ӵ�д���
	 * 
	 * @return ���ش���
	 */
	public boolean hasTouchPoint()
	{
		return getTouchPoint()!=null;
	}
	private static C2D_PointF M_cp=new C2D_PointF();
	/**
	 * ����Ƿ�����ӵ�д��㣬���ӵ���򷵻ش���
	 * 
	 * @return ���ش���
	 */
	public C2D_PointF getTouchPoint()
	{
		C2D_Stage stage = getStageAt();
		if (m_hotRegion == null || stage == null || stage.m_curTouchData == null)
		{
			return null;
		}
		// ������������
		C2D_TouchData td = stage.m_curTouchData;
		if (td.m_touchCount == 0)
		{
			return null;
		}
		CountHotRegion.setValue(m_hotRegion);
		C2D_PointF plt = getLeftTop();
		CountHotRegion.m_x = plt.m_x;
		CountHotRegion.m_y = plt.m_y;
		for (int i = 0; i < td.m_touchStates.length; i++)
		{
			if (td.m_touchStates[i])
			{
				if (td.m_touchPoints[i].inRegion(CountHotRegion))
				{
					M_cp.setValue(td.m_touchPoints[i]);
					return M_cp;
				}
			}
		}
		return null;
	}
	/**
	 * ����Ƿ����ƶ��Ĵ���λ�ý���
	 * 
	 * @return �Ƿ����ƶ��Ĵ���λ�ý���
	 */
	public boolean crossWithTouchPoint(C2D_PointF point)
	{
		C2D_Stage stage = getStageAt();
		if (m_hotRegion == null || stage == null ||point == null)
		{
			return false;
		}
		// ������������
		CountHotRegion.setValue(m_hotRegion);
		C2D_PointF plt = getLeftTop();
		CountHotRegion.m_x = plt.m_x;
		CountHotRegion.m_y = plt.m_y;
		return (point.inRegion(CountHotRegion));
	}
	/**
	 * �Ƿ�ɼ�.
	 * 
	 * @return boolean �Ƿ�ɼ�
	 */
	public boolean getVisible()
	{
		return m_visible;
	}

	/**
	 * �����Ƿ�ɼ�.
	 * 
	 * @param visibleNew
	 *            �Ƿ�ɼ�
	 */
	public void setVisible(boolean visibleNew)
	{
		if (m_visible != visibleNew)
		{
			m_visible = visibleNew;
			layoutChanged();
		}
	}

	/**
	 * �����Ƿ�λ�ڵ�ǰ��ͷ�У�ֻ�о�ͷ�еĶ���ſ��Ա���ʾ
	 * 
	 * @param inCamera
	 *            �Ƿ�λ�ڵ�ǰ��ͷ��
	 */
	public void setInCamera(boolean inCamera)
	{
		m_inCamera = inCamera;
	}

	/**
	 * �鿴�Ƿ�λ�ڵ�ǰ��ͷ�У�ֻ�о�ͷ�еĶ���ſ��Ա���ʾ
	 * 
	 * @return �Ƿ�λ�ڵ�ǰ��ͷ��
	 */
	public boolean isInCamera()
	{
		return m_inCamera;
	}

	protected static C2D_RectF rectFTemp = new C2D_RectF();

	/**
	 * ��ȡ�ؼ�ռ����������Ͻǵ��������꣬<br>
	 * �������������Ŀؼ���˵������������꣬������ͼ��˵���������ϽǾ������� ��<br>
	 * ע��������丸��㼶�к��л�����ͼ(BufferedView)�Ļ���<br>
	 * ������꽫����������ӽ�����Ļ�����ͼ��
	 * 
	 * @return ���Ͻǵ����������
	 */
	protected C2D_PointF getLeftTop()
	{
		if (m_needUpdateLT)
		{
			rectFTemp.m_x = m_xToTop;
			rectFTemp.m_y = m_yToTop;
			rectFTemp.m_width = getWidth();
			rectFTemp.m_height = getHeight();
			C2D_PointF newPos = C2D_Graphics.applyAnchor(rectFTemp, m_anchor);
			m_LT2Root.m_x = newPos.m_x;
			m_LT2Root.m_y = newPos.m_y;
			m_needUpdateLT = false;
		}
		return m_LT2Root;
	}

	protected abstract float getWidth();

	protected abstract float getHeight();

	/**
	 * �Ƿ�û�б����أ��⽫������и��ڵ㣬 ȷ���Լ�û�б�����һ�����ڵ�������
	 * 
	 * @return boolean �Ƿ�������ʾ
	 */
	public boolean allowedShow()
	{
		return m_visible && !m_hiddenByParent;
	}

	/**
	 * ���¿ɼ���
	 */
	public void refreshVisible()
	{
		C2D_Widget p = m_parentNode;
		while (p != null)
		{
			if (!p.m_visible)
			{
				m_hiddenByParent = true;
				break;
			}
			p = p.m_parentNode;
		}
	}

	/**
	 * ����ָ����ʱ�䣬ִ�������ƶ�ָ����λ��(ȫ������)���ƶ���ϴ������ƶ������¼���
	 * 
	 * @param offX
	 *            ȫ��Xƫ��
	 * @param offY
	 *            ȫ��Yƫ��
	 * @param time
	 *            ���ѵ�ʱ�䣬����Ϊ��λ
	 * @param a
	 *            ���ٶ�
	 */
	public void moveBy(float offX, float offY, int time, float a)
	{
		addMotion(new C2D_Motion(this, offX, offY, a, time));
	}

	/**
	 * ��Ӷ���,���������ظ����
	 * 
	 * @param motion
	 *            ����
	 */
	public void addMotion(C2D_Motion motion)
	{
		if (motion == null)
		{
			return;
		}
		if (m_motions == null)
		{
			m_motions = new C2D_Array();
		}
		if (!m_motions.contains(motion))
		{
			m_motions.addElement(motion);
		}
	}

	/**
	 * �Ƴ����еĶ���
	 */
	public void clearMotions()
	{
		if (m_motions != null)
		{
			int size = m_motions.size();
			for (int i = 0; i < size; i++)
			{
				C2D_Object oI = (C2D_Object) m_motions.elementAt(i);
				if (oI != null)
				{
					oI.doRelease();
				}
			}
			m_motions.removeAllElements();
			m_motions = null;
		}
	}

	/**
	 * ����ָ����ʱ�䣬ִ�������ƶ�ָ����λ��(ȫ������)���ƶ���ϴ������ƶ������¼���
	 * 
	 * @param offX
	 *            Xƫ��
	 * @param offY
	 *            Yƫ��
	 * @param time
	 *            ���ѵ�ʱ�䣬����Ϊ��λ
	 */
	public void moveBy(float offX, float offY, int time)
	{
		moveBy(offX, offY, time, 0);
	}

	/**
	 * ����ָ����ʱ�䣬�����ƶ�����Ŀ��λ�ã��ƶ���ϴ������ƶ������¼���
	 * 
	 * @param destX
	 *            Ŀ���X����
	 * @param destY
	 *            Ŀ���Y����
	 * @param time
	 *            ���ѵ�ʱ�䣬����Ϊ��λ
	 */
	public void moveTo(float destX, float destY, int time)
	{
		float offX = destX - m_x;
		float offY = destY - m_y;
		moveBy(offX, offY, time, 0);
	}

	/**
	 * ����ָ����ʱ�䣬�����ƶ�����Ŀ��λ�ã��ƶ���ϴ������ƶ������¼���
	 * 
	 * @param destX
	 *            Ŀ���X����
	 * @param destY
	 *            Ŀ���Y����
	 * @param time
	 *            ���ѵ�ʱ�䣬����Ϊ��λ
	 * @param a
	 *            ���ٶ�
	 */
	public void moveTo(float destX, float destY, int time, float a)
	{
		float offX = destX - m_x;
		float offY = destY - m_y;
		moveBy(offX, offY, time, a);
	}

	/**
	 * �Զ�����
	 */
	protected void onAutoUpdate()
	{
		// ˢ������XYƽ������
		if (m_needUpdatePos)
		{
			refreshPos();
			m_needUpdatePos = false;
		}
		// ˢ�¿ɼ���
		if (m_needUpdateVisible)
		{
			refreshVisible();
			m_hiddenByParent = false;
		}
		refreshHotRegion(false);
	}

	/**
	 * ��ȡ�Ƿ����㣬��������ʱ��������Ϣ��ֻ�ᱻ�������͵���ǰ������� �����д��������û�������Widget�ĸ����¼�����ǰ�ػ񰴼�������
	 * 
	 * @return �Ƿ����ӵ�н���
	 */
	public boolean getFocusable()
	{
		return m_focusable;
	}

	/**
	 * ����Ƿ�ӵ�н���
	 * 
	 * @return �Ƿ�ӵ�н���
	 */
	public boolean isFocused()
	{
		return m_focused;
	}

	/**
	 * �����Ƿ����ӵ�н��㣬��������ʱ��������Ϣ��ֻ�ᱻ�������͵���ǰ������� �����д��������û�������Widget�ĸ����¼�����ǰ�ػ񰴼�������
	 * 
	 * @param focusable
	 *            �Ƿ����ӵ�н���
	 */
	public void setFocusable(boolean focusable)
	{
		this.m_focusable = focusable;
		this.setUse_HotRegion(true);
	}

	/**
	 * ���ý���ͼƬ�Լ�����ͼƬ�����λ�ã���������ڵĽ�����Ƭ���󣬻��Զ��ÿؼ�����ӵ�н��㣬 ����յĽ�����Ƭ���󣬻��Զ��ÿؼ�����ӵ�н���
	 * 
	 * @param imgClip
	 *            ������Ƭ
	 * @param x
	 *            ������Ƭ��X����
	 * @param y
	 *            ������Ƭ��Y����
	 */
	public void setFocusImage(C2D_ImageClip imgClip, float x, float y)
	{
		if (m_focusImgClip != null)
		{
			m_focusImgClip.doRelease(this);
			m_focusImgClip = null;
		}
		m_focusImgClip = imgClip;
		// m_focusable = m_focusImgClip != null;
		m_focusX = x;
		m_focusY = y;
		layoutChanged();
	}

	/**
	 * �õ�ǰ�ؼ�ӵ�н��㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 ����ǰ�����Ľ���ؼ���������㲻Ӧ���ֶ��������������
	 * 
	 * @param another
	 *            ԭ����
	 */
	protected void gotFocus(C2D_Widget another)
	{
		C2D_Scene scene = getSceneAt();
		if (scene == null)
		{
			C2D_Debug.log("��error��in gotFocus()-->widget is not in scene tree!");
			return;
		}
		if(m_Events_Button!=null)
		{
			m_Events_Button.onFocused(true, another);
		}
		if (m_Events_Focus != null)
		{
			m_Events_Focus.onCalled(true, another);
		}
		if (m_focusImgClip != null)// TODO ���ܻ�������³���
		{
			layoutChanged();
		}
	}
	/**
	 * ʧȥ���㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 �㲻Ӧ���ֶ�������������������Ļ�����ǰҳ�潫ʧȥӵ�н���Ŀؼ�
	 * 
	 * @param another
	 *            Ŀ�꽹��
	 */
	protected void lostFocus(C2D_Widget another)
	{
		C2D_Stage stage = getStageAt();
		C2D_Scene scene = getSceneAt();
		if (stage == null || scene == null)
		{
			C2D_Debug.log("��error��in lostFocus()-->widget is not in scene tree!");
			return;
		}
		if(m_Events_Button!=null)
		{
			m_Events_Button.onFocused(false, another);
		}
		if (m_Events_Focus != null)
		{
			m_Events_Focus.onCalled(false, another);
		}
		if (m_focusImgClip != null)// TODO ���ܻ�������³���
		{
			layoutChanged();
		}
	}

	/**
	 * �˺������Ὣλ�����õ������������ģ����Ҳ���ˮƽ�ʹ�ֱ���ж����ê�㡣
	 */
	public void setToParentCenter()
	{
		if (m_parentNode != null)
		{
			setPosTo(m_parentNode.getWidth() / 2, m_parentNode.getHeight() / 2);
			setAnchor(C2D_Consts.HVCENTER);
		}
	}

	/**
	 * ��������λ�ã����֮ǰû�����������ᴴ������
	 * 
	 * @param _x
	 *            �������������������Ͻ�X����
	 * @param _y
	 *            �������������������Ͻ�Y������
	 * @param _w
	 *            �������
	 * @param _h
	 *            �����߶�
	 */
	public void setHotRegion(float _x, float _y, float _w, float _h)
	{
		if (m_hotRegion == null)
		{
			m_hotRegion = new C2D_RectF();
		}
		m_hotRegion.setValue(_x, _y, _w, _h);
	}

	/**
	 * ��������λ�ã����֮ǰû�����������ᴴ������
	 * 
	 * @param region
	 *            �����õ���������
	 */
	public void setHotRegion(C2D_RectF region)
	{
		if (m_hotRegion == null)
		{
			m_hotRegion = new C2D_RectF();
		}
		m_hotRegion.setValue(region);
	}

	/**
	 * ˢ������������������õĻ���
	 * 
	 * @param alwaysUpdate
	 *            �������Ѿ���������Ҳ����ˢ��
	 */
	public void refreshHotRegion(boolean alwaysUpdate)
	{
		if (m_useHotRegion)
		{
			if (m_hotRegion == null || alwaysUpdate)
			{
				setHotRegion(getXToTop(), getYToTop(), getWidth(), getHeight());
			}
		}
	}

	/**
	 * ����ƶ������¼���(TODO �����motion��������motionʱ������ջ�������⡣��Ҫ�޸�)
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_MotionEnd Events_MotionEnd()
	{
		if (m_Events_MotionEnd == null)
		{
			m_Events_MotionEnd = new C2D_EventPool_MotionEnd(this);
			m_Events_MotionEnd.transHadler(this);
			;
		}
		return m_Events_MotionEnd;
	}

	/**
	 * �ı佹���¼���
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_ChangeFocus Events_ChangeFocus()
	{
		if (m_Events_Focus == null)
		{
			m_Events_Focus = new C2D_EventPool_ChangeFocus(this);
		}
		return m_Events_Focus;
	}

	/**
	 * ��ø����¼���
	 * 
	 * @return �����¼���
	 */
	public C2D_EventPool_Update Events_Update()
	{
		if (m_Events_Update == null)
		{
			m_Events_Update = new C2D_EventPool_Update(this);
		}
		return m_Events_Update;
	}

	/**
	 * ��õȴ������¼���
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_WaitTime Events_WaitEnd()
	{
		if (m_Events_WaitTime == null)
		{
			m_Events_WaitTime = new C2D_EventPool_WaitTime(this);
		}
		return m_Events_WaitTime;
	}

	/**
	 * ��ð�ť�¼���
	 * 
	 * @return ��ʾ�¼���
	 */
	public C2D_EventPool_Button Events_Button()
	{
		if (m_Events_Button == null)
		{
			m_Events_Button = new C2D_EventPool_Button(this);
		}
		return m_Events_Button;
	}

	/**
	 * ��ð����¼���
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_KeyPress Events_KeyPress()
	{
		if (m_Events_KeyPress == null)
		{
			m_Events_KeyPress = new C2D_EventPool_KeyPress(this);
		}
		return m_Events_KeyPress;
	}
	
	/**
	 * ��ô����¼���
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_Touch Events_Touch()
	{
		if (m_Events_Touch == null)
		{
			m_Events_Touch = new C2D_EventPool_Touch(this);
		}
		return m_Events_Touch;
	}

	/**
	 * ������е��¼�������Դ�ͷ�ʱ������Ҫ����
	 */
	public void clearEvents()
	{
		Events_Button().clear();
		Events_ChangeFocus().clear();
		Events_MotionEnd().clear();
		Events_Update().clear();
		Events_WaitEnd().clear();
		Events_KeyPress().clear();
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		// m_widgetName = null;
		m_parentNode = null;
		if (m_focusImgClip != null)
		{
			m_focusImgClip.doRelease();
			m_focusImgClip = null;
		}
		m_atStage = null;
		m_atScene = null;
		if (m_Events_MotionEnd != null)
		{
			m_Events_MotionEnd.doRelease();
			m_Events_MotionEnd = null;
		}
		if (m_Events_Focus != null)
		{
			m_Events_Focus.doRelease();
			m_Events_Focus = null;
		}
		clearMotions();
		if (m_Events_WaitTime != null)
		{
			m_Events_WaitTime.doRelease();
			m_Events_WaitTime = null;
		}
		if (m_Events_Update != null)
		{
			m_Events_Update.doRelease();
			m_Events_Update = null;
		}
		if (m_Events_Button != null)
		{
			m_Events_Button.doRelease();
			m_Events_Button = null;
		}
		if (m_Events_KeyPress != null)
		{
			m_Events_KeyPress.doRelease();
			m_Events_KeyPress = null;
		}
		if (m_physicBox != null)
		{
			m_physicBox.doRelease(this);
			m_physicBox = null;
		}
	}

	/**
	 * �����û��Զ�����
	 * 
	 * @param flag
	 *            �û��Զ�����
	 */
	public C2D_Widget setFlag(int flag)
	{
		m_iFlag = flag;
		return this;
	}

	/**
	 * �����û��Զ�����
	 * 
	 * @param flag
	 *            �û��Զ�����
	 */
	public C2D_Widget setStrFlag(String flag)
	{
		m_strFlag = flag;
		return this;
	}

	/**
	 * ��ȡ�û��Զ�����
	 * 
	 * @return �û��Զ�����
	 */
	public int getFlag()
	{
		return m_iFlag;
	}

	/**
	 * ��ȡ�û��Զ�����
	 * 
	 * @return �û��Զ�����
	 */
	public String getStrFlag()
	{
		return m_strFlag;
	}

	/**
	 * ��������ײ��
	 * 
	 * @param phyBox
	 */
	public void bindPhysicBox(C2D_PhysicBox phyBox)
	{
		if (m_physicBox != null)
		{
			m_physicBox.doRelease(this);
			m_physicBox = null;
		}
		m_physicBox = phyBox;
		if (m_physicBox != null)
		{
			m_physicBox.transHadler(this);
			;
			m_physicBox.setBoundWidget(this);
		}
	}

	/**
	 * ��������ײ��
	 * 
	 * @param phyBox
	 */
	public void bindPhysicBox(C2D_PhysicBoxCreator pbc)
	{
		if (pbc != null)
		{
			bindPhysicBox(pbc.onCreatePhysicBox(this));
		}
	}

	/**
	 * ���õ��Ӹ������Ƴ�ʱ����Ӧ���¼�
	 * 
	 * @param event
	 *            �¼�
	 */
	public void setOnRemoveFromViewEvt(C2D_Event_RemovedFromView event)
	{
		m_removedEvt = event;
	}

	/**
	 * �󶨵���ײ����
	 * 
	 * @return
	 */
	public C2D_RectF getBoundingRect()
	{
		return null;
	}

	/**
	 * �����Ƿ񼤻�����
	 * 
	 * @param active
	 *            ����״̬
	 */
	public void setUse_HotRegion(boolean active)
	{
		if (active != m_useHotRegion)
		{
			m_useHotRegion = active;
			if (!m_useHotRegion)
			{
				m_hotRegion = null;
			}
		}
	}

	/**
	 * �鿴�Ƿ񼤻�����
	 * 
	 * @return �Ƿ񼤻�
	 */
	public boolean getUse_HotRegion()
	{
		return m_useHotRegion;
	}
}
