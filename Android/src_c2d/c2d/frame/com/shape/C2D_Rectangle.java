package c2d.frame.com.shape;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_RectF;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;

/**
 * ���οؼ�����Ҫָ����ʾ����ɫ��λ�úͳߴ硣
 * 
 * @author AndrewFan
 */
public class C2D_Rectangle extends C2D_Widget
{
	/** ��� */
	protected float m_width;
	/** �߶� */
	protected float m_height;
	/** �����ɫ */
	protected C2D_Color m_bgColor;
	/** �߿���ɫ */
	protected C2D_Color m_borderColor;

	/**
	 * ���캯��
	 */
	public C2D_Rectangle()
	{
	}

	/**
	 * ��ȡ�����ɫ���㲻Ӧ�øı��ȡ������ɫ�� �����Ҫ���ã�����setBgColor
	 * 
	 * @return �ı���ɫ
	 */
	public C2D_Color getBgColor()
	{
		return m_bgColor;
	}

	/**
	 * ���������ɫ
	 * 
	 * @param color
	 *            ��ɫ����
	 */
	public void setBgColor(C2D_Color color)
	{

		this.m_bgColor = color;
		layoutChanged();
	}

	/**
	 * ���������ɫ,ARGB
	 * 
	 * @param color
	 *            ARGB��ɫ��ֵ
	 */
	public void setBgColor(int color)
	{

		this.m_bgColor = C2D_Color.makeARGB(color);
		layoutChanged();
	}

	/**
	 * ��ȡ�߿���ɫ���㲻Ӧ�øı��ȡ������ɫ�� �����Ҫ���ã�����setBorderColor
	 * 
	 * @return �߿���ɫ
	 */
	public C2D_Color getBorderColor()
	{
		return m_borderColor;
	}

	/**
	 * ���ñ߿���ɫ
	 * 
	 * @param color
	 *            ��ɫ
	 */
	public void setBorderColor(C2D_Color color)
	{

		m_borderColor = color;
		layoutChanged();
	}

	/**
	 * ���������ɫ,ARGB
	 * 
	 * @param color
	 *            ARGB��ɫ��ֵ
	 */
	public void setBorderColor(int color)
	{

		this.m_borderColor = C2D_Color.makeARGB(color);
		layoutChanged();
	}

	/**
	 * ��ȡ���
	 * 
	 * @return ���
	 */
	public float getWidth()
	{
		return m_width;
	}

	/**
	 * ���ÿ��
	 * 
	 * @param width
	 *            �¿��
	 * @return �����Ƿ����ı�
	 */
	public boolean setWidth(int width)
	{
		if (m_width == width)
		{
			return false;
		}

		m_width = width;

		layoutChanged();
		return true;
	}

	/**
	 * ��ȡ�߶�
	 * 
	 * @return �߶�
	 */
	public float getHeight()
	{
		return m_height;
	}

	/**
	 * ���ø߶�
	 * 
	 * @param height
	 *            �¸߶�
	 * @return �����Ƿ����ı�
	 */
	public boolean setHeight(int height)
	{
		if (m_height == height)
		{
			return false;
		}

		m_height = height;

		layoutChanged();
		return true;
	}

	/**
	 * ���ÿ��
	 * 
	 * @param width
	 *            �¿��
	 * @param height
	 *            �¸߶�
	 * @return �����Ƿ����ı�
	 */
	public boolean setSize(float width, float height)
	{
		if (m_width == width && m_height == height)
		{
			return false;
		}

		m_width = width;
		m_height = height;

		layoutChanged();
		return true;
	}

	/**
	 * ���ƽڵ�
	 * 
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if (!m_visible || stage == null)
		{
			return;
		}
		if (m_bgColor != null)
		{
			g.fillRect(m_xToTop, m_yToTop, m_width, m_height, m_bgColor.getColor(), m_anchor);
		}
		if (m_borderColor != null)
		{
			g.drawRect(m_xToTop, m_yToTop, m_width, m_height, m_borderColor.getColor(), m_anchor);
		}
		paintFocus(g, m_width, m_height);
	}

	/**
	 * ��ȡ��Բ��־��Σ������ڵ�ǰ�����꣬�ߴ磬ê�㣬��ת����Ϣ�� �����������丸�ڵ���ռ�ݵľ������򡣽���Ϣ����ڴ����
	 * ���β������������Ƿ�ɹ�ȡ�á�
	 * 
	 * @param resultRect
	 *            ���ڽ����ŵľ��ζ���
	 * @return �Ƿ�ɹ�ȡ��
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, m_width, m_height, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * ���ÿؼ������в���
	 * 
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param bgColor
	 *            ������ɫ
	 * @param borderColor
	 *            �߿���ɫ
	 * @param anchor
	 *            ê������
	 */
	public void setParameters(int width, int height, int bgColor, int borderColor, int anchor)
	{

		m_width = width;
		m_height = height;
		m_bgColor = C2D_Color.makeARGB(bgColor);
		m_borderColor = C2D_Color.makeARGB(borderColor);
		m_anchor = anchor;

		layoutChanged();
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		m_bgColor = null;
		m_borderColor = null;
	}
}
