package c2d.frame.com.shape;

import c2d.plat.gfx.C2D_Graphics;

public class C2D_Circle extends C2D_Rectangle
{
	/**
	 * »æÖÆ½Úµã
	 * @param g »­±Ê
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
//@			g.fillArc(m_xToTop, m_yToTop, m_width, m_height,0, 360,m_bgColor.getColor(),m_anchor,m_regionShow);
//@		}	
//@		if(m_borderColor!=null)
//@		{
//@			g.drawArc(m_xToTop, m_yToTop, m_width, m_height,0, 360,m_borderColor.getColor(),m_anchor,m_regionShow);
//@		}
//@		paintFocus(g,m_width,m_height);
		//#endif
	}
}
