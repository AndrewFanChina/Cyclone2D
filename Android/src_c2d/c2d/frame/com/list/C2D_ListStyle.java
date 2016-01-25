package c2d.frame.com.list;

import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_PointI;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 列表风格
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ListStyle extends C2D_Object
{
	/** 背景图片，如果不设置，将没有背景图片 */
	public C2D_ImageClip m_imgClip;
	/** 背景图片的百分比坐标，传入的坐标将被除以100变成比率 */
	public C2D_PointI m_imgPos = new C2D_PointI();
	/** 背景图片的锚点 */
	public int m_imgAnchor;
	/** 背景颜色，如果不设置，将没有列表项背景颜色 */
	public C2D_Color m_bgColor;
	/** 文本颜色，如果不设置，将使用默认前景颜色 */
	public C2D_Color m_txtColor;
	/**
	 * 构造背景图片配置
	 * 
	 * @param imcClip
	 *            背景图片
	 * @param imgPos
	 *            背景图片的百分比坐标
	 * @param imgAnchor
	 *            背景图片的锚点
	 */
	public C2D_ListStyle(C2D_ImageClip imcClip, C2D_PointI imgPos, int imgAnchor)
	{
		m_imgClip = imcClip;
		m_imgClip.transHadler(this);
		if (imgPos != null)
		{
			m_imgPos.setValue(imgPos);
		}
		m_imgAnchor = imgAnchor;
	}
	public C2D_ListStyle(int bgColor ,int textColor)
	{
		setTextColor(bgColor,textColor);
	}
	public void setTextColor(int bgColor ,int textColor)
	{
		if(m_bgColor==null)
		{
			m_bgColor=new C2D_Color(bgColor);
		}
		else
		{
			m_bgColor.setColor(bgColor);
		}
		if(m_txtColor==null)
		{
			m_txtColor=new C2D_Color(textColor);
		}
		else
		{
			m_txtColor.setColor(textColor);
		}
	}
	public void onRelease()
	{
		if(m_imgClip!=null)
		{
			m_imgClip.doRelease(this);
			m_imgClip = null;
		}
		m_imgPos = null;
	}
}
