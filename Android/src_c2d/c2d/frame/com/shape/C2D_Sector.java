package c2d.frame.com.shape;

import c2d.lang.math.type.C2D_Color;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_Sector extends C2D_Rectangle
{
	/** ���ο��ڽǶ� */
	private int m_angleBegin;
	/** ���ι��ɽǶ� */
	private int m_angleSweep;
	/**
	 * ��ȡ���ο��ڽǶ�
	 *
	 * @return ���ο��ڽǶ�
	 */
	public int getAngleBegin()
	{
		return m_angleBegin;
	}
	
	/**
	 * �������ο��ڽǶ�
	 * @param angle ���ο��ڽǶ�
	 * @return �����Ƿ����ı�
	 */
	public boolean setAngleBegin(int angle)
	{
		if(m_angleBegin==angle)
		{
			return false;
		}
		m_angleBegin = angle;
		return true;
	}
	/**
	 * ��ȡ���αտڽǶ�
	 *
	 * @return ���αտڽǶ�
	 */
	public int getAngleEnd()
	{
		return m_angleSweep;
	}
	/**
	 * �������ι��ɽǶ�
	 * @param angle ���ι��ɽǶ�
	 * @return �����Ƿ����ı�
	 */
	public boolean setAngleSweep(int angle)
	{
		if(m_angleSweep==angle)
		{
			return false;
		}
		m_angleSweep = angle;
		return true;
	}
	/**
	 * �������οؼ������в���
	 * @param width       ���ο��
	 * @param height      ���θ߶�
	 * @param bgColor	    ���α�����ɫ
	 * @param borderColor ���α߿���ɫ
	 * @param anchor	    ê������
	 * @param angleBegin  ���ο��ڽǶ�
	 * @param angleSweep  ���ι��ɽǶ�
	 */
	public void setParameters(int width,int height,int bgColor, int borderColor,int anchor,
			int angleBegin,int angleSweep)
	{
		m_width=width;
		m_height=height;
		m_bgColor=C2D_Color.makeARGB(bgColor);
		m_borderColor=C2D_Color.makeARGB(borderColor);
		m_anchor=anchor;
		m_angleBegin=angleBegin;
		m_angleSweep=angleSweep;
		layoutChanged();
	}
	/**
	 * ���ƽڵ�
	 * @param g ����
	 */
	public void onPaint(C2D_Graphics g)
	{
		if (g == null || !m_visible ||m_hiddenByParent || !m_inCamera)
		{
			return;
		}
		//#if Platform=="J2me"
//@		if(m_bgColor!=null)
//@		{
//@			g.fillArc(m_xToTop, m_yToTop, m_width, m_height,m_angleBegin, m_angleSweep,m_bgColor.getColor(),m_anchor,m_regionShow);
//@		}	
//@		if(m_borderColor!=null)
//@		{
//@			g.drawArc(m_xToTop, m_yToTop, m_width, m_height,m_angleBegin, m_angleSweep,m_borderColor.getColor(),m_anchor,m_regionShow);
//@		}
//@		paintFocus(g,m_width,m_height);
		//#endif
	}
}
