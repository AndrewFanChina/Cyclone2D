package c2d.frame.com.shape;

import c2d.lang.math.type.C2D_Color;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_RoundRect extends C2D_Rectangle
{
	/** Բ�ǿ�� */
	private int m_ArchWidth;
	/** Բ�Ǹ߶� */
	private int m_ArchHeight;
	/**
	 * ��ȡ���
	 *
	 * @return ���
	 */
	public int getArchWidth()
	{
		return m_ArchWidth;
	}
	
	/**
	 * ����Բ�ǿ��
	 * @param width Բ���¿��
	 * @return �����Ƿ����ı�
	 */
	public boolean setArchWidth(int width)
	{
		if(m_ArchWidth==width)
		{
			return false;
		}
		m_ArchWidth = width;
		return true;
	}
	/**
	 * ��ȡԲ�Ǹ߶�
	 *
	 * @return �߶�
	 */
	public int getArchHeight()
	{
		return m_ArchHeight;
	}
	
	/**
	 * ����Բ�ǿ��
	 * @param height �¸߶�
	 * @return �����Ƿ����ı�
	 */
	public boolean setHeight(int height)
	{
		if(m_ArchHeight==height)
		{
			return false;
		}
		m_ArchHeight = height;
		layoutChanged();
		return true;
	}
	/**
	 * ����Բ�Ǿ��οؼ������в���
	 * @param width       Բ�Ǿ��ο��
	 * @param height      Բ�Ǿ��θ߶�
	 * @param bgColor	    Բ�Ǿ��α�����ɫ
	 * @param borderColor Բ�Ǿ��α߿���ɫ
	 * @param anchor	    ê������
	 * @param archW		    Բ�ǿ��
	 * @param archH		    Բ�Ǹ߶�
	 */
	public void setParameters(int width,int height,int bgColor, int borderColor,int anchor,
			int archW,int archH)
	{
		m_width=width;
		m_height=height;
		m_bgColor=C2D_Color.makeARGB(bgColor);
		m_borderColor=C2D_Color.makeARGB(borderColor);
		m_anchor=anchor;
		m_ArchWidth=archW;
		m_ArchHeight=archH;
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
//@			g.fillRoundRect(m_xToTop, m_yToTop, m_width, m_height,m_ArchWidth, m_ArchHeight,m_bgColor.getColor(),m_anchor,m_regionShow);
//@		}	
//@		if(m_borderColor!=null)
//@		{
//@			g.drawRoundRect(m_xToTop, m_yToTop, m_width, m_height,m_ArchWidth, m_ArchHeight,m_borderColor.getColor(),m_anchor,m_regionShow);
//@		}
//@		paintFocus(g,m_width,m_height);
		//#endif
	}
}
