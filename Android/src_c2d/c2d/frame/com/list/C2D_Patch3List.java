package c2d.frame.com.list;

import c2d.frame.com.data.unit_type.C2D_UnitType;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

public class C2D_Patch3List extends C2D_List
{
	private C2D_Image m_imgCom, m_imgFocus;
	/** �б����ͼƬ���� */
	protected C2D_ListStyle m_itemStyles[];
	/** �б����ͼƬ���� */
	protected C2D_ListStyle m_focusStyles[];

	public C2D_Patch3List(C2D_UnitType[] itemFormat)
	{
		super(itemFormat);
	}

	/**
	 * �����б���ķ��
	 * 
	 * @param pt3Com
	 *            �б�δѡ��ʱ���
	 * @param pt3Focus
	 *            �б���ѡ��ʱ���
	 */
	public void setPt3Style(String pt3Com, String pt3Focus)
	{
		if (pt3Com != null)
		{
			releaseCfgs(m_itemStyles);
			m_itemStyles = null;
			if (m_imgCom != null)
			{
				m_imgCom.doRelease(this);
				m_imgCom = null;
			}
			m_imgCom = C2D_Image.createImage(pt3Com);
			if (m_imgCom != null)
			{
				m_imgCom.transHadler(this);
				m_itemStyles = createStyles(m_imgCom);
			}
		}
		if (pt3Focus != null)
		{
			releaseCfgs(m_focusStyles);
			m_focusStyles = null;
			if (m_imgFocus != null)
			{
				m_imgFocus.doRelease(this);
				m_imgFocus = null;
			}
			m_imgFocus = C2D_Image.createImage(pt3Focus);
			if (m_imgFocus != null)
			{
				m_imgFocus.transHadler(this);
				m_focusStyles = createStyles(m_imgFocus);
			}
		}
		m_needRepack = true;
	}

	/**
	 * ��ͼƬ�д����б�������
	 * 
	 * @param img
	 *            ���ڵ��б���Ƭͼ
	 * @return �б�������
	 */
	private C2D_ListStyle[] createStyles(C2D_Image img)
	{
		if (img == null)
		{
			return null;
		}
		C2D_ListStyle styles[] = new C2D_ListStyle[3];
		int h = img.bitmapWidth();
		int h3 = h / 3;
		int w = img.bitmapHeight();
		for (int i = 0; i < styles.length; i++)
		{
			C2D_ImageClip ic = new C2D_ImageClip(img);
			ic.setContentRect(0, h3 * i, w, h3);
			styles[i] = new C2D_ListStyle(ic, null, 0);
			styles[i].transHadler(this);
		}
		return styles;
	}

	/**
	 * 
	 */
	private void releaseCfgs(C2D_ListStyle styles[])
	{
		if (styles != null)
		{
			for (int i = 0; i < styles.length; i++)
			{
				if (styles[i] != null)
				{
					styles[i].doRelease(this);
					styles[i] = null;
				}
			}
		}

	}
	/**
	 * ������Ŀ��ȡ��ӦӦ����ʾ�ķ��
	 * @param itemID ��ĿID
	 * @return ��ʾ���
	 */
	protected C2D_ListStyle getShowStyle(int itemID)
	{
		C2D_ListStyle styles[] = m_itemStyles;
		if(m_focused && itemID == m_currentScroll)
		{
			styles = m_focusStyles;
		}
		if(styles==null)
		{
			return null;
		}
		
		if(itemID==m_firstRow)
		{
			return styles[0];
		}
		else if(itemID==m_firstRow+getPageRow()-1)
		{
			return styles[2];
		}
		else
		{
			return styles[1];
		}
	}
	public void onRelease()
	{
		super.onRelease();
		releaseCfgs(m_itemStyles);
		m_itemStyles = null;
		releaseCfgs(m_focusStyles);
		m_focusStyles = null;
		if (m_imgCom != null)
		{
			m_imgCom.doRelease(this);
			m_imgCom = null;
		}
		if (m_imgFocus != null)
		{
			m_imgFocus.doRelease(this);
			m_imgFocus = null;
		}
	}

}
