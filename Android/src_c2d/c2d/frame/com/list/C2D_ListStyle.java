package c2d.frame.com.list;

import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_PointI;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * �б���
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ListStyle extends C2D_Object
{
	/** ����ͼƬ����������ã���û�б���ͼƬ */
	public C2D_ImageClip m_imgClip;
	/** ����ͼƬ�İٷֱ����꣬��������꽫������100��ɱ��� */
	public C2D_PointI m_imgPos = new C2D_PointI();
	/** ����ͼƬ��ê�� */
	public int m_imgAnchor;
	/** ������ɫ����������ã���û���б������ɫ */
	public C2D_Color m_bgColor;
	/** �ı���ɫ����������ã���ʹ��Ĭ��ǰ����ɫ */
	public C2D_Color m_txtColor;
	/**
	 * ���챳��ͼƬ����
	 * 
	 * @param imcClip
	 *            ����ͼƬ
	 * @param imgPos
	 *            ����ͼƬ�İٷֱ�����
	 * @param imgAnchor
	 *            ����ͼƬ��ê��
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
