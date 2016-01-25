package c2d.frame.com.shape;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;

/**
 * �߶οؼ�����ǰλ��Ϊ��ʼ�˵㣬��Ҫָ����ʾ����ɫ�ͽ����˵㡣
 * �����˵�����������ڵ�ǰλ�ã���㣩��������ꡣ�����߶���˵��ê��û������
 * @author AndrewFan
 */
public class C2D_Segement extends C2D_Widget
{
	/** �����˵�X���� */
	protected float m_xEnd;
	/** �����˵�Y����*/
	protected float m_yEnd;
	/** �߶���ɫ */
	protected int m_Color=0xFFFFFFFF;

	/**
	 * ���캯��
	 */
	public C2D_Segement()
	{
	}

	/**
	 * ��ȡ��ɫ
	 * 
	 * @return �߿���ɫ
	 */
	public int getColor()
	{
		return m_Color;
	}

	/**
	 * ������ɫ,ARGB
	 * 
	 * @param color
	 *            ARGB��ɫ��ֵ
	 */
	public void setColor(int color)
	{
		this.m_Color = color;
	}
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
			m_LT2Root.m_x = m_xEnd<0?(m_xToTop+m_xEnd):m_xToTop;
			m_LT2Root.m_y = m_yEnd<0?(m_yToTop+m_yEnd):m_yToTop;
			m_needUpdateLT = false;
		}
		return m_LT2Root;
	}
	/**
	 * ��ȡ���
	 * 
	 * @return ���
	 */
	public float getWidth()
	{
		float w=C2D_Math.abs(m_xEnd);
		return w;
	}
	/**
	 * ��ȡ�߶�
	 * 
	 * @return �߶�
	 */
	protected float getHeight()
	{
		float h=C2D_Math.abs(m_yEnd);
		return h;
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
		if (m_xEnd == width)
		{
			return false;
		}

		m_xEnd = width;
		layoutChanged();
		return true;
	}

	/**
	 * ��ȡ������Y����
	 * 
	 * @return ������Y����
	 */
	public float getYEnd()
	{
		return m_yEnd;
	}

	/**
	 * ���ø߶�
	 * 
	 * @param yEnd
	 *            �¸߶�
	 * @return �����Ƿ����ı�
	 */
	public boolean setYEnd(int yEnd)
	{
		if (m_yEnd == yEnd)
		{
			return false;
		}

		m_yEnd = yEnd;
		layoutChanged();
		return true;
	}

	/**
	 * ���ý����˵�����,��������ڵ�ǰλ�ã���㣩��������ꡣ
	 * 
	 * @param xEnd
	 *            �¿��
	 * @param yEnd
	 *            �¸߶�
	 * @return �����Ƿ����ı�
	 */
	public boolean setPointEnd(float xEnd, float yEnd)
	{
		if (m_xEnd == xEnd && m_yEnd == yEnd)
		{
			return false;
		}

		m_xEnd = xEnd;
		m_yEnd = yEnd;
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
		if ( !m_visible || stage == null)
		{
			return;
		}
		g.setColor(m_Color);
		g.drawLine(m_xToTop, m_yToTop, m_xEnd, m_yEnd);
		paintFocus(g,m_xEnd, m_yEnd);
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
		float xMin=C2D_Math.min(m_x,m_x+m_xEnd);
		float yMin=C2D_Math.min(m_y,m_y+m_yEnd);
		float xMax=C2D_Math.max(m_x,m_x+m_xEnd);
		float yMax=C2D_Math.max(m_y,m_y+m_yEnd);
		return C2D_GdiGraphics.computeLayoutRect(xMin, yMin, xMax-xMin, yMax-yMin, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * ���ÿؼ������в���
	 * 
	 * @param xEnd
	 *            ���
	 * @param yEnd
	 *            �߶�
	 * @param color
	 *            �߶���ɫ
	 */
	public void setParameters(float xEnd, float yEnd, int color)
	{

		m_xEnd = xEnd;
		m_yEnd = yEnd;
		m_Color = color;
		layoutChanged();
	}
}
